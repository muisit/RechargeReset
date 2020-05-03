# RechargeReset

This is a privately developed Android application to reset the `/sys/class/power_supply/battery/charging_enabled` setting when the power cable is connected.

## Problem Description

My Moto X Play device developed an issue after a few years where the battery temperature sensor broke down and reported invalid temperatures.
This causes the device to disable the charge cycle while powered on. If the device is powered off, it will charge up to about 80%.
More people are running into this issue and some of it is documented here, for example:

[Lenovo Forum](https://forums.lenovo.com/t5/MOTO-G-3rd-Gen/Moto-x-play-not-charging/m-p/3359066?page=2)

## Solution

The solution seems to be to reset the `charging_enabled` setting, which overrides the process that disabled charging due to the temperature.
Unfortunately, the temperature is checked periodically, so you need to reset this setting periodically as well.
The only way to do that is by having the device **root**-ed. Yes, no other way around this: you need to fool with device settings that are
exclusively accessed by the original system code and should not be touchable by applications.
Rooting does not damage your device. There are a ton of manuals about how to root your device and in some cases it takes only a few minutes.
In my case, I needed an access code from Motorola to unlock the bootloader, but that was sent to me quickly.

After rooting the device and installing [SuperSU](https://supersuroot.org/), you have the option of running a script to reset the value.
In order to run this script, you need a script manager. Unfortunately, it is rather a pain to have this script run at boot time automatically.

## Android App

This **RechargeReset** android app replaces the script on rooted phones. You can open the project in [Android Studio](https://developer.android.com/studio/)
and build the application. Then 'Run' the project with your phone connected over the USB cable (make sure USB debugging is allowed).
This will install the application on your phone and start it. Upon start it opens the battery optimization screen. Select all applications, find the
**RechargeReset** application and mark it as not available for battery optimization. This will make sure it can run with a low battery as well (which we
need if the battery is low and needs to be recharged).
Once the application has run, it will register itself at boot time and when the power cable is connected. The first time that happens, it will request
`superSU` powers, which you must manually grant once. Because the power cable is connected when uploading the application through USB, you will get
this question immediately.

The project requires the [libsuperuser library](https://github.com/Chainfire/libsuperuser) by Jorrit _Chainfire_ Jongma. Download that project and
include it in your development environment using `File->New->Import Project`.
There is probably a better way, but this is my first Android application. I am open to suggestions.

## Breakdown

The application consists of a simple Activity to display a text, register the `BroadcastReceiver` and allow you to select the application for battery-optimization-whitelisting.
It has a `BroadcastReceiver` that receives the `BOOT` and `LOCKED_BOOT` Intents as well as the `POWER_CONNECTED` Intent. Because `POWER_CONNECTED` cannot be
registered in the manifest since Android 8.0, that intent is registered at `BOOT` as well.
On `POWER_CONNECTED`, the application instantiates the background service `RechargeReset` to monitor the battery status while power is connected. If the
battery status is `FULL`, the application will recheck in another 10 minutes to avoid overcharging. Otherwise the `/sys/class/power_supply/battery/charging_enabled`
setting is set to `1` and the application will recheck in 10 seconds. This continues while the power cable is connected. When the power cable is disconnected,
the service dies naturally.

Issues may arise if the battery is full and you repeatedly connect and disconnect the power cable. Each time it is connected, a service is created that will
wait for 10 minutes to recheck again. There is no code to check whether the service is already running, which I could have added, but did not. Deal with it.
