package com.example.agroecologico

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.agroecologico.Models.MarketStall

// Class for communication between fragment-activity
class ItemViewModel : ViewModel(){

    var marketStall: MutableLiveData<MarketStall> = MutableLiveData<MarketStall>()

    fun getMarketStall(): LiveData<MarketStall>{
        return marketStall
    }

    fun setMarketStall(marketS: MarketStall){
        marketStall.value = marketS
    }

}

