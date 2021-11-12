package com.myradio.features.miniplayer.ui.views

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.media.MediaBrowserServiceCompat
import androidx.media.session.MediaButtonReceiver
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.myradio.MainActivity
import com.myradio.R
import com.myradio.utils.Constants
import com.myradio.utils.Constants.CHANNEL_ID
import com.myradio.utils.Constants.NOTIFICATION_ID
import kotlinx.coroutines.*

const val SERVICE_TAG ="MediaPlaybackService"
class MediaPlaybackService : MediaBrowserServiceCompat() {

    private val job = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main+job)


    //MediaSession
    private lateinit var mediaSessionCompat:MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector




    override fun onCreate() {
        super.onCreate()

        //get Activity intent for our notification
        val activityIntent = packageManager.getLaunchIntentForPackage(packageName)?.let {
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

            it.putExtra("Hello","Hello")
            PendingIntent.getActivity(this,0,it,0)
        }

        mediaSessionCompat = MediaSessionCompat(this,SERVICE_TAG).apply {
            setSessionActivity(activityIntent)
            isActive = true

        }
        sessionToken = mediaSessionCompat.sessionToken
         mediaSessionConnector = MediaSessionConnector(mediaSessionCompat)


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val input = intent?.getStringExtra("inputExtra")

        val activityIntent = packageManager.getLaunchIntentForPackage(packageName)?.let {
            PendingIntent.getActivity(this,0,it,0)
        }
        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            NOTIFICATION_ID, notificationIntent, 0
        )
        val notification =  NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle("Radio Channel")
            .setContentText("Radio Channel is Working")
            .setContentIntent(activityIntent)
            .setSmallIcon(R.drawable.music)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setDeleteIntent(
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    this,
                    PlaybackStateCompat.ACTION_STOP
                )
            )
            .build()
        startForeground(1, notification)
        return START_NOT_STICKY
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


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.CHANNEL_ID, Constants.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lightColor = Color.DKGRAY
                enableLights(true)
            }

            val manager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }




    fun stopService(){
        serviceScope.cancel()
        stopSelf(1)
    }
}