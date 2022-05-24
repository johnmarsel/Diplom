package com.johnmarsel.diplom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity(), TourListFragment.Callbacks {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onTourSelected(position: Int) {
        val args = Bundle().apply {
            putInt(TOUR_POSITION, position)
        }
        navController.navigate(R.id.action_tourListFragment_to_tourFragment, args)
    }
}