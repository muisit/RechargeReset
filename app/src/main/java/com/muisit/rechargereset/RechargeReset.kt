package com.muisit.rechargereset

import android.app.IntentService
import android.content.Intent
import android.os.IBinder
import eu.chainfire.libsuperuser.Shell
import android.util.Log
import androidx.core.app.NotificationCompat


class RechargeReset : IntentService("ResetCharger") {

    override fun onBind(intent: Intent): IBinder? {
        // no reason to bind to anything at the moment
        return null;
    }

    override fun onHandleIntent(intent: Intent?) {
        // handle the intent in a subthread
        //
        MyLog.log("rechargereset service",baseContext)
        val notid: Int = Notifier.notify(baseContext, "Recharge Reset", "Resetting recharge setting", true)

        if(intent !== null) {
            MyLog.log("trying to reset battery settings using SU shell",baseContext)
            try {
                while(true) {
                    Shell.Pool.SU.run("echo 1 > /sys/class/power_supply/battery/charging_enabled");

                    // check to see if the the USB port is still connected
                    if(!Power.isConnected(baseContext)) {
                        MyLog.log("power disconnected, stopping service",baseContext)
                        break;
                    }
                    else {
                        if (Power.isFull(baseContext)) {
                            MyLog.log("battery full, waiting 10 minutes",baseContext)
                            Thread.sleep(10 * 60 * 1000);
                        } else {
                            MyLog.log("battery still charging, waiting 10 seconds",baseContext)
                            Thread.sleep(10 * 1000)
                        }
                    }
                }
            }
            catch(e:Exception) {
                MyLog.log("caught exception in service, probably in the shell",baseContext)
            }
        }

        MyLog.log("end of resetcharger service", baseContext)
        Notifier.cancelNotification(baseContext, notid)
    }
}
