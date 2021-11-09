package com.myradio.features.miniplayer.utils

import android.util.Log
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player

private const val TAG = "PlayerListener"

class PlayerListener(val onErrorEvent: () -> Unit, val onLoadingEvent: (Boolean) -> Unit) :Player.Listener {
    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            ExoPlayer.EVENT_PLAYER_ERROR -> {
                Log.d(TAG, "ExoPlayer.Error -")
            }
            ExoPlayer.STATE_IDLE -> {
                Log.d(TAG, "ExoPlayer.STATE_IDLE      -")
                onLoadingEvent(false)

            }
            ExoPlayer.STATE_BUFFERING -> {
                Log.d(TAG, "ExoPlayer.STATE_BUFFERING -")
                onLoadingEvent(true)

            }
            ExoPlayer.STATE_READY -> {
                Log.d(TAG, "ExoPlayer.STATE_READY     -")
                onLoadingEvent(false)

            }
            ExoPlayer.STATE_ENDED -> {
                Log.d(TAG, "STATE_ENDED             -")
                onLoadingEvent(false)

            }
            else -> {
                Log.d(TAG, "UNKNOWN_STATE             -")
                onLoadingEvent(false)

            }
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        onLoadingEvent(false)
        onErrorEvent()
    }
}