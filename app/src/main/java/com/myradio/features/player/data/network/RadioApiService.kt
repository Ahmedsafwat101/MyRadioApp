package com.myradio.features.player.data.network

import com.myradio.features.player.data.models.RadioApiResponse
import com.myradio.features.player.utils.Constants.END_POINT
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RadioApiService {


    @GET(END_POINT)
    suspend fun getAllRadioChannels(
        @Query("query") query:String="eg",
        @Query("page") page:Int
    ):Response<RadioApiResponse>

}