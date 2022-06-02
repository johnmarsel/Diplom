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
import com.johnmarsel.diplom.database.TourNew
import com.johnmarsel.diplom.databinding.FragmentTourBinding

const val TOUR_POSITION = "tour_pos"
const val COLLECTION_PATH = "requests"

class TourFragment : Fragment() {

    private var pos = 0
    private lateinit var binding: FragmentTourBinding
    private lateinit var tourDetailViewModel: TourDetailViewModel
    private lateinit var mActivity : FragmentActivity
    private lateinit var tour: TourNew

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as FragmentActivity
    }

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
        binding.button.setOnClickListener {
            val args = Bundle().apply {
                putInt(SEARCH_TOUR_ID, tour.id)
            }
            findNavController().navigate(R.id.action_tourFragment_to_mapsFragment, args)
        }
        binding.buttonFirestore.setOnClickListener {
            val args = Bundle().apply {
                putString(TOUR_TITLE, tour.title)
                putString("dynamicTitle", tour.title)
            }
            findNavController().navigate(R.id.action_tourFragment_to_requestFragment, args)
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

        val image = tourDetailViewModel.imageBox[pos]
        binding.apply {
            tourDescription.text = tour.description
            tourLocationArrival.text = tour.location
            tourLocationDeparture.text = "Из Москвы"
            tourDepartureTime.text = "5 июня"
            tourDuration.text = "6 ночей"
            tourFoodAbout.text = "AI - Все включено"
            toorRoomAbout.text = "garden view"
            roomAboutGuests.text = "2 взрослых"
            servicesAbout.text = "Медицинская страховка, трансфер"
            tourImage.setImageBitmap(image.bitmap)
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