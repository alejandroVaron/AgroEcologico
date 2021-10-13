package com.example.agroecologico

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agroecologico.Fragments.productMarketStallFragment
import com.example.agroecologico.Models.Product

class MyAdapterProductStore(val productsList: MutableList<Product>, val mListener: OnItemClickListener, val fragment: Fragment): RecyclerView.Adapter<MyAdapterProductStore.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    inner class ViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var productImage: ImageView
        var productName: TextView
        var productUnit: TextView
        var productPrice: TextView
        var productButton: Button
        init{
            productImage = itemView.findViewById(R.id.ivProductMarketStall)
            productName = itemView.findViewById(R.id.tvProductMarketStall)
            productUnit = itemView.findViewById(R.id.tvUnitProductMarketStall)
            productPrice = itemView.findViewById(R.id.tvPriceProductMarketStall)
            productButton = itemView.findViewById(R.id.btnProductMarketStall)
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                //mListener.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapterProductStore.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_product_store, parent, false)
        return ViewHolder(v, mListener)
    }

    override fun onBindViewHolder(holder: MyAdapterProductStore.ViewHolder, position: Int) {
        val product: Product = productsList.get(position)
        Glide.with(holder.productImage)
            .load(product.imageProduct)
            .centerCrop()
            .into(holder.productImage)
        holder.productName.text = product.nameProduct
        holder.productUnit.text = product.salesUnitProduct
        holder.productPrice.text = "$"+product.priceProduct

        holder.productButton.setOnClickListener {
            mListener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

}