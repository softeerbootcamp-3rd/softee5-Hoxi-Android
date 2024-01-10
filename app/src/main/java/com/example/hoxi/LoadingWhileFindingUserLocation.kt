package com.example.hoxi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class LoadingWhileFindingUserLocation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loding_while_finding_user_location)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, DarkMap::class.java)
            intent.putExtra("hours", 1)
            intent.putExtra("minutes", 30)
            startActivity(intent)
        }, 3000) // 5000ms = 5초
    }
}