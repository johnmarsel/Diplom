package com.johnmarsel.diplom

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class TourApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        TourRepository.initialize(this)
        MapKitFactory.setApiKey("83c02378-2b49-44a8-be37-d4d1d3a82cc4")
    }
}