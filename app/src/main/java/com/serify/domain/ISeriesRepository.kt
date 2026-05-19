package com.serify.domain

import com.serify.data.model.Serie

interface ISeriesRepository {

    // Buscar serie por ID y nombre

    suspend fun searchSeries(query: String): List<Serie>
    suspend fun getSerieById(id: Int): Serie
}