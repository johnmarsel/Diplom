package com.johnmarsel.diplom

import android.app.Application
import com.johnmarsel.diplom.model.TourBox

class TourApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        TourRepository.initialize(this)
        TourBox.initialize(assets)
    }
}