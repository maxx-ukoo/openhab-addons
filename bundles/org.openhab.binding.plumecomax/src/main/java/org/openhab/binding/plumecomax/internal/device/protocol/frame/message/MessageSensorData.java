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
package org.openhab.binding.plumecomax.internal.device.protocol.frame.message;

import java.util.Arrays;

import org.openhab.binding.plumecomax.internal.device.protocol.DeviceState;
import org.openhab.binding.plumecomax.internal.device.protocol.frame.FrameType;
import org.openhab.binding.plumecomax.internal.device.sensor.ModuleType;
import org.openhab.binding.plumecomax.internal.device.sensor.OutputFlags;
import org.openhab.binding.plumecomax.internal.device.sensor.OutputsState;
import org.openhab.binding.plumecomax.internal.device.sensor.SensorData;
import org.openhab.binding.plumecomax.internal.device.sensor.TemperaturesStructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Maksym Krasovskyi - Initial contribution
 */
public class MessageSensorData {

    private static final Logger logger = LoggerFactory.getLogger(MessageSensorData.class);

    public static SensorData fromByteArray(byte[] data) {
        int offset = 0;
        if (data[offset] != FrameType.MESSAGE_SENSOR_DATA.getId()) {
            throw new IllegalArgumentException(String.format("Wrong frame type: %02X", data[offset]));
        }
        SensorData sensorData = new SensorData();
        // parse versions
        offset++;
        int versionSize = Byte.toUnsignedInt(data[offset]);
        offset++;
        for (int i = 0; i < versionSize; i++) {
            FrameType frameType = FrameType.getType(Byte.toUnsignedInt(data[offset]));
            int frameVersion = Byte.toUnsignedInt(data[offset + 1]) + (Byte.toUnsignedInt(data[offset + 2]) << 8);
            sensorData.getVersionMap().put(frameType, frameVersion);
            offset += 3;
        }
        int devState = Byte.toUnsignedInt(data[offset++]);
        logger.info("Got devState value {}", devState);
        sensorData.setDeviceState(DeviceState.getDeviceState(devState));
        sensorData.setOutputsState(new OutputsState(Arrays.copyOfRange(data, offset, offset + 4)));
        offset += 4;
        sensorData.setOutputFlags(new OutputFlags(Arrays.copyOfRange(data, offset, offset + 4)));
        offset += 4;
        int temperatureNumber = Byte.toUnsignedInt(data[offset]);
        int temperatureArraySize = temperatureNumber * TemperaturesStructure.SIZE + 1;
        sensorData.setTemperaturesStructure(
                new TemperaturesStructure(Arrays.copyOfRange(data, offset, offset + temperatureArraySize)));
        offset += temperatureArraySize;
        sensorData.setHeating_target(Byte.toUnsignedInt(data[offset++]));
        sensorData.setHeating_status(Byte.toUnsignedInt(data[offset++]));
        sensorData.setWater_heater_target(Byte.toUnsignedInt(data[offset++]));
        sensorData.setWater_heater_status(Byte.toUnsignedInt(data[offset++]));
        int alertsNumber = Byte.toUnsignedInt(data[offset++]); // TODO implement alert processing, skip for now
        sensorData.setFuel_level(Byte.toUnsignedInt(data[offset++]));
        sensorData.setTransmission(Byte.toUnsignedInt(data[offset++]));
        sensorData.setFanPower(Float.intBitsToFloat((((data[offset + 3] & 0xff) << 24)
                | ((data[offset + 2] & 0xff) << 16) | ((data[offset + 1] & 0xff) << 8) | (data[offset] & 0xff))));
        offset += 4;
        sensorData.setLoad(Byte.toUnsignedInt(data[offset++]));
        sensorData.setPower(Float.intBitsToFloat((((data[offset + 3] & 0xff) << 24) | ((data[offset + 2] & 0xff) << 16)
                | ((data[offset + 1] & 0xff) << 8) | (data[offset] & 0xff))));
        offset += 4;
        sensorData.setFuel_consumption(Float.intBitsToFloat((((data[offset + 3] & 0xff) << 24)
                | ((data[offset + 2] & 0xff) << 16) | ((data[offset + 1] & 0xff) << 8) | (data[offset] & 0xff))));
        offset += 4;
        sensorData.setThermostat(Byte.toUnsignedInt(data[offset++]));
        // parsing versions
        String version = parseVersion(Arrays.copyOfRange(data, offset, offset + 5), ModuleType.MODULE_A);
        if (version != null) {
            sensorData.getDeviceVersions().put(ModuleType.MODULE_A, version);
            offset += 5;
        } else {
            offset++;
        }
        version = parseVersion(Arrays.copyOfRange(data, offset, offset + 3), ModuleType.MODULE_B);
        if (version != null) {
            sensorData.getDeviceVersions().put(ModuleType.MODULE_B, version);
            offset += 3;
        } else {
            offset++;
        }
        version = parseVersion(Arrays.copyOfRange(data, offset, offset + 3), ModuleType.MODULE_C);
        if (version != null) {
            sensorData.getDeviceVersions().put(ModuleType.MODULE_C, version);
            offset += 3;
        } else {
            offset++;
        }
        version = parseVersion(Arrays.copyOfRange(data, offset, offset + 3), ModuleType.ECOLAMBDA);
        if (version != null) {
            sensorData.getDeviceVersions().put(ModuleType.ECOLAMBDA, version);
            offset += 3;
        } else {
            offset++;
        }
        version = parseVersion(Arrays.copyOfRange(data, offset, offset + 3), ModuleType.ECOSTER);
        if (version != null) {
            sensorData.getDeviceVersions().put(ModuleType.ECOSTER, version);
            offset += 3;
        } else {
            offset++;
        }
        version = parseVersion(Arrays.copyOfRange(data, offset, offset + 3), ModuleType.PANEL);
        if (version != null) {
            sensorData.getDeviceVersions().put(ModuleType.PANEL, version);
            offset += 3;
        } else {
            offset++;
        }

        return sensorData;
    }

    private static String parseVersion(byte[] data, ModuleType module) {
        if (data[0] == (byte) 0xFF) {
            return null;
        }
        if (module == ModuleType.MODULE_A) {
            return String.format("%d.%d.%d%s%d", Byte.toUnsignedInt(data[0]), Byte.toUnsignedInt(data[1]),
                    Byte.toUnsignedInt(data[2]), (char) data[3], Byte.toUnsignedInt(data[4]));
        }
        return String.format("%d.%d.%d", Byte.toUnsignedInt(data[0]), Byte.toUnsignedInt(data[1]),
                Byte.toUnsignedInt(data[2]));
    }
}
