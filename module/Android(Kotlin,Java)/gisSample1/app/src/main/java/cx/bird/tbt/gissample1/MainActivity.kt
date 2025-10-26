package cx.bird.tbt.gissample1


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.Basemap
import com.esri.arcgisruntime.mapping.Viewpoint
import com.esri.arcgisruntime.mapping.view.LocationDisplay
import com.esri.arcgisruntime.mapping.view.MapView

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment
import com.esri.arcgisruntime.data.ServiceFeatureTable
import com.esri.arcgisruntime.geometry.*
import com.esri.arcgisruntime.layers.FeatureLayer
import com.esri.arcgisruntime.mapping.BasemapStyle
import com.esri.arcgisruntime.mapping.view.Graphic
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay
import com.esri.arcgisruntime.symbology.SimpleFillSymbol
import com.esri.arcgisruntime.symbology.SimpleLineSymbol
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol
import com.esri.arcgisruntime.symbology.SimpleRenderer


var mMapView: MapView? = null
private var mLocationDisplay: LocationDisplay? = null
lateinit var lineOverlay:GraphicsOverlay
lateinit var pointOverlay:GraphicsOverlay
lateinit var polylineBuilder: PolylineBuilder
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //APIKeyセット
        ArcGISRuntimeEnvironment.setApiKey("AAPKe265ca709b7043a091172dcfa89ed2bfIMBJr_L1g7siyCQ_NEct8ftwr-Kbizmd6apSsrLtp4YFOYU4NrsuhYUPLfuPyhaN")
        mMapView= findViewById<View>(R.id.mapView) as MapView
        //Mapセット
        val map = ArcGISMap("http://www.arcgis.com/home/item.html?id=69690c769d0446818b96af5c1892d8ba")
        mMapView!!.map = map

        //point
        pointOverlay = GraphicsOverlay()
        val pointSymbol = SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 3f)
        pointOverlay.renderer = SimpleRenderer(pointSymbol)
        //line
        lineOverlay = GraphicsOverlay()
        val polyLineSymbol = SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 2f)
        lineOverlay.renderer = SimpleRenderer(polyLineSymbol)

        //point,line追加
        mMapView!!.graphicsOverlays.addAll(listOf(pointOverlay, lineOverlay))
        //lineBuilderをWGS84(KID:4326)で生成
        polylineBuilder = PolylineBuilder(SpatialReference.create(4326))

        // MapView 上に現在位置を表示するために LocationDisplay を取得
        mLocationDisplay = mMapView!!.locationDisplay
        // LocationDisplay に LocationListner を設定
        mLocationDisplay!!.addLocationChangedListener(MyLocationListener())
        // 現在位置の表示を開始
        mLocationDisplay!!.startAsync()
        mMapView!!.locationDisplay.autoPanMode = LocationDisplay.AutoPanMode.RECENTER
        mMapView!!.locationDisplay.initialZoomScale = 300000.0
    }

    override fun onPause() {
        mMapView!!.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mMapView!!.resume()
    }

}


fun addPolyline(newPointX: Double,newPointY:Double){
    polylineBuilder.addPoint(newPointY,newPointX)
    pointOverlay.graphics.add(Graphic(newPointX,newPointY))
    lineOverlay.graphics.apply {
        clear()
        add(Graphic(polylineBuilder.toGeometry()))
    }
}

private class MyLocationListener : LocationDisplay.LocationChangedListener {
    override fun onLocationChanged(locationChangedEvent: LocationDisplay.LocationChangedEvent) {
        if (locationChangedEvent == null) {
            return
        }
        var newPointY = locationChangedEvent.location.position.x//lat,lon,x,yの関係がArcGISは逆
        var newPointX = locationChangedEvent.location.position.y
        addPolyline(newPointX,newPointY)

    }
}
