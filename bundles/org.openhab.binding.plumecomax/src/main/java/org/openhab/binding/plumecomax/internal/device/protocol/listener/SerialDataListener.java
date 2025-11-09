/*
 * Copyright (c) 2010-2025 Contributors to the openHAB project
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
package org.openhab.binding.plumecomax.internal.device.protocol.listener;

import org.openhab.binding.plumecomax.internal.device.protocol.frame.Frame;
import org.openhab.binding.plumecomax.internal.device.protocol.manager.IncomingFrameManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Maksym Krasovskyi - Initial contribution
 */
public class SerialDataListener extends AbstractSerialListener {

    private final Logger logger = LoggerFactory.getLogger(SerialDataListener.class);

    private final IncomingFrameManager frameManager;

    public SerialDataListener(IncomingFrameManager frameManager) {
        this.frameManager = frameManager;
    }

    byte[] processFrame(Frame frame) {
        return frameManager.processFrame(frame);
    }
}
