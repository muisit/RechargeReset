package com.muisit.rechargereset

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log

class PowerReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Notifier.debug(context,"RechargeReset","broadcastreceiver receives intent", false)
        if(    intent !== null
            && (  intent.action == Intent.ACTION_LOCKED_BOOT_COMPLETED
               || intent.action == Intent.ACTION_BOOT_COMPLETED
               )
          ){

            Notifier.debug(context, "RechargeReset","receiving power connected events", false)
            val filter = IntentFilter()
            filter.addAction(Intent.ACTION_POWER_CONNECTED)
            context.applicationContext.registerReceiver(this, filter)

            Notifier.debug(context, "RechargeReset","Received Boot Intent", false)
            // if we are already connected, send the intent immediately as well
            if(Power.isConnected(context)){
                //Notifier.notify(context, "RechargeReset","Sending Intent", false)
                val intent2 = Intent(context, RechargeReset::class.java);
                MyLog.log("power already connected, so starting service",context)
                context.startService(intent2)
            }
        }
        else if(    intent !== null
            && intent.action == Intent.ACTION_POWER_CONNECTED) {
            Notifier.debug(context, "RechargeReset","Received Power Intent", false)

            val intent2 = Intent(context, RechargeReset::class.java)
            Notifier.debug(context, "RechargeReset","Sending Intent (2)", false)
            context.startService(intent2)
        }
        MyLog.log("end of powerreceiver",context)
    }
}
