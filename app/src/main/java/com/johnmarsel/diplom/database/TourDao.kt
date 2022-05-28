package com.johnmarsel.diplom.database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface TourDao {
    @Query("SELECT * FROM tournew")
    fun getTours(): LiveData<List<TourNew>>
    @Query("SELECT * FROM tournew WHERE id=(:id)")
    fun getTour(id: Int): LiveData<TourNew>
}