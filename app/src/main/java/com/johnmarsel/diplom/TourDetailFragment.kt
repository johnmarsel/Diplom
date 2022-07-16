package com.johnmarsel.diplom

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.johnmarsel.diplom.databinding.FragmentTourBinding
import com.johnmarsel.diplom.model.Tour

const val TOUR_ID = "tour_id"

class TourFragment : Fragment() {

    private lateinit var binding: FragmentTourBinding
    private lateinit var tourDetailViewModel: TourDetailViewModel
    private lateinit var mActivity : FragmentActivity
    private lateinit var tour: Tour
    private lateinit var tourId: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as FragmentActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tourDetailViewModel = ViewModelProvider(this).get(TourDetailViewModel::class.java)
        arguments?.let {
            tourId = it.getString(TOUR_ID).toString()
        }
        tourDetailViewModel.loadTour(tourId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTourBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        tourDetailViewModel.tourLiveData.observe(
            viewLifecycleOwner
        ) { tour ->
            tour?.let {
                this.tour = tour
                updateUI()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val navController = findNavController()

        binding.button.setOnClickListener {
            val args = Bundle().apply {
                putString(SEARCH_TOUR_ID, tourId)
            }
            navController.navigate(R.id.action_tourFragment_to_mapsFragment, args)
        }
        binding.buttonFirestore.setOnClickListener {
            val args = Bundle().apply {
                putString(TOUR_TITLE, tour.title)
            }
            navController.navigate(R.id.action_tourFragment_to_requestFragment, args)
        }
        binding.button2.setOnClickListener {
            val args = Bundle().apply {
                putString("dynamicTitle", tour.title)
                putString(TOUR_ID, tourId)
            }
            navController.navigate(R.id.action_tourFragment_to_tourPhotoFragment, args)
        }
        binding.button3.setOnClickListener {
            val args = Bundle().apply {
                putString(TOUR_ID, tourId)
            }
            navController.navigate(R.id.action_tourFragment_to_tourReviewFragment, args)
        }
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
        tourDetailViewModel.loadImageFromUrl(binding.tourImage.context, tour.photo, binding.tourImage)
        binding.apply {
            tourRatingBar.rating = tour.rating.toFloat()
            tourName.text = tour.title
            tourCity.text = tour.location.substringAfter(",").trim()
            tourDescription.text = tour.description
            tourLocationArrival.text = tour.location
            tourLocationDeparture.text = tour.departureCity
            tourDepartureTime.text = tour.departureDate
            tourDuration.text = tour.duration
            tourFoodAbout.text = tour.foodAbout
            toorRoomAbout.text = tour.roomAbout
            roomAboutGuests.text = tour.aboutGuests
            servicesAbout.text = tour.services
            tourCost.text = binding.root.context.getString(R.string.tour_price, tour.price)
        }
    }
}