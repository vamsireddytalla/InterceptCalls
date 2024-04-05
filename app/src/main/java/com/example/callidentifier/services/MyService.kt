package com.example.callidentifier.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.Color
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.R
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.example.callidentifier.MainActivity
import com.example.callidentifier.network.ApiClient
import com.example.callidentifier.pojo.NetworkResModel
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import java.util.UUID

private const val CHANNEL_ID = "CHANNEL_ID"
private const val NOTIFICATION_ID = 1

class MyService : Service() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val data = intent?.getStringExtra("no")
        Log.d("vamsi", "onStartCommand: $data")
        data?.let {
            val notification = createNotification(this, "Incoming call from: $it")
            startForeground(NOTIFICATION_ID, notification)
            networkCall(it)
        }

        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel(context: Context) {
        val name = "Foreground Service Channel"
        val description = "This is the description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            this.description = description
            enableLights(true)
            lightColor = Color.BLUE
            enableVibration(true)
        }
        val notificationManager = context.getSystemService<NotificationManager>()
        notificationManager?.createNotificationChannel(channel)
    }

    private fun createNotification(context: Context, contentTxt: String): Notification {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        return NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setContentTitle("Calling Service")
            setContentText(contentTxt)
            setSmallIcon(R.drawable.notification_bg)
            setContentIntent(pendingIntent)
            setAutoCancel(true) // Remove the notification when clicked
        }.build()
    }


    private fun updateNotification(notificationManager: NotificationManager, contentTxt: String) {
        val notification = createNotification(this, contentTxt)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun networkCall(data: String) {
        ApiClient.apiService.searchNoDetails(q = data).enqueue(object :
            Callback<NetworkResModel> {
            override fun onResponse(
                call: Call<NetworkResModel>,
                response: retrofit2.Response<NetworkResModel>
            ) {
                if (response.isSuccessful) {
                    val res = response.body()?.let {
                        var res = ""
                        res = it.data[0].name+"\n"+it.data[0].phones[0].carrier+"\n"+it.data[0].addresses[0].city
                        res
                    }

                    res?.run {
                        val notificationManager = getSystemService<NotificationManager>()
                        updateNotification(notificationManager!!,res)
                    }

                }
            }

            override fun onFailure(call: Call<NetworkResModel>, t: Throwable) {
                println(t.toString())
            }
        })
    }


}
