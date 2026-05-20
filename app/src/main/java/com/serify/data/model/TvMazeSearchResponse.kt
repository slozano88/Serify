package com.serify.data.model

data class TvMazeSearchResponse(
    val score: Double?,
    val show: TvMazeShow
)

data class TvMazeShow(
    val id: Int,
    val name: String,
    val summary: String?,
    val image: TvMazeImage?,
    val rating: TvMazeRating?,
    val genres: List<String>?,
    val premiered: String?,
    val status: String?
)

data class TvMazeImage(
    val medium: String?,
    val original: String?
)

data class TvMazeRating(
    val average: Double?
)