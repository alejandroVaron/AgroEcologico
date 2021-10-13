package com.example.agroecologico.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.agroecologico.MenuActivityPurchaser
import com.example.agroecologico.Models.MarketStall
import com.example.agroecologico.Models.Order
import com.example.agroecologico.Models.Product
import com.example.agroecologico.Models.SalesPerson
import com.example.agroecologico.MyAdaptarMarketStalls
import com.example.agroecologico.MyAdapterProductStore
import com.example.agroecologico.R
import com.example.agroecologico.databinding.FragmentProductMarketStallBinding
import com.example.agroecologico.databinding.FragmentProductsMarketStallBinding
import com.example.agroecologico.databinding.FragmentStoresPurchaserBinding
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class productsMarketStallFragment : Fragment(), MyAdapterProductStore.OnItemClickListener {
    private var _binding: FragmentProductsMarketStallBinding? = null
    private val binding get() = _binding!!
    private var marketStall: MarketStallSerializable = MarketStallSerializable()
    private var productList: MutableList<Product> = mutableListOf<Product>()
    private lateinit var toast: Toast
    data class MarketStallSerializable(var nameMarketStall : String? = null,
                           var email : String? = null,
                           var password : String? = null,
                           var identification : String? = null,
                           var cellphone : String? = null,
                           var products : MutableList<Product>? = null,
                           var workers : MutableList<SalesPerson>? = null,
                           var orders: MutableList<Order>? = null,
                           var terrainPhoto : String? = null,
                           var salesPersonName : String? = null,
                           var salesPersonPhoto : String? = null): Serializable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductsMarketStallBinding.inflate(inflater, container, false)
        val view = binding.root
        toast = Toast.makeText(activity, "", Toast.LENGTH_SHORT)
        var bundle: Bundle = requireArguments()
        marketStall = bundle.getSerializable("marketStall") as MarketStallSerializable
        launchProducts()
        return view
    }
    fun launchProducts(){
        binding.tvMarketStallNameInProducts.text = marketStall.nameMarketStall
        Glide.with(binding.ivMarketStallInProduct)
            .load(marketStall.terrainPhoto)
            .centerCrop()
            .into(binding.ivMarketStallInProduct)
        productList = marketStall.products!!
        if(productList.isEmpty()){
            binding.ivEmptyList.isInvisible= false
            binding.tvEmptyList.isInvisible= false
        }else{
            binding.ivEmptyList.isInvisible= true
            binding.tvEmptyList.isInvisible= true
            var adapter = MyAdapterProductStore(marketStall.products!!,this, this)
            var recyclerView = binding.rvRecyclerProducts
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter
        }
    }

    override fun onItemClick(position: Int) {
        toast.cancel()
        (activity as MenuActivityPurchaser).addProduct(productList.get(position))
        toast = Toast.makeText(activity, "", Toast.LENGTH_SHORT)
        toast.setText("¡Se ha agregado: ${productList.get(position).salesUnitProduct} de ${productList.get(position).nameProduct} al carrito de compra con éxito!")
        toast.show()
    }

}