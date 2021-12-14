package com.myradio.features.record

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlin.time.Duration.Companion.seconds

private const val TAG ="RecordService"

class RecordService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStart(intent: Intent?, startId: Int) {
        Log.i(TAG," RecordService: onStart() is called ")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG," RecordService: onStartCommand() is called "+ System.currentTimeMillis().seconds)

        return super.onStartCommand(intent, flags, startId)
    }
}