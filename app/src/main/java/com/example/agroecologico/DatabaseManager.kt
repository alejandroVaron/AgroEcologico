package com.example.agroecologico

import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.agroecologico.Models.MarketStall
import com.example.agroecologico.Models.Product
import com.example.agroecologico.Models.SalesPerson
import com.google.firebase.database.*
import com.google.gson.Gson

class DatabaseManager {
    private lateinit var database: DatabaseReference

    fun getMarketStall(identification: String): MarketStall {
        Log.d("aiuda", "Este es la identificación: $identification")
        var  marketStall: MarketStall = MarketStall()
        var product: Product = Product()
        var products: MutableList<Product> = mutableListOf<Product>()
        var salesPerson: SalesPerson = SalesPerson()
        var workers: MutableList<SalesPerson> = mutableListOf<SalesPerson>()
        database = FirebaseDatabase.getInstance().getReference("MarketStall")
        val query = database.child(identification)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                        Log.d("aiuda", "Este es: !! ${dataSnapshot.child("email").getValue(String::class.java)}")
                        for(dsP in dataSnapshot.child("products").children){
                            product= Product(
                                dsP.child("nameProduct").getValue(String::class.java),
                                dsP.child("priceProduct").getValue(Double::class.java),
                                dsP.child("imageProduct").getValue(String::class.java),
                                dsP.child("salesUnitProduct").getValue(String::class.java)
                            )
                            products.add(product)
                        }
                        for(dsW in dataSnapshot.child("workers").children){
                            salesPerson = SalesPerson(
                                dsW.child("imageProduct").getValue(String::class.java),
                                dsW.child("nameSalesPerson").getValue(String::class.java)
                            )
                            workers.add(salesPerson)
                        }
                        marketStall = MarketStall(
                            dataSnapshot.child("nameMarketStall").getValue(String::class.java),
                            dataSnapshot.child("email").getValue(String::class.java),
                            dataSnapshot.child("password").getValue(String::class.java),
                            dataSnapshot.child("identification").getValue(String::class.java),
                            dataSnapshot.child("cellphone").getValue(String::class.java),
                            products,
                            workers,
                            dataSnapshot.child("terrainPhoto").getValue(String::class.java),
                            dataSnapshot.child("salesPersonName").getValue(String::class.java),
                            dataSnapshot.child("salesPersonPhoto").getValue(String::class.java),
                        )
                    Log.d("aiuda", "Este es: !! ${marketStall}")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("", databaseError.getMessage()) //Don't ignore errors!
            }
        }
        query.addValueEventListener(valueEventListener)
        return marketStall
    }



    fun getProducts(identification: String): MutableList<Product>?{

        var products: MutableList<Product> = arrayListOf()
        var product: Product = Product()
        database = FirebaseDatabase.getInstance().getReference("MarketStall")
        val query = database.child(identification)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    for (ds in dataSnapshot.child("products").children){
                        product = Product(ds.child("nameProduct").getValue(String::class.java),
                            ds.child("priceProduct").getValue(Double::class.java),
                            ds.child("imageProduct").getValue(String::class.java),
                            ds.child("salesUnitProduct").getValue(String::class.java))
                        products.add(product)
                    }
                }

            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("", databaseError.getMessage()) //Don't ignore errors!

            }
        }

        query.addValueEventListener(valueEventListener)
        return products
    }

    fun getSalesPerson(identification: String): MutableList<SalesPerson>?{
        var workers: MutableList<SalesPerson> = arrayListOf()
        var salesPerson: SalesPerson = SalesPerson()
        database = FirebaseDatabase.getInstance().getReference("MarketStall")

        val query = database.child(identification)

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    for (ds in dataSnapshot.child("workers").children){
                        salesPerson = SalesPerson(ds.child("imageProduct").getValue(String::class.java),
                            ds.child("nameSalesPerson").getValue(String::class.java))
                        workers.add(salesPerson)
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("", databaseError.getMessage()) //Don't ignore errors!
            }
        }

        query.addValueEventListener(valueEventListener)
        return workers
    }

}