package com.johnmarsel.diplom

import android.content.Context
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.johnmarsel.diplom.model.Tour

class TourDetailViewModel: ViewModel() {

    private val tourRepository = TourRepository.get()

    private val tourIdLiveData = MutableLiveData<String>()

    var tourLiveData: LiveData<Tour> =
        Transformations.switchMap(tourIdLiveData) { tourId ->
            tourRepository.getTour(tourId)
        }

    fun loadTour(tourId: String) {
        tourIdLiveData.value = tourId
    }

    fun loadImageFromUrl(context: Context, url: String, view: ImageView)  {
        tourRepository.loadImageFromUrl(context, url, view)
    }

}