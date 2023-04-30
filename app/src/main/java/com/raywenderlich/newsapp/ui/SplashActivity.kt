package com.raywenderlich.newsapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.raywenderlich.newsapp.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val mainIntent = Intent(this, NewsActivity::class.java)
            startActivity(mainIntent)
            finish()
        }, 3000) // 3000 milliseconds = 3 seconds delay
    }
}