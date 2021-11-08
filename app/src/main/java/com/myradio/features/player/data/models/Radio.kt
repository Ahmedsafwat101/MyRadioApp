package com.myradio.features.player.data.models

data class Radio(
    val channel_id: Int,
    val countryCode: String,
    val genre: String,
    val image_url: String,
    val name: String,
    val uri: String
)