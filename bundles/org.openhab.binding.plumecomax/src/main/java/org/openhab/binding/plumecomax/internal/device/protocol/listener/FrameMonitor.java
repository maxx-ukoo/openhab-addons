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
package org.openhab.binding.plumecomax.internal.device.protocol.listener;

import org.openhab.binding.plumecomax.internal.device.protocol.frame.Frame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Maksym Krasovskyi - Initial contribution
 */
public class FrameMonitor extends AbstractSerialListener {

    private final Logger logger = LoggerFactory.getLogger(FrameMonitor.class);

    @Override
    byte[] processFrame(Frame frame) {
        Frame.printRAWFrame(frame);
        return new byte[0];
    }
}
