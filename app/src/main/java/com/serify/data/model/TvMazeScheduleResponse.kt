package com.serify.data.model

data class TvMazeScheduleResponse(
    val id: Int?,
    val name: String?,
    val season: Int?,
    val number: Int?,
    val airtime: String?,
    val image: TvMazeImage?,
    val show: TvMazeShow?
)