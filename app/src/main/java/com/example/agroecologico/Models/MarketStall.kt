package com.example.agroecologico.Models

data class MarketStall(var nameMarketStall : String? = null,
                       var email : String? = null,
                       var password : String? = null,
                       var identification : String? = null,
                       var cellphone : String? = null,
                       var products : MutableList<Product>? = null,
                       var workers : MutableList<SalesPerson>? = null,
                       var orders: MutableList<Order>? = null,
                       var terrainPhoto : String? = null,
                       var salesPersonName : String? = null,
                       var salesPersonPhoto : String? = null)
