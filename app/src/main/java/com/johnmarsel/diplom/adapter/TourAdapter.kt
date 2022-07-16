package com.johnmarsel.diplom.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.johnmarsel.diplom.R
import com.johnmarsel.diplom.databinding.ListItemTourBinding
import com.johnmarsel.diplom.model.Tour

/**
 * RecyclerView adapter for a list of Tours.
 */
open class TourAdapter(query: Query, private val listener: OnTourSelectedListener) :
        FirestoreAdapter<TourAdapter.ViewHolder>(query) {

    interface OnTourSelectedListener {

        fun onTourSelected(tour: DocumentSnapshot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemTourBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position), listener)
    }

    inner class ViewHolder(val binding: ListItemTourBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(snapshot: DocumentSnapshot, listener: OnTourSelectedListener?) {

            val tour = snapshot.toObject<Tour>() ?: return

            tourRepository.loadImageFromUrl(
                binding.tourImage.context,
                tour.photo,
                binding.tourImage
            )
            binding.tourTitle.text = tour.title
            binding.tourRatingBar.rating = tour.rating.toFloat()
            binding.tourLocation.text = tour.location
            binding.tourPrice.text = binding.root.context.getString(R.string.tour_price, tour.price)

            // Click listener
            binding.root.setOnClickListener {
                listener?.onTourSelected(snapshot)
            }
        }
    }
}
