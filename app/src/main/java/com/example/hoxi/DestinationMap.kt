package com.example.hoxi

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DestinationMap : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var mapViewContainer: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_destination_map)
            setText()
            setDestinationListener()

            // 권한 체크
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }

            mapView = MapView(this)
            mapViewContainer = findViewById(R.id.map_view)
            mapViewContainer.addView(mapView)

            val latitude = intent.getStringExtra("latitude")?.toDouble()
            val longitude = intent.getStringExtra("longitude")?.toDouble()

            if(latitude != null && longitude != null) {
                val mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude)
                mapView.setMapCenterPoint(mapPoint, true)
                val marker = MapPOIItem()
                marker.itemName = "Marker"
                marker.tag = 0
                marker.mapPoint = mapPoint
                marker.markerType = MapPOIItem.MarkerType.CustomImage
                marker.customImageResourceId = R.drawable.pin3
                mapView.addPOIItem(marker)
            }


            mapView.setMapViewEventListener(object : MapView.MapViewEventListener {
                override fun onMapViewInitialized(mapView: MapView) {
                    // 지도가 초기화된 후 호출되는 콜백
                }

                override fun onMapViewCenterPointMoved(mapView: MapView, mapPoint: MapPoint) {
                    // 지도 중심 좌표가 이동한 경우 호출되는 콜백
                }

                override fun onMapViewZoomLevelChanged(mapView: MapView, zoomLevel: Int) {
                    // 지도의 줌 레벨이 변경된 경우 호출되는 콜백
                }

                override fun onMapViewSingleTapped(mapView: MapView, mapPoint: MapPoint) {
                    // 지도를 한 번 탭한 경우 호출되는 콜백
                }

                override fun onMapViewDoubleTapped(mapView: MapView, mapPoint: MapPoint) {
                    // 지도를 두 번 탭한 경우 호출되는 콜백
                }

                override fun onMapViewLongPressed(mapView: MapView, mapPoint: MapPoint) {
                    // 지도를 길게 누른 경우 호출되는 콜백
                }

                override fun onMapViewDragStarted(mapView: MapView, mapPoint: MapPoint) {
                    // 지도 드래그가 시작된 경우 호출되는 콜백
                }

                override fun onMapViewDragEnded(mapView: MapView, mapPoint: MapPoint) {
                    // 지도 드래그가 종료된 경우 호출되는 콜백
                }

                override fun onMapViewMoveFinished(mapView: MapView, mapPoint: MapPoint) {
                    // 지도 이동이 완료된 경우 호출되는 콜백
                }
            })

        }
        catch (e : Exception){
            println("에러" + e.cause)
            println("에러" + e.message)
        }
    }

    private fun setText(){
        val placeView = findViewById<TextView>(R.id.place_name)
        val addressView = findViewById<TextView>(R.id.address)

        val placeName = intent.getStringExtra("placeName")
        val address = intent.getStringExtra("address")

        placeView.setText(placeName)
        addressView.setText(address)
    }

    private fun setDestinationListener(){
        val settingBtn = findViewById<View>(R.id.setting_btn)

        settingBtn.setOnClickListener{
            val intent = Intent(this, RequestInformation::class.java)
            intent.putExtra("src", getIntent().getStringExtra("src"))
            intent.putExtra("placeName", getIntent().getStringExtra("placeName"))
            mapViewContainer.removeAllViews()
            startActivity(intent)
        }
    }

}