package com.johnmarsel.diplom

import android.graphics.Bitmap

private const val JPG = ".jpg"

class Tour(val assetPath: String, var bitmap: Bitmap? = null) {

    val name = assetPath.split("/").last().removeSuffix(JPG)
}