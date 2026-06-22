package com.serify.components.seriesdetail

import com.serify.data.model.CastMember
import com.serify.data.model.Serie
import com.serify.components.seriesdetail.model.Season
import com.serify.components.seriesdetail.model.Episode

data class SeriesDetailScreenState(
    val isLoading: Boolean = false,
    val serie: Serie? = null,
    val cast: List<CastMember> = emptyList(),
    val error: String? = null,
    val seasons: List<Season> = emptyList(),
    val episodes: List<Episode> = emptyList(),
    val selectedSeason: Int? = null,
    val isSaved: Boolean = false,
    val isSaving: Boolean = false,
    val saveMessage: String? = null,
    val isTranslatingEpisodes: Boolean = false,
    val translatedSeasonNumbers: Set<Int> = emptySet()
)
