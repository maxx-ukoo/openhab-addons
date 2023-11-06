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
package org.openhab.binding.plumecomax.internal.device.sensor;

/**
 * @author Maksym Krasovskyi - Initial contribution
 */
public class OutputFlags {
    private boolean heating_pump_flag;
    private boolean water_heater_pump_flag;
    private boolean circulation_pump_flag;
    private boolean solar_pump_flag;

    private long flags;

    public OutputFlags(byte[] rawFlags) {
        this.flags = (Byte.toUnsignedInt(rawFlags[3]) << 24) + (Byte.toUnsignedInt(rawFlags[2]) << 16)
                + (Byte.toUnsignedInt(rawFlags[1]) << 8) + Byte.toUnsignedInt(rawFlags[0]);
        this.heating_pump_flag = (flags & 0x04) > 0;
        this.water_heater_pump_flag = (flags & 0x08) > 0;
        this.circulation_pump_flag = (flags & 0x10) > 0;
        this.solar_pump_flag = (flags & 0x800) > 0;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("OutputFlags{");
        sb.append("heating_pump_flag=").append(heating_pump_flag);
        sb.append(", water_heater_pump_flag=").append(water_heater_pump_flag);
        sb.append(", circulation_pump_flag=").append(circulation_pump_flag);
        sb.append(", solar_pump_flag=").append(solar_pump_flag);
        sb.append(", flags=").append(flags);
        sb.append('}');
        return sb.toString();
    }
}
