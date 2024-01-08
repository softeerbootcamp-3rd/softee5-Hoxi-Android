package com.example.hoxi

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.service.autofill.FieldClassification.Match
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var mapViewContainer: ViewGroup
    private lateinit var srcMarker : MapPOIItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, RequestInformation::class.java)
        startActivity(intent)

        // 권한 체크
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        mapView = MapView(this)
        mapViewContainer = findViewById(R.id.map_view)
        mapViewContainer.addView(mapView)

        srcMarker = MapPOIItem()
        srcMarker.itemName = "출발 위치"
        srcMarker.tag = 0
        srcMarker.markerType = MapPOIItem.MarkerType.CustomImage
        srcMarker.customImageResourceId = R.drawable.pin

        if (location != null) {
            val latitude = location.latitude
            val longitude = location.longitude
            val mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude)

            mapView.setMapCenterPoint(mapPoint, true)
        }

        mapView.setMapViewEventListener(object : MapView.MapViewEventListener {
            override fun onMapViewInitialized(mapView: MapView) {
                // 지도가 초기화된 후 호출되는 콜백
            }

            override fun onMapViewCenterPointMoved(mapView: MapView, mapPoint: MapPoint) {
                // 지도 중심 좌표가 이동한 경우 호출되는 콜백
                srcMarker.mapPoint = mapPoint;
                mapView.removeAllPOIItems();
                mapView.addPOIItem(srcMarker);
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
                updateAddressUsingCoordinate(srcMarker)
            }

            override fun onMapViewMoveFinished(mapView: MapView, mapPoint: MapPoint) {
                // 지도 이동이 완료된 경우 호출되는 콜백
            }
        })

        val layout = findViewById<View>(R.id.sliding_layout) as SlidingUpPanelLayout

        layout.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {
                // 패널이 슬라이드되는 동안에는 아무런 동작을 하지 않습니다.
            }

            override fun onPanelStateChanged(panel: View?, previousState: PanelState?, newState: PanelState) {
                val hoxiLogo = findViewById<View>(R.id.hoxi_logo)
                val userIcon = findViewById<View>(R.id.user_icon)
                val whiteCircle = findViewById<View>(R.id.white_circle)
                val compass = findViewById<View>(R.id.compass)
                val dispatchText = findViewById<View>(R.id.dispatch_start_text)
                val dispatchBtn = findViewById<View>(R.id.dispatch_start_button)
                val backBtn = findViewById<View>(R.id.back_button)
                val srcBox =  findViewById<View>(R.id.src_rectangle_box)
                val divider = findViewById<View>(R.id.divider)
                val latestDest = findViewById<View>(R.id.latest_dest)

                if(newState == PanelState.DRAGGING) {
                    hoxiLogo.toggleVisibility()
                    userIcon.toggleVisibility()
                    whiteCircle.toggleVisibility()
                    compass.toggleVisibility()
                    dispatchText.toggleVisibility()
                    dispatchBtn.toggleVisibility()
                    backBtn.toggleVisibility()
                    divider.toggleVisibility()
                    latestDest.toggleVisibility()
                    srcBox.changeAnchor(backBtn)
                }
            }

        })
    }

    fun updateAddressUsingCoordinate(marker: MapPOIItem) {
        val thread = Thread(Runnable {
            try {
                val url = URL("https://dapi.kakao.com/v2/local/geo/coord2address.json?x=${marker.mapPoint.mapPointGeoCoord.longitude}&y=${marker.mapPoint.mapPointGeoCoord.latitude}&input_coord=WGS84")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("Authorization", "KakaoAK e54a4cc8f2a5e289dd5e950624ad0e0f")

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val line = reader.readLine()
                    val jsonObject = JSONObject(line)
                    val documents = jsonObject.getJSONArray("documents")

                    if (documents.length() > 0) {
                        val place = documents.getJSONObject(0)
                        val addressInfo = place.getJSONObject("address")
                        val address = addressInfo.getString("address_name")

                        // 이제 'address'에는 주소 정보가 저장되어 있습니다.
                        // 이를 사용자 인터페이스에 표시하거나 다른 작업을 수행하면 됩니다.

                        runOnUiThread{
                            findViewById<TextView>(R.id.src_location).text = address
                        }
                    }
                }

                connection.disconnect()
            } catch (e: Exception) {
                println(e.message)
            }
        })
        thread.start()
    }
    fun View.toggleVisibility() {
        visibility = if (visibility == View.VISIBLE) {
            View.INVISIBLE
        } else if(visibility == View.VISIBLE && this.id == R.id.back_button){
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    fun View.changeAnchor(backBtn : View){
        val layoutParams = this.layoutParams as ConstraintLayout.LayoutParams
        if(backBtn.visibility == View.VISIBLE){
            layoutParams.topToTop = ConstraintLayout.LayoutParams.UNSET
            layoutParams.topToBottom = backBtn.id
        } else{
            layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        }

        this.layoutParams = layoutParams
    }
}