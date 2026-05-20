package com.serify.components.seriesdetail

import com.serify.data.model.CastMember
import com.serify.data.model.Serie

data class SeriesDetailScreenState(
    val isLoading: Boolean = false,
    val serie: Serie? = null,
    val cast: List<CastMember> = emptyList(),
    val error: String? = null
)