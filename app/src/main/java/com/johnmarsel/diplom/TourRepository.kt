package com.johnmarsel.diplom

import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.johnmarsel.diplom.model.Tour

private const val TAG = "FirestoreRepository"

class TourRepository private constructor(private val context: Context) {

    // Firestore part
    private val mFireStore = Firebase.firestore
    private val storage = Firebase.storage

    fun <K, V> addRequestFirestore(map: MutableMap<K, V>, collectionPath: String) {
        mFireStore.collection(collectionPath)
            .add(map)
            .addOnSuccessListener   {
                Toast.makeText(context, "Заявка успешно оставлена!", Toast.LENGTH_LONG).show()
            }.addOnFailureListener  {
                Toast.makeText(context, "Произошла ошибка $it", Toast.LENGTH_LONG).show()
            }
    }

    private fun getProgressDrawable(context: Context) : CircularProgressDrawable {
        return CircularProgressDrawable(context).apply {
            strokeWidth = 10f
            centerRadius =50f
            start()
        }
    }

    fun loadImageFromUrl(context: Context, url: String, view: ImageView)  {
        val options = RequestOptions()
            .placeholder(getProgressDrawable(context))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
        val gsReference = storage.getReferenceFromUrl(url)

        Glide.with(context)
            .setDefaultRequestOptions(options)
            .load(gsReference)
            .into(view)
    }

    fun getTour(tourId: String): LiveData<Tour> {
        val responseLiveData: MutableLiveData<Tour> = MutableLiveData()
        val tourRef = mFireStore.collection("tours").document(tourId)
        tourRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    val tour = document.toObject<Tour>() ?: Tour()
                    responseLiveData.value = tour
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

        return responseLiveData
    }

    fun getCollectionRef(collectionPath: String): CollectionReference {
        return mFireStore.collection(collectionPath)
    }

    companion object {
        private var INSTANCE: TourRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = TourRepository(context)
            }
        }

        fun get(): TourRepository {
            return INSTANCE ?:
            throw IllegalStateException("TourRepository must be initialized")
        }
    }
}