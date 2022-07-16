package com.johnmarsel.diplom

import androidx.lifecycle.ViewModel

class TourListViewModel : ViewModel() {

    private val tourRepository = TourRepository.get()

    val collectionRef = tourRepository.getCollectionRef("tours")
}