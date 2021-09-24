package com.example.agroecologico

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.AsyncTask.execute
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.agroecologico.Fragments.*
import com.example.agroecologico.Models.MarketStall
import com.example.agroecologico.Models.Product
import com.example.agroecologico.databinding.ActivityMenuAdminBinding
import com.example.agroecologico.databinding.ActivityMenuSalesPersonBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MenuActivitySalesPerson : AppCompatActivity() {
    private var marketStallPerson: MarketStall = MarketStall()
    private  lateinit var viewBinding: ActivityMenuSalesPersonBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var toolbar: Toolbar
    private lateinit var navigationView: NavigationView
    private lateinit var fragmentManagerInit: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction
    private lateinit var database: DatabaseReference
    private var databaseManager: DatabaseManager = DatabaseManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMenuSalesPersonBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Initialize the navegation drawer component
        initNavegationDrawer()

        // Fill in the market Stall data
        bringMarketStall()

        // Initialize the principal fragment
        initFragment()


        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.itemProduct -> {
                    val model: ItemViewModel by viewModels()
                    model.setMarketStall(marketStallPerson)
                    viewBinding.drawer.closeDrawer(GravityCompat.START)
                    fragmentManagerInit = getSupportFragmentManager()
                    fragmentTransaction = fragmentManagerInit.beginTransaction()
                    fragmentTransaction.replace(R.id.container, productMarketStallFragment())
                    fragmentTransaction.commit()
                    toolbar.title = "Productos"
                    hideSoftKeyboard()

                    true
                }
                R.id.itemAddProduct -> {
                    val model: ItemViewModel by viewModels()
                    Log.d("aiuda", "( Al presionar el botón add )- Estos son los productos: ${marketStallPerson.products}")
                    model.setMarketStall(marketStallPerson)
                    viewBinding.drawer.closeDrawer(GravityCompat.START)
                    fragmentManagerInit = getSupportFragmentManager()
                    fragmentTransaction = fragmentManagerInit.beginTransaction()
                    fragmentTransaction.replace(R.id.container, addProductMarketStall())
                    fragmentTransaction.commit()
                    toolbar.title = "Productos"
                    hideSoftKeyboard()

                    true
                }
                R.id.itemMarketStall -> {
                    val model: ItemViewModel by viewModels()
                    Log.d("aiuda", "( Al presionar el botón marketStall )- Estos son los productos: ${marketStallPerson.products}")
                    model.setMarketStall(marketStallPerson)
                    viewBinding.drawer.closeDrawer(GravityCompat.START)
                    fragmentManagerInit = getSupportFragmentManager()
                    fragmentTransaction = fragmentManagerInit.beginTransaction()
                    fragmentTransaction.replace(R.id.container, marketStallPersonFragment())
                    fragmentTransaction.commit()
                    toolbar.title = "Puesto de venta"
                    hideSoftKeyboard()
                    true
                }
                R.id.itemSalesWorker -> {
                    val model: ItemViewModel by viewModels()
                    model.setMarketStall(marketStallPerson)
                    viewBinding.drawer.closeDrawer(GravityCompat.START)
                    fragmentManagerInit = getSupportFragmentManager()
                    fragmentTransaction = fragmentManagerInit.beginTransaction()
                    fragmentTransaction.replace(R.id.container, salesPersonFragment())
                    fragmentTransaction.commit()
                    toolbar.title = "Trabajadores"
                    hideSoftKeyboard()
                    true
                }
                R.id.itemEditMarketStall -> {
                    val model: ItemViewModel by viewModels()
                    Log.d("aiuda", "( Al presionar el botón EditmarketStall )- Estos son los productos: ${marketStallPerson.products}")
                    model.setMarketStall(marketStallPerson)
                    viewBinding.drawer.closeDrawer(GravityCompat.START)
                    fragmentManagerInit = getSupportFragmentManager()
                    fragmentTransaction = fragmentManagerInit.beginTransaction()
                    fragmentTransaction.replace(R.id.container, editMarketStallFragment())
                    fragmentTransaction.commit()
                    toolbar.title = "Editar puesto de venta"
                    hideSoftKeyboard()
                    true
                }
                else -> false
            }
        }
    }
    private fun bringMarketStall(){
        marketStallPerson = MarketStall(cellphone = intent.getStringExtra("cellphone").toString(),
            email = intent.getStringExtra("email").toString(),
            identification = intent.getStringExtra("identification").toString(),
            nameMarketStall = intent.getStringExtra("nameMarketStall").toString(),
            password = intent.getStringExtra("password").toString(),
            products = mutableListOf<Product>(),
            salesPersonName = intent.getStringExtra("salesPersonName").toString(),
            salesPersonPhoto = intent.getStringExtra("salesPersonPhoto").toString(),
            terrainPhoto= intent.getStringExtra("terrainPhoto").toString())

        databaseManager.getMarketStall(marketStallPerson.identification!!)
        GlobalScope.launch(Dispatchers.IO) {
            marketStallPerson.products = databaseManager.getProducts(marketStallPerson.identification!!)
            marketStallPerson.workers = databaseManager.getSalesPerson(marketStallPerson.identification!!)
        }
    }
    // Initialize the principal fragment
    private fun initFragment(){
        fragmentManagerInit = getSupportFragmentManager()
        fragmentTransaction = fragmentManagerInit.beginTransaction()
        fragmentTransaction.add(R.id.container, marketStallPersonFragment())
        fragmentTransaction.commit()
        val model: ItemViewModel by viewModels()
        model.setMarketStall(marketStallPerson)
    }

    fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager: InputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    // Initialize the navegation drawer component
    private fun initNavegationDrawer(){
        toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Puesto de venta"
        setSupportActionBar(toolbar)
        toolbar.title = "Puesto de venta"
        drawerLayout = findViewById(R.id.drawer)
        navigationView = findViewById(R.id.navigationView)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true)
        actionBarDrawerToggle.syncState()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
    }


    fun setMarketStall(marketStall: MarketStall){
        marketStallPerson = marketStall
        Log.d("aiuda", "( En la actividad menuActivitySalesPerson )- Estos son los productos: ${marketStallPerson.products}")
    }

}

