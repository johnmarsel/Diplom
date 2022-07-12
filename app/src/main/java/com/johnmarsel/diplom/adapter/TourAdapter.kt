package com.johnmarsel.diplom.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.johnmarsel.diplom.database.TourNew
import com.johnmarsel.diplom.database.TourNew2
import com.johnmarsel.diplom.databinding.ListItemTourBinding

/**
 * RecyclerView adapter for a list of Restaurants.
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

    class ViewHolder(val binding: ListItemTourBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(snapshot: DocumentSnapshot, listener: OnTourSelectedListener?) {

            val tour = snapshot.toObject<TourNew2>()
            if (tour == null) {
                return
            }

            val resources = binding.root.resources

            val storage = Firebase.storage
            val gsReference = storage.getReferenceFromUrl(tour.photo)

            // Load image
            Glide.with(binding.tourImage.context)
                    .load(gsReference)
                    .into(binding.tourImage)


            binding.tourTitle.text = tour.title
            binding.tourRatingBar.rating = tour.rating.toFloat()
            binding.tourLocationImage.text = tour.location
            binding.tourPrice.text = "${tour.price} ₽"

            // Click listener
            binding.root.setOnClickListener {
                listener?.onTourSelected(snapshot)
            }
        }
    }
}
