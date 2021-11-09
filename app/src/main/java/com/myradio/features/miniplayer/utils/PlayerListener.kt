package com.myradio.features.miniplayer.utils

import android.util.Log
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player

private const val TAG = "PlayerListener"

class PlayerListener() :Player.Listener {
    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            ExoPlayer.EVENT_PLAYER_ERROR -> {
                Log.d(TAG, "ExoPlayer.Error -")
            }
            ExoPlayer.STATE_IDLE -> {
                Log.d(TAG, "ExoPlayer.STATE_IDLE      -")
                ("Idle")
            }
            ExoPlayer.STATE_BUFFERING -> {
                Log.d(TAG, "ExoPlayer.STATE_BUFFERING -")
            }
            ExoPlayer.STATE_READY -> {
                Log.d(TAG, "ExoPlayer.STATE_READY     -")
            }
            ExoPlayer.STATE_ENDED -> {
                Log.d(TAG, "STATE_ENDED             -")
            }
            else -> {
                Log.d(TAG, "UNKNOWN_STATE             -")

            }
        }
    }
}