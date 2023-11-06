/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.plumecomax.internal.device.protocol.frame;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 * The {@link Frame} contains raw serial message
 *
 * @author Maksym Krasovskyi - Initial contribution
 */
public class Frame {

    public static final byte FRAME_START_BYTE = 0x68;
    public static final byte FRAME_END_BYTE = 0x16;

    public static final int START_BYTE = 0;
    public static final int SIZE_BYTE_HI = 2;
    public static final int SIZE_BYTE_LO = 1;
    public static final int DESTINATION_BYTE = 3;
    public static final int SOURCE_BYTE = 4;
    public static final int SENDER_TYPE_BYTE = 5;
    public static final int VERSION_BYTE = 6;
    public static final int FRAME_TYPE_BYTE = 7;
    public static final int FRAME_DATA_START_BYTE = 8;
    public static final int FRAME_DATA_END_BYTE_OFFSET = -2;

    private int size;
    private DeviceType source;
    private DeviceType destination;
    private byte version;
    private byte senderType;
    private FrameType frameType;
    private byte[] data;
    private byte crc;

    private Frame() {
    }

    public FrameType getFrameType() {
        return this.frameType;
    }

    public DeviceType getSource() {
        return source;
    }

    public DeviceType getDestination() {
        return destination;
    }

    public byte[] getRAWFrame() {
        int lenght = 10 + data.length;
        byte[] raw = new byte[lenght];
        raw[START_BYTE] = FRAME_START_BYTE;
        raw[SIZE_BYTE_LO] = (byte) (lenght & 0xFF);
        raw[SIZE_BYTE_HI] = (byte) ((lenght >> 8) & 0xFF);
        raw[DESTINATION_BYTE] = this.destination.getId();
        raw[SOURCE_BYTE] = this.source.getId();
        raw[SENDER_TYPE_BYTE] = this.senderType;
        raw[VERSION_BYTE] = this.version;
        raw[FRAME_TYPE_BYTE] = frameType.getId();
        if (data.length > 0) {
            for (int i = 0; i < data.length; i++) {
                raw[FRAME_DATA_START_BYTE + i] = data[i];
            }
        }
        raw[raw.length - 2] = getCRC(Arrays.copyOfRange(raw, 0, raw.length - 2));
        raw[raw.length - 1] = FRAME_END_BYTE;
        return raw;
    }

    public byte[] getBody() {
        int lenght = data.length + 1;
        byte[] body = new byte[lenght];
        body[0] = frameType.getId();
        System.arraycopy(data, 0, body, 1, data.length);
        return body;
    }

    public static Builder builder() {
        return new Frame().new Builder();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Frame{");
        sb.append("size=").append(size);
        sb.append(", destination=").append(destination);
        sb.append(", source=").append(source);
        sb.append(", senderType=").append(String.format("%02X", senderType));
        sb.append(", version=").append(version);
        sb.append(", frameType=").append(frameType);
        sb.append(", data=");
        if (data == null) {
            sb.append("null");
        } else {
            sb.append('[');
            for (int i = 0; i < data.length; ++i)
                sb.append(i == 0 ? "" : ", ").append(data[i]);
            sb.append(']');
        }
        sb.append('}');
        return sb.toString();
    }

    public class Builder {
        public Builder() {
        }

        public Builder setFrameSize(int size) {
            Frame.this.size = size;
            return this;
        }

        public Builder setSource(DeviceType source) {
            Frame.this.source = source;
            return this;
        }

        public Builder setDestination(DeviceType destination) {
            Frame.this.destination = destination;
            return this;
        }

        public Builder setFrameType(FrameType frameType) {
            Frame.this.frameType = frameType;
            return this;
        }

        public Builder setVersion(byte version) {
            Frame.this.version = version;
            return this;
        }

        public Builder setSenderType(byte senderType) {
            Frame.this.senderType = senderType;
            return this;
        }

        public Builder setFrameData(byte[] data) {
            Frame.this.data = data;
            return this;
        }

        public Builder setFrameBody(FrameBody body) {
            Frame.this.data = body.getMessageData();
            Frame.this.frameType = body.getFrameType();
            return this;
        }

        public Frame build() {
            Frame frame = new Frame();
            frame.size = Frame.this.size;
            frame.source = Frame.this.source;
            frame.destination = Frame.this.destination;
            frame.senderType = Frame.this.senderType;
            frame.version = Frame.this.version;
            frame.frameType = Frame.this.frameType;
            frame.data = Frame.this.data;
            return frame;
        }

        public Frame buildResponse() {
            Frame frame = new Frame();
            frame.size = Frame.this.size;
            frame.source = Frame.this.source;
            frame.destination = Frame.this.destination;
            frame.senderType = 0x30;
            frame.version = 0x05;
            frame.frameType = Frame.this.frameType;
            frame.data = Frame.this.data;
            return frame;
        }
    }

    public static byte getCRC(byte[] data) {
        int crc = 0;
        for (int i = 0; i < data.length; i++) {
            crc = (crc ^ Byte.toUnsignedInt(data[i]));
        }
        return (byte) crc;
    }

    public static String getRAWFrameAsString(Frame frame) {
        byte[] data = frame.getRAWFrame();
        StringJoiner sj = new StringJoiner(" ", "", "");
        for (int i = 0; i < data.length; i++) {
            sj.add(String.format("%02X", data[i]));
        }
        return sj.toString();
    }

    public static void printRAWFrame(Frame frame) {
        System.out.println("RAW frame: " + getRAWFrameAsString(frame));
    }
}
