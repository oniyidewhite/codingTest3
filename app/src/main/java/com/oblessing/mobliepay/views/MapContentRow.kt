package com.oblessing.mobliepay.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.oblessing.mobliepay.core.mavericks.viewBinding
import com.oblessing.mobliepay.core.setBlockingOnClickListener
import com.oblessing.mobliepay.databinding.RowMapContentBinding
import com.oblessing.mobliepay.model.Place


@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_MATCH_HEIGHT)
class MapContentRow @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr) {
    private val bindings: RowMapContentBinding by viewBinding()
    private var googleMap: GoogleMap? = null
    private var places: List<Place> = emptyList()
    private var cameraPos: LatLng? = null

    // TODO: Register the map to lifecycle
    init {
        with(bindings.mapView) {
            // Initialise the MapView
            onCreate(null)
            onStart()
            onResume()
            // Set the map ready callback to receive the GoogleMap object
            getMapAsync {
                MapsInitializer.initialize(context)
                googleMap = it
                addMarkers(it)
            }
        }

        bindings.zoomIn.setBlockingOnClickListener {
            googleMap?.moveCamera(CameraUpdateFactory.zoomIn())
        }

        bindings.zoomOut.setBlockingOnClickListener {
            googleMap?.moveCamera(CameraUpdateFactory.zoomOut())
        }
    }

    private fun addMarkers(googleMap: GoogleMap) {
        // Add a marker in Sydney and move the camera
        googleMap.clear()

        if (places.isNotEmpty()) {
            places.forEachIndexed { i, v ->
                val location = LatLng(v.latitude.toDouble(), v.longitude.toDouble())
                googleMap.addMarker(
                    MarkerOptions()
                        .position(location)
                        .title(v.name)
                )

                // update camera only for fresh list
                if (i == 0 && cameraPos != location) cameraPos = location
            }
            cameraPos?.let { googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                CameraPosition.fromLatLngZoom(it, 0F))) }
        }
    }

    @ModelProp
    fun updateWithPlaces(data: List<Place>) {
        places = data
        googleMap?.let { addMarkers(it) }
    }
}