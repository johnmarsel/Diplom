package com.johnmarsel.diplom.model

import android.content.res.AssetManager
import android.graphics.BitmapFactory
import android.util.Log
import java.io.IOException
import java.io.InputStream

private const val TAG = "TourBox"
private const val IMAGES_FOLDER = "sample_images"

class TourBox private constructor(private val assets: AssetManager) {

    val images: List<Image>

    init {
        images = loadImages()
    }

    private fun loadImages(): List<Image> {

        val imageNames: Array<String>

        try {
            imageNames = assets.list(IMAGES_FOLDER)!!
        } catch (e: Exception) {
            Log.e(TAG, "Could not list assets", e)
            return emptyList()
        }
        val images = mutableListOf<Image>()
        imageNames.forEach { filename ->
            val assetPath = "$IMAGES_FOLDER/$filename"
            val image = Image(assetPath)
            try {
                load(image)
                images.add(image)
            } catch (ioe: IOException) {
                Log.e(TAG, "Cound not load image $filename", ioe)
            }
        }
        return images
    }

    private fun load(image: Image) {
        val ais: InputStream = assets.open(image.assetPath)
        val bitmap = BitmapFactory.decodeStream(ais)
        image.bitmap = bitmap
        ais.close()
    }

    companion object {
        private var INSTANCE: TourBox? = null

        fun initialize(assets: AssetManager) {
            if (INSTANCE == null) {
                INSTANCE = TourBox(assets)
            }
        }

        fun get(): TourBox {
            return INSTANCE ?:
            throw IllegalStateException("ImageBox must be initialized")
        }
    }
}