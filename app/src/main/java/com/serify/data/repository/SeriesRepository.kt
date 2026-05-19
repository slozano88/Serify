package com.serify.data.repository

import com.serify.data.api.Retrofit
import com.serify.data.model.Serie
import com.serify.data.model.TvMazeShow
import com.serify.domain.ISeriesRepository

class SeriesRepository : ISeriesRepository {

    private val api = Retrofit.tvMazeApi

    override suspend fun searchSeries(query: String): List<Serie> {
        return api.searchShows(query).map { response ->
            response.show.toSerie()
        }
    }

    override suspend fun getSerieById(id: Int): Serie {
        return api.getShowById(id).toSerie()
    }

    private fun TvMazeShow.toSerie(): Serie {
        return Serie(
            id = id,
            name = name,
            summary = summary,
            imageUrl = image?.medium ?: image?.original,
            rating = rating?.average,
            genres = genres ?: emptyList(),
            premiered = premiered,
            status = status
        )
    }
}