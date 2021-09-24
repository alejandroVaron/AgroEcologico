package com.example.agroecologico.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agroecologico.ItemViewModel
import com.example.agroecologico.Models.MarketStall
import com.example.agroecologico.MyAdapterProducts
import com.example.agroecologico.R
import com.example.agroecologico.databinding.FragmentAddProductMarketStallBinding
import com.example.agroecologico.databinding.FragmentProductMarketStallBinding

class productMarketStallFragment : Fragment() {

    private  var marketStallPersonInFrag: MarketStall = MarketStall()
    private var _binding: FragmentProductMarketStallBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("aiuda", "Me llamaron!!!!!")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductMarketStallBinding.inflate(inflater, container, false)
        val view = binding.root
        val model: ItemViewModel by activityViewModels()
        // Method to bring the marketStall from the activity
        model.getMarketStall().observe(this, Observer<MarketStall>{ marketStall ->
            marketStallPersonInFrag = marketStall
            val recyclerView = binding.rvRecycler
            val adapter = MyAdapterProducts(marketStallPersonInFrag.products!!)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter
        })
        return view
    }

}