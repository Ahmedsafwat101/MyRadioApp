package com.myradio.features.miniplayer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.myradio.features.miniplayer.utils.PlayerListener
import com.myradio.features.miniplayer.utils.PlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MiniPlayerViewModel @Inject constructor(
    private var player: ExoPlayer
) : ViewModel() {


    private val playbackState: MutableLiveData<Int> = MutableLiveData<Int>()

    fun getPlaybackState() = playbackState


     var isPlaying = false
     var isFailure = false

    lateinit var playerEvents: PlayerListener


    fun setPlayerEvents(){
        playerEvents = PlayerListener(::getPlayerState)
        player.addListener(playerEvents)
    }

    private fun getPlayerState(state:Int) {
        playbackState.postValue(state)
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
        val currMediaSource = PlayerManager.getMediaType(uri, httpDataSourceFactory, mediaItem)

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
}