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

/**
 * The {@link FrameType} list all knoiwn RAW frames
 *
 * @author Maksym Krasovskyi - Initial contribution
 */
public enum FrameType {
    REQUEST_STOP_MASTER(24),
    REQUEST_START_MASTER(25),
    REQUEST_CHECK_DEVICE(48),
    REQUEST_ECOMAX_PARAMETERS(49),
    REQUEST_MIXER_PARAMETERS(50),
    REQUEST_SET_ECOMAX_PARAMETER(51),
    REQUEST_SET_MIXER_PARAMETER(52),
    REQUEST_SCHEDULES(54),
    REQUEST_SET_SCHEDULE(55),
    REQUEST_UID(57),
    REQUEST_PASSWORD(58),
    REQUEST_ECOMAX_CONTROL(59),
    REQUEST_ALERTS(61),
    REQUEST_PROGRAM_VERSION(64),
    REQUEST_DATA_SCHEMA(85),
    REQUEST_THERMOSTAT_PARAMETERS(92),
    REQUEST_SET_THERMOSTAT_PARAMETER(93),
    RESPONSE_DEVICE_AVAILABLE(176),
    RESPONSE_ECOMAX_PARAMETERS(177),
    RESPONSE_MIXER_PARAMETERS(178),
    RESPONSE_SET_ECOMAX_PARAMETER(179),
    RESPONSE_SET_MIXER_PARAMETER(180),
    RESPONSE_SCHEDULES(182),
    RESPONSE_UID(185),
    RESPONSE_PASSWORD(186),
    RESPONSE_ECOMAX_CONTROL(187),
    RESPONSE_ALERTS(189),
    RESPONSE_PROGRAM_VERSION(192),
    RESPONSE_DATA_SCHEMA(213),
    RESPONSE_THERMOSTAT_PARAMETERS(220),
    RESPONSE_SET_THERMOSTAT_PARAMETER(221),
    MESSAGE_REGULATOR_DATA(8),
    MESSAGE_SENSOR_DATA(53),
    FRAME_TYPE_UNKNOWN(0);

    private final int type;

    FrameType(int type) {
        this.type = type;
    }

    public static FrameType getType(int type) {
        for (FrameType v : values()) {
            if (v.type == type) {
                return v;
            }
        }
        return FRAME_TYPE_UNKNOWN;
    }

    public byte getId() {
        return (byte) type;
    }
}
