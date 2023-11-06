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
package org.openhab.binding.plumecomax.internal.device.protocol.frame.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.plumecomax.internal.device.DeviceData;
import org.openhab.binding.plumecomax.internal.device.protocol.frame.FrameType;
import org.openhab.binding.plumecomax.internal.device.protocol.schema.SchemaType;
import org.openhab.binding.plumecomax.internal.device.protocol.schema.SchemaValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Maksym Krasovskyi - Initial contribution
 */
@NonNullByDefault
public class MessageRegulatorData {

    private static final Logger logger = LoggerFactory.getLogger(MessageRegulatorData.class);

    public static void fromByteArray(DeviceData deviceData, byte[] data) {
        int offset = 0;
        if (data[offset++] != FrameType.MESSAGE_REGULATOR_DATA.getId()) {
            throw new IllegalArgumentException(String.format("Wrong frame type: %02X", data[offset - 1]));
        }
        int frameVersion = Byte.toUnsignedInt(data[offset]) + (Byte.toUnsignedInt(data[offset + 1]) << 8);
        // System.out.println("Frame version: " + frameVersion);
        // TODO calculate version and check for update
        offset = 3;
        String dataVersion = String.format("%s.%s", data[offset + 1], data[offset]);
        // System.out.println(dataVersion);
        offset += 2;
        int versionSize = Byte.toUnsignedInt(data[offset++]);
        // System.out.println("Version size: " + versionSize);
        Map<FrameType, Integer> versionMap = new HashMap<>();
        for (int i = 0; i < versionSize; i++) {
            FrameType frameType = FrameType.getType(Byte.toUnsignedInt(data[offset]));
            int version = Byte.toUnsignedInt(data[offset + 1]) + (Byte.toUnsignedInt(data[offset + 2]) << 8);
            versionMap.put(frameType, version);
            offset += 3;
        }
        // System.out.println(versionMap);
        // int schemaPosition = 0;
        int dataPosition = offset;
        int booleanIndex = 0;
        int booleanByte = 0;
        List<SchemaType> schema = deviceData.getSchema();
        for (int schemaPosition = 0; schemaPosition < schema.size(); schemaPosition++) {
            SchemaType schemaType = schema.get(schemaPosition);
            Object value;
            // int valueId = schemaValue.getValueId();
            switch (schemaType.getType()) {
                case SignedChar:
                case Byte: {
                    value = Byte.toUnsignedInt(data[dataPosition++]);
                    break;
                }
                case Boolean: {
                    if (booleanIndex == 0) {
                        booleanByte = Byte.toUnsignedInt(data[dataPosition++]);

                    }
                    value = (booleanByte & (1 << booleanIndex)) > 0;
                    booleanIndex++;
                    if (booleanIndex == 8) {
                        booleanIndex = 0;
                    }
                    break;
                }
                case UnsignedShort:
                case Short: {
                    value = (Byte.toUnsignedInt(data[dataPosition + 1]) << 8) + Byte.toUnsignedInt(data[dataPosition]);
                    dataPosition += 2;
                    break;
                }
                case UnsignedInt:
                case Int: {
                    value = Byte.toUnsignedInt(data[dataPosition]) + (Byte.toUnsignedInt(data[dataPosition + 1]) << 8)
                            + Byte.toUnsignedInt(data[dataPosition + 16])
                            + (Byte.toUnsignedInt(data[dataPosition + 3]) << 24);
                    dataPosition += 4;
                    break;
                }
                case Float: {
                    value = Float.intBitsToFloat(
                            (((data[dataPosition + 3] & 0xff) << 24) | ((data[dataPosition + 2] & 0xff) << 16)
                                    | ((data[dataPosition + 1] & 0xff) << 8) | (data[dataPosition] & 0xff)));
                    dataPosition += 4;
                    break;
                }
                case Double: {
                    value = Double.longBitsToDouble((((data[dataPosition + 54] & 0xffL))
                            | ((data[dataPosition + 6] & 0xffL) << 48) | ((data[dataPosition + 5] & 0xffL) << 40)
                            | ((data[dataPosition + 4] & 0xffL) << 32))
                            + (((data[dataPosition + 3] & 0xffL) << 24) | ((data[dataPosition + 2] & 0xffL) << 16)
                                    | ((data[dataPosition + 1] & 0xff) << 8) | (data[dataPosition] & 0xff)));
                    dataPosition += 8;
                    break;
                }
                case String_A:
                case String_B: {
                    value = String.valueOf(data[dataPosition++]);
                    break;
                }
                case UInt64:
                case Int64: {
                    Long lval = (((data[dataPosition + 54] & 0xffL)) | ((data[dataPosition + 6] & 0xffL) << 48)
                            | ((data[dataPosition + 5] & 0xffL) << 40) | ((data[dataPosition + 4] & 0xffL) << 32))
                            + (((data[dataPosition + 3] & 0xffL) << 24) | ((data[dataPosition + 2] & 0xffL) << 16)
                                    | ((data[dataPosition + 1] & 0xffL) << 8) | (data[dataPosition] & 0xff));
                    value = lval;
                    dataPosition += 8;
                    break;
                }
                case IPv4: {
                    value = String.format("%d.%d.%d.%d", Byte.toUnsignedInt(data[dataPosition]),
                            Byte.toUnsignedInt(data[dataPosition + 1]), Byte.toUnsignedInt(data[dataPosition + 2]),
                            Byte.toUnsignedInt(data[dataPosition + 3]));
                    dataPosition += 4;
                    break;
                }
                case IPv6: {
                    value = String.format("%d.%d.%d.%d.%d.%d.%d.%d.%d.%d.%d.%d.%d.%d.%d.%d",
                            Byte.toUnsignedInt(data[dataPosition]), Byte.toUnsignedInt(data[dataPosition + 1]),
                            Byte.toUnsignedInt(data[dataPosition + 2]), Byte.toUnsignedInt(data[dataPosition + 3]),
                            Byte.toUnsignedInt(data[dataPosition + 4]), Byte.toUnsignedInt(data[dataPosition + 5]),
                            Byte.toUnsignedInt(data[dataPosition + 6]), Byte.toUnsignedInt(data[dataPosition + 7]),
                            Byte.toUnsignedInt(data[dataPosition + 8]), Byte.toUnsignedInt(data[dataPosition + 9]),
                            Byte.toUnsignedInt(data[dataPosition + 10]), Byte.toUnsignedInt(data[dataPosition + 11]),
                            Byte.toUnsignedInt(data[dataPosition + 12]), Byte.toUnsignedInt(data[dataPosition + 13]),
                            Byte.toUnsignedInt(data[dataPosition + 14]), Byte.toUnsignedInt(data[dataPosition + 15]));
                    dataPosition += 16;
                    break;
                }
                default: {
                    value = "Undefined";
                    logger.error("Can't parse value with type: {}", schemaType.getType());
                }
            }
            SchemaValue schemaValue = new SchemaValue(schemaType, value);
            deviceData.setValue(new SchemaValue(schemaType, value));
            logger.trace("Set value: {}", schemaValue);
            // logger.trace("Get value: {}", deviceData.getValue(DataValue.getDataType(schemaType.getValueId())));
            // System.out.println(schemaValue);
        }
    }

    private static float toFloatValue(byte[] data, int offset) {
        return Float.intBitsToFloat((((data[offset + 3] & 0xff) << 24) | ((data[offset + 2] & 0xff) << 16)
                | ((data[offset + 1] & 0xff) << 8) | (data[offset] & 0xff)));
    }
}
