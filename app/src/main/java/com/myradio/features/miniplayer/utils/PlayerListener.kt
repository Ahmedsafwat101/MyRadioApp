package com.myradio.features.miniplayer.utils

import android.util.Log
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player

private const val TAG = "PlayerListener"

class PlayerListener(val getPlayerState: (Int) -> Unit) :Player.Listener {
    override fun onPlaybackStateChanged(playbackState: Int) {

        getPlayerState(playbackState)

    }

    override fun onPlayerError(error: PlaybackException) {
       /*
        Actually I will not put any logic here for now because after triggering this call fun
        the playback state will be ideal so we can listen for this state instead
        */
    }
}