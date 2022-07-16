package com.johnmarsel.diplom

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.johnmarsel.diplom.adapter.ReviewAdapter
import com.johnmarsel.diplom.databinding.FragmentTourReviewBinding
import com.johnmarsel.diplom.model.Tour

class TourReviewFragment: Fragment() {


    private lateinit var adapter: ReviewAdapter
    private lateinit var binding: FragmentTourReviewBinding
    private lateinit var mActivity : FragmentActivity
    private lateinit var tourReviewViewModel: TourReviewViewModel
    private lateinit var tourId: String
    private lateinit var tour: Tour


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as FragmentActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tourReviewViewModel = ViewModelProvider(this).get(TourReviewViewModel::class.java)
        arguments?.let {
            tourId = it.getString(TOUR_ID).toString()
        }
        tourReviewViewModel.loadTour(tourId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTourReviewBinding.inflate(inflater, container, false)
        binding.recyclerRatings.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        tourReviewViewModel.tourLiveData.observe(
            viewLifecycleOwner
        ) { tour ->
            tour?.let {
                this.tour = tour
                updateUI()
            }
        }

        val store = Firebase.firestore
        val query = store.collection("tours").document(tourId).collection("reviews")

        // RecyclerView
        adapter = object : ReviewAdapter(query) {
            override fun onDataChanged() {
                // Show/hide content if the query returns empty.
                if (itemCount == 0) {
                    binding.recyclerRatings.visibility = View.GONE
                    binding.emptyView.visibility = View.VISIBLE
                } else {
                    binding.recyclerRatings.visibility = View.VISIBLE
                    binding.emptyView.visibility = View.GONE
                }
            }

            override fun onError(e: FirebaseFirestoreException) {
                // Show a snackbar on errors
                Snackbar.make(binding.root,
                    "Error: check logs for info.", Snackbar.LENGTH_LONG).show()

                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
            }
        }
        binding.recyclerRatings.adapter = adapter

    }

    private fun updateUI() {
        binding.apply {
            tourName.text = tour.title
            tourCity.text = tour.location
            tourRatingBar.rating = tour.rating.toFloat()
        }
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    private fun setUpToolbar() {
        val mainActivity = mActivity as MainActivity
        val navigationView: NavigationView = mActivity.findViewById(R.id.nav_view)

        mainActivity.setSupportActionBar(binding.toolbar)
        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(mainActivity, navController)
        NavigationUI.setupWithNavController(navigationView, navController)
    }
}
