package com.myradio.features.player.ui.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.ExoPlayer
import com.myradio.databinding.FragmentChannelBinding
import com.myradio.features.miniplayer.viewmodel.MiniPlayerViewModel
import com.myradio.features.player.ui.adpters.RadioChannelsAdapter
import com.myradio.features.player.viewmdoel.RadioChannelsViewModel
import com.myradio.utils.DisplayHelper
import com.myradio.utils.DisplayHelper.displayProgressbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChannelFragment : Fragment() {

    private val TAG = "ChannelFragment"
    private lateinit var binding: FragmentChannelBinding
    private lateinit var radioAdapter: RadioChannelsAdapter

    private val radioViewModel: RadioChannelsViewModel by viewModels()
    private val minPlayerViewModel: MiniPlayerViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChannelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpRecyclerView()
        intiListener()
        subscribeObserver()

        binding.minPlayer.playOrPauseBtn.setOnClickListener {
            if (minPlayerViewModel.isPlaying) {
                pausePlayer()
            } else {
                resumePlayer()
            }
        }
    }


    private fun subscribeObserver() {
        minPlayerViewModel.getPlaybackState().observe(viewLifecycleOwner,{ playbackState ->
            when(playbackState){
                ExoPlayer.STATE_IDLE -> {
                     Log.d(TAG, "ExoPlayer.STATE_IDLE      -")
                    displayProgressbar(false,binding.progressCircular)
                    onPlayerError()
                }
                ExoPlayer.STATE_BUFFERING -> {
                    Log.d(TAG, "ExoPlayer.STATE_BUFFERING -")
                    displayProgressbar(true,binding.progressCircular)
                }
                ExoPlayer.STATE_READY -> {
                     Log.d(TAG, "ExoPlayer.STATE_READY     -")
                    displayProgressbar(false,binding.progressCircular)
                }
                ExoPlayer.STATE_ENDED -> {
                    Log.d(TAG, "STATE_ENDED             -")
                    displayProgressbar(false,binding.progressCircular)
                }
                else -> {
                     Log.d(TAG, "UNKNOWN_STATE             -")
                    displayProgressbar(false,binding.progressCircular)
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
        currUri?.let {
            minPlayerViewModel.playNewStation(currUri)
        }

        //update custom view
        binding.minPlayer.getData(
            curChannelImage = currImg,
            curChannelName = currName,
            curChannelUri = currUri
        )

        Log.d(TAG, "CLICKED")
    }

    private fun pausePlayer() {
        Log.d(TAG,"tttt")
        Log.d(TAG,minPlayerViewModel.isFailure.toString())

        if (!minPlayerViewModel.isFailure) {
            minPlayerViewModel.pause()
            binding.minPlayer.pause()
        }
    }

    private fun resumePlayer() {
        Log.d(TAG,"SSSS")
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


}

