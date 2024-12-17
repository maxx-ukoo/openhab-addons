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
package org.openhab.binding.plumecomax.internal.device.protocol.listener;

import java.util.Arrays;

import org.openhab.binding.plumecomax.internal.device.EcoMaxDevice;
import org.openhab.binding.plumecomax.internal.device.protocol.frame.Frame;
import org.openhab.binding.plumecomax.internal.device.protocol.frame.parcer.FrameParcer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;

/**
 * @author Maksym Krasovskyi - Initial contribution
 */
public abstract class AbstractSerialListener implements SerialPortMessageListener {

    private final Logger logger = LoggerFactory.getLogger(AbstractSerialListener.class);

    private byte[] start_data = new byte[0];

    @Override
    public byte[] getMessageDelimiter() {
        return new byte[] { (byte) 0x68 };
    }

    @Override
    public boolean delimiterIndicatesEndOfMessage() {
        return false;
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    abstract byte[] processFrame(Frame frame);

    @Override
    public void serialEvent(SerialPortEvent event) {
        byte[] delimitedMessage = event.getReceivedData();
        logger.trace("Incoming SerialPortEvent with {} bytes", event.getReceivedData().length);
        try {
            Frame frame = null;
            if (FrameParcer.isFullFrame(delimitedMessage)) {
                start_data = new byte[0];
                frame = FrameParcer.parce(delimitedMessage);
            } else {
                int current_lenght = start_data.length;
                start_data = Arrays.copyOf(start_data, start_data.length + delimitedMessage.length);
                for (int i = 0; i < delimitedMessage.length; i++) {
                    start_data[current_lenght + i] = delimitedMessage[i];
                }
                if (FrameParcer.isFullFrame(start_data)) {
                    frame = FrameParcer.parce(start_data);
                    start_data = new byte[0];
                }
            }
            if (frame != null) {
                // logger.trace("{}", Frame.getRAWFrameAsString(frame));
                // logger.debug("{}", frame);
                byte[] response = processFrame(frame);
                if (response.length > 0) {
                    // logger.debug("Response: {}", Frame.getRAWFrameAsString(FrameParcer.parce(response)));
                    EcoMaxDevice.queueFrame.add(FrameParcer.parce(response));
                    // event.getSerialPort().writeBytes(response, response.length);
                }
            }
        } catch (Exception e) {
            logger.error("Error during processing frame", e);
        }
    }
}
