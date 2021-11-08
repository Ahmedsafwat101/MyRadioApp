package com.myradio.features.player.data.models

data class RadioApiResponse(
    val current_page: Int,
    val has_more: Boolean,
    val page_count: Int,
    val radios: List<Radio>
)