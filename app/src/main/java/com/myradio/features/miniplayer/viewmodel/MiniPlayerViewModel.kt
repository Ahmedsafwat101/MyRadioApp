package com.myradio.features.miniplayer.viewmodel

import android.text.TextUtils
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.BaseMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.rtsp.RtspMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Util
import com.myradio.features.miniplayer.utils.PlayerListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MiniPlayerViewModel @Inject constructor(
    private var player: ExoPlayer
) : ViewModel() {

     var isPlaying = false
     var isFailure = false

    lateinit var playerEvents: PlayerListener

    fun setPlayerEvents(onErrorEvent: () -> Unit = {}, onLoadingEvent: (Boolean) -> Unit = {}){
        playerEvents = PlayerListener(onErrorEvent, onLoadingEvent)
        player.addListener(playerEvents)
    }

    fun playNewStation(uri:String) {
        isPlaying = true
        isFailure = false

        player.stop()

        val mediaItem = MediaItem.Builder()
            .setUri(uri)
            .build()

        val httpDataSourceFactory =
            DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true)

        //get media type
        val currMediaSource = getMediaType(uri, httpDataSourceFactory, mediaItem)

        player.setMediaSource(currMediaSource)

        player.seekToNextMediaItem()
        player.playWhenReady = true

        player.prepare()

    }

    fun play() { // resume player
        isPlaying = true
        player.playWhenReady = true
    }

    fun pause() {
        isPlaying = false
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