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
import com.google.android.material.navigation.NavigationView
import com.johnmarsel.diplom.database.TourNew
import com.johnmarsel.diplom.databinding.FragmentMapsBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.map.*
import com.yandex.mapkit.search.*
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError

const val SEARCH_TOUR_ID = "search_tour_id"

class MapsFragment : Fragment(), Session.SearchListener {

    private lateinit var binding: FragmentMapsBinding
    private lateinit var mActivity : FragmentActivity
    private lateinit var searchManager: SearchManager
    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var tour: TourNew

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as FragmentActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mapsViewModel = ViewModelProvider(this).get(MapsViewModel::class.java)

        MapKitFactory.initialize(context)
        SearchFactory.initialize(context)
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)

        val tourId = arguments?.getInt(SEARCH_TOUR_ID) as Int
        mapsViewModel.loadTour(tourId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        mapsViewModel.tourLiveData.observe(
            viewLifecycleOwner
        ) { tour ->
            tour?.let {
                this.tour = tour
                submitQuery(tour.title)
                updateUI()
            }
        }
    }

    private fun updateUI() {
        binding.apply {
            tourRatingBar.rating = tour.rating.toFloat()
            tourTitle.text = tour.title
            tourLocation.text = tour.location
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapview.onStart()
    }

    private fun setUpToolbar() {
        val mainActivity = mActivity as MainActivity
        val navigationView: NavigationView = mActivity.findViewById(R.id.nav_view)

        mainActivity.setSupportActionBar(binding.toolbar)
        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(mainActivity, navController)
        NavigationUI.setupWithNavController(navigationView, navController)
    }

    override fun onStop() {
        binding.mapview.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    private fun submitQuery(query: String) {
        searchManager.submit(
            query,
            VisibleRegionUtils.toPolygon(binding.mapview.map.visibleRegion),
            SearchOptions(),
            this
        )
    }

    override fun onSearchResponse(responce: Response) {
        val mapObjects: MapObjectCollection = binding.mapview.map.mapObjects
        mapObjects.clear()

        val targetLocation = responce.collection.children[0].obj?.geometry?.get(0)?.point
        targetLocation?.let {
            mapObjects.addPlacemark(
                it,
                ImageProvider.fromResource(context, R.drawable.search_result)
            )
            binding.mapview.map.move(
                CameraPosition(it, 17.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 3F),
                null
            )
        }
    }

    override fun onSearchError(error: Error) {
        var errorMessage = getString(R.string.unknown_error_message)
        if (error is RemoteError) {
            errorMessage = getString(R.string.remote_error_message)
        } else if (error is NetworkError) {
            errorMessage = getString(R.string.network_error_message)
        }

        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }
}