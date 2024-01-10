package com.example.hoxi

import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class TimePickerModal : AppCompatActivity() {
    private var periodDoneTv: TextView? = null
    private var alertDatepickerAmpm: NumberPicker? = null
    private var alertDatepickerHour: NumberPicker? = null
    private var alertDatepickerMinute: NumberPicker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_picker)

        periodDoneTv = findViewById<View>(R.id.period_done_tv) as TextView
        alertDatepickerAmpm = findViewById<View>(R.id.alert_datepicker_ampm) as NumberPicker
        alertDatepickerHour = findViewById<View>(R.id.alert_datepicker_hour) as NumberPicker
        alertDatepickerMinute = findViewById<View>(R.id.alert_datepicker_minute) as NumberPicker

        // NumberPicker에 값 설정
        alertDatepickerAmpm!!.minValue = 0
        alertDatepickerAmpm!!.maxValue = 1
        alertDatepickerAmpm!!.displayedValues = arrayOf("AM", "PM")

        alertDatepickerHour!!.minValue = 0
        alertDatepickerHour!!.maxValue = 11

        alertDatepickerMinute!!.minValue = 0
        alertDatepickerMinute!!.maxValue = 1
        alertDatepickerMinute!!.displayedValues = arrayOf("0", "30")
    }
}