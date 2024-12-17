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
package org.openhab.binding.plumecomax.internal.device.protocol.schema;

/**
 * @author Maksym Krasovskyi - Initial contribution
 */
public enum DataValue {
    UNKNOWN(0),
    HEATING_TARGET(1280),
    TEMPERATURE_BOILER(1024),
    TEMPERATURE_FEEDER(1026),
    TEMPERATURE_RETURN(28),
    TEMPERATURE_EXHAUST(1030),
    TEMPERATURE_UPPER_BUFFER_UP(1028),
    TEMPERATURE_LOVER_BUFFER_UP(1029),
    TEMPERATURE_OUTDOOR(1027),
    DEVICE_STATE(1792),
    FUEL_LEVEL(96),
    FLAME_LEVEL(27);

    public int getValueId() {
        return valueId;
    }

    private int valueId;

    DataValue(int valueId) {
        this.valueId = valueId;
    }

    public static DataValue getDataType(int valueId) {
        for (DataValue v : values()) {
            if (v.valueId == valueId) {
                return v;
            }
        }
        return UNKNOWN;
    }
}
