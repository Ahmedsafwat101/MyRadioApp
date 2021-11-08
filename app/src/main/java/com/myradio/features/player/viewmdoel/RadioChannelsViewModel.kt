package com.myradio.features.player.viewmdoel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.myradio.features.player.data.network.RadioApiService
import com.myradio.features.player.paging.RadioChannelPageSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RadioChannelsViewModel @Inject constructor(private val radioApi: RadioApiService):ViewModel() {

    val radioChannelsList = Pager(PagingConfig(pageSize = 50 )){
        RadioChannelPageSource(radioApi)
    }.flow.cachedIn(viewModelScope)


}