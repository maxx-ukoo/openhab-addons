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
public enum DataType {

    Undefined0(0),
    SignedChar(1),
    Short(2),
    Int(3),
    Byte(4),
    UnsignedShort(5),
    UnsignedInt(6),
    Float(7),
    Undefined8(8),
    Double(9),
    Boolean(10),
    String_A(11),
    String_B(12),
    Int64(13),
    UInt64(14),
    IPv4(15),
    IPv6(16);

    int typeId;

    DataType(int typeId) {
        this.typeId = typeId;
    }

    public static DataType getDataType(int typeId) {
        for (DataType v : values()) {
            if (v.typeId == typeId) {
                return v;
            }
        }
        return Undefined0;
    }
}
