package com.myradio.features.player.ui.views

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.google.android.exoplayer2.ExoPlayer
import com.myradio.MainActivity
import com.myradio.R
import com.myradio.databinding.FragmentChannelBinding
import com.myradio.features.miniplayer.viewmodel.MiniPlayerViewModel
import com.myradio.features.player.ui.adpters.RadioChannelsAdapter
import com.myradio.features.player.viewmdoel.RadioChannelsViewModel
import com.myradio.utils.Constants.CHANNEL_ID
import com.myradio.utils.Constants.CHANNEL_NAME
import com.myradio.utils.Constants.NOTIFICATION_ID
import com.myradio.utils.DisplayHelper
import com.myradio.utils.DisplayHelper.displayProgressbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.content.Intent.getIntent
import androidx.core.content.ContextCompat
import com.myradio.features.miniplayer.ui.views.MediaPlaybackService


@AndroidEntryPoint
class ChannelFragment : Fragment() {

    private val TAG = "ChannelFragment"
    private lateinit var binding: FragmentChannelBinding
    private lateinit var radioAdapter: RadioChannelsAdapter

    private val radioViewModel: RadioChannelsViewModel by viewModels()
    private val minPlayerViewModel: MiniPlayerViewModel by viewModels()

    private lateinit var notification: Notification

    private lateinit var notificationManager: NotificationManagerCompat


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"Create")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChannelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        Log.d(TAG,"onViewCreated")
        setUpRecyclerView()
        intiListener()
        subscribeObserver()
        //createNotificationChannel()

       // notificationManager = NotificationManagerCompat.from(requireContext())

        //Control Play and Pause
        binding.minPlayer.playOrPauseBtn.setOnClickListener {
            if (minPlayerViewModel.isPlaying) {
                pausePlayer()
            } else {
                resumePlayer()
            }
        }
    }

    private fun subscribeObserver() {
        minPlayerViewModel.getPlaybackState().observe(viewLifecycleOwner, { playbackState ->
            when (playbackState) {
                ExoPlayer.STATE_IDLE -> {
                    Log.d(TAG, "ExoPlayer.STATE_IDLE      -")
                    displayProgressbar(false, binding.progressCircular)
                    onPlayerError()
                }
                ExoPlayer.STATE_BUFFERING -> {
                    Log.d(TAG, "ExoPlayer.STATE_BUFFERING -")
                    displayProgressbar(true, binding.progressCircular)
                }
                ExoPlayer.STATE_READY -> {
                    Log.d(TAG, "ExoPlayer.STATE_READY     -")
                    displayProgressbar(false, binding.progressCircular)
                }
                ExoPlayer.STATE_ENDED -> {
                    Log.d(TAG, "STATE_ENDED             -")
                    displayProgressbar(false, binding.progressCircular)
                }
                else -> {
                    Log.d(TAG, "UNKNOWN_STATE             -")
                    displayProgressbar(false, binding.progressCircular)
                }
            }
        })
    }


    private fun intiListener() {
        minPlayerViewModel.setPlayerEvents()
    }

    private fun setUpRecyclerView() {
        radioAdapter = RadioChannelsAdapter(::playRadioChannel)
        binding.recyclerView.apply {
            adapter = radioAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            // setHasFixedSize(true)
        }
        loadData()
    }

    private fun loadData() {
        lifecycleScope.launch {
            radioViewModel.radioChannelsList.collect { pagingData ->
                Log.d(TAG, pagingData.toString())
                radioAdapter.submitData(pagingData)

            }
        }
    }

    private fun playRadioChannel(pos: Int) {

        val currImg = radioAdapter.snapshot()[pos]?.image_url
        val currName = radioAdapter.snapshot()[pos]?.name
        val currUri = radioAdapter.snapshot()[pos]?.uri


        //play radio channel
        minPlayerViewModel.playNewStation(currUri.toString())

        //update custom view
        binding.minPlayer.getData(
            curChannelImage = currImg,
            curChannelName = currName,
            curChannelUri = currUri
        )

        Log.d(TAG, "CLICKED")
        startService()

       /* lifecycleScope.launch {
            notification = buildNotification(
                convertFromUriToBitmap(currImg.toString()), pos
            )
            notificationManager.notify(NOTIFICATION_ID, notification)

        }*/

    }


    private fun pausePlayer() {
        Log.d(TAG, "tttt")
        Log.d(TAG, minPlayerViewModel.isFailure.toString())

        if (!minPlayerViewModel.isFailure) {
            minPlayerViewModel.pause()
            binding.minPlayer.pause()
        }
    }

    private fun resumePlayer() {
        Log.d(TAG, "SSSS")
        if (!minPlayerViewModel.isFailure) {
            minPlayerViewModel.play()
            binding.minPlayer.play()
        }
    }

    private fun onPlayerError() {
        minPlayerViewModel.isPlaying = false
        minPlayerViewModel.isFailure = true
        binding.minPlayer.pause()
        Toast.makeText(requireContext(), "Error playing the channel", Toast.LENGTH_SHORT).show()
    }


    // Notifications

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lightColor = Color.DKGRAY
                enableLights(true)
            }

            val manager =
                requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }


    private fun buildNotification(bitmap: Bitmap?, pos: Int): Notification {


        val bigPicStyle = NotificationCompat.BigPictureStyle()
            .bigPicture(bitmap)
            .bigLargeIcon(null)

        return NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setContentTitle("Radio Channel")
            .setContentText("Radio Channel is Working")
            .setStyle(bigPicStyle)
            .setLargeIcon(bitmap)
            .setContentIntent(createIIntent())
            .setSmallIcon(R.drawable.music)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun createIIntent(): PendingIntent {
        val contentIntent = Intent(requireContext(), MainActivity::class.java)
        return PendingIntent.getActivity(
            requireContext(),
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_NO_CREATE
        )

    }

    private suspend fun convertFromUriToBitmap(url: String): Bitmap {
        val loader = ImageLoader(requireContext())
        val req = ImageRequest.Builder(requireContext())
            .data(url) // demo link
            .build()
        val result = (loader.execute(req) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }

    private fun startService() {
        val serviceIntent = Intent(requireContext(), MediaPlaybackService::class.java)
        serviceIntent.putExtra("inputExtra", "sss")
        ContextCompat.startForegroundService(requireContext(), serviceIntent)
    }
    private fun stopService() {
        val serviceIntent = Intent(requireContext(), MediaPlaybackService::class.java)
        MediaPlaybackService().stopService()
    }


    override fun onDestroy() {
        super.onDestroy()
        stopService()
    }

}

