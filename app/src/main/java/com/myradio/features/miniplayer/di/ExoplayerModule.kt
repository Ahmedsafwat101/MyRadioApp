package com.myradio.features.miniplayer.di

import android.content.Context
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExoplayerModule {

    @Provides
    @Singleton
    fun provideTrackSelector(@ApplicationContext context: Context): DefaultTrackSelector =
        DefaultTrackSelector(context).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }

    @Provides
    @Singleton
    fun provideAudioAttributes(): AudioAttributes =
        AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA) // C.USAGE_MEDIA = 1
            .setContentType(C.CONTENT_TYPE_SPEECH) //C.CONTENT_TYPE_SPEECH = 1
            .build()


    @Provides
    @Singleton
    fun provideExoplayer(
        @ApplicationContext context: Context,
        trackSelector: DefaultTrackSelector,
        audioAttributes: AudioAttributes
    ): ExoPlayer =
        ExoPlayer.Builder(context).setTrackSelector(trackSelector).build().apply {
            setHandleAudioBecomingNoisy(true)
            setAudioAttributes(audioAttributes, true)
        }

}