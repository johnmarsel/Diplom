package com.johnmarsel.diplom.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ TourNew::class ], version=1)
abstract class TourDatabase : RoomDatabase() {
    abstract fun tourDao(): TourDao

}