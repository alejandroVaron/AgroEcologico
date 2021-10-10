package com.example.agroecologico.Models

data class Order(var clientName: String? = null,
                 var quantityPerProducts: MutableList<QuantityPerProduct>? = null,
                 var deliveryType: String? = null,
                 var clientAddress: String? = null,
                 var clientCellphone: String? = null,
                 var clientEmail: String? = null)
