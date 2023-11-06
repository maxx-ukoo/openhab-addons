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

/**
 * @author Maksym Krasovskyi - Initial contribution
 */
public enum EcomaxParameter {
    HEATING_TARGET(0x62);

    private final int parameterId;

    EcomaxParameter(int parameterId) {
        this.parameterId = parameterId;
    }

    public int getParameterId() {
        return parameterId;
    }
}
