package com.serify.data.model

data class TvMazeCastResponse(
    val person: TvMazePerson?,
    val character: TvMazeCharacter?
)

data class TvMazePerson(
    val id: Int?,
    val name: String?,
    val image: TvMazeImage?
)

data class TvMazeCharacter(
    val id: Int?,
    val name: String?,
    val image: TvMazeImage?
)