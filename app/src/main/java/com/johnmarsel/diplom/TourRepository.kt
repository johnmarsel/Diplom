package com.johnmarsel.diplom

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.johnmarsel.diplom.database.TourDatabase
import com.johnmarsel.diplom.database.TourNew
import java.util.*

private const val DATABASE_NAME = "tour-database"

class TourRepository private constructor(context: Context) {

    private val database : TourDatabase = Room.databaseBuilder(
        context.applicationContext,
        TourDatabase::class.java,
        DATABASE_NAME
    ).createFromAsset("database/temp.db")
        .build()

    private val tourDao = database.tourDao()

    fun getTours(): LiveData<List<TourNew>> = tourDao.getTours()
    fun getTour(id: Int): LiveData<TourNew> = tourDao.getTour(id)

    companion object {
        private var INSTANCE: TourRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = TourRepository(context)
            }
        }

        fun get(): TourRepository {
            return INSTANCE ?:
            throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}