package com.example.hoxi

import android.content.pm.PackageManager
import android.Manifest
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MainActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var mapViewContainer: ViewGroup
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 권한 체크
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

        mapView = MapView(this)
        mapViewContainer = findViewById(R.id.map_view)
        mapViewContainer.addView(mapView)

        mapView.setMapViewEventListener(object : MapView.MapViewEventListener {
            override fun onMapViewInitialized(mapView: MapView) {
                // 지도가 초기화된 후 호출되는 콜백
            }

            override fun onMapViewCenterPointMoved(mapView: MapView, mapPoint: MapPoint) {
                // 지도 중심 좌표가 이동한 경우 호출되는 콜백
//                srcMarker.mapPoint = mapPoint;
//                mapView.removeAllPOIItems();
//                mapView.addPOIItem(srcMarker);
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
                //updateAddressUsingCoordinate(srcMarker)
            }

            override fun onMapViewMoveFinished(mapView: MapView, mapPoint: MapPoint) {
                // 지도 이동이 완료된 경우 호출되는 콜백
            }
        })

    }
}