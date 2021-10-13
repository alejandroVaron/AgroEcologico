package com.example.agroecologico

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agroecologico.Models.Product
import com.example.agroecologico.Models.QuantityPerProduct

class MyAdapterProductOrder(val productsList: MutableList<QuantityPerProduct>, val mListener: OnItemClickListener): RecyclerView.Adapter<MyAdapterProductOrder.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(position: Int, value: Int)
    }
    inner class ViewHolder(itemView: View, listener: MyAdapterProductOrder.OnItemClickListener): RecyclerView.ViewHolder(itemView), View.OnClickListener{

        var nameProductOrder: TextView
        var quantityProductOrder: TextView
        var totalProductOrder: TextView
        var btnPlus: ImageButton
        var btnMinus: ImageButton
        init{
            nameProductOrder = itemView.findViewById(R.id.tvNameProductOrder)
            quantityProductOrder = itemView.findViewById(R.id.tvquantityProductOrder)
            totalProductOrder = itemView.findViewById(R.id.tvTotalProductOrder)
            btnPlus = itemView.findViewById(R.id.btnPlusProduct)
            btnMinus = itemView.findViewById(R.id.btnMinusProduct)
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                //mListener.onItemClick(position)
            }
        }
    }
    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): MyAdapterProductOrder.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_product_order, parent, false)
        return ViewHolder(v, mListener)
    }
    override fun onBindViewHolder(holder: MyAdapterProductOrder.ViewHolder, position: Int) {
        val product: Product = productsList.get(position).product!!
        var price = productsList.get(position).product!!.priceProduct
        holder.nameProductOrder.text = product.nameProduct
        holder.quantityProductOrder.text = productsList.get(position).quantity.toString()
        holder.totalProductOrder.text = "$${productsList.get(position).quantity!!.toDouble() * price!!}"
        holder.btnPlus.setOnClickListener {
            holder.quantityProductOrder.text = (holder.quantityProductOrder.text.toString().toInt() + 1).toString()
            holder.totalProductOrder.text = "$${holder.quantityProductOrder.text.toString().toInt() * price!!}"
            mListener.onItemClick(position, 1)
            if(holder.quantityProductOrder.text.toString().toInt() == 0){
                notifyDataSetChanged()
            }
        }
        holder.btnMinus.setOnClickListener {
            holder.quantityProductOrder.text = (holder.quantityProductOrder.text.toString().toInt() - 1).toString()
            holder.totalProductOrder.text = "$${holder.quantityProductOrder.text.toString().toInt() * price!!}"
            mListener.onItemClick(position, -1)
            if(holder.quantityProductOrder.text.toString().toInt() == 0){
                notifyDataSetChanged()
            }
        }
    }
    fun clear(){
        productsList.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

}