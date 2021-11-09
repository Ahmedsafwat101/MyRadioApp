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
import com.myradio.databinding.FragmentChannelBinding
import com.myradio.features.miniplayer.viewmodel.MiniPlayerViewModel
import com.myradio.features.player.ui.adpters.RadioChannelsAdapter
import com.myradio.features.player.viewmdoel.RadioChannelsViewModel
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
    private val playerViewModel: MiniPlayerViewModel by viewModels()

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

        binding.minPlayer.playOrPauseBtn.setOnClickListener {
            if (playerViewModel.isPlaying) {
                pausePlayer()
            } else {
                resumePlayer()
            }
        }

    }

    private fun intiListener() {
        playerViewModel.setPlayerEvents(::onPlayerError, ::displayProgressbar)
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

        binding.cardView.visibility = View.VISIBLE

        val currImg = radioAdapter.snapshot()[pos]?.image_url
        val currName = radioAdapter.snapshot()[pos]?.name
        val currUri = radioAdapter.snapshot()[pos]?.uri

        //play radio channel
        currUri?.let {
            playerViewModel.playNewStation(currUri)
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
        Log.d(TAG,playerViewModel.isFailure.toString())

        if (!playerViewModel.isFailure) {
            playerViewModel.pause()
            binding.minPlayer.pause()
        }
    }

    private fun resumePlayer() {
        Log.d(TAG,"SSSS")
        if (!playerViewModel.isFailure) {
            playerViewModel.play()
            binding.minPlayer.play()
        }
    }

    private fun displayProgressbar(isDisplayed: Boolean) {
        //binding.minPlayer.pause()
        binding.progressCircular.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

    private fun onPlayerError() {
        playerViewModel.isPlaying = false
        playerViewModel.isFailure = true
        binding.minPlayer.pause()
        Toast.makeText(requireContext(), "Error playing the channel", Toast.LENGTH_SHORT).show()
    }


}

