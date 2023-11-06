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
package org.openhab.binding.plumecomax.internal.device.sensor;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.plumecomax.internal.device.protocol.DeviceState;
import org.openhab.binding.plumecomax.internal.device.protocol.frame.FrameType;

/**
 * @author Maksym Krasovskyi - Initial contribution
 */
public class SensorData {

    private Map<FrameType, Integer> versionMap = new HashMap<>();
    private DeviceState deviceState;
    private OutputsState outputsState;
    private OutputFlags outputFlags;
    private TemperaturesStructure temperaturesStructure;

    private int heating_target;
    private int heating_status;
    private int water_heater_target;
    private int water_heater_status;

    private PendingAlerts pendingAlerts;
    private int fuel_level;
    private int transmission;
    private float fanPower;
    private int load;
    private float power;
    private float fuel_consumption;
    private int thermostat;
    private Map<ModuleType, String> deviceVersions = new HashMap<>();

    public Map<FrameType, Integer> getVersionMap() {
        return versionMap;
    }

    public void setVersionMap(Map<FrameType, Integer> versionMap) {
        this.versionMap = versionMap;
    }

    public DeviceState getDeviceState() {
        return deviceState;
    }

    public void setDeviceState(DeviceState deviceState) {
        this.deviceState = deviceState;
    }

    public OutputsState getOutputsState() {
        return outputsState;
    }

    public void setOutputsState(OutputsState outputsState) {
        this.outputsState = outputsState;
    }

    public OutputFlags getOutputFlags() {
        return outputFlags;
    }

    public void setOutputFlags(OutputFlags outputFlags) {
        this.outputFlags = outputFlags;
    }

    public TemperaturesStructure getTemperaturesStructure() {
        return temperaturesStructure;
    }

    public void setTemperaturesStructure(TemperaturesStructure temperaturesStructure) {
        this.temperaturesStructure = temperaturesStructure;
    }

    public int getHeating_target() {
        return heating_target;
    }

    public void setHeating_target(int heating_target) {
        this.heating_target = heating_target;
    }

    public int getHeating_status() {
        return heating_status;
    }

    public void setHeating_status(int heating_status) {
        this.heating_status = heating_status;
    }

    public int getWater_heater_target() {
        return water_heater_target;
    }

    public void setWater_heater_target(int water_heater_target) {
        this.water_heater_target = water_heater_target;
    }

    public int getWater_heater_status() {
        return water_heater_status;
    }

    public void setWater_heater_status(int water_heater_status) {
        this.water_heater_status = water_heater_status;
    }

    public PendingAlerts getPendingAlerts() {
        return pendingAlerts;
    }

    public void setPendingAlerts(PendingAlerts pendingAlerts) {
        this.pendingAlerts = pendingAlerts;
    }

    public int getFuel_level() {
        return fuel_level;
    }

    public void setFuel_level(int fuel_level) {
        this.fuel_level = fuel_level;
    }

    public int getTransmission() {
        return transmission;
    }

    public void setTransmission(int transmission) {
        this.transmission = transmission;
    }

    public float getFanPower() {
        return fanPower;
    }

    public void setFanPower(float fanPower) {
        this.fanPower = fanPower;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    public float getPower() {
        return power;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public float getFuel_consumption() {
        return fuel_consumption;
    }

    public void setFuel_consumption(float fuel_consumption) {
        this.fuel_consumption = fuel_consumption;
    }

    public int getThermostat() {
        return thermostat;
    }

    public void setThermostat(int thermostat) {
        this.thermostat = thermostat;
    }

    public Map<ModuleType, String> getDeviceVersions() {
        return deviceVersions;
    }

    public void setDeviceVersions(Map<ModuleType, String> deviceVersions) {
        this.deviceVersions = deviceVersions;
    }
}
