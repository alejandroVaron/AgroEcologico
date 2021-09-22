package com.example.agroecologico.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.agroecologico.ItemViewModel
import com.example.agroecologico.Models.MarketStall
import com.example.agroecologico.databinding.FragmentMarketStallPersonBinding


class marketStallPersonFragment : Fragment() {

    // En este objeto se encuentra el puesto de venta del trabajador
    lateinit var marketStallPersonInFrag: MarketStall

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
        val model: ItemViewModel by activityViewModels()
        // Method to bring the marketStall from the activity
        model.getMarketStall().observe(this, Observer<MarketStall>{ marketStall ->
            Log.d("aiuda", "Entré en el viewModel: ")
            marketStallPersonInFrag = marketStall
            bringData()
        })
        return view
    }

    private fun bringData(){
        binding.tvMarketStallName.setText("nameMarketStall: ${marketStallPersonInFrag.nameMarketStall}")
        binding.tvWorkerId.setText("identification: ${marketStallPersonInFrag.identification}")
        binding.tvCellphoneWorker.setText("cellphone: ${marketStallPersonInFrag.cellphone}")
        binding.tvEmailWorker.setText("email: ${marketStallPersonInFrag.email}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}