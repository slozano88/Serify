package com.serify.components.home

import com.serify.data.model.Serie
import com.serify.data.model.TodayTvItem

data class HomeScreenState(
    val isLoading: Boolean = false,
    val featuredSerie: Serie? = null,
    val trendingSeries: List<Serie> = emptyList(),
    val error: String? = null,
    val todayTv: List<TodayTvItem> = emptyList(),
    val savedSerieIds: Set<Int> = emptySet(),
    val recentlyViewed: List<Serie> = emptyList()
)
