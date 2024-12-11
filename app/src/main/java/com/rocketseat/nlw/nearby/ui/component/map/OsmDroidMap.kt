package com.rocketseat.nlw.nearby.ui.component.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

@Composable
fun OsmDroidMap(modifier: Modifier = Modifier) {
    // Define o contexto e inicializa o OSMDroid
    val context = androidx.compose.ui.platform.LocalContext.current
    Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", 0))

    AndroidView(
        factory = { ctx ->
            // Cria o MapView do OSMDroid
            val mapView = MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK) // Fonte de mapa padrão
                setMultiTouchControls(true) // Suporte ao zoom por toque
                controller.setZoom(15.0) // Zoom inicial
                controller.setCenter(GeoPoint(-23.5505, -46.6333)) // Posição inicial (São Paulo)
            }
            mapView
        },
        update = { mapView ->
            // Atualizações podem ser feitas aqui, se necessário
        }
    )
}
