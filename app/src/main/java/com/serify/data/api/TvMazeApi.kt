package com.serify.data.api

import com.serify.data.model.TvMazeCastResponse
import com.serify.data.model.TvMazeSearchResponse
import com.serify.data.model.TvMazeShow
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvMazeApi {

    // Sirve para buscar series por texto y por id

    @GET("search/shows")
    suspend fun searchShows(
        @Query("q") query: String
    ): List<TvMazeSearchResponse>

    @GET("shows/{id}")
    suspend fun getShowById(
        @Path("id") id: Int
    ): TvMazeShow

    @GET("shows")
    suspend fun getShows(
        @Query("page") page: Int
    ): List<TvMazeShow>
    @GET("shows/{id}/cast")
    suspend fun getShowCast(
        @Path("id") id: Int
    ): List<TvMazeCastResponse>
}