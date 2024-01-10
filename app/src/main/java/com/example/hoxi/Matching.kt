package com.example.hoxi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.example.hoxi.service.ApiService
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class Matching : AppCompatActivity() {
    private lateinit var apiService: ApiService
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matching)
        setSrcANDDest()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://13.124.164.211")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)
        GlobalScope.launch {
            sendGetRequest()

            // sendGetRequest()가 완료될 때까지 대기
            // withContext(Dispatchers.Main)는 UI 스레드에서 실행되어야 함을 나타냄
            withContext(Dispatchers.Main) {
                val intent = Intent(this@Matching, HandOverStatus::class.java)
                startActivity(intent) // sendGetRequest() 완료 후 startActivity(intent) 호출
            }
        }
    }

    private fun setSrcANDDest(){
        findViewById<TextView>(R.id.src_location).setText(getIntent().getStringExtra("src"))
        findViewById<TextView>(R.id.dest_location).setText(getIntent().getStringExtra("placeName"))
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun sendGetRequest() {
        // GlobalScope.launch를 사용하여 코루틴 시작
        GlobalScope.launch {
            while (true) {
                delay(3000) // 3초 지연

                try {
                    val response = apiService.getDriverInfo()
                    if (response.isSuccessful) {
                        println("요청이 성공했습니다.")
                        break // statusCode가 200이면 요청 중단
                    } else {
                        println("요청 중...")
                    }
                } catch (e: IOException) {
                    println("네트워크 에러: ${e.message}")
                }
            }
        }
    }
}