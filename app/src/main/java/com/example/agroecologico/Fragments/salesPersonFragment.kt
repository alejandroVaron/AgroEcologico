package com.example.agroecologico.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.agroecologico.R
import com.example.agroecologico.databinding.FragmentMarketStallPersonBinding

class salesPersonFragment : Fragment() {
    private var _binding: FragmentMarketStallPersonBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMarketStallPersonBinding.inflate(inflater, container, false)
        val view = binding.root


        return view
    }

}