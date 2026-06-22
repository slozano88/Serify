package com.serify.data.repository

import com.serify.components.seriesdetail.model.Episode
import com.serify.components.seriesdetail.model.Season
import com.serify.data.api.Retrofit
import com.serify.data.model.CastMember
import com.serify.data.model.Serie
import com.serify.data.model.TodayTvItem
import com.serify.data.model.TvMazeShow
import com.serify.data.util.SpanishTextMapper
import com.serify.domain.ISeriesRepository

class SeriesRepository : ISeriesRepository {

    private val api = Retrofit.tvMazeApi

    override suspend fun searchSeries(query: String): List<Serie> {
        return api.searchShows(query).map { response ->
            response.show.toSerieForList()
        }
    }

    override suspend fun getSerieById(id: Int): Serie {
        return api.getShowById(id).toSerieForDetail()
    }

    override suspend fun getSeriesByGenre(genre: String): List<Serie> {
        val pagesToLoad = listOf(0, 1, 2)

        return pagesToLoad
            .flatMap { page ->
                api.getShows(page)
            }
            .filter { show ->
                show.genres.orEmpty().any { item ->
                    item.equals(genre, ignoreCase = true)
                }
            }
            .map { show ->
                show.toSerieForList()
            }
            .distinctBy { serie ->
                serie.id
            }
            .shuffled()
            .take(10)
    }

    override suspend fun getSerieCast(id: Int): List<CastMember> {
        return api.getShowCast(id)
            .mapNotNull { item ->
                val personName = item.person?.name ?: return@mapNotNull null

                CastMember(
                    personName = personName,
                    characterName = item.character?.name,
                    imageUrl = item.person?.image?.medium ?: item.person?.image?.original
                )
            }
            .take(10)
    }

    override suspend fun getSeasons(id: Int): List<Season> {
        return api.getSeasons(id)
    }

    override suspend fun getEpisodes(id: Int): List<Episode> {
        return api.getEpisodes(id)
    }

    override suspend fun getTodayTv(): List<TodayTvItem> {
        return api.getTodaySchedule(country = "US")
            .mapNotNull { item ->
                val show = item.show ?: return@mapNotNull null

                val episodeName = item.name ?: "Nuevo episodio"
                val translatedEpisodeName = EnglishToSpanishTranslationRepository.translate(episodeName)
                    ?: episodeName

                TodayTvItem(
                    episodeName = translatedEpisodeName,
                    showName = show.name,
                    showId = show.id,
                    imageUrl = item.image?.medium
                        ?: item.image?.original
                        ?: show.image?.medium
                        ?: show.image?.original,
                    airtime = item.airtime,
                    season = item.season,
                    number = item.number
                )
            }
            .distinctBy { it.showId }
            .take(10)
    }

    private fun TvMazeShow.toSerieForList(): Serie {
        return Serie(
            id = id,
            name = name,
            summary = summary,
            imageUrl = image?.medium ?: image?.original,
            rating = rating?.average,
            genres = genres.orEmpty().map { genre -> SpanishTextMapper.genre(genre) },
            premiered = premiered,
            status = SpanishTextMapper.status(status)
        )
    }

    private suspend fun TvMazeShow.toSerieForDetail(): Serie {
        return Serie(
            id = id,
            name = name,
            summary = summary,
            imageUrl = image?.medium ?: image?.original,
            rating = rating?.average,
            genres = genres.orEmpty().map { genre -> SpanishTextMapper.genre(genre) },
            premiered = premiered,
            status = SpanishTextMapper.status(status)
        )
    }
}
