package com.johnmarsel.diplom

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.johnmarsel.diplom.database.TourDatabase
import com.johnmarsel.diplom.database.TourNew

private const val DATABASE_NAME = "tour-database"

class TourRepository private constructor(private val context: Context) {

    // Firestore part
    private val mFireStore = FirebaseFirestore.getInstance()

    fun <K, V> addRequestFirestore(map: MutableMap<K, V>, collectionPath: String) {
        mFireStore.collection(collectionPath)
            .add(map)
            .addOnSuccessListener   {
                Toast.makeText(context, "Заявка успешно оставлена!", Toast.LENGTH_LONG).show()
            }.addOnFailureListener  {
                Toast.makeText(context, "Произошла ошибка $it", Toast.LENGTH_LONG).show()
            }
    }

    // Room database part
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