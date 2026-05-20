package com.serify.domain

import com.serify.data.model.CastMember
import com.serify.data.model.Serie
import com.serify.components.seriesdetail.model.Season
import com.serify.components.seriesdetail.model.Episode

interface ISeriesRepository {

    // Buscar serie por ID y nombre

    suspend fun searchSeries(query: String): List<Serie>
    suspend fun getSerieById(id: Int): Serie
    suspend fun getSeriesByGenre(genre: String): List<Serie>
    suspend fun getSerieCast(id: Int): List<CastMember>
    suspend fun getSeasons(id: Int): List<Season>

    suspend fun getEpisodes(id: Int): List<Episode>
}