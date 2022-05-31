package com.johnmarsel.diplom

import android.app.Application
import com.johnmarsel.diplom.model.TourBox
import com.yandex.mapkit.MapKitFactory

class TourApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        TourRepository.initialize(this)
        TourBox.initialize(assets)
        MapKitFactory.setApiKey("83c02378-2b49-44a8-be37-d4d1d3a82cc4")
    }
}