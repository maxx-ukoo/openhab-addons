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
public class ResponseDeviceAvailable implements MessageBody, FrameBody {

    private final Logger logger = LoggerFactory.getLogger(ResponseDeviceAvailable.class);

    private byte[] tag = { (byte) 0x01 };
    private byte[] eth_ip = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
    private byte[] eth_mask = { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x00 };
    private byte[] eth_gw = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
    private byte[] eth_status = { (byte) 0x00 };
    private byte[] wlan_ip = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
    private byte[] wlan_mask = { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x00 };
    private byte[] wlan_gw = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
    private byte[] server_status = { (byte) 0x01 };
    private byte[] wlan_encryption = { (byte) 0x01 };
    private byte[] wlan_signal_quality = { (byte) 0x64 };
    private byte[] wlan_status = { (byte) 0x00 };
    private byte[] wlan_ssid = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };

    @Override
    public byte[] getMessageBody() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            os.write(FrameType.RESPONSE_DEVICE_AVAILABLE.getId());
            os.write(tag);
            os.write(eth_ip);
            os.write(eth_mask);
            os.write(eth_gw);
            os.write(eth_status);
            os.write(wlan_ip);
            os.write(wlan_mask);
            os.write(wlan_gw);
            os.write(server_status);
            os.write(wlan_encryption);
            os.write(wlan_signal_quality);
            os.write(wlan_status);
            os.write(wlan_ssid);
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
        return FrameType.RESPONSE_DEVICE_AVAILABLE;
    }
}
