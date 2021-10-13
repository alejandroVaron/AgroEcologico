package com.example.agroecologico

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import com.example.agroecologico.Fragments.cartPurchaserFragment
import com.example.agroecologico.Fragments.profilePurchaserFragment
import com.example.agroecologico.Fragments.storesPurchaserFragment
import com.example.agroecologico.Models.MarketStall
import com.example.agroecologico.Models.Product
import com.example.agroecologico.Models.QuantityPerProduct
import com.example.agroecologico.databinding.ActivityMenuPurchaserBinding
import com.example.agroecologico.databinding.ActivityMenuSalesPersonBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.Serializable
import java.lang.ref.WeakReference
import kotlin.math.log

class MenuActivityPurchaser : AppCompatActivity() {
    private var marketStalls: MutableList<MarketStall> = mutableListOf<MarketStall>()
    private var profileFragment = profilePurchaserFragment()
    private var storesFragment = storesPurchaserFragment()
    private var cartFragment = cartPurchaserFragment()
    private var products: MutableList<Product> = mutableListOf<Product>()
    private var productsOrder: MutableList<QuantityPerProduct> = mutableListOf<QuantityPerProduct>()
    private  lateinit var viewBinding: ActivityMenuPurchaserBinding
    lateinit var mInstanceActivity: MenuActivityPurchaser
    private var activeSalesPerson: String = ""
    private var activeMarketStall: MarketStall = MarketStall()
    // etc..
    fun getmInstanceActivity(): MenuActivityPurchaser? {
        return mInstanceActivity
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mInstanceActivity = this
        viewBinding = ActivityMenuPurchaserBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        loadMarketStalls()
        viewBinding.bottomNavigationView.setOnNavigationItemSelectedListener {
            viewBinding.containerFragmentPurchaser.background = null
            when(it.itemId){
                R.id.itemProfilePurchaser ->{
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.containerFragmentPurchaser, profileFragment)
                        commit()
                    }
                    true
                }
                R.id.itemMarketsPurchaser ->{
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.containerFragmentPurchaser, storesFragment)
                        commit()
                    }
                    true
                }
                R.id.itemCartPurchaser ->{
                    moveToCar()
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.containerFragmentPurchaser, cartFragment)
                        commit()
                    }
                    true
                }
                else -> false
            }
        }

    }
    private fun loadMarketStalls(){
        var bundle: Bundle = Bundle()
        bundle.putSerializable("marketStall", marketStalls as Serializable)
        storesFragment.arguments = bundle
    }
    fun fragmentManager(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.containerFragmentPurchaser, fragment)
            commit()
        }
    }
    fun addProduct(product: Product){
        products.add(product)
    }
    fun setActiveMarketStall(marketStal: MarketStall){
        activeMarketStall = marketStal
    }
    fun clearProductList(){
        products = mutableListOf()
    }
    fun getProductList():MutableList<Product>{
        return products
    }
    fun moveToCar(){
        productsOrder = mutableListOf()
        var bool: Boolean = false
        var bool2: Boolean = true
        for(i in 0..products.size-1){
            bool = false
            bool2 = true
            if(i != 0){
                for(j in 0..productsOrder.size-1){
                    if(products.get(i).equals(productsOrder.get(j).product) && bool2){
                        productsOrder.get(j).quantity = productsOrder.get(j).quantity!! + 1
                        bool = true
                        bool2 = false
                    }
                    if(j == productsOrder.size-1 && !bool){
                        productsOrder.add(QuantityPerProduct(1,products.get(i)))
                    }
                }
            }else{
                productsOrder.add(QuantityPerProduct(1,products.get(i)))
            }
        }
        var bundle: Bundle = Bundle()
        bundle.putSerializable("quantityPerProduct", productsOrder as Serializable)
        bundle.putString("clientAddress", intent.getStringExtra("clientAddress").toString())
        bundle.putString("clientCellphone",intent.getStringExtra("clientCellphone").toString())
        bundle.putString("clientEmail",intent.getStringExtra("clientEmail").toString())
        bundle.putString("clientName",intent.getStringExtra("clientName").toString())
        bundle.putString("marketStallId", activeMarketStall.identification)
        cartFragment.arguments = bundle
    }
}