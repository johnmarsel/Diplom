package com.johnmarsel.diplom

import android.content.Context
import android.content.res.AssetManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.johnmarsel.diplom.database.TourNew
import com.johnmarsel.diplom.databinding.FragmentTourPhotoBinding
import com.johnmarsel.diplom.databinding.ListPhotoItemTourBinding
import com.johnmarsel.diplom.model.Image
import java.io.IOException
import java.io.InputStream

private const val IMAGES_FOLDER = "photos"
private const val TAG = "TourPhotoFragment"

class TourPhotoFragment : Fragment() {

    private lateinit var binding: FragmentTourPhotoBinding
    private lateinit var mActivity : FragmentActivity
    private lateinit var photoList: List<Image>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as FragmentActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        photoList = context?.let { loadImages(it.assets) }!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTourPhotoBinding.inflate(inflater, container, false)

        binding.recyclerViewPhoto.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewPhoto.adapter = PhotoAdapter(photoList)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
    }

    override fun onStart() {
        super.onStart()
    }

    private fun setUpToolbar() {
        val mainActivity = mActivity as MainActivity
        val navigationView: NavigationView = mActivity.findViewById(R.id.nav_view)

        mainActivity.setSupportActionBar(binding.toolbar)
        val navController = NavHostFragment.findNavController(this)
        setupActionBarWithNavController(mainActivity, navController)
        setupWithNavController(navigationView, navController)
    }

    private fun updateUI()  {

    }

    private fun loadImages(assets: AssetManager): List<Image> {

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
                load(image, assets)
                images.add(image)
            } catch (ioe: IOException) {
                Log.e(TAG, "Cound not load image $filename", ioe)
            }
        }
        return images
    }

    private fun load(image: Image, assets: AssetManager) {
        val ais: InputStream = assets.open(image.assetPath)
        val bitmap = BitmapFactory.decodeStream(ais)
        image.bitmap = bitmap
        ais.close()
    }

    private inner class PhotoHolder(private val binding: ListPhotoItemTourBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Image) {
            binding.apply {
                tourImagePhoto.setImageBitmap(photo.bitmap)
            }
        }
    }

    private inner class PhotoAdapter(private val photos: List<Image>)
        : RecyclerView.Adapter<PhotoHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
            val binding = ListPhotoItemTourBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return PhotoHolder(binding)
        }

        override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
            val sound = photos[position]
            holder.bind(sound)
        }

        override fun getItemCount() = photos.size
    }

    companion object {
        fun newInstance(pos: Int) =
            TourFragment().apply {
                arguments = Bundle().apply {
                    putInt(TOUR_POSITION, pos)
                }
            }
    }
}