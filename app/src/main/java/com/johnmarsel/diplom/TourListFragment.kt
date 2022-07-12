package com.johnmarsel.diplom

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.johnmarsel.diplom.adapter.TourAdapter
import com.johnmarsel.diplom.database.TourNew

const val TOUR_PATH = "tours"

class TourListFragment : Fragment(), TourAdapter.OnTourSelectedListener {

    interface Callbacks {
        fun onTourSelected(position: Int, title: String)
    }

    lateinit var firestore: FirebaseFirestore
    lateinit var query: Query
    lateinit var adapter: TourAdapter

    private var callbacks: Callbacks? = null
    private lateinit var tourListViewModel: TourListViewModel
    private lateinit var toursRecyclerView: RecyclerView
    private lateinit var toolbar: Toolbar

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tourListViewModel = ViewModelProvider(this).get(TourListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tour_list, container, false)
        toursRecyclerView = view.findViewById(R.id.recycler_view)
        toursRecyclerView.layoutManager = GridLayoutManager(context, 2)
        toolbar = view.findViewById(R.id.toolbar)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*

        tourListViewModel.tourListLiveData.observe(
            viewLifecycleOwner
        ) { tours ->
            tours?.let {
                for (i in tours) {
                    val userMap = mutableMapOf<String, String>()
                    userMap["title"] = i.title
                    userMap["location"] = i.location
                    userMap["price"] = i.price
                    userMap["description"] = i.description
                    userMap["rating"] = i.rating
                    tourListViewModel.addRequestFirestore(userMap, TOUR_PATH)
                }
                // toursRecyclerView.adapter = ImagesAdapter(tours)
            }
        }

         */
        firestore = Firebase.firestore
        // Get restaurants
        query = firestore.collection("tours")

        // RecyclerView
        adapter = object : TourAdapter(query, this@TourListFragment) {
            override fun onDataChanged() {
                /*
                // Show/hide content if the query returns empty.
                if (itemCount == 0) {
                    binding.recyclerRestaurants.visibility = View.GONE
                    binding.viewEmpty.visibility = View.VISIBLE
                } else {
                    binding.recyclerRestaurants.visibility = View.VISIBLE
                    binding.viewEmpty.visibility = View.GONE
                }

                 */
            }

            override fun onError(e: FirebaseFirestoreException) {
                // Show a snackbar on errors
                /*
                Snackbar.make(binding.root,
                    "Error: check logs for info.", Snackbar.LENGTH_LONG).show()

                 */
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
            }
        }
        toursRecyclerView.adapter = adapter

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
        val mainActivity = callbacks as MainActivity
        val navigationView: NavigationView = mainActivity.findViewById(R.id.nav_view)

        mainActivity.setSupportActionBar(toolbar)
        val navController = NavHostFragment.findNavController(this)
        val appBarConfiguration = mainActivity.appBarConfiguration
        setupActionBarWithNavController(mainActivity,navController,appBarConfiguration)
        setupWithNavController(navigationView,navController)
    }

    /*

    private inner class ImagesHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        private lateinit var tour: TourNew
        private var pos = 0

        private val tourImage: ImageView = itemView.findViewById(R.id.tour_image)
        private val tourTitle: TextView = itemView.findViewById(R.id.tour_title)
        private val tourLocation: TextView = itemView.findViewById(R.id.tour_location_image)
        private val tourPrice: TextView = itemView.findViewById(R.id.tour_price)
        private val toorRating: RatingBar = itemView.findViewById(R.id.tourRatingBar)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(tour: TourNew, position: Int) {
            this.tour = tour
            this.pos = position
            tourImage.setImageBitmap(tourListViewModel.imageBox[position].bitmap)
            tourTitle.text = tour.title
            val rating = tour.rating
            toorRating.rating = rating.toFloat()

            tourLocation.text = tour.location
            tourPrice.text = "${tour.price} ₽"
        }

        override fun onClick(view: View) {
            callbacks?.onTourSelected(pos, tour.title)
        }

    }

    private inner class ImagesAdapter(private val tours: List<TourNew>) :
        RecyclerView.Adapter<ImagesHolder>() {

        override fun onCreateViewHolder(container: ViewGroup, viewType: Int): ImagesHolder {
            val layoutInflater = LayoutInflater.from(container.context)
            val view = layoutInflater
                .inflate(R.layout.list_item_tour, container, false) as CardView
            return ImagesHolder(view)
        }

        override fun onBindViewHolder(holder: ImagesHolder, position: Int) {
            val image = tours[position]
            holder.bind(image, position)
        }

        override fun getItemCount(): Int { return tours.size }
    }

     */

    companion object {
        fun newInstance(): TourListFragment {
            return TourListFragment()
        }
    }

    override fun onTourSelected(tour: DocumentSnapshot) {
        Toast.makeText(context, "Test", Toast.LENGTH_LONG).show()
    }
}