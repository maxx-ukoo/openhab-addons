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
package org.openhab.binding.plumecomax.internal.device.protocol.frame.response;

import java.util.ArrayList;
import java.util.List;

import org.openhab.binding.plumecomax.internal.device.protocol.frame.FrameType;
import org.openhab.binding.plumecomax.internal.device.protocol.schema.DataType;
import org.openhab.binding.plumecomax.internal.device.protocol.schema.SchemaType;

/**
 * @author Maksym Krasovskyi - Initial contribution
 */
public class ResponseDataSchema {

    private final static int ITEM_SIZE = 3;

    public List<SchemaType> getSchema() {
        return schema;
    }

    private List<SchemaType> schema = new ArrayList<>();

    public ResponseDataSchema() {
    }

    public static ResponseDataSchema fromByteArray(byte[] data) {
        int offset = 0;
        if (data[offset++] != FrameType.RESPONSE_DATA_SCHEMA.getId()) {
            throw new IllegalArgumentException(String.format("Wrong frame type: %02X", data[offset]));
        }
        ResponseDataSchema schemaResponse = new ResponseDataSchema();
        int count = Byte.toUnsignedInt(data[offset]) | Byte.toUnsignedInt(data[offset + 1]) << 8;
        for (int i = 0; i < count; i++) {
            offset = ITEM_SIZE * i + 3;
            DataType paramType = DataType.getDataType(Byte.toUnsignedInt(data[offset]));
            int paramId;
            if ((offset + 2) == data.length) {
                paramId = Byte.toUnsignedInt(data[offset + 1]);
            } else {
                paramId = Byte.toUnsignedInt(data[offset + 1]) | Byte.toUnsignedInt(data[offset + 2]) << 8;
            }
            SchemaType schemaValue = new SchemaType(paramType, paramId);
            schemaResponse.schema.add(schemaValue);
        }
        return schemaResponse;
    }
}
