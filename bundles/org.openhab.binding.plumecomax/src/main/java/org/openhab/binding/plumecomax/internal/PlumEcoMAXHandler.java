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
package org.openhab.binding.plumecomax.internal;

import static org.openhab.binding.plumecomax.internal.PlumEcoMAXBindingConstants.BOILER_STATE;
import static org.openhab.binding.plumecomax.internal.PlumEcoMAXBindingConstants.BOILER_TEMP;
import static org.openhab.binding.plumecomax.internal.PlumEcoMAXBindingConstants.BOILER_TEMP_TARGET;
import static org.openhab.binding.plumecomax.internal.PlumEcoMAXBindingConstants.EXHAUST_TEMP;
import static org.openhab.binding.plumecomax.internal.PlumEcoMAXBindingConstants.FAN_POWER;
import static org.openhab.binding.plumecomax.internal.PlumEcoMAXBindingConstants.FEEDER_TEMP;
import static org.openhab.binding.plumecomax.internal.PlumEcoMAXBindingConstants.FLAME_INTENSITY;
import static org.openhab.binding.plumecomax.internal.PlumEcoMAXBindingConstants.FUEL_CONSUMPTION;
import static org.openhab.binding.plumecomax.internal.PlumEcoMAXBindingConstants.FUEL_LEVEL;
import static org.openhab.binding.plumecomax.internal.PlumEcoMAXBindingConstants.HEATING_LOAD;
import static org.openhab.binding.plumecomax.internal.PlumEcoMAXBindingConstants.HEATING_MODE;
import static org.openhab.binding.plumecomax.internal.PlumEcoMAXBindingConstants.HEATING_POWER;
import static org.openhab.binding.plumecomax.internal.PlumEcoMAXBindingConstants.LOWER_BUFFER_TEMP;
import static org.openhab.binding.plumecomax.internal.PlumEcoMAXBindingConstants.OUTSIDE_TEMP;
import static org.openhab.binding.plumecomax.internal.PlumEcoMAXBindingConstants.RETURN_TEMP;
import static org.openhab.binding.plumecomax.internal.PlumEcoMAXBindingConstants.THING_TYPE_PLUM_ECOMAX_CONTROLLER;
import static org.openhab.binding.plumecomax.internal.PlumEcoMAXBindingConstants.UPPER_BUFFER_TEMP;
import static org.openhab.binding.plumecomax.internal.PlumEcoMAXBindingConstants.WATER_HEATER;
import static org.openhab.binding.plumecomax.internal.PlumEcoMAXBindingConstants.WATER_HEATER_TARGET;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.plumecomax.internal.device.EcoMaxDevice;
import org.openhab.binding.plumecomax.internal.device.protocol.frame.ControlState;
import org.openhab.binding.plumecomax.internal.device.protocol.frame.DeviceType;
import org.openhab.binding.plumecomax.internal.device.protocol.frame.Frame;
import org.openhab.binding.plumecomax.internal.device.protocol.frame.request.EcomaxParameter;
import org.openhab.binding.plumecomax.internal.device.protocol.frame.request.EcomaxParameterRequest;
import org.openhab.binding.plumecomax.internal.device.protocol.frame.request.RequestEcoMaxControl;
import org.openhab.binding.plumecomax.internal.device.protocol.schema.DataValue;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.unit.SIUnits;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link PlumEcoMAXHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Maksym Krasovskyi - Initial contribution
 */
@NonNullByDefault
public class PlumEcoMAXHandler extends BaseThingHandler {

    private final Logger logger = LoggerFactory.getLogger(PlumEcoMAXHandler.class);

    private @Nullable PlumEcoMAXConfiguration config;

    private @Nullable ScheduledFuture<?> pollingJob1;
    private @Nullable ScheduledFuture<?> pollingJob2;

    private @Nullable EcoMaxDevice device;

    public PlumEcoMAXHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        // if (CHANNEL_STATE.equals(channelUID.getId())) {
        // if (command instanceof RefreshType) {
        // logger.info("Command {} on unknown channel {}, {}", command.toFullString(), channelUID.getId(),
        // channelUID);
        // }

        // TODO: handle command

        // Note: if communication with thing fails for some reason,
        // indicate that by setting the status with detail information:
        // updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
        // "Could not control device at IP address x.x.x.x");
        // }
        logger.info("Got command: {} for channel: {}", command, channelUID.getId());
        switch (channelUID.getId()) {
            case BOILER_STATE:
                logger.info("Got power command: {}", command);
                ControlState newControlState = command == OnOffType.ON ? ControlState.ON : ControlState.OFF;
                logger.info("Got power command: {}, will send to device: {}", command, newControlState);
                RequestEcoMaxControl controlMessage = new RequestEcoMaxControl(newControlState);
                Frame controlFrame = Frame.builder().setDestination(DeviceType.ECOMAX).setSource(DeviceType.ECONET)
                        .setFrameBody(controlMessage).buildResponse();
                EcoMaxDevice.queueFrame.add(controlFrame);
                break;
            case BOILER_TEMP_TARGET:
                logger.trace("Got boiler_temp_target command: {}, {}", command, command.getClass());
                if (command instanceof QuantityType) {
                    QuantityType<?> convertedCommand = ((QuantityType<?>) command).toUnit(SIUnits.CELSIUS);
                    logger.trace("Boiler_temp_target got value: {}", convertedCommand.intValue());

                    EcomaxParameterRequest parameterRequest = new EcomaxParameterRequest(EcomaxParameter.HEATING_TARGET,
                            convertedCommand.intValue());
                    Frame parameterFrame = Frame.builder().setDestination(DeviceType.ECOMAX)
                            .setSource(DeviceType.ECONET).setFrameBody(parameterRequest).buildResponse();
                    EcoMaxDevice.queueFrame.add(parameterFrame);
                }
                break;
            default:
                logger.info("Command {} on unknown channel {}, {}", command.toFullString(), channelUID.getId(),
                        channelUID);
        }
    }

    @Override
    public void initialize() {
        config = getConfigAs(PlumEcoMAXConfiguration.class);
        if (config.serialPort.isEmpty()) {
            logger.warn("PlumEcomax serial port not configured");
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_PENDING, "Serial port not configured");
            return;
        }

        // TODO: Initialize the handler.
        // The framework requires you to return from this method quickly, i.e. any network access must be done in
        // the background initialization below.
        // Also, before leaving this method a thing status from one of ONLINE, OFFLINE or UNKNOWN must be set. This
        // might already be the real thing status in case you can decide it directly.
        // In case you can not decide the thing status directly (e.g. for long running connection handshake using WAN
        // access or similar) you should set status UNKNOWN here and then decide the real status asynchronously in the
        // background.

        // set the thing status to UNKNOWN temporarily and let the background task decide for the real status.
        // the framework is then able to reuse the resources from the thing handler initialization.
        // we set this upfront to reliably check status updates in unit tests.
        updateStatus(ThingStatus.UNKNOWN);

        // Example for background initialization:
        scheduler.execute(() -> {
            device = new EcoMaxDevice(config.serialPort);
            boolean status = device.init();
            if (status) {
                updateStatus(ThingStatus.UNKNOWN);
            } else {
                updateStatus(ThingStatus.OFFLINE);
            }
            pollingJob1 = scheduler.scheduleWithFixedDelay(this::pollingCode, 0, 30, TimeUnit.SECONDS);
            pollingJob2 = scheduler.scheduleWithFixedDelay(this::pollingFrameQueueCode, 0, 1, TimeUnit.SECONDS);
        });

        // These logging types should be primarily used by bindings
        // logger.trace("Example trace message");
        // logger.debug("Example debug message");
        // logger.warn("Example warn message");
        //
        // Logging to INFO should be avoided normally.
        // See https://www.openhab.org/docs/developer/guidelines.html#f-logging

        // Note: When initialization can NOT be done set the status with more details for further
        // analysis. See also class ThingStatusDetail for all available status details.
        // Add a description to give user information to understand why thing does not work as expected. E.g.
        // updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
        // "Can not access device as username and/or password are invalid");
    }

    private void pollingFrameQueueCode() {
        Frame frame = EcoMaxDevice.queueFrame.poll();
        if (frame != null) {
            byte[] rawFrame = frame.getRAWFrame();
            device.getSerialPort().writeBytes(rawFrame, rawFrame.length);
        }
    }

    @Override
    public void dispose() {
        final ScheduledFuture<?> job1 = pollingJob1;
        if (job1 != null) {
            job1.cancel(true);
            pollingJob1 = null;
        }
        final ScheduledFuture<?> job2 = pollingJob2;
        if (job2 != null) {
            job2.cancel(true);
            pollingJob2 = null;
        }
        device.getSerialPort().closePort();
    }

    private void pollingCode() {
        logger.trace("Refresh EcoMAX state");
        if (device.isAvailable()) {
            updateStatus(ThingStatus.ONLINE);
        } else {
            updateStatus(ThingStatus.OFFLINE);
            return;
        }
        logger.trace("Publish EcoMAX channels values");
        if (device.getDeviceData().isDataValid()) {
            List<Channel> channels = getThing().getChannels();
            for (Channel channel : channels) {
                publishChannel(channel.getUID());
            }
        }
    }

    private void publishChannel(ChannelUID channelUID) {
        String channelID = channelUID.getId();
        try {
            // logger.info("Publishing channel: {}", channelUID.getIdWithoutGroup());
            State state = switch (channelUID.getIdWithoutGroup()) {
                case BOILER_TEMP ->
                    fromFloatValue((Float) device.getDeviceData().getValue(DataValue.TEMPERATURE_BOILER));
                case WATER_HEATER -> new QuantityType<>(42, SIUnits.CELSIUS); // TODO
                case OUTSIDE_TEMP ->
                    fromFloatValue((Float) device.getDeviceData().getValue(DataValue.TEMPERATURE_OUTDOOR));
                case BOILER_TEMP_TARGET ->
                    fromIntValue((Integer) device.getDeviceData().getValue(DataValue.HEATING_TARGET));
                case WATER_HEATER_TARGET -> new DecimalType(DecimalType.valueOf("42"));
                case HEATING_MODE -> new StringType(device.getSensorData().getDeviceState().toString());
                case EXHAUST_TEMP -> new DecimalType(DecimalType.valueOf("142"));
                case FEEDER_TEMP ->
                    fromFloatValue((Float) device.getDeviceData().getValue(DataValue.TEMPERATURE_FEEDER));
                case RETURN_TEMP ->
                    fromFloatValue((Float) device.getDeviceData().getValue(DataValue.TEMPERATURE_RETURN));
                case HEATING_LOAD -> fromIntValue(device.getSensorData().getLoad());
                case FAN_POWER -> fromFloatValue(device.getSensorData().getFanPower());
                case FUEL_LEVEL -> new DecimalType(device.getDeviceData().getValue(DataValue.FUEL_LEVEL).toString());
                case FUEL_CONSUMPTION -> fromFloatValue(device.getSensorData().getFuel_consumption());
                case HEATING_POWER -> fromFloatValue(device.getSensorData().getPower());
                case LOWER_BUFFER_TEMP ->
                    fromFloatValue((Float) device.getDeviceData().getValue(DataValue.TEMPERATURE_LOVER_BUFFER_UP));
                case UPPER_BUFFER_TEMP ->
                    fromFloatValue((Float) device.getDeviceData().getValue(DataValue.TEMPERATURE_UPPER_BUFFER_UP));
                case FLAME_INTENSITY -> fromFloatValue((Float) device.getDeviceData().getValue(DataValue.FLAME_LEVEL));
                default -> null;
            };
            logger.trace("Got state value: {}", state);
            if (state != null) {
                logger.debug("{}: Updating channel {} : {}", THING_TYPE_PLUM_ECOMAX_CONTROLLER.getId(), channelID,
                        state);
                updateState(channelID, state);
            }
        } catch (Exception e) {
            logger.warn("{}: {}", THING_TYPE_PLUM_ECOMAX_CONTROLLER.getId(), channelID, e);
        }
    }

    private DecimalType fromFloatValue(Float value) {
        String strVal = value.toString();
        logger.trace("Convert float {} to BigDecimal", strVal);
        return DecimalType.valueOf(strVal);
    }

    private DecimalType fromIntValue(Integer value) {
        return DecimalType.valueOf(String.format("%d", value));
    }
}
