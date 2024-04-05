package com.example.callidentifier.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import com.example.callidentifier.services.MyService


class CallerReceiver : BroadcastReceiver()
{
    override fun onReceive(context: Context?, intent: Intent?) {
        println("onReceive  Called")
        intent?.let {
            if (it.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
                val state = it.getStringExtra(TelephonyManager.EXTRA_STATE)
                if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                    val phoneNumber = it.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                    Log.d("CallReceiver", "Incoming call from: $phoneNumber")
                    phoneNumber?.let {
                        context?.let { ctx ->
                            Intent(ctx,MyService::class.java).also {intent ->
                                intent.putExtra("no",it)
                                ctx.startForegroundService(intent)
                            }
                        }
                    }
                }
            }
        }

    }


}