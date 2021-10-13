package com.example.agroecologico.Fragments

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agroecologico.Models.MarketStall
import com.example.agroecologico.Models.QuantityPerProduct
import com.example.agroecologico.MyAdapterProductOrder
import com.example.agroecologico.MyAdapterProductStore
import com.example.agroecologico.R
import com.example.agroecologico.databinding.FragmentAddSalesPersonsBinding
import com.example.agroecologico.databinding.FragmentCartPurchaserBinding
import android.widget.RadioGroup
import android.widget.Toast
import com.example.agroecologico.Models.Order
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class cartPurchaserFragment : Fragment(), MyAdapterProductOrder.OnItemClickListener {
    private var _binding: FragmentCartPurchaserBinding? = null
    private val binding get() = _binding!!
    private var deliveryType: String = ""
    private var productListOrder: MutableList<QuantityPerProduct> = mutableListOf<QuantityPerProduct>()
    private var clientAddress: String? = ""
    private var clientCellphone: String? = ""
    private var clientName: String? = ""
    private var clientEmail: String? = ""
    private var marketStallId: String? = ""
    private lateinit var adapter: MyAdapterProductOrder
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().getReference("MarketStall")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartPurchaserBinding.inflate(inflater, container, false)
        val view = binding.root

        var bundle: Bundle = requireArguments()
        productListOrder = bundle.getSerializable("quantityPerProduct") as MutableList<QuantityPerProduct>
        clientAddress = bundle.getString("clientAddress")
        clientCellphone = bundle.getString("clientCellphone")
        clientName = bundle.getString("clientName")
        clientEmail = bundle.getString("clientEmail")
        marketStallId = bundle.getString("marketStallId")
        launchCart()
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId -> // checkedId is the RadioButton selected
            when (checkedId) {
                R.id.rbStall -> {
                    deliveryType = "Recoger en puesto de venta"
                }
                R.id.rbDomicile -> {
                    deliveryType = "Domicilio"
                }
            }
        }
        binding.btnCreateOrder.setOnClickListener {
            if(productListOrder.isEmpty()){
                Toast.makeText(activity, "¡Debe seleccionar productos de un puesto de venta para realizar el pedido!", Toast.LENGTH_SHORT).show()
            }else if(deliveryType == ""){
                Toast.makeText(activity, "¡Debe seleccionar un tipo de entrega!", Toast.LENGTH_SHORT).show()
            }else{
                createOrder()
            }
        }
        return view
    }
    fun launchCart(){
        var total: Double = 0.0
        for(i in 0..productListOrder.size-1){
            total+=(productListOrder.get(i).quantity!!*productListOrder.get(i).product!!.priceProduct!!)
        }
        binding.tvInputTotal.text = total.toString()
        adapter = MyAdapterProductOrder(productListOrder!!,this)
        var recyclerView = binding.rvRecyclerProductsOrder
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

    }
    fun createOrder(){
        var order: Order = Order(clientName, productListOrder, deliveryType, clientAddress, clientCellphone, clientEmail)
        database.child(marketStallId!!).child("orders").child("${UUID.randomUUID().toString()}")
            .setValue(order).addOnSuccessListener {
                clear()
                Toast.makeText(activity, "¡El pedido se ha creado con éxito!", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(activity, "¡Hubo un problema en la creación del pedido!", Toast.LENGTH_SHORT).show()
            }
    }
    override fun onItemClick(position: Int, value: Int) {

        productListOrder.get(position).quantity = productListOrder.get(position).quantity!! + value
        if(productListOrder.get(position).quantity == 0){
            productListOrder.removeAt(position)
        }
        var total: Double = 0.0
        for(i in 0..productListOrder.size-1){
            total+=(productListOrder.get(i).quantity!!*productListOrder.get(i).product!!.priceProduct!!)
        }
        binding.tvInputTotal.text = total.toString()
    }
    fun clear(){
        productListOrder = mutableListOf()
        binding.tvInputTotal.text = "0.0"
        binding.radioGroup.clearCheck()
        adapter.clear()
    }
}