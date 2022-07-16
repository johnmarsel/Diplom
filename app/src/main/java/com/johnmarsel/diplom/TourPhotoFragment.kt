package com.johnmarsel.diplom

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.johnmarsel.diplom.adapter.PhotoAdapter
import com.johnmarsel.diplom.databinding.FragmentTourPhotoBinding

class TourPhotoFragment : Fragment() {

    private lateinit var adapter: PhotoAdapter
    private lateinit var binding: FragmentTourPhotoBinding
    private lateinit var mActivity : FragmentActivity
    private lateinit var tourId: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as FragmentActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            tourId = it.getString(TOUR_ID).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTourPhotoBinding.inflate(inflater, container, false)
        binding.recyclerViewPhoto.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()

        val store = Firebase.firestore
        val query = store.collection("tours").document(tourId).collection("photos")

        // RecyclerView
        adapter = object : PhotoAdapter(query) {
            override fun onDataChanged() {
                // Show/hide content if the query returns empty.
                if (itemCount == 0) {
                    binding.recyclerViewPhoto.visibility = View.GONE
                    binding.emptyView.visibility = View.VISIBLE
                } else {
                    binding.recyclerViewPhoto.visibility = View.VISIBLE
                    binding.emptyView.visibility = View.GONE
                }
            }

            override fun onError(e: FirebaseFirestoreException) {
                // Show a snackbar on errors
                Snackbar.make(binding.root,
                    "Error: check logs for info.", Snackbar.LENGTH_LONG).show()


            }
        }
        binding.recyclerViewPhoto.adapter = adapter
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
        setupActionBarWithNavController(mainActivity, navController)
        setupWithNavController(navigationView, navController)
    }

}