package com.example.hoxi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import com.example.hoxi.service.ApiService
import com.example.hoxi.service.CallResponseDto
import com.example.hoxi.service.DriverInfoResponse
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class Matching : AppCompatActivity() {
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val retrofit = Retrofit.Builder()
            .baseUrl("http://13.124.164.211")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

        setContentView(R.layout.activity_matching)
        setSrcANDDest()
//        sendGetRequest()
        var flag = true
        val handler = Handler()

        val runnable = object : Runnable {
            override fun run() {
                if (flag) { // flag가 true인 경우에만 요청을 보냅니다. 원하는 조건에 따라 flag를 변경해야 합니다.
                    val call: Call<DriverInfoResponse> = apiService.getDriverInfo()
                    call.enqueue(object : Callback<DriverInfoResponse> {
                        override fun onResponse(call: Call<DriverInfoResponse>, response: Response<DriverInfoResponse>) {
                            if (response.isSuccessful) {
                                val driverInfo: DriverInfoResponse? = response.body()
                                flag = false // 성공적인 응답을 받으면 flag를 false로 변경하여 중복 요청을 방지합니다.
                                val intent = Intent(this@Matching, HandOverStatus::class.java)
                                startActivity(intent)
                            }
                        }
                        override fun onFailure(call: Call<DriverInfoResponse>, t: Throwable) {
                            // 요청 실패 시 처리
                        }
                    })
                }
                // 5초 후에 다시 요청을 보내도록 합니다.
                handler.postDelayed(this, 5000)
            }
        }

// 처음에 한 번 요청을 보내도록 합니다.
        handler.post(runnable)
    }

    private fun setSrcANDDest(){
        findViewById<TextView>(R.id.src_location).setText(getIntent().getStringExtra("src"))
        findViewById<TextView>(R.id.dest_location).setText(getIntent().getStringExtra("placeName"))
    }
}