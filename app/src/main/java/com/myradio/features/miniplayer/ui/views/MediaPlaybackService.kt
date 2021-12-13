package com.myradio.features.miniplayer.ui.views

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.myradio.MainActivity
import com.myradio.R
import com.myradio.utils.Constants.CHANNEL_ID
import com.myradio.utils.Constants.NOTIFICATION_ID
import kotlinx.coroutines.*
import com.myradio.utils.Constants.CHANNEL_NAME
import com.myradio.utils.Constants.NOTIFICATION_MESSAGE
import com.myradio.utils.NotificationHelper


const val SERVICE_TAG = "MediaPlaybackService"

class MediaPlaybackService : MediaBrowserServiceCompat() {

    private val job = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + job)


    //MediaSession
    private lateinit var mediaSessionCompat: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector


    override fun onCreate() {
        super.onCreate()

        //get Activity intent for our notification
        /*val activityIntent = packageManager.getLaunchIntentForPackage(packageName)?.let {
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

            it.putExtra("Hello", "Hello")
            PendingIntent.getActivity(this, 0, it, 0)
        }

        mediaSessionCompat = MediaSessionCompat(this, SERVICE_TAG).apply {
            setSessionActivity(activityIntent)
            isActive = true

        }
        sessionToken = mediaSessionCompat.sessionToken
        mediaSessionConnector = MediaSessionConnector(mediaSessionCompat)*/


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startForegroundService()
        else
            startForeground(2, Notification())
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun startForegroundService() {


        /*val activityIntent = packageManager.getLaunchIntentForPackage(packageName)?.let {
            PendingIntent.getActivity(this, 0, it, 0)
        }*/

        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.putExtra("data", "data")

        //val uniqueInt = (System.currentTimeMillis() and 0xfffffff).toInt()

        val pendingIntent = PendingIntent.getActivity(
            this,
            NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationHelper.makeStatusNotification(NOTIFICATION_MESSAGE,pendingIntent,this)

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        return BrowserRoot("root", rootHints)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    fun stopService() {
        serviceScope.cancel()
        stopSelf(2)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)

            val manager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}