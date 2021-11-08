package com.myradio.features.player.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.myradio.features.player.data.models.Radio
import com.myradio.features.player.data.network.RadioApiService

class RadioChannelPageSource(private val radioApi: RadioApiService) : PagingSource<Int, Radio>() {
    override fun getRefreshKey(state: PagingState<Int, Radio>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Radio> {
        return try {
            val currPage = params.key ?: 1
            val response = radioApi.getAllRadioChannels(page = currPage)
            val responseData = mutableListOf<Radio>()
            val data = response.body()?.radios?: emptyList()
            responseData.addAll(data)

            Log.d("Lol",responseData.toString())
            LoadResult.Page(
                data = responseData,
                prevKey = if (currPage == 1) null else -1,
                nextKey = if(currPage>=3)null else currPage.plus(1)
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}