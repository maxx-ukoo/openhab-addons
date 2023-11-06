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
package org.openhab.binding.plumecomax.internal.device.protocol.frame.request;

import org.openhab.binding.plumecomax.internal.device.protocol.frame.FrameBody;
import org.openhab.binding.plumecomax.internal.device.protocol.frame.FrameType;
import org.openhab.binding.plumecomax.internal.device.protocol.frame.MessageBody;

/**
 * @author Maksym Krasovskyi - Initial contribution
 */
public class EcomaxParameterRequest implements FrameBody, MessageBody {

    private final EcomaxParameter parameter;
    private final int parameterValue;

    public EcomaxParameterRequest(EcomaxParameter parameter, int parameterValue) {
        this.parameter = parameter;
        this.parameterValue = parameterValue;
    }

    @Override
    public byte[] getMessageData() {
        return new byte[] { (byte) parameter.getParameterId(), (byte) parameterValue };
    }

    @Override
    public FrameType getFrameType() {
        return FrameType.REQUEST_SET_ECOMAX_PARAMETER;
    }

    @Override
    public byte[] getMessageBody() {
        return new byte[] { FrameType.REQUEST_SET_ECOMAX_PARAMETER.getId() };
    }
}
