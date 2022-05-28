package com.johnmarsel.diplom.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TourNew(
    @PrimaryKey val id: Int,
    val title: String,
    val location: String,
    val price: String,
    val description: String
)