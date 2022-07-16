package com.johnmarsel.diplom.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.johnmarsel.diplom.databinding.ItemReviewBinding
import com.johnmarsel.diplom.model.Review

/**
 * RecyclerView adapter for a list of [Review].
 */
open class ReviewAdapter(query: Query) : FirestoreAdapter<ReviewAdapter.ViewHolder>(query) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemReviewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position).toObject<Review>())
    }

    inner class ViewHolder(val binding: ItemReviewBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(review: Review?) {
            if (review == null) {
                return
            }

            binding.apply {
                ratingItemName.text = review.name
                ratingItemDate.text = review.date
                ratingItemText.text = review.text
                ratingItemRating.rating = review.rating.toFloat()
            }

        }

    }
}
