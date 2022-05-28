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
import com.johnmarsel.diplom.database.TourNew

const val TOUR_POSITION = "tour_pos"

class TourFragment : Fragment() {

    private var pos = 0

    private lateinit var tourDetailViewModel: TourDetailViewModel
    private lateinit var tourDescription: TextView
    private lateinit var tourImage: ImageView
    private lateinit var tour: TourNew

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tourDetailViewModel = ViewModelProvider(this).get(TourDetailViewModel::class.java)
        arguments?.let {
            pos = it.getInt(TOUR_POSITION)
        }
        tourDetailViewModel.loadTour(pos+1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tour, container, false)
        tourImage = view.findViewById(R.id.tour_image)
        tourDescription = view.findViewById(R.id.tour_description)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tourDetailViewModel.tourLiveData.observe(
            viewLifecycleOwner
        ) { tour ->
            tour?.let {
                this.tour = tour
                updateUI()
            }
        }
    }

    private fun updateUI()  {
        val image = tourDetailViewModel.imageBox[pos]
        tourDescription.text = tour.description
        tourImage.setImageBitmap(image.bitmap)
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