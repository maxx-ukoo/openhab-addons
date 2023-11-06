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
package org.openhab.binding.plumecomax.internal.device.protocol;

/**
 * @author Maksym Krasovskyi - Initial contribution
 */
public enum DeviceState {
    OFF(0),
    STABILIZATION(1),
    KINDLING(2),
    WORKING(3),
    SUPERVISION(4),
    PAUSED(5),

    // 27 - 99% float????
    STANDBY(6),
    BURNING_OFF(7),
    ALERT(8),
    MANUAL(9),
    UNSEALING(10),
    OTHER(11);

    private final int stateId;

    DeviceState(int stateId) {
        this.stateId = stateId;
    }

    public static DeviceState getDeviceState(int stateId) {
        try {
            Integer stateValue = (Integer) stateId;
            for (DeviceState v : values()) {
                if (v.stateId == stateValue) {
                    return v;
                }
            }
        } catch (Exception e) {

        }
        return OTHER;
    }
}
