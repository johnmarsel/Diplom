package com.johnmarsel.diplom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.johnmarsel.diplom.database.TourNew

class MapsViewModel: ViewModel() {

    private val tourRepository = TourRepository.get()
    private val tourIdLiveData = MutableLiveData<Int>()

    var tourLiveData: LiveData<TourNew> =
        Transformations.switchMap(tourIdLiveData) { tourId ->
            tourRepository.getTour(tourId)
        }

    fun loadTour(tourId: Int) {
        tourIdLiveData.value = tourId
    }
}