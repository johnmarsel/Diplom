package com.johnmarsel.diplom

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.johnmarsel.diplom.database.TourNew
import com.johnmarsel.diplom.model.TourBox

class TourListViewModel : ViewModel() {

    private val tourRepository = TourRepository.get()

    var tourListLiveData: LiveData<List<TourNew>> = tourRepository.getTours()
    val imageBox = TourBox.get().images

    fun <K, V> addRequestFirestore(map: MutableMap<K, V>, collectionPath: String) {
        tourRepository.addRequestFirestore(map, collectionPath)
    }

}