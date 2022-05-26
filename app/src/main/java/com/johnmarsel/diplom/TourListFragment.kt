package com.johnmarsel.diplom

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

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
        toursRecyclerView.adapter = ImagesAdapter(tourListViewModel.imageBox.tours)
        toursRecyclerView.layoutManager = GridLayoutManager(context, 2)
        return view
    }

    private inner class ImagesHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        private lateinit var tour: Tour
        private var pos = 0

        private val imageName: TextView = itemView.findViewById(R.id.info_text)
        private val infoImage: ImageView = itemView.findViewById(R.id.info_image)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(tour: Tour, position: Int) {
            this.tour = tour
            this.pos = position
            imageName.text = tour.name

            infoImage.setImageBitmap(tour.bitmap)
        }

        override fun onClick(view: View) {
            callbacks?.onTourSelected(pos)
        }

    }

    private inner class ImagesAdapter(private val tours: List<Tour>) :
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