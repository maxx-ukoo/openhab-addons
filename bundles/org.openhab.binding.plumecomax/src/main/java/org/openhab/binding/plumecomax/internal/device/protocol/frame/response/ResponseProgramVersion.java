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
package org.openhab.binding.plumecomax.internal.device.protocol.frame.response;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import org.openhab.binding.plumecomax.internal.device.protocol.frame.FrameBody;
import org.openhab.binding.plumecomax.internal.device.protocol.frame.FrameType;
import org.openhab.binding.plumecomax.internal.device.protocol.frame.MessageBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Maksym Krasovskyi - Initial contribution
 */
public class ResponseProgramVersion implements MessageBody, FrameBody {

    private final Logger logger = LoggerFactory.getLogger(ResponseProgramVersion.class);
    private byte[] tag = { (byte) 0xff, (byte) 0xff };
    private byte[] version = { (byte) 005 };
    private byte[] device_id = { (byte) 0x7A, (byte) 0x00 };
    private byte[] processor_signature = { (byte) 0x00, (byte) 0x00, (byte) 0x00 };
    private byte[] software_version1 = { (byte) 0x00, (byte) 0x00 };
    private byte[] software_version2 = { (byte) 0x04, (byte) 0x00 };
    private byte[] software_version3 = { (byte) 0x0E, (byte) 0x00 };
    private byte recipient;

    public ResponseProgramVersion(byte recipient) {
        this.recipient = recipient;
    }

    @Override
    public byte[] getMessageBody() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            os.write(FrameType.RESPONSE_PROGRAM_VERSION.getId());
            os.write(tag);
            os.write(version);
            os.write(device_id);
            os.write(processor_signature);
            os.write(software_version1);
            os.write(software_version2);
            os.write(software_version3);
            os.write(recipient);
        } catch (Exception e) {
            logger.error("Error on creating message body", e);
        }
        return os.toByteArray();
    }

    @Override
    public byte[] getMessageData() {
        byte[] body = getMessageBody();
        return Arrays.copyOfRange(body, 1, body.length);
    }

    @Override
    public FrameType getFrameType() {
        return FrameType.RESPONSE_PROGRAM_VERSION;
    }
}
