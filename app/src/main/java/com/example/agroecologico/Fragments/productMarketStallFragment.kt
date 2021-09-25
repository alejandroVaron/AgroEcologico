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
import com.bumptech.glide.load.engine.executor.GlideExecutor.UncaughtThrowableStrategy.LOG
import com.example.agroecologico.DatabaseManager
import com.example.agroecologico.ItemViewModel
import com.example.agroecologico.Models.MarketStall
import com.example.agroecologico.Models.Product
import com.example.agroecologico.MyAdapterProducts
import com.example.agroecologico.R
import com.example.agroecologico.databinding.FragmentAddProductMarketStallBinding
import com.example.agroecologico.databinding.FragmentProductMarketStallBinding
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class productMarketStallFragment : Fragment() {

    private var marketStallPersonInFrag: MarketStall = MarketStall()
    private var _binding: FragmentProductMarketStallBinding? = null
    private val binding get() = _binding!!
    private val databaseManager: DatabaseManager = DatabaseManager()
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductMarketStallBinding.inflate(inflater, container, false)
        val view = binding.root
        val model: ItemViewModel by activityViewModels()
        // Method to bring the marketStall from the activity
        model.getMarketStall().observe(this, Observer<MarketStall> { marketStall ->
            marketStallPersonInFrag = marketStall
            launchProduct()
        })
/*
            val adapter = MyAdapterProducts(marketStallPersonInFrag.products!!)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter
 */
        return view
    }

    fun launchProduct() {
        val recyclerView = binding.rvRecycler
        var products: MutableList<Product> = arrayListOf()
        var product: Product = Product()
        database = FirebaseDatabase.getInstance().getReference("MarketStall")
        val query = database.child(marketStallPersonInFrag.identification!!)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (ds in dataSnapshot.child("products").children) {
                        product = Product(
                            ds.child("nameProduct").getValue(String::class.java),
                            ds.child("priceProduct").getValue(Double::class.java),
                            ds.child("imageProduct").getValue(String::class.java),
                            ds.child("salesUnitProduct").getValue(String::class.java)
                        )
                        products.add(product)
                    }
                    val adapter = MyAdapterProducts(products)
                    recyclerView.layoutManager = LinearLayoutManager(context)
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