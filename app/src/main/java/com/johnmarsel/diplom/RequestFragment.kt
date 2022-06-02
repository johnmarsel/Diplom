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
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.johnmarsel.diplom.databinding.FragmentRequestBinding

const val TOUR_TITLE = "tour_title"

class RequestFragment : Fragment() {

    private lateinit var binding: FragmentRequestBinding
    private lateinit var param1: String
    private lateinit var requestViewModel: RequestViewModel
    private lateinit var mActivity : FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as FragmentActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestViewModel = ViewModelProvider(this).get(RequestViewModel::class.java)

        arguments?.let {
            param1 = it.getString(TOUR_TITLE).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRequestBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
    }

    override fun onStart() {
        super.onStart()
        binding.sendRequestButton.setOnClickListener {
            val tourName = param1
            val userMap = mutableMapOf<String, String>()
            userMap["Тур"] = tourName
            userMap["Имя"] = binding.clientName.text.toString()
            userMap["Телефон"] = binding.clientPhone.text.toString()
            userMap["E-Mail"] = binding.clientEmail.text.toString()
            requestViewModel.addRequestFirestore(userMap, COLLECTION_PATH)
            }
    }

    private fun setUpToolbar() {
        val mainActivity = mActivity as MainActivity
        val navigationView: NavigationView = mActivity.findViewById(R.id.nav_view)

        mainActivity.setSupportActionBar(binding.toolbar)
        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(mainActivity, navController)
        NavigationUI.setupWithNavController(navigationView, navController)
    }
}