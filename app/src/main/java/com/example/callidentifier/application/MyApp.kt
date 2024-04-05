package com.example.callidentifier.application

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import androidx.core.content.getSystemService
private const val CHANNEL_ID = "CHANNEL_ID"
class MyApp : Application()
{
    init {
     createNotificationChannel(applicationContext)
    }

    private fun createNotificationChannel(context: Context) {
        val name = "Foreground Service Channel"
        val description = "This is the description"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            this.description = description
            enableLights(true)
            lightColor = Color.BLUE
            enableVibration(true)
        }
        val notificationManager = context.getSystemService<NotificationManager>()
        notificationManager?.createNotificationChannel(channel)
    }


}