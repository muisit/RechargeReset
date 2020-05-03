package com.muisit.rechargereset

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService

object Notifier {
    var notificationId : Int = 1

    fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("RechargeReset", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun cancelNotification(context: Context, id: Int) {
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            cancel(id)
        }
    }

    fun debug(context:Context, title: String, message: String, sticky: Boolean) {
        MyLog.log(message, context)
        //notify(context,title, message, sticky)
    }

    fun notify(context: Context, title: String, message:String, sticky: Boolean) : Int {
        this.createNotificationChannel(context)

        val intent = Intent(context, ResetChargerActivity::class.java)
        //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context,0,intent,0)

        var builder = NotificationCompat.Builder(context, "RechargeReset")
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(!sticky)


        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, builder.build())
            notificationId += 1
        }

        return notificationId - 1
    }
}