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

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link PlumEcoMAXBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Maksym Krasovskyi - Initial contribution
 */
@NonNullByDefault
public class PlumEcoMAXBindingConstants {

    private static final String BINDING_ID = "plumecomax";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_PLUM_ECOMAX_CONTROLLER = new ThingTypeUID(BINDING_ID,
            "ecomaxController");

    // List of all Channel ids
    public static final String BOILER_TEMP = "boiler_temp";
    public static final String WATER_HEATER = "water_heater";
    public static final String OUTSIDE_TEMP = "outside_temp";
    public static final String BOILER_TEMP_TARGET = "boiler_temp_target";
    public static final String WATER_HEATER_TARGET = "water_heater_target";
    public static final String HEATING_MODE = "heating_mode";
    public static final String EXHAUST_TEMP = "exhaust_temp";
    public static final String FEEDER_TEMP = "feeder_temp";
    public static final String RETURN_TEMP = "return_temp";
    public static final String HEATING_LOAD = "heating_load";
    public static final String FAN_POWER = "fan_power";
    public static final String FUEL_LEVEL = "fuel_level";
    public static final String FUEL_CONSUMPTION = "fuel_consumption";
    public static final String HEATING_POWER = "heating_power";
    public static final String LOWER_BUFFER_TEMP = "lower_buffer_temp";
    public static final String UPPER_BUFFER_TEMP = "upper_buffer_temp";
    public static final String FLAME_INTENSITY = "flame_intensity";
    public static final String BOILER_STATE = "boiler_state";
}
