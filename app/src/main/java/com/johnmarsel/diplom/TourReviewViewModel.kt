package com.johnmarsel.diplom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.johnmarsel.diplom.model.Tour

class TourReviewViewModel: ViewModel() {

    private val tourRepository = TourRepository.get()

    private val tourIdLiveData = MutableLiveData<String>()

    var tourLiveData: LiveData<Tour> =
        Transformations.switchMap(tourIdLiveData) { tourId ->
            tourRepository.getTour(tourId)
        }

    fun loadTour(tourId: String) {
        tourIdLiveData.value = tourId
    }
}