package com.rocketseat.nlw.nearby.ui.screen.home

import com.rocketseat.nlw.nearby.data.model.Category
import com.rocketseat.nlw.nearby.data.model.Market
import org.osmdroid.util.GeoPoint

data class HomeUiState(
    val categories: List<Category>? = null,
    val markets: List<Market>? = null,
    val marketLocations: List<GeoPoint>? = null
)