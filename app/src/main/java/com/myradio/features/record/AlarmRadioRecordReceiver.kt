package com.myradio.features.record

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlin.math.log
import kotlin.time.Duration.Companion.seconds

private const val TAG ="AlarmRadioRecordReceiver"
class AlarmRadioRecordReceiver : BroadcastReceiver() {

    @SuppressLint("LongLogTag")
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        //Trigger a background Service for reading streaming data and save into file

        Log.i(TAG,"AlarmRadioRecordReceiver: onReceive() is called"+System.currentTimeMillis().seconds)
        context.startService(Intent(context,RecordService::class.java))

    }
}