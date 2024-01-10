package com.example.hoxi

import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import kotlin.concurrent.timer

class ProceedStatus : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proceed_status)
        setTimer(findViewById<TextView>(R.id.time_text),1,30)
        findViewById<View>(R.id.accept_button).setOnClickListener{
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }
    }
    private fun setTimer(time : TextView, targetHour : Int, targetMinute : Int){
        val targetSecond = 0

        timer(period=1000) {
            val now = Calendar.getInstance()
            var remainingHour = targetHour - now.get(Calendar.HOUR_OF_DAY)
            var remainingMinute = targetMinute - now.get(Calendar.MINUTE)
            var remainingSecond = targetSecond - now.get(Calendar.SECOND)

            if (remainingSecond < 0) {
                remainingSecond += 60
                remainingMinute--
            }
            if (remainingMinute < 0) {
                remainingMinute += 60
                remainingHour--
            }
            if (remainingHour < 0) {
                remainingHour += 24
            }

            // UI 업데이트는 메인 스레드에서만 가능하므로 runOnUiThread() 메서드를 사용합니다.
            runOnUiThread {
                time.text = String.format("%02d : %02d : %02d", remainingHour, remainingMinute, remainingSecond)
            }
        }
    }
}