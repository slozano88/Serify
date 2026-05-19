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

    override suspend fun getSeriesByGenre(genre: String): List<Serie> {
        val pagesToLoad = listOf(0, 1, 2)

        return pagesToLoad
            .flatMap { page ->
                api.getShows(page)
            }
            .map { show ->
                show.toSerie()
            }
            .filter { serie ->
                serie.genres.any { item ->
                    item.equals(genre, ignoreCase = true)
                }
            }
            .distinctBy { serie ->
                serie.id
            }
            .shuffled()
            .take(10)
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