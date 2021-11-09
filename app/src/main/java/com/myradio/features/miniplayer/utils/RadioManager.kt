package com.myradio.features.miniplayer.utils

import android.text.TextUtils
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.BaseMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.rtsp.RtspMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Util

private const val TAG = "RadioManager"

class RadioManager(val player: ExoPlayer) {


    private val playbackStateListener: Player.Listener = PlayerListener()

    fun initializePlayer(uri: String) {

        val mediaItem = MediaItem.Builder()
            .setUri(uri)
            .build()

        val httpDataSourceFactory =
            DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true)

        //get media type
        val currMediaSource = getMediaType(uri, httpDataSourceFactory, mediaItem)

        player.setMediaSource(currMediaSource)
        player.addListener(playbackStateListener)
        player.seekToNextMediaItem()
        player.playWhenReady = true

        player.prepare()
    }


    fun play() {
        player.playWhenReady = true
    }

    fun pause() {
        player.playWhenReady = false
    }

    private fun getMediaType(
        uri: String,
        factory: DefaultHttpDataSource.Factory,
        mediaItem: MediaItem
    ): BaseMediaSource {
        @C.ContentType val type =
            if (TextUtils.isEmpty(uri)) Util.inferContentType(uri)
            else Util.inferContentType(".$uri")
        return when (type) {
            C.TYPE_DASH -> {
                DashMediaSource.Factory(factory)
                    .createMediaSource(mediaItem)
            }
            C.TYPE_SS -> {
                SsMediaSource.Factory(factory)
                    .createMediaSource(mediaItem)

            }
            C.TYPE_HLS -> {
                HlsMediaSource.Factory(factory)
                    .createMediaSource(mediaItem)
            }
            C.TYPE_RTSP -> {
                RtspMediaSource.Factory().createMediaSource(mediaItem)
            }
            else -> { // TypeOthers
                ProgressiveMediaSource.Factory(factory)
                    .createMediaSource(mediaItem)
            }
        }
    }

}


