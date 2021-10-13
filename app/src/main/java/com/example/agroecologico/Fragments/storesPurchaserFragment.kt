package com.example.agroecologico.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.agroecologico.*
import com.example.agroecologico.Models.MarketStall
import com.example.agroecologico.Models.Product
import com.example.agroecologico.Models.SalesPerson
import com.example.agroecologico.R
import com.example.agroecologico.databinding.FragmentCartPurchaserBinding
import com.example.agroecologico.databinding.FragmentStoresPurchaserBinding
import com.google.firebase.database.*
import java.io.Serializable

class storesPurchaserFragment : Fragment(), MyAdaptarMarketStalls.OnItemClickListener {
    private var _binding: FragmentStoresPurchaserBinding? = null
    private val binding get() = _binding!!
    private var marketStallList: MutableList<MarketStall> = mutableListOf<MarketStall>()
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        marketStallList = mutableListOf()
        _binding = FragmentStoresPurchaserBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.tbToolbarStore.title = "Puestos de venta"
        var bundle: Bundle = requireArguments()
        var marketStalls: MutableList<MarketStall> = bundle.getSerializable("marketStall") as MutableList<MarketStall>
        launchMarketStalls()
        return view
    }

    fun launchMarketStalls(){
        val recyclerView = binding.rvRecyclerMarketStalls
        database = FirebaseDatabase.getInstance().getReference("MarketStall")
        var marketStall: MarketStall = MarketStall()
        var productList: MutableList<Product> = mutableListOf<Product>()
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for(ds in dataSnapshot.children){
                        for(dsProduct in ds.child("products").children){
                            Log.d("aiuda","Este es el precio: ${dsProduct.child("priceProduct").getValue(Double::class.java)}")
                            val product = Product(nameProduct = dsProduct.child("nameProduct").getValue(String::class.java),
                            priceProduct = dsProduct.child("priceProduct").getValue(Double::class.java),
                            imageProduct = dsProduct.child("imageProduct").getValue(String::class.java),
                            salesUnitProduct = dsProduct.child("salesUnitProduct").getValue(String::class.java))
                            productList.add(product)
                        }
                        val products: MutableList<Product> = productList
                        marketStall = MarketStall(ds.child("nameMarketStall").getValue(String::class.java),
                            ds.child("email").getValue(String::class.java),
                            ds.child("password").getValue(String::class.java),
                            ds.child("identification").getValue(String::class.java),
                            ds.child("cellphone").getValue(String::class.java),
                            products,
                            null,
                            null,
                            ds.child("terrainPhoto").getValue(String::class.java),
                            ds.child("salesPersonName").getValue(String::class.java),
                            ds.child("salesPersonPhoto").getValue(String::class.java))
                        marketStallList.add(marketStall)
                        productList = mutableListOf()
                    }
                    var adapter = MyAdaptarMarketStalls(marketStallList, this@storesPurchaserFragment)
                    recyclerView.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                    recyclerView.adapter = adapter

                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("", databaseError.getMessage()) //Don't ignore errors!
            }
        }
        database.addValueEventListener(valueEventListener)
    }

    override fun onItemClick(position: Int) {
        var bundle: Bundle = Bundle()
        var marketStallSer: productsMarketStallFragment.MarketStallSerializable =
            productsMarketStallFragment.MarketStallSerializable(marketStallList.get(position).nameMarketStall,
                marketStallList.get(position).email,
                marketStallList.get(position).password,
                marketStallList.get(position).identification,
                marketStallList.get(position).cellphone,
                marketStallList.get(position).products,
                marketStallList.get(position).workers,
                marketStallList.get(position).orders,
                marketStallList.get(position).terrainPhoto,
                marketStallList.get(position).salesPersonName,
                marketStallList.get(position).salesPersonPhoto)
        bundle.putSerializable("marketStall", marketStallSer as Serializable)
        var fragment = productsMarketStallFragment()
        fragment.arguments = bundle
        (activity as MenuActivityPurchaser).setActiveMarketStall(marketStallList.get(position))
        (activity as MenuActivityPurchaser).fragmentManager(fragment)
    }

}