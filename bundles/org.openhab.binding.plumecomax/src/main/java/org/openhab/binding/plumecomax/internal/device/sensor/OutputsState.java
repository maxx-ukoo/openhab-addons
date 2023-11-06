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
public class OutputsState {

    private boolean fan;
    private boolean feeder;
    private boolean heating_pump;
    private boolean water_heater_pump;
    private boolean circulation_pump;
    private boolean lighter;
    private boolean alarm;
    private boolean outer_boiler;
    private boolean fan2_exhaust;
    private boolean feeder2;
    private boolean outer_feeder;
    private boolean solar_pump;
    private boolean fireplace_pump;
    private boolean gcz_contactp;
    private boolean blow_fan1;
    private boolean blow_fan2;

    private long rawState;

    public OutputsState(byte[] state) {
        this.rawState = (Byte.toUnsignedLong(state[3]) << 24) + (Byte.toUnsignedLong(state[2]) << 16)
                + (Byte.toUnsignedLong(state[1]) << 8) + Byte.toUnsignedLong(state[0]);

        this.fan = (rawState & (1 << 0)) > 0;
        this.feeder = (rawState & (1 << 1)) > 0;
        this.heating_pump = (rawState & (1 << 2)) > 0;
        this.water_heater_pump = (rawState & (1 << 3)) > 0;
        this.circulation_pump = (rawState & (1 << 4)) > 0;
        this.lighter = (rawState & (1 << 5)) > 0;
        this.alarm = (rawState & (1 << 6)) > 0;
        this.outer_boiler = (rawState & (1 << 7)) > 0;
        this.fan2_exhaust = (rawState & (1 << 8)) > 0;
        this.feeder2 = (rawState & (1 << 9)) > 0;
        this.outer_feeder = (rawState & (1 << 10)) > 0;
        this.solar_pump = (rawState & (1 << 11)) > 0;
        this.fireplace_pump = (rawState & (1 << 12)) > 0;
        this.gcz_contactp = (rawState & (1 << 13)) > 0;
        this.blow_fan1 = (rawState & (1 << 14)) > 0;
        this.blow_fan2 = (rawState & (1 << 15)) > 0;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("OutputsState{");
        sb.append("fan=").append(fan);
        sb.append(", feeder=").append(feeder);
        sb.append(", heating_pump=").append(heating_pump);
        sb.append(", water_heater_pump=").append(water_heater_pump);
        sb.append(", circulation_pump=").append(circulation_pump);
        sb.append("\n lighter=").append(lighter);
        sb.append(", alarm=").append(alarm);
        sb.append(", outer_boiler=").append(outer_boiler);
        sb.append(", fan2_exhaust=").append(fan2_exhaust);
        sb.append(", feeder2=").append(feeder2);
        sb.append(", outer_feeder=").append(outer_feeder);
        sb.append("\n solar_pump=").append(solar_pump);
        sb.append(", fireplace_pump=").append(fireplace_pump);
        sb.append(", gcz_contactp=").append(gcz_contactp);
        sb.append(", blow_fan1=").append(blow_fan1);
        sb.append(", blow_fan2=").append(blow_fan2);
        sb.append(", rawState=").append(rawState);
        sb.append('}');
        return sb.toString();
    }
}
