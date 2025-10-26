package cx.bird.tbt.flightdisplayfors270malus


import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment
import com.esri.arcgisruntime.geometry.*
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.BasemapStyle
import com.esri.arcgisruntime.mapping.Viewpoint
import com.esri.arcgisruntime.mapping.view.Graphic
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay
import com.esri.arcgisruntime.mapping.view.LocationDisplay
import com.esri.arcgisruntime.mapping.view.MapView
import com.esri.arcgisruntime.symbology.SimpleLineSymbol
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol
import com.esri.arcgisruntime.symbology.SimpleRenderer
import kotlin.math.roundToInt


class Map() : Fragment(), LocationDisplay.LocationChangedListener {
    var mMapView: MapView? = null
    private var mLocationDisplay: LocationDisplay? = null
    private lateinit var lineOverlay: GraphicsOverlay
    private lateinit var polylineBuilder: PolylineBuilder


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_map, container, false)
        // APIKeyセット
        ArcGISRuntimeEnvironment.setApiKey(getString(R.string.mapAPIKey))
        mMapView = view.findViewById<View>(R.id.mapView) as MapView
        //Mapセット
        val map = ArcGISMap("http://www.arcgis.com/home/item.html?id=${getString(R.string.mapID)}")
        mMapView!!.map = map

        // line
        lineOverlay = GraphicsOverlay()
        val polyLineSymbol = SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 2f)
        lineOverlay.renderer = SimpleRenderer(polyLineSymbol)

        // line追加
        mMapView!!.graphicsOverlays.add(lineOverlay)
        // PolylineBuilderをWGS84(EPSG:4326)で生成
        polylineBuilder = PolylineBuilder(SpatialReference.create(4326))

        // MapView 上に現在位置を表示するために LocationDisplay を取得
        mLocationDisplay = mMapView!!.locationDisplay
        // LocationDisplay に LocationListner を設定
        mLocationDisplay!!.addLocationChangedListener(this)
        // 現在位置の表示を開始
        mLocationDisplay!!.startAsync()
        mMapView!!.locationDisplay.autoPanMode = LocationDisplay.AutoPanMode.RECENTER
        mMapView!!.locationDisplay.initialZoomScale = 300000.0
        return view
    }

    private var oldRoundLat = 0.0
    private var oldRoundLon = 0.0
    override fun onLocationChanged(locationChangedEvent: LocationDisplay.LocationChangedEvent) {
        if (locationChangedEvent == null) {
            return
        }
        val newPointLon = locationChangedEvent.location.position.x//経度
        val newPointLat = locationChangedEvent.location.position.y//緯度

        val newRoundLon = (newPointLon * 10000.0).roundToInt() / 10000.0//桁丸めるxxx.xxxx
        val newRoundLat = (newPointLat * 10000.0).roundToInt() / 10000.0//桁丸めるxx.xxxx
        if ((newRoundLat != oldRoundLat) || (newRoundLon != oldRoundLon)) {
            addPolyline(newPointLon, newPointLat)
            oldRoundLat = newRoundLat
            oldRoundLon = newRoundLon
        }


    }

    override fun onPause() {
        mMapView!!.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mMapView!!.resume()
    }

    private fun addPolyline(newPointLon: Double, newPointLat: Double) {
        polylineBuilder.addPoint(newPointLon, newPointLat)
        lineOverlay.graphics.apply {
            clear()
            add(Graphic(polylineBuilder.toGeometry()))
        }
    }

}



