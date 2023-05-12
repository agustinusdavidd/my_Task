package com.example.my_task

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val homeFragment = homeFragment()                                                                       //Declare dafault fragment
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerMain, homeFragment).commit()
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> {
                    loadFragment(homeFragment())
                    true
                }
                R.id.calendar -> {
                    startActivity(Intent(this, calendarActivity::class.java))
                    true
                }
                R.id.notification -> {
                    loadFragment(notificationFragment())
                    true
                }
                R.id.profile -> {
                    loadFragment(profileFragment())
                    true
                }
                else -> false
            }
        }

    }

    private fun loadFragment(fragment : Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerMain, fragment)
            .commit()
    }
}