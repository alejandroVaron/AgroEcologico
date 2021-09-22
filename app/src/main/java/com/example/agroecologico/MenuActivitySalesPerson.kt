package com.example.agroecologico

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
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
import androidx.lifecycle.ViewModelProvider
import com.example.agroecologico.Fragments.addProductMarketStall
import com.example.agroecologico.Fragments.marketStallPersonFragment
import com.example.agroecologico.Models.MarketStall
import com.example.agroecologico.Models.Product
import com.example.agroecologico.databinding.ActivityMenuAdminBinding
import com.example.agroecologico.databinding.ActivityMenuSalesPersonBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.GlobalScope
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
                R.id.itemAddProduct -> {
                    viewBinding.drawer.closeDrawer(GravityCompat.START)
                    fragmentManagerInit = getSupportFragmentManager()
                    fragmentTransaction = fragmentManagerInit.beginTransaction()
                    fragmentTransaction.replace(R.id.container, addProductMarketStall())
                    fragmentTransaction.commit()
                    true
                }
                R.id.itemMarketStall -> {
                    viewBinding.drawer.closeDrawer(GravityCompat.START)
                    fragmentManagerInit = getSupportFragmentManager()
                    fragmentTransaction = fragmentManagerInit.beginTransaction()
                    fragmentTransaction.replace(R.id.container, marketStallPersonFragment())
                    fragmentTransaction.commit()
                    val model: ItemViewModel by viewModels()
                    model.setMarketStall(marketStallPerson)
                    true
                }
                R.id.itemSalesWorker -> {
                    viewBinding.drawer.closeDrawer(GravityCompat.START)
                    fragmentManagerInit = getSupportFragmentManager()
                    fragmentTransaction = fragmentManagerInit.beginTransaction()
                    fragmentTransaction.replace(R.id.container, marketStallPersonFragment())
                    fragmentTransaction.commit()
                    val model: ItemViewModel by viewModels()
                    model.setMarketStall(marketStallPerson)
                    true
                }
                else -> false
            }
        }
        /*

        <uses-permission android:name="android.permission.CAMERA" />
        <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                0
            )
        }
        */

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

    // Initialize the navegation drawer component
    private fun initNavegationDrawer(){
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawer)
        navigationView = findViewById(R.id.navigationView)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true)
        actionBarDrawerToggle.syncState()
    }

    fun toastManual(msg: String){
        Log.d("aiuda", msg)
        Toast.makeText(this, "El puesto de venta se ha añadido exitosamente", Toast.LENGTH_SHORT ).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
    }
}

