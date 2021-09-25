package com.example.agroecologico.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.agroecologico.*
import com.example.agroecologico.Models.MarketStall
import com.example.agroecologico.Models.Product
import com.example.agroecologico.Models.SalesPerson
import com.example.agroecologico.R
import com.example.agroecologico.databinding.FragmentMarketStallPersonBinding
import com.example.agroecologico.databinding.FragmentSalesPersonBinding
import com.google.firebase.database.*
import java.io.IOException

class salesPersonFragment : Fragment() {
    private var _binding: FragmentSalesPersonBinding? = null
    private val binding get() = _binding!!
    lateinit var marketStallPersonInFrag: MarketStall
    private val databaseManager: DatabaseManager = DatabaseManager()
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSalesPersonBinding.inflate(inflater, container, false)
        val view = binding.root
        val model: ItemViewModel by activityViewModels()
        // Method to bring the marketStall from the activity
        model.getMarketStall().observe(this, Observer<MarketStall> { marketStall ->
            marketStallPersonInFrag = marketStall
            launchSalesPerson()
        })

        return view
    }

    fun launchSalesPerson() {
        val recyclerView = binding.rvRecyclerSalesPerson
        var salesPersonList: MutableList<SalesPerson> = arrayListOf()
        var salesPerson: SalesPerson = SalesPerson()
        database = FirebaseDatabase.getInstance().getReference("MarketStall")
        val query = database.child(marketStallPersonInFrag.identification!!)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (ds in dataSnapshot.child("workers").children) {
                        salesPerson = SalesPerson(
                            ds.child("imageProduct").getValue(String::class.java),
                            ds.child("nameSalesPerson").getValue(String::class.java)
                        )
                        salesPersonList.add(salesPerson)
                        Log.d("aiuda", "Estos son los trabajadores: $salesPersonList")
                    }
                    val adapter = MyAdapterSalesPerson(salesPersonList)
                    recyclerView.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                    recyclerView.adapter = adapter
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("", databaseError.getMessage()) //Don't ignore errors!

            }
        }
        query.addValueEventListener(valueEventListener)
    }

}

