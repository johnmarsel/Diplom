package com.johnmarsel.diplom

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class TourListViewModel(app: Application): AndroidViewModel(app) {
    val imageBox = TourBox(app.assets)
}