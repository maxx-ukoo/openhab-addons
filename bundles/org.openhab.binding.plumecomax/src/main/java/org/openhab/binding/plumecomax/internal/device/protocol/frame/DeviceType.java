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
 * The {@link DeviceType} list all available devices
 *
 * @author Maksym Krasovskyi - Initial contribution
 */
public enum DeviceType {
    ALL(0),
    ECOMAX(69), // 0x45
    ECOSTER_PANEL(81),
    ECONET(86), // 0x56
    DEV_80(80),
    DEV_85(85),
    DEV_87(87),
    DEV_88(88),
    DEV_89(89);

    private final int type;

    DeviceType(int type) {
        this.type = type;
    }

    public static DeviceType getType(int type) {
        for (DeviceType v : values()) {
            if (v.type == type) {
                return v;
            }
        }
        throw new IllegalArgumentException(String.format("Unknown device type, %02X", type));
    }

    public byte getId() {
        return (byte) this.type;
    }
}
