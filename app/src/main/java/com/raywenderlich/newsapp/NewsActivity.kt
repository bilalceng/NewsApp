package com.raywenderlich.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class NewsActivity : AppCompatActivity() {
    private lateinit var newsNavHostFragment: Fragment
    private lateinit var buttonNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        buttonNavigationView = findViewById(R.id.bottomNavigationView)

        buttonNavigationView.setupWithNavController(supportFragmentManager.findFragmentById(R.id.newsNavHostFragment)!!.findNavController())

    }
}
