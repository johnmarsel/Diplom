package com.johnmarsel.diplom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController

const val TOUR_POSITION = "tour_pos"

class TourFragment : Fragment() {

    private var pos = 0

    private lateinit var tourListViewModel: TourListViewModel
    private lateinit var imagesText: TextView
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pos = it.getInt(TOUR_POSITION)
        }
        tourListViewModel = ViewModelProvider(this).get(TourListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tour, container, false)
        imagesText = view.findViewById(R.id.image_text)
        imageView = view.findViewById(R.id.image_image)
        val image = tourListViewModel.imageBox.tours[pos]
        imagesText.text = image.name
        imageView.setImageBitmap(image.bitmap)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        val toolbar = view.findViewById(R.id.toolbar) as Toolbar
        toolbar.apply {
            setupWithNavController(navController, appBarConfiguration)
        }
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