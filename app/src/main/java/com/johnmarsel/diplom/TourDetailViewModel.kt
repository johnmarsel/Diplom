package com.johnmarsel.diplom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.johnmarsel.diplom.database.TourNew
import com.johnmarsel.diplom.model.TourBox

class TourDetailViewModel: ViewModel() {

    private val tourRepository = TourRepository.get()
    val imageBox = TourBox.get().images
    private val tourIdLiveData = MutableLiveData<Int>()

    var tourLiveData: LiveData<TourNew> =
        Transformations.switchMap(tourIdLiveData) { tourId ->
            tourRepository.getTour(tourId)
        }

    fun loadTour(tourId: Int) {
        tourIdLiveData.value = tourId
    }

}