package com.muisit.rechargereset

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

object Power {
    fun isConnected(context: Context): Boolean {
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            context.registerReceiver(null, ifilter)
        }

        val plugged: Int = batteryStatus ?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) ?: -1
        return plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB || plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS
    }

    fun isFull(context: Context): Boolean {
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            context.registerReceiver(null, ifilter)
        }

        val full: Int = batteryStatus ?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        return full == BatteryManager.BATTERY_STATUS_FULL
    }
}
