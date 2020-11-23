package com.sirius.net.tlink.activities.Main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import com.shreyaspatil.material.navigationview.MaterialNavigationView
import com.sirius.net.tlink.R
import com.sirius.net.tlink.activities.Login.LoginActivity
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var sharedPrefs:SharedPreferences
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TLink)
        setContentView(R.layout.activity_main)
        sharedPrefs = getSharedPreferences("TLINK", MODE_PRIVATE)
        stylizeToolBar()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: MaterialNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_order, R.id.nav_profile
        ), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        initHeader(navView.getHeaderView(0))
    }

    private fun initHeader(header: View){
        val fullName : TextView = header.findViewById(R.id.full_name)
        val phoneNumber: TextView = header.findViewById(R.id.header_phone)
        val disconnectButton: Button = header.findViewById(R.id.disconnect_button)
        val username = sharedPrefs.getString("name","...")
        val userSurName = sharedPrefs.getString("surname","...")
        val userPhone = sharedPrefs.getString("tel1", "...")

        fullName.text =  "$username  $userSurName"
        phoneNumber.text = userPhone
        disconnectButton.setOnClickListener {
            sharedPrefs.edit().putBoolean("IS_USER_LOGED",false).apply()
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent).also {
                finish()
            }
        }
    }

    private fun stylizeToolBar() {
        val background = ResourcesCompat.getDrawable(resources,
                R.drawable.background, theme)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setBackgroundDrawable(background)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}