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
package org.openhab.binding.plumecomax.internal.device.protocol.frame.parcer;

import static org.openhab.binding.plumecomax.internal.device.protocol.frame.Frame.DESTINATION_BYTE;
import static org.openhab.binding.plumecomax.internal.device.protocol.frame.Frame.FRAME_DATA_END_BYTE_OFFSET;
import static org.openhab.binding.plumecomax.internal.device.protocol.frame.Frame.FRAME_DATA_START_BYTE;
import static org.openhab.binding.plumecomax.internal.device.protocol.frame.Frame.FRAME_END_BYTE;
import static org.openhab.binding.plumecomax.internal.device.protocol.frame.Frame.FRAME_START_BYTE;
import static org.openhab.binding.plumecomax.internal.device.protocol.frame.Frame.FRAME_TYPE_BYTE;
import static org.openhab.binding.plumecomax.internal.device.protocol.frame.Frame.SENDER_TYPE_BYTE;
import static org.openhab.binding.plumecomax.internal.device.protocol.frame.Frame.SIZE_BYTE_HI;
import static org.openhab.binding.plumecomax.internal.device.protocol.frame.Frame.SIZE_BYTE_LO;
import static org.openhab.binding.plumecomax.internal.device.protocol.frame.Frame.SOURCE_BYTE;
import static org.openhab.binding.plumecomax.internal.device.protocol.frame.Frame.START_BYTE;
import static org.openhab.binding.plumecomax.internal.device.protocol.frame.Frame.VERSION_BYTE;

import java.util.Arrays;

import org.openhab.binding.plumecomax.internal.device.protocol.frame.DeviceType;
import org.openhab.binding.plumecomax.internal.device.protocol.frame.Frame;
import org.openhab.binding.plumecomax.internal.device.protocol.frame.FrameType;

/**
 * The {@link FrameParcer} is responsible for parse serial frames
 *
 * @author Maksym Krasovskyi - Initial contribution
 */
public class FrameParcer {

    public static Frame parce(byte[] data) {
        if (!isLengthOK(data)) {
            throw new IllegalArgumentException(String.format("Wrong data lenght, expected > 9, has: %s", data.length));
        }
        if (!isStartByteOK(data)) {
            throw new IllegalArgumentException(String.format("Wrong start byte: %02X", data[START_BYTE]));
        }
        if (!isEndByteOK(data)) {
            throw new IllegalArgumentException(String.format("Wrong end byte: %02X", data[data.length - 1]));
        }
        if (!isFrameSizeOK(data)) {
            throw new IllegalArgumentException(String.format("Wrong format[frame size], expect %d bytes, has %d bytes",
                    getFrameSize(data), data.length));
        }
        // TODO add check CRC

        Frame frame = Frame.builder().setFrameSize(getFrameSize(data))
                .setDestination(DeviceType.getType(data[DESTINATION_BYTE]))
                .setSource(DeviceType.getType(Byte.toUnsignedInt(data[SOURCE_BYTE])))
                .setFrameType(FrameType.getType(Byte.toUnsignedInt(data[FRAME_TYPE_BYTE])))
                .setVersion(data[VERSION_BYTE]).setSenderType(data[SENDER_TYPE_BYTE]).setFrameData(getMessageData(data))
                .build();

        return frame;
    }

    private static byte[] getMessageData(byte[] data) {
        if (data.length == 10) {
            return new byte[] {};
        } else {
            return Arrays.copyOfRange(data, FRAME_DATA_START_BYTE, data.length + FRAME_DATA_END_BYTE_OFFSET);
        }
    }

    public static boolean isFullFrame(byte[] data) {
        if (!isLengthOK(data)) {
            return false;
        }
        if (!isStartByteOK(data)) {
            return false;
        }

        if (!isFrameSizeOK(data)) {
            return false;
        }
        if (!isEndByteOK(data)) {
            return false;
        }
        byte localCRC = Frame.getCRC(Arrays.copyOfRange(data, 0, data.length - 2));
        if (localCRC != data[data.length - 2]) {
            return false;
        }

        return true;
    }

    private static boolean isLengthOK(byte[] data) {
        if (data.length < 10) {
            return false;
        }
        return true;
    }

    private static boolean isStartByteOK(byte[] data) {
        if (FRAME_START_BYTE != data[START_BYTE]) {
            return false;
        }
        return true;
    }

    private static boolean isEndByteOK(byte[] data) {
        if (FRAME_END_BYTE != data[data.length - 1]) {
            return false;
        }
        return true;
    }

    private static boolean isFrameSizeOK(byte[] data) {
        int frameSize = getFrameSize(data);
        if (frameSize != data.length) {
            return false;
        }
        return true;
    }

    private static int getFrameSize(byte[] data) {
        return (Byte.toUnsignedInt(data[SIZE_BYTE_HI]) << 8) + Byte.toUnsignedInt(data[SIZE_BYTE_LO]);
    }
}
