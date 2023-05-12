package com.example.my_task

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY

class onboardingActivity : AppCompatActivity() {
    private var currentFragmentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        supportActionBar?.hide()


        val btnNext = findViewById<Button>(R.id.btnNext)                            //declare local variable for button next
        val btnSkip = findViewById<Button>(R.id.btnSkip)                            //declare local variable for button skip

        val onboarding1 = onboarding1Fragment()                                     //make first onboarding show
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, onboarding1)
        fragmentTransaction.commit()

        btnNext.setOnClickListener{showNextFragment()}                              //set logic for button next if button next is pressed

        btnSkip.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)        //set logic for button skip if button skip is pressed
            startActivity(intent)
            finish()
        }
    }

    private fun showNextFragment(){
        val btnNext = findViewById<Button>(R.id.btnNext)                            //declare local variable for button next
        val btnSkip = findViewById<Button>(R.id.btnSkip)                            //declare local variable for button skip

        currentFragmentIndex++                                                      //increment index

        if(currentFragmentIndex == 1)                                               //checking index
        {
            val onboarding2 = onboarding2Fragment()                                 //declare next onboarding screen
            val fragmentTransaction = supportFragmentManager.beginTransaction()     //make transaction to move to second fragment
            fragmentTransaction.replace(R.id.fragmentContainer, onboarding2)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        else if (currentFragmentIndex == 2)                                         //checking index
        {
            val onboarding3 = onboarding3Fragment()                                 //declare next onboarding screen
            val fragmentTransaction = supportFragmentManager.beginTransaction()     //make transaction to move to third fragment
            fragmentTransaction.replace(R.id.fragmentContainer, onboarding3)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
            btnSkip.visibility = View.GONE                                          //set so button skip are no longer visible and cannot be used
            btnSkip.isEnabled = false
            btnNext.setText("Skip")                                                 //set text in button next so user thought the button next become button skip
        }
        else                                                                        //indicating the button next already become a button skip. onboarding screen ended. user directed to main activity after pressing "button skip"
        {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}