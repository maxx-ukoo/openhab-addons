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
package org.openhab.binding.plumecomax.internal.device;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.openhab.binding.plumecomax.internal.device.protocol.schema.DataValue;
import org.openhab.binding.plumecomax.internal.device.protocol.schema.SchemaType;
import org.openhab.binding.plumecomax.internal.device.protocol.schema.SchemaValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Maksym Krasovskyi - Initial contribution
 */
public class DeviceData {

    private final Logger logger = LoggerFactory.getLogger(DeviceData.class);

    private static final int VALID_DATA_TIME = 30000;

    private long lastUpdateTime = 0;

    private List<SchemaType> schema = new ArrayList<>();
    private Map<SchemaType, SchemaValue> data = new HashMap<>();

    public boolean isDataValid() {
        if (schema.size() == 0) {
            // logger.info("Size id zero, data not available");
            return false;
        }
        // logger.info("Time check: {} - {} , {}", System.currentTimeMillis(), lastUpdateTime, VALID_DATA_TIME);
        return (System.currentTimeMillis() - lastUpdateTime) < VALID_DATA_TIME;
    }

    public boolean isSchemaValid() {
        return schema.size() > 0;
    }

    public void setSchema(List<SchemaType> schema) {
        this.schema.clear();
        lastUpdateTime = 0;
        if (schema == null || schema.size() == 0) {
            return;
        }
        for (SchemaType value : schema) {
            SchemaType localValue = new SchemaType(value.getType(), value.getValueId());
            this.schema.add(localValue);
        }
    }

    public List<SchemaType> getSchema() {
        return Collections.unmodifiableList(schema);
    }

    public Object

            getValue(int valueId) {
        Optional<SchemaValue> value = data.keySet().stream().filter(key -> key.getValueId() == valueId)
                .map(key -> data.get(key)).findFirst();
        if (value.isPresent()) {
            return value.get().getValue();
        }
        return null;
    }

    public Object getValue(DataValue dataType) {
        return getValue(dataType.getValueId());
    }

    public void setValue(SchemaValue schemaValue) {
        data.put(schemaValue.getType(), schemaValue);
        lastUpdateTime = System.currentTimeMillis();
    }
}
