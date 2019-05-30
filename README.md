# ShadRep

**ShadRep** is a highly customizable bed-voting plugin. This is used to skip the night or thunderstorm.

## Features
- Anti-spam (One person cannot spam the bed messages, will only be sent to that person.)
- Highly customizable (every message can be edited.)
- Works on a world to world basis:
    - If you were to have multiple overworld type worlds (with multiverse for example), this plugin would completely handle it.
- Resets *phantom* spawn timer! (only for the people who were sleeping during the skipping)
- Can skip both night and thunderstorm
- Percentage customizable
- Color integration

## Setup
Drop the `ShadBed.jar` file in the plugins folder of your server, that's it!

## Configuration
Configuration for **ShadBed** can be found under `server/plugins/ShadBed/config.yml`

We'll go more into detail on the commands for configuration in the **commands** section.

## Commands
`/sb` -> shows percentage required to sleep.

`/sb reload` -> reloads the plugin.

Permission node for both: **ShadBed.sb**

`/sbchange` -> used for changing config

`/sbchange <config_option> <value>`

for example: `/sbchange percentage 50` (Will change percentage to 50.)

Permission node: **ShadBed.sb.change**