package com.example.hoxi

import LuggageNotice
import TimePickerListener
import TimePickerModal
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class RequestInformation : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_information)

        setPeriodListener()
        try {
            emphasizeText()
            setRadioListener()
            setCountListener()
            setDoneListener()
        } catch (e: Exception) {
            println("에러다!" + e.message)
        }
    }

    fun setPeriodListener(){
        val timePeriodView = findViewById<TextView>(R.id.time_period)
        val hoursView = findViewById<TextView>(R.id.desired_arrival_time_hours)
        val minutesView = findViewById<TextView>(R.id.desired_arrival_time_minutes)
        val colon1View = findViewById<TextView>(R.id.desired_arrival_time_box_colon_1)
        val colon2View = findViewById<TextView>(R.id.desired_arrival_time_box_colon_2)
        val boxView = findViewById<View>(R.id.desired_arrival_time_box)

        val textViewClickListener = View.OnClickListener { openTimePickerDialog() }

        timePeriodView.setOnClickListener(textViewClickListener)
        hoursView.setOnClickListener(textViewClickListener)
        minutesView.setOnClickListener(textViewClickListener)
        colon1View.setOnClickListener(textViewClickListener)
        colon2View.setOnClickListener(textViewClickListener)
        boxView.setOnClickListener(textViewClickListener)
    }
    private fun openTimePickerDialog() {
        val dialog = TimePickerModal(object : TimePickerListener {
            override fun onTimeSelected(hour: Int, minute: Int, amPm: String) {
                val timePeriodView = findViewById<TextView>(R.id.time_period)
                val hoursView = findViewById<TextView>(R.id.desired_arrival_time_hours)
                val minutesView = findViewById<TextView>(R.id.desired_arrival_time_minutes)
                val colon1View = findViewById<TextView>(R.id.desired_arrival_time_box_colon_1)
                val colon2View = findViewById<TextView>(R.id.desired_arrival_time_box_colon_2)

                timePeriodView.text = amPm
                hoursView.text = String.format("%02d시", hour)
                minutesView.text = String.format("%02d분", minute)
                timePeriodView.setTextColor(Color.parseColor("#282828"))
                hoursView.setTextColor(Color.parseColor("#282828"))
                minutesView.setTextColor(Color.parseColor("#282828"))
                colon1View.setTextColor(Color.parseColor("#282828"))
                colon2View.setTextColor(Color.parseColor("#282828"))
            }
        })
        dialog.show(supportFragmentManager, "TimePickerModal")
    }

    private fun openLuggageDialog() {
        val dialog = LuggageNotice()
        dialog.show(supportFragmentManager, "TimePickerModal")
    }

    private fun emphasizeText(){
        val text = "28인치 이상 캐리어 크기의 짐이\n포함되어 있나요?"
        val spannableString = SpannableString(text)

        val colorSpan = ForegroundColorSpan(Color.parseColor("#FF6550"))
        spannableString.setSpan(colorSpan, 0, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val textView = findViewById<TextView>(R.id.has_over_28_inch_luggage_title)
        textView.text = spannableString
    }

    fun setRadioListener(){
        val noBox = findViewById<View>(R.id.no_over_28_inch_luggage_box)
        val noText = findViewById<TextView>(R.id.no_over_28_inch_luggage_text)
        val yesBox = findViewById<View>(R.id.yes_over_28_inch_luggage_box)
        val yesText = findViewById<TextView>(R.id.yes_over_28_inch_luggage_text)


        val textViewClickListener = View.OnClickListener {
            if(it == noText || it == noBox){
                noText.setTextColor(Color.parseColor("#282828"))
                yesText.setTextColor(Color.parseColor("#D9D9D9"))
            }
            else{
                yesText.setTextColor(Color.parseColor("#282828"))
                noText.setTextColor(Color.parseColor("#D9D9D9"))
                try {
                    openLuggageDialog()
                }
                catch (e : Exception){
                    println("에러다 !" + e.message)
                }
            }
        }

        noBox.setOnClickListener(textViewClickListener)
        noText.setOnClickListener(textViewClickListener)
        yesBox.setOnClickListener(textViewClickListener)
        yesText.setOnClickListener(textViewClickListener)
    }

    private fun setCountListener(){
        val plusBtn = findViewById<View>(R.id.plus)
        val minusBtn = findViewById<View>(R.id.minus)

        val textViewClickListener = View.OnClickListener {
            val currentCount = findViewById<TextView>(R.id.luggage_count_text)
            val count = getLuggageCount(currentCount.text.toString())

            if(it == plusBtn){
                currentCount.setText("${count+1}개")
                currentCount.setTextColor(Color.parseColor("#282828"))
                plusBtn.setBackgroundResource(R.drawable.plus_fill)
                minusBtn.setBackgroundResource(R.drawable.minus_fill)
            }
            else if(it == minusBtn && count > 0){
                currentCount.setText("${count-1}개")
                if(count-1 == 0){
                    currentCount.setTextColor(Color.parseColor("#D9D9D9"))
                    plusBtn.setBackgroundResource(R.drawable.plus)
                    minusBtn.setBackgroundResource(R.drawable.minus)
                } else {
                    currentCount.setTextColor(Color.parseColor("#282828"))
                    plusBtn.setBackgroundResource(R.drawable.plus_fill)
                    minusBtn.setBackgroundResource(R.drawable.minus_fill)
                }
            }
        }

        plusBtn.setOnClickListener(textViewClickListener)
        minusBtn.setOnClickListener(textViewClickListener)

    }
    private fun getLuggageCount(count : String) : Int{
        return count.dropLast(1).toInt()
    }

    private fun isDone() : Boolean{

            val userName = findViewById<TextView>(R.id.user_name_text).text.toString()
            val userPhone = findViewById<TextView>(R.id.user_phone_number_text).text.toString()
            val accommodationContact = findViewById<TextView>(R.id.accommodation_contact_text).text.toString()
            val luggageCount = getLuggageCount(findViewById<TextView>(R.id.luggage_count_text).text.toString())
            val hoursView = findViewById<TextView>(R.id.desired_arrival_time_hours).textColors
            val no = findViewById<TextView>(R.id.no_over_28_inch_luggage_text).textColors
            val yes = findViewById<TextView>(R.id.yes_over_28_inch_luggage_text).textColors
            val defaultTextColor = Color.parseColor("#D9D9D9")

            if (userName.isEmpty() || userPhone.isEmpty() || accommodationContact.isEmpty() ||
                hoursView.equals(defaultTextColor) || (no.equals(defaultTextColor) && yes.equals(defaultTextColor)) ||
                luggageCount <= 0
            ) {
                return false
            } else {
                return true
            }
    }

    fun String.isEmpty() : Boolean{
        return this.isNullOrEmpty()
    }

    fun setDoneListener(){
//        val userName = findViewById<TextView>(R.id.user_name_text)
//        val userPhone = findViewById<TextView>(R.id.user_phone_number_text)
//        val accommodationContact = findViewById<TextView>(R.id.accommodation_contact_text)
//        val hoursView = findViewById<TextView>(R.id.desired_arrival_time_hours)
//        val no = findViewById<TextView>(R.id.no_over_28_inch_luggage_text)
//        val yes = findViewById<TextView>(R.id.yes_over_28_inch_luggage_text)
        val plus =  findViewById<View>(R.id.plus)
        val minus =  findViewById<View>(R.id.minus)
        val paymentBtn = findViewById<View>(R.id.payment_box)

//
//        val textWatcher = object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                // 입력하기 전에 조치
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                // 입력이 끝났을 때 조치
////                if(isDone()){
////                    paymentBtn.setBackgroundResource(R.drawable.rounded_12_main_color_rectangle)
////                } else{
////                    paymentBtn.setBackgroundResource(R.drawable.rounded_12_gray_rectangle)
////                }
//            }
//        }

        // 각 TextView에 TextWatcher 설정
        // plus, minus 버튼에 클릭 리스너 설정
        plus.setOnClickListener {

            val currentCount = findViewById<TextView>(R.id.luggage_count_text)
            val count = getLuggageCount(currentCount.text.toString())
            val plusBtn = findViewById<View>(R.id.plus)
            val minusBtn = findViewById<View>(R.id.minus)
            val charge = findViewById<TextView>(R.id.charge_text)

            currentCount.setText("${count+1}개")
            currentCount.setTextColor(Color.parseColor("#282828"))
            plusBtn.setBackgroundResource(R.drawable.plus_fill)
            minusBtn.setBackgroundResource(R.drawable.minus_fill)

            if (isDone()) {
                paymentBtn.setBackgroundResource(R.drawable.rounded_12_main_color_rectangle)
                charge.setTextColor(Color.parseColor("#FF634E"))
            } else {
                paymentBtn.setBackgroundResource(R.drawable.rounded_16_rectangle)
                charge.setTextColor(Color.parseColor("#D9D9D9"))
            }

        }

        minus.setOnClickListener {

            val currentCount = findViewById<TextView>(R.id.luggage_count_text)
            val count = getLuggageCount(currentCount.text.toString())
            val plusBtn = findViewById<View>(R.id.plus)
            val minusBtn = findViewById<View>(R.id.minus)
            val charge = findViewById<TextView>(R.id.charge_text)

            if(count > 0){
                currentCount.setText("${count-1}개")
                if(count-1 == 0){
                    currentCount.setTextColor(Color.parseColor("#D9D9D9"))
                    plusBtn.setBackgroundResource(R.drawable.plus)
                    minusBtn.setBackgroundResource(R.drawable.minus)
                } else {
                    currentCount.setTextColor(Color.parseColor("#282828"))
                    plusBtn.setBackgroundResource(R.drawable.plus_fill)
                    minusBtn.setBackgroundResource(R.drawable.minus_fill)
                }
            }

            if(isDone()){
                paymentBtn.setBackgroundResource(R.drawable.rounded_12_main_color_rectangle)
                charge.setTextColor(Color.parseColor("#FF634E"))
            } else{
                paymentBtn.setBackgroundResource(R.drawable.rounded_16_rectangle)
                charge.setTextColor(Color.parseColor("#D9D9D9"))
            }
        }
    }

}

