package com.serify.components.seriesdetail

import com.serify.data.model.Serie

data class SeriesDetailScreenState(
    val isLoading: Boolean = false,
    val serie: Serie? = null,
    val error: String? = null
)