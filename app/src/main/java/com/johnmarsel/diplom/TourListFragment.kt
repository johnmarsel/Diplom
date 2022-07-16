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
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.johnmarsel.diplom.adapter.TourAdapter
import com.johnmarsel.diplom.databinding.FragmentTourListBinding

class TourListFragment : Fragment(), TourAdapter.OnTourSelectedListener {

    private lateinit var adapter: TourAdapter
    private lateinit var query: Query
    private lateinit var mActivity: FragmentActivity
    private lateinit var binding: FragmentTourListBinding
    private lateinit var tourListViewModel: TourListViewModel


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as FragmentActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tourListViewModel = ViewModelProvider(this).get(TourListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View {
        binding = FragmentTourListBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get tours
        query = tourListViewModel.collectionRef

        adapter = object : TourAdapter(query, this@TourListFragment) {
            override fun onDataChanged() {
                // Show/hide content if the query returns empty.
                if (itemCount == 0) {
                    binding.recyclerView.visibility = View.GONE
                    binding.emptyView.visibility = View.VISIBLE
                } else {
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.emptyView.visibility = View.GONE
                }
            }
            override fun onError(e: FirebaseFirestoreException) {
                // Show a snackbar on errors
                Snackbar.make(binding.root,
                    "Error: check logs for info.", Snackbar.LENGTH_LONG).show()

            }
        }
        binding.recyclerView.adapter = adapter

        setUpToolbar()
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
        val navigationView: NavigationView = mainActivity.findViewById(R.id.nav_view)

        mainActivity.setSupportActionBar(binding.toolbar)
        val navController = NavHostFragment.findNavController(this)
        val appBarConfiguration = mainActivity.appBarConfiguration
        setupActionBarWithNavController(mainActivity,navController,appBarConfiguration)
        setupWithNavController(navigationView,navController)
    }

    override fun onTourSelected(tour: DocumentSnapshot) {
        val args = Bundle().apply {
            putString(TOUR_ID, tour.id)
        }
        findNavController().navigate(R.id.action_tourListFragment_to_tourFragment, args)
    }
}