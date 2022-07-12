package com.johnmarsel.diplom

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.johnmarsel.diplom.databinding.FragmentTourPhotoBinding
import com.johnmarsel.diplom.databinding.FragmentTourReviewBinding
import com.johnmarsel.diplom.databinding.ItemReviewBinding
import com.johnmarsel.diplom.databinding.ListPhotoItemTourBinding
import com.johnmarsel.diplom.model.Image

class TourReviewFragment: Fragment() {

    private lateinit var binding: FragmentTourReviewBinding
    private lateinit var mActivity : FragmentActivity


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as FragmentActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTourReviewBinding.inflate(inflater, container, false)
        val ratings = listOf(mutableMapOf
            ("name" to "Мария", "date" to "Апрель 2022", "text" to "Очень хороший компактный отель. Нам все понравилось, до берега дойти 5 мин., отель чистый. Персонал приятный. Отдыхали в апреле 2022 с друзьями." , "rating" to "5"),
            mutableMapOf("name" to "Жанна", "date" to "Май 2022", "text" to "Отличный отель, рекомендую. Очень чисто, уютно, уборка каждый день, постельное белье и полотенце меняют каждый день, отличный персонал, бар работает 24/7.", "rating" to "5"),
            mutableMapOf("name" to "Марина", "date" to "Май 2022", "text" to "Ездила в этот отель с 14 по 21 мая 2022. Все понравилось! Везде чисто. 1 большой бассейн с подогревом, 1 с горками, 1 мелкий для маленьких детей.", "rating" to "5"),
        )
        binding.recyclerRatings.layoutManager = LinearLayoutManager(context)
        binding.recyclerRatings.adapter = ReviewAdapter(ratings)
        binding.tourName.text = "Panorama Sea View Studios & Apartments"
        binding.tourCity.text = "Греция, Крит - Ираклион"
        binding.tourRatingBar.rating = 3.0F

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
    }

    override fun onStart() {
        super.onStart()
    }

    private fun setUpToolbar() {
        val mainActivity = mActivity as MainActivity
        val navigationView: NavigationView = mActivity.findViewById(R.id.nav_view)

        mainActivity.setSupportActionBar(binding.toolbar)
        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(mainActivity, navController)
        NavigationUI.setupWithNavController(navigationView, navController)
    }

    private inner class ReviewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(rating: MutableMap<String, String>) {
            binding.apply {
                ratingItemName.text = rating["name"]
                ratingItemDate.text = rating["date"]
                ratingItemText.text = rating["text"]
                ratingItemRating.rating = rating["rating"]?.toFloat()!!
            }
        }
    }

    private inner class ReviewAdapter(private val ratings: List<MutableMap<String, String>>)
        : RecyclerView.Adapter<ReviewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewHolder {
            val binding = ItemReviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return ReviewHolder(binding)
        }

        override fun onBindViewHolder(holder: ReviewHolder, position: Int) {
            val rating = ratings[position]
            holder.bind(rating)
        }

        override fun getItemCount() = ratings.size
    }

    companion object {
        fun newInstance(pos: Int) =
            TourFragment().apply {
                arguments = Bundle().apply {
                    putInt(TOUR_POSITION, pos)
                }
            }
    }
}
