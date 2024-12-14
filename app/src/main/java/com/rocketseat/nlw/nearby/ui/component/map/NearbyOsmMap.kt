package com.rocketseat.nlw.nearby.ui.component.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.drawable.toBitmap
import com.rocketseat.nlw.nearby.R
import com.rocketseat.nlw.nearby.data.model.mock.mockUserLocation
import com.rocketseat.nlw.nearby.ui.screen.home.HomeUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun NearbyOsmMap(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Configurações do OSMDroid
    Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", 0))

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { ctx ->
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)

                // Configura posição inicial do mapa
                controller.setZoom(17.0)
                controller.setCenter(mockUserLocation)

                // Adiciona marcador do usuário
                addCustomMarker(
                    mapView = this,
                    geoPoint = mockUserLocation,
                    drawableResId = R.drawable.ic_user_location,
                    context = ctx,
                    sizeDp = 72
                )


            }
        },
        update = { ctx ->
            ctx.apply {
                // Remove marcadores anteriores
                ctx.overlays.clear()
                // Adiciona marcadores de mercados, se disponíveis
                if (!uiState.markets.isNullOrEmpty()) {
                    uiState.marketLocations?.forEachIndexed { index, geoPoint ->
                        addCustomMarker(
                            mapView = this,
                            geoPoint = geoPoint,
                            drawableResId = R.drawable.img_pin,
                            context = ctx.context,
                            sizeDp = 38,
                            title = uiState.markets[index].name
                        )
                    }// Ajusta a câmera para englobar todos os pontos
                    coroutineScope.launch {

                        val allPoints = uiState.marketLocations?.plus(
                            mockUserLocation
                        )

                        val locator =
                            allPoints?.let { calculateCenter(it) }

                        if (locator != null) {
                                delay(200) // Aguarda para garantir que os pontos sejam adicionados
                                /*val boundingBox = BoundingBox.fromGeoPoints(allPoints)
                                this@apply.zoomToBoundingBox(boundingBox, true)*/
                                controller.animateTo(locator,16.32,1000)

                        }
                    }
                }
            }
        }
    )
}

// Função para adicionar um marcador personalizado
fun addCustomMarker(
    mapView: MapView,
    geoPoint: GeoPoint,
    drawableResId: Int,
    context: android.content.Context,
    sizeDp: Int,
    title: String? = null,
) {
    val marker = Marker(mapView)
    marker.position = geoPoint
    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
    marker.title = title

    // Configurar ícone personalizado
    val drawable = context.getDrawable(drawableResId)
    val bitmap = drawable?.toBitmap(sizeDp, sizeDp)
    marker.icon = bitmap?.let { android.graphics.drawable.BitmapDrawable(context.resources, it) }

    mapView.overlays.add(marker)
}


