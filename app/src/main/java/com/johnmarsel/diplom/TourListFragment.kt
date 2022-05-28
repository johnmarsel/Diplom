package com.johnmarsel.diplom

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.johnmarsel.diplom.database.TourNew
import com.johnmarsel.diplom.model.TourBox

class TourListFragment : Fragment() {

    interface Callbacks {
        fun onTourSelected(position: Int)
    }

    private var callbacks: Callbacks? = null
    private lateinit var tourListViewModel: TourListViewModel
    private lateinit var toursRecyclerView: RecyclerView

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
        toursRecyclerView.layoutManager = GridLayoutManager(context, 1)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tourListViewModel.tourListLiveData.observe(
            viewLifecycleOwner
        ) { tours ->
            tours?.let {
                toursRecyclerView.adapter = ImagesAdapter(tours)
            }
        }
    }

    private inner class ImagesHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        private lateinit var tour: TourNew
        private var pos = 0

        private val tourImage: ImageView = itemView.findViewById(R.id.tour_image)
        private val tourTitle: TextView = itemView.findViewById(R.id.tour_title)
        private val tourLocation: TextView = itemView.findViewById(R.id.tour_location)
        private val tourPrice: TextView = itemView.findViewById(R.id.tour_price)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(tour: TourNew, position: Int) {
            this.tour = tour
            this.pos = position
            tourImage.setImageBitmap(tourListViewModel.imageBox[position].bitmap)
            tourTitle.text = tour.title
            tourLocation.text = tour.location
            tourPrice.text = "${tour.price} ₽"
        }

        override fun onClick(view: View) {
            callbacks?.onTourSelected(pos)
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

    companion object {
        fun newInstance(): TourListFragment {
            return TourListFragment()
        }
    }
}