package com.muisit.rechargereset

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import androidx.preference.PreferenceManager
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager

import kotlinx.android.synthetic.main.activity_reset_charger.*
import kotlinx.android.synthetic.main.content_reset_charger.*
import kotlinx.android.synthetic.main.fragment_first.*
import java.util.*
import java.util.concurrent.TimeUnit

class ResetChargerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_charger)
        setSupportActionBar(toolbar)

        MyLog.log("opening ignore battery optimization settings", applicationContext)
        var intent= Intent();
        intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
        applicationContext.startActivity(intent)

        MyLog.log("registering power receiver now", applicationContext)
        IntentFilter(Intent.ACTION_POWER_CONNECTED).let { ifilter ->
            applicationContext.registerReceiver(PowerReceiver(), ifilter)
        }
        MyLog.log("checking if power is already on", applicationContext)
        if(Power.isConnected(applicationContext)){
            val intent2 = Intent(applicationContext, RechargeReset::class.java);
            MyLog.log("sending intent to service from main", applicationContext)
            applicationContext.startService(intent2)
        }
        MyLog.log("end of main activity", applicationContext)
    }
}
