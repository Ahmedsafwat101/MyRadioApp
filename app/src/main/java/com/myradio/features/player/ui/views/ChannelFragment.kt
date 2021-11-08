package com.myradio.features.player.ui.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.myradio.R
import com.myradio.databinding.FragmentChannelBinding
import com.myradio.features.player.ui.adpters.RadioChannelsAdapter
import com.myradio.features.player.viewmdoel.RadioChannelsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@InternalCoroutinesApi
@AndroidEntryPoint
class ChannelFragment : Fragment() {
    private val TAG = "ChannelFragment"
    private lateinit var binding: FragmentChannelBinding

    private lateinit var radioAdapter: RadioChannelsAdapter
    private val radioViewModel: RadioChannelsViewModel by viewModels()

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
        TODO()
    }
}

