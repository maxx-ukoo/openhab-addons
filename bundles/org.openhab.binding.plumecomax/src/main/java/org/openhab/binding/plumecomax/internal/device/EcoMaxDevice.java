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
package org.openhab.binding.plumecomax.internal.device;

import java.util.LinkedList;
import java.util.Queue;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.plumecomax.internal.device.protocol.frame.Frame;
import org.openhab.binding.plumecomax.internal.device.protocol.listener.SerialDataListener;
import org.openhab.binding.plumecomax.internal.device.protocol.manager.IncomingFrameManager;
import org.openhab.binding.plumecomax.internal.device.sensor.SensorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fazecast.jSerialComm.SerialPort;

/**
 * The {@link EcoMaxDevice} is responsible for handling EcoMAX communication
 *
 * @author Maksym Krasovskyi - Initial contribution
 */
@NonNullByDefault
public class EcoMaxDevice {

    private final Logger logger = LoggerFactory.getLogger(EcoMaxDevice.class);
    private final String serialPortName;

    private final DeviceData deviceData = new DeviceData();

    private SensorData sensorData = new SensorData();
    public static Queue<Frame> queueFrame = new LinkedList<>();

    @Nullable
    private SerialPort serialPort;

    public EcoMaxDevice(String serialPortName) {
        this.serialPortName = serialPortName;
    }

    public boolean isAvailable() {
        return deviceData.isDataValid();
    }

    public boolean init() {
        return refreshSerialPortConnection();
    }

    public boolean refreshSerialPortConnection() {
        logger.debug("(Re)init EcoMAX serial connection");
        logger.debug("Serial port name: {}", serialPortName);
        if (this.serialPort == null) {
            logger.debug("No serial port, ok for first connection. Try to find serial port");
            this.serialPort = SerialPort.getCommPort(serialPortName);
        }
        this.serialPort.setBaudRate(115200);
        if (this.serialPort.isOpen()) {
            logger.debug("Serial port active, try to close connection");
            boolean closeStatus = this.serialPort.closePort();
            logger.debug("Serial port close status: {}", closeStatus);
            // We will try to reconnect regardless close status, continue
        }
        this.serialPort.removeDataListener();
        boolean status = serialPort.openPort();
        if (status == false) {
            logger.debug("Can't open serial port: {}", serialPortName);
            return false;
        }
        IncomingFrameManager frameManager = new IncomingFrameManager(this);
        SerialDataListener listener = new SerialDataListener(frameManager);
        this.serialPort.addDataListener(listener);
        return true;
    }

    @Nullable
    public SerialPort getSerialPort() {
        return serialPort;
    }

    public DeviceData getDeviceData() {
        return deviceData;
    }

    public SensorData getSensorData() {
        return sensorData;
    }

    public void setSensorData(SensorData sensorData) {
        this.sensorData = sensorData;
    }
}
