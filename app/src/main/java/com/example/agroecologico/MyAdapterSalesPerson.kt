package com.example.agroecologico

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agroecologico.Models.Product
import com.example.agroecologico.Models.SalesPerson

class MyAdapterSalesPerson(val salesPersonList: MutableList<SalesPerson>): RecyclerView.Adapter<MyAdapterSalesPerson.ViewHolder>()  {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var salesPersonImage: ImageView
        var salesPersonName: TextView
        init{
            salesPersonImage = itemView.findViewById(R.id.ivSalesPersonImageS)
            salesPersonName = itemView.findViewById(R.id.tvSalesPersonNameS)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapterSalesPerson.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_salesperson, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyAdapterSalesPerson.ViewHolder, position: Int) {
        val salesPerson: SalesPerson = salesPersonList.get(position)
        Glide.with(holder.salesPersonImage)
            .load(salesPerson.imageProduct)
            .centerCrop()
            .into(holder.salesPersonImage)
        holder.salesPersonName.text = salesPerson.nameSalesPerson
    }

    override fun getItemCount(): Int {
        return salesPersonList.size
    }

}