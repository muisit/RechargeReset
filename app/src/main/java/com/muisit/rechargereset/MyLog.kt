package com.muisit.rechargereset

import android.content.Context
import android.util.Log
import java.io.File
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object MyLog {
    fun log(data :String, context: Context) {
        Log.i("resetcharger",data)
        //this.logToFile(data,context)
    }

     fun logToFile(data: String, context: Context) {
         val format = SimpleDateFormat("HH:mm:ss")
         val date = Calendar.getInstance().time
         val message = format.format(date) +": " + data

         try {
             val file = File(context.getExternalFilesDir(null),"resetcharger.log")
             if (!file.exists()) {
                 file.createNewFile()
             }

             PrintWriter(file).use { out -> out.println(message) }
         } catch (e: Exception) {
             Log.e("Exception", "File write failed: " + e.toString())
         }
     }
}