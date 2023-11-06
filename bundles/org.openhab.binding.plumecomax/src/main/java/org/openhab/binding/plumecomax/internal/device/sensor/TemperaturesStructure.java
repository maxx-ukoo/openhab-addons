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
public class TemperaturesStructure {
    public static int SIZE = 5;
    private float heating_temp;
    private float feeder_temp;
    private float water_heater_temp;
    private float outside_temp;
    private float return_temp;
    private float exhaust_temp;
    private float optical_temp;
    private float upper_buffer_temp;
    private float lower_buffer_temp;
    private float upper_solar_temp;
    private float lower_solar_temp;
    private float fireplace_temp;
    private float total_gain;
    private float hydraulic_coupler_temp;
    private float exchanger_temp;
    private float air_in_temp;
    private float air_out_temp;

    public TemperaturesStructure(byte[] data) {
        int temperatureNumber = Byte.toUnsignedInt(data[0]);
        for (int i = 0; i < temperatureNumber; i++) {
            int index = Byte.toUnsignedInt(data[5 * i + 1]);
            float value = Float.intBitsToFloat((((data[5 * i + 5] & 0xff) << 24) | ((data[5 * i + 4] & 0xff) << 16)
                    | ((data[5 * i + 3] & 0xff) << 8) | (data[5 * i + 2] & 0xff)));

            switch (index) {
                case 0 -> heating_temp = value;
                case 1 -> feeder_temp = value;
                case 2 -> water_heater_temp = value;
                case 3 -> outside_temp = value;
                case 4 -> return_temp = value;
                case 5 -> exhaust_temp = value;
                case 6 -> optical_temp = value;
                case 7 -> upper_buffer_temp = value;
                case 8 -> lower_buffer_temp = value;
                case 9 -> upper_solar_temp = value;
                case 10 -> lower_solar_temp = value;
                case 11 -> fireplace_temp = value;
                case 12 -> total_gain = value;
                case 13 -> hydraulic_coupler_temp = value;
                case 14 -> exchanger_temp = value;
                case 15 -> air_in_temp = value;
                case 16 -> air_out_temp = value;
                default -> System.out.printf("Can't parce temp with index %d\n", index);
            }
        }
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("TemperaturesStructure{");
        sb.append("heating_temp=").append(heating_temp);
        sb.append(", feeder_temp=").append(feeder_temp);
        sb.append(", water_heater_temp=").append(water_heater_temp);
        sb.append(", outside_temp=").append(outside_temp);
        sb.append(", return_temp=").append(return_temp);
        sb.append("\n exhaust_temp=").append(exhaust_temp);
        sb.append(", optical_temp=").append(optical_temp);
        sb.append(", upper_buffer_temp=").append(upper_buffer_temp);
        sb.append(", lower_buffer_temp=").append(lower_buffer_temp);
        sb.append(", upper_solar_temp=").append(upper_solar_temp);
        sb.append(", lower_solar_temp=").append(lower_solar_temp);
        sb.append("\n fireplace_temp=").append(fireplace_temp);
        sb.append(", total_gain=").append(total_gain);
        sb.append(", hydraulic_coupler_temp=").append(hydraulic_coupler_temp);
        sb.append(", exchanger_temp=").append(exchanger_temp);
        sb.append(", air_in_temp=").append(air_in_temp);
        sb.append(", air_out_temp=").append(air_out_temp);
        sb.append('}');
        return sb.toString();
    }
}
