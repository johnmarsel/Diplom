package com.johnmarsel.diplom.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TourNew(
    @PrimaryKey val id: Int = 0,
    val title: String = "",
    val location: String = "",
    val price: String = "",
    val description: String = "",
    val photo: String = "",
    val rating: String = ""
)

data class TourNew2(
    val title: String = "",
    val location: String = "",
    val price: String = "",
    val description: String = "",
    val photo: String = "",
    val rating: String = ""
)