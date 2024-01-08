package com.example.hoxi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView

class Matching : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matching)
        setSrcANDDest()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, HandOverStatus::class.java)
            startActivity(intent)
        }, 3000) // 5000ms = 5초
    }

    private fun setSrcANDDest(){
        findViewById<TextView>(R.id.src_location).setText(getIntent().getStringExtra("src"))
        findViewById<TextView>(R.id.dest_location).setText(getIntent().getStringExtra("placeName"))
    }
}