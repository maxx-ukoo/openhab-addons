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
public class RequestStartMaster implements MessageBody, FrameBody {
    @Override
    public byte[] getMessageBody() {
        return new byte[] { FrameType.REQUEST_START_MASTER.getId() };
    }

    @Override
    public byte[] getMessageData() {
        return new byte[] {};
    }

    @Override
    public FrameType getFrameType() {
        return FrameType.REQUEST_START_MASTER;
    }
}
