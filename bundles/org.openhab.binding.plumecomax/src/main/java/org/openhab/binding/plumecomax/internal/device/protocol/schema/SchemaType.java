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

import java.util.Objects;

/**
 * @author Maksym Krasovskyi - Initial contribution
 */
public class SchemaType {
    private final DataType type;
    private final int valueId;

    public SchemaType(DataType type, int valueId) {
        this.type = type;
        this.valueId = valueId;
    }

    public DataType getType() {
        return type;
    }

    public int getValueId() {
        return valueId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SchemaType that = (SchemaType) o;
        return valueId == that.valueId && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, valueId);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SchemaDataType{");
        sb.append("type=").append(type);
        sb.append(", valueId=").append(valueId);
        sb.append('}');
        return sb.toString();
    }
}
