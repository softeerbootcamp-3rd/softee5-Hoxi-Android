package com.example.hoxi

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.service.autofill.FieldClassification.Match
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.Dimension
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
        setDestKeyListener()

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
                    clearRecommendedList()
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

    private fun setDestKeyListener(){
        val destText = findViewById<EditText>(R.id.dest_location)
        destText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                searchPlaces(s.toString())
                if(s.toString().length == 0)
                    clearRecommendedList()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                searchPlaces(s.toString())
            }
        })
    }
    fun searchPlaces(query: String) {
        val thread = Thread(Runnable {
            try {
                val url = URL("https://dapi.kakao.com/v2/local/search/keyword.json?query=$query")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("Authorization", "KakaoAK e54a4cc8f2a5e289dd5e950624ad0e0f")

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val line = reader.readLine()
                    val jsonObject = JSONObject(line)
                    val documents = jsonObject.getJSONArray("documents")

                    val layoutPlaces = findViewById<LinearLayout>(R.id.layout_places)
                    layoutPlaces.removeAllViews() // 기존에 추가된 view를 모두 제거

                    for (i in 0 until documents.length()) {
                        val place = documents.getJSONObject(i)
                        val name = place.getString("place_name")
                        val address = place.getString("address_name")
                        val latitude = place.getString("y")
                        val longitude = place.getString("x")

                        runOnUiThread {
                            val textView = TextView(this) // TextView 생성
                            val spannable = SpannableString(name) // SpannableString 생성
                            // dp를 px로 변환
                            val scale = resources.displayMetrics.density
                            val px = 16 * scale
                            val marginTop = 20 * scale
                            val marginStart = 22 * scale

                            // LayoutParams 생성
                            val layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )

                            // 상단 마진과 시작 마진 설정
                            layoutParams.topMargin = marginTop.toInt()
                            layoutParams.marginStart = marginStart.toInt()
                            textView.layoutParams = layoutParams

                            // TextView 글자 크기 설정 (단위는 px)
                            textView.setTextSize(Dimension.DP, 40f)
                            textView.setBackgroundColor(Color.WHITE)

                            // 검색어와 일치하는 부분의 시작과 끝 인덱스를 구함
                            val start = name.indexOf(query)
                            val end = start + query.length

                            // 검색어와 일치하는 부분에 회색 색상을 적용
                            if (start != -1) {
                                spannable.setSpan(
                                    ForegroundColorSpan(Color.parseColor("#FF634E")),
                                    start,
                                    end,
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                            }

                            textView.text = spannable // TextView에 name 설정

                            // OnClickListener 설정
                            textView.setOnClickListener {

                                val intent = Intent(this, DestinationMap::class.java)
                                intent.putExtra("placeName", name) // placeName 데이터를 intent에 추가
                                intent.putExtra("address", address) // placeName 데이터를 intent에 추가
                                intent.putExtra("latitude", latitude) // placeName 데이터를 intent에 추가
                                intent.putExtra("longitude", longitude) // placeName 데이터를 intent에 추가
                                intent.putExtra("src", findViewById<TextView>(R.id.src_location).text.toString())
                                mapViewContainer.removeAllViews()
                                startActivity(intent) // activity_request_information Activity로 이동
                            }

                            layoutPlaces.addView(textView) // LinearLayout에 TextView 추가
                        }
                    }
                }

                connection.disconnect()
            } catch (e: Exception) {
                println("뭐지" +e.message)
            }
        })
        thread.start()
    }

    private fun clearRecommendedList(){
        val layoutPlaces = findViewById<LinearLayout>(R.id.layout_places)
        layoutPlaces.removeAllViews() // 기존에 추가된 view를 모두 제거
    }

}