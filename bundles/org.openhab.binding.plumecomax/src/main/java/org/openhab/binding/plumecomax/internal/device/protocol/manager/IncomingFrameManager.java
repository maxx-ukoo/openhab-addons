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
package org.openhab.binding.plumecomax.internal.device.protocol.manager;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.plumecomax.internal.device.EcoMaxDevice;
import org.openhab.binding.plumecomax.internal.device.protocol.frame.DeviceType;
import org.openhab.binding.plumecomax.internal.device.protocol.frame.Frame;
import org.openhab.binding.plumecomax.internal.device.protocol.frame.FrameType;
import org.openhab.binding.plumecomax.internal.device.protocol.frame.message.MessageRegulatorData;
import org.openhab.binding.plumecomax.internal.device.protocol.frame.message.MessageSensorData;
import org.openhab.binding.plumecomax.internal.device.protocol.frame.request.RequestDataSchema;
import org.openhab.binding.plumecomax.internal.device.protocol.frame.request.RequestStartMaster;
import org.openhab.binding.plumecomax.internal.device.protocol.frame.response.ResponseDataSchema;
import org.openhab.binding.plumecomax.internal.device.protocol.frame.response.ResponseDeviceAvailable;
import org.openhab.binding.plumecomax.internal.device.protocol.frame.response.ResponseProgramVersion;
import org.openhab.binding.plumecomax.internal.device.sensor.SensorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Maksym Krasovskyi - Initial contribution
 */
@NonNullByDefault
public class IncomingFrameManager {

    private final byte[] EMPTY_RESPONSE = new byte[] {};
    private final Logger logger = LoggerFactory.getLogger(IncomingFrameManager.class);

    private final EcoMaxDevice device;

    public IncomingFrameManager(EcoMaxDevice device) {
        this.device = device;
    }

    public byte[] processFrame(Frame frame) {
        if (!(frame.getDestination() == DeviceType.ALL || frame.getDestination() == DeviceType.ECONET)) {
            logger.trace("Skip due another destination");
            if (frame.getFrameType() != FrameType.MESSAGE_SENSOR_DATA) {
                return EMPTY_RESPONSE;
            }
            logger.debug("MESSAGE_SENSOR_DATA not skipped");
        }
        logger.debug("Processing frame: {}", frame);
        switch (frame.getFrameType()) {
            case REQUEST_PROGRAM_VERSION -> {
                return processRequestProgramVersion(frame);
            }
            case REQUEST_CHECK_DEVICE -> {
                return processRequestCheckDevice(frame);
            }
            case MESSAGE_SENSOR_DATA -> {
                return processMessageSensorData(frame);
            }
            case MESSAGE_REGULATOR_DATA -> {
                return processMessageRegulatorData(frame);
            }
            case RESPONSE_DATA_SCHEMA -> {
                return processDataSchema(frame);
            }
            default -> logger.debug("Non implemented frame: {}", frame.getFrameType());
        }
        return EMPTY_RESPONSE;
    }

    private byte[] processMessageSensorData(Frame frame) {
        logger.debug("Processing sensor data frame");
        logger.debug("SensorData frame: {}", Frame.getRAWFrameAsString(frame));
        SensorData sensorData = MessageSensorData.fromByteArray(frame.getBody());
        device.setSensorData(sensorData);
        return EMPTY_RESPONSE;
    }

    private byte[] processDataSchema(Frame response) {
        ResponseDataSchema schemaResponse = ResponseDataSchema.fromByteArray(response.getBody());
        device.getDeviceData().setSchema(schemaResponse.getSchema());
        return EMPTY_RESPONSE;
    }

    private byte[] processMessageRegulatorData(Frame message) {
        if (device.getDeviceData().isSchemaValid()) {
            MessageRegulatorData.fromByteArray(device.getDeviceData(), message.getBody());
        } else {
            logger.warn("Skip processing regulator data due no schema");
        }
        return EMPTY_RESPONSE;
    }

    private byte[] processRequestCheckDevice(Frame request) {
        ResponseDeviceAvailable responseBody = new ResponseDeviceAvailable();
        Frame response = Frame.builder().setDestination(DeviceType.ECOMAX).setSource(DeviceType.ECONET)
                .setFrameBody(responseBody).buildResponse();
        logger.trace("{}", Frame.getRAWFrameAsString(response));
        if (!device.getDeviceData().isSchemaValid()) {

            RequestStartMaster bodyResponse1 = new RequestStartMaster();
            Frame frame1 = Frame.builder().setDestination(DeviceType.ECOMAX).setSource(DeviceType.ECONET)
                    .setFrameBody(bodyResponse1).buildResponse();

            logger.trace("Check device response: {}", Frame.getRAWFrameAsString(frame1));
            EcoMaxDevice.queueFrame.add(frame1);

            RequestDataSchema bodyResponse2 = new RequestDataSchema();
            frame1 = Frame.builder().setDestination(DeviceType.ECOMAX).setSource(DeviceType.ECONET)
                    .setFrameBody(bodyResponse2).buildResponse();
            logger.trace("Check device response: {}", Frame.getRAWFrameAsString(frame1));
            EcoMaxDevice.queueFrame.add(frame1);
        }
        return response.getRAWFrame();
    }

    private byte[] processRequestProgramVersion(Frame request) {
        ResponseProgramVersion responseBody = new ResponseProgramVersion(request.getDestination().getId());
        Frame response = Frame.builder().setDestination(DeviceType.ECOMAX).setSource(DeviceType.ECONET)
                .setFrameBody(responseBody).buildResponse();
        logger.trace("{}", Frame.getRAWFrameAsString(response));
        return response.getRAWFrame();
    }
}
