package com.example.agroecologico

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agroecologico.Models.MarketStall
class MyAdaptarMarketStalls(val marketStallList: MutableList<MarketStall>, val mListener: OnItemClickListener): RecyclerView.Adapter<MyAdaptarMarketStalls.ViewHolder>() {


    inner class ViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var marketStallImage: ImageView
        var marketStallName: TextView
        init{
            marketStallImage = itemView.findViewById(R.id.ivMarketStall)
            marketStallName = itemView.findViewById(R.id.tvMarketStall)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                mListener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdaptarMarketStalls.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_marketstall, parent, false)
        return ViewHolder(v, mListener)
    }

    override fun onBindViewHolder(holder: MyAdaptarMarketStalls.ViewHolder, position: Int) {
        val marketStall: MarketStall = marketStallList.get(position)
        Glide.with(holder.marketStallImage)
            .load(marketStall.terrainPhoto)
            .centerCrop()
            .into(holder.marketStallImage)
        holder.marketStallName.text = marketStall.nameMarketStall

    }

    override fun getItemCount(): Int {
        return marketStallList.size
    }
}