package com.johnmarsel.diplom.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.johnmarsel.diplom.databinding.ListPhotoItemTourBinding
import com.johnmarsel.diplom.model.Photo

/**
 * RecyclerView adapter for a list of tours photos.
 */
open class PhotoAdapter(query: Query) : FirestoreAdapter<PhotoAdapter.ViewHolder>(query) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListPhotoItemTourBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position).toObject<Photo>())
    }

    inner class ViewHolder(val binding: ListPhotoItemTourBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo?) {

            if (photo == null) {
                return
            }

            tourRepository.loadImageFromUrl(
                binding.tourImagePhoto.context,
                photo.url,
                binding.tourImagePhoto
            )
        }

    }
}
