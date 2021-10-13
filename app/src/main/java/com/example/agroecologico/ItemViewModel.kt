package com.example.agroecologico

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.agroecologico.Models.MarketStall

// Class for communication between fragment-activity
class ItemViewModel : ViewModel(){

    var marketStall: MutableLiveData<MarketStall> = MutableLiveData<MarketStall>()

    var marketStalls: MutableLiveData<MutableList<MarketStall>> = MutableLiveData<MutableList<MarketStall>>()


    fun getMarketStall(): LiveData<MarketStall>{
        return marketStall
    }

    fun setMarketStall(marketS: MarketStall){
        marketStall.value = marketS
    }

    fun getMarketStalls(): LiveData<MutableList<MarketStall>>{
        return marketStalls
    }

    fun setMarketStall(marketStallls: MutableList<MarketStall>){
        marketStalls.value = marketStallls
    }
}

