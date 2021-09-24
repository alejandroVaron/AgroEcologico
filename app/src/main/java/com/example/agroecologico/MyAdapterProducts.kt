package com.example.agroecologico

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agroecologico.Fragments.productMarketStallFragment
import com.example.agroecologico.Models.Product

class MyAdapterProducts(val products: MutableList<Product>): RecyclerView.Adapter<MyAdapterProducts.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var productImage: ImageView
        var productName: TextView
        var productPrice: TextView
        var productUnit: TextView

        init{
            productImage = itemView.findViewById(R.id.ivItemProductPhoto)
            productName = itemView.findViewById(R.id.tvItemProductName)
            productPrice = itemView.findViewById(R.id.tvItemProductPrice)
            productUnit = itemView.findViewById(R.id.tvItemProductUnit)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.productImage
        val product: Product = products.get(position)
        Glide.with(holder.productImage)
            .load(product.imageProduct)
            .centerCrop()
            .into(holder.productImage)
        holder.productName.text = product.nameProduct
        holder.productPrice.text = "$"+product.priceProduct.toString()
        holder.productUnit.text = product.salesUnitProduct

    }

    override fun getItemCount(): Int {
        return products.size
    }
}