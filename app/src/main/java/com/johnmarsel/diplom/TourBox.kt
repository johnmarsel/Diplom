package com.johnmarsel.diplom

import android.content.res.AssetManager
import android.graphics.BitmapFactory
import android.util.Log
import java.io.IOException
import java.io.InputStream

private const val TAG = "TourBox"
private const val IMAGES_FOLDER = "sample_images"

class TourBox(private val assets: AssetManager) {

    val tours: List<Tour>

    init {
        tours = loadTours()
    }

    private fun loadTours(): List<Tour> {

        val imageNames: Array<String>

        try {
            imageNames = assets.list(IMAGES_FOLDER)!!
        } catch (e: Exception) {
            Log.e(TAG, "Could not list assets", e)
            return emptyList()
        }
        val tours = mutableListOf<Tour>()
        imageNames.forEach { filename ->
            val assetPath = "$IMAGES_FOLDER/$filename"
            val tour = Tour(assetPath)
            try {
                load(tour)
                tours.add(tour)
            } catch (ioe: IOException) {
                Log.e(TAG, "Cound not load image $filename", ioe)
            }
        }
        return tours
    }

    private fun load(tour: Tour) {
        val ais: InputStream = assets.open(tour.assetPath)
        val bitmap = BitmapFactory.decodeStream(ais)
        tour.bitmap = bitmap
        ais.close()
    }
}