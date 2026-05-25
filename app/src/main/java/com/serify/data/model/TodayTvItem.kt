package com.serify.data.model

data class TodayTvItem(
    val episodeName: String,
    val showName: String,
    val showId: Int?,
    val imageUrl: String?,
    val airtime: String?,
    val season: Int?,
    val number: Int?
)