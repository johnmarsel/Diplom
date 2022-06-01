package com.johnmarsel.diplom

import androidx.lifecycle.ViewModel

class RequestViewModel: ViewModel() {

    private val tourRepository = TourRepository.get()

    fun <K, V> addRequestFirestore(map: MutableMap<K, V>, collectionPath: String) {
        tourRepository.addRequestFirestore(map, collectionPath)
    }
}