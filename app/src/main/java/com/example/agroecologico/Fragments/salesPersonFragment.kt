package com.example.agroecologico.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.agroecologico.ItemViewModel
import com.example.agroecologico.Models.MarketStall
import com.example.agroecologico.R
import com.example.agroecologico.databinding.FragmentMarketStallPersonBinding
import com.example.agroecologico.databinding.FragmentSalesPersonBinding
import java.io.IOException

class salesPersonFragment : Fragment() {
    private var _binding: FragmentSalesPersonBinding? = null
    private val binding get() = _binding!!
    lateinit var marketStallPersonInFrag: MarketStall

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSalesPersonBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

}