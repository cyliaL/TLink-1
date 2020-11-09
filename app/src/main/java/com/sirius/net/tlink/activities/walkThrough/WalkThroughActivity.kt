package com.sirius.net.tlink.activities.walkThrough

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager2.widget.ViewPager2
import com.sirius.net.tlink.R
import com.sirius.net.tlink.activities.Login.LoginActivity
import com.sirius.net.tlink.activities.Main.MainActivity
import com.sirius.net.tlink.adapters.WalkThroughAdapter

class WalkThroughActivity : AppCompatActivity() {

    private lateinit var preferences:SharedPreferences
    private lateinit var skipButton:Button
    private lateinit var nextButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TLink)
        verifyStartUp()
        setContentView(R.layout.activity_walk_through)
        stylizeToolBar()

        skipButton = findViewById(R.id.skip_button)
        nextButton = findViewById(R.id.next_button)

        populateViewPager()
    }

    private fun verifyStartUp() {
        preferences=getSharedPreferences("TLINK", MODE_PRIVATE)
        if(!preferences.getBoolean("FIRST_START",true)){
            startActivity(Intent(this,LoginActivity::class.java)).also {
                finish()
            }
        }
    }

    private fun populateViewPager() {
        val walkThroughPager:ViewPager2=findViewById(R.id.walk_through_pager)
        walkThroughPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        walkThroughPager.adapter = WalkThroughAdapter(preferences)

       nextButton.setOnClickListener {
           when(walkThroughPager.currentItem){
               0,1,2 ->{
                   walkThroughPager.setCurrentItem(walkThroughPager.currentItem+1,true)
               }
               3 ->{
                   val intent = Intent(this, LoginActivity::class.java)
                   startActivity(intent).also {
                       finish()
                   }
                   preferences.edit().putBoolean("FIRST_START",false).apply()
               }
           }
        }
        skipButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent).also {
                finish()
            }
            preferences.edit().putBoolean("FIRST_START",false).apply()
        }

    }

    private fun stylizeToolBar() {
        val background = ResourcesCompat.getDrawable(resources,
            R.drawable.background, theme)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setBackgroundDrawable(background)
    }
}