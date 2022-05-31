package com.johnmarsel.diplom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp

class MainActivity : AppCompatActivity(), TourListFragment.Callbacks {

    private lateinit var navController: NavController
    lateinit var appBarConfiguration : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Set up Action Bar
        val drawerLayout : DrawerLayout = findViewById(R.id.drawer_layout)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.tourListFragment),
            drawerLayout)

    }

    override fun onSupportNavigateUp(): Boolean {

        return navController.navigateUp(appBarConfiguration)
    }

    override fun onTourSelected(position: Int, title: String) {
        val args = Bundle().apply {
            putInt(TOUR_POSITION, position)
            putString("dynamicTitle", title)
        }
        navController.navigate(R.id.action_tourListFragment_to_tourFragment, args)
    }
}