# Plum ecoMAX860P Binding

Plum ecoMAX860P allow integration openhab with ecoMAX860P pellet boilers controller.
This binding emulate ecoNET300 module and communicate with boiler via RS485 physical interface.

## Supported Things

This controller support one Thing type `ecomaxController`

## Discovery

No auto discovery. With more than 1 boiler we should add new Thing manually and configure serial port parameter. 


## Thing Configuration

Every `ecomaxController` Thing require separate serial port to communicate with ecoMAX860P controller.
Serial port name should be configured in OpenHAB UI.


_Note that it is planned to generate some part of this based on the XML files within ```src/main/resources/OH-INF/thing``` of your binding._

### `sample` Thing Configuration

| Name            | Type    | Description                           | Default | Required | Advanced |
|-----------------|---------|---------------------------------------|---------|----------|----------|
| serialPort      | text    | Serial port name                     | N/A     | yes      | no       |


## Channels

The following channels are supported:

| Channel | Type   | Read/Write                        | Description                     |
|---------|--------|-----------------------------------|---------------------------------|
| boiler_state | Switch | W                                 | Boiler control                  |
| boiler_temp | Number | R                                 | Boiler temperature              |
| water_heater_temp | Number | R                                 | Water heater temperature        |
| outside_temp | Number | R                                 | Outside temperature             |
| boiler_temp_target | Number | R/W                               | Boiler target temperature       |
| water_heater_target | Number | R | Water heater target temperature |
| heating_mode | String | R                                 | Heating mode                    |
| exhaust_temp | Number | R                                 | Exhaust temperature             |
| feeder_temp | Number | R                                 | Feeder temperature              |
| return_temp | Number | R                                 | Return temperature              |
| heating_load | Number | R                                 | Heating load                    |
| fan_power | Number | R                                 | Fan power                       |
| fuel_level | Number | R                                 | Fuel level                      |
| fuel_consumption | Number | R                                 | Fuel consumption                |
| heating_power | Number | R                                 | Heating power                   |
| lower_buffer_temp | Number | R                                 | Lower buffer temperature        |
| upper_buffer_temp | Number | R                                 | Upper buffer temperature        |
| flame_intensity | Number | R                                 | Flame intensity                 |
| exhaust_temperature | Number | R                                 | Exhaust temperature             |



## Full Example

_Provide a full usage example based on textual configuration files._
_*.things, *.items examples are mandatory as textual configuration is well used by many users._
_*.sitemap examples are optional._

### Thing Configuration

```java
Example thing configuration goes here.
```
### Item Configuration

```java
Example item configuration goes here.
```

### Sitemap Configuration

```perl
Optional Sitemap configuration goes here.
Remove this section, if not needed.
```

## Any custom content here!

Special thanks to [PyPlumIO](https://github.com/denpamusic/PyPlumIO/) project for information about protocol 
