package com.example.hoxi

import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import org.w3c.dom.Text
import kotlin.concurrent.timer


class RequestList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_list)
        init()
    }

    private fun init(){
        val itemList = findViewById<View>(R.id.item_list) as LinearLayout
        val acceptBtn = findViewById<View>(R.id.accept_btn)
        val acceptText = findViewById<TextView>(R.id.accept_text)
        for (i in 0 until itemList.childCount) {
            val itemView = itemList.getChildAt(i)
            itemView.setOnClickListener{
                it.setBackgroundResource(R.drawable.rounded_12_gray_800_white_border_rectangle)
                acceptBtn.setBackgroundResource(R.drawable.rounded_12_main_color_rectangle)
                acceptText.setTextColor(Color.parseColor("#FFFFFF"))
            }
            if(i==0) continue
            val srcTextView = itemView.findViewById<View>(R.id.src) as TextView
            val destTextView = itemView.findViewById<View>(R.id.dest_text) as TextView
            setTimer(itemView.findViewById<TextView>(R.id.time_text), 2, 40)
            srcTextView.text = "부산 초량제3동 부산역"
            destTextView.text = "서면역"
        }
        setTimer(itemList.getChildAt(0).findViewById<TextView>(R.id.time_text), 1, 30)

        acceptBtn.setOnClickListener{
            val intent = Intent(this, RequestDetail::class.java)
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