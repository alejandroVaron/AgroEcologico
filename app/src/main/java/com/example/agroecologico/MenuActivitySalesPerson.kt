package com.example.agroecologico

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.example.agroecologico.Fragments.*
import com.example.agroecologico.Models.*
import com.example.agroecologico.databinding.ActivityMenuSalesPersonBinding
import com.google.android.gms.tasks.Tasks
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import kotlinx.coroutines.*
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.lang.Runnable

class MenuActivitySalesPerson : AppCompatActivity(){
    private var marketStallPerson: MarketStall = MarketStall()
    private  lateinit var viewBinding: ActivityMenuSalesPersonBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var toolbar: Toolbar
    private lateinit var navigationView: NavigationView
    private lateinit var fragmentManagerInit: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction
    private lateinit var database: DatabaseReference
    private var context: Context = this
    private var databaseManager: DatabaseManager = DatabaseManager()
    private lateinit var filePath: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMenuSalesPersonBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Initialize the navegation drawer component
        initNavegationDrawer()
        result()
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
                    model.setMarketStall(marketStallPerson)
                    viewBinding.drawer.closeDrawer(GravityCompat.START)
                    fragmentManagerInit = getSupportFragmentManager()
                    fragmentTransaction = fragmentManagerInit.beginTransaction()
                    fragmentTransaction.replace(R.id.container, addProductMarketStall())
                    fragmentTransaction.commit()
                    toolbar.title = "Añadir producto"
                    hideSoftKeyboard()

                    true
                }
                R.id.itemMarketStall -> {
                    val model: ItemViewModel by viewModels()
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
                R.id.itemAddSalesPerson -> {
                    val model: ItemViewModel by viewModels()
                    model.setMarketStall(marketStallPerson)
                    viewBinding.drawer.closeDrawer(GravityCompat.START)
                    fragmentManagerInit = getSupportFragmentManager()
                    fragmentTransaction = fragmentManagerInit.beginTransaction()
                    fragmentTransaction.replace(R.id.container, AddSalesPersonsFragment())
                    fragmentTransaction.commit()
                    toolbar.title = "Añadir trabajador"
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
    fun result(){
        GlobalScope.launch{
            runBlocking{
                val marketStalsl = async(start = CoroutineStart.LAZY){databaseManager.getProducts("${marketStallPerson.identification}")}
                marketStalsl.start()
                imp(marketStalsl.await()!!)
            }
        }
    }
    fun imp(string: MutableList<Product>){
        Log.d("aiuda", "Vamos a comprobar:  ${string}")
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
    fun setMarketStall(marketStallInFrag: MarketStall){
        marketStallPerson = marketStallInFrag
    }

    fun toastInContext(text: String){
        var container: FrameLayout = findViewById(R.id.container)
        Toast.makeText(container.context, "${text}", Toast.LENGTH_LONG).show()
    }

    fun bringOrders(){
        var orders: MutableList<Order> = mutableListOf<Order>()
        var order: Order = Order()
        var QuantityPerProduct: MutableList<QuantityPerProduct> = mutableListOf<QuantityPerProduct>()
        var product: Product = Product()
        var quantityPerProduct: QuantityPerProduct = QuantityPerProduct()

        database = FirebaseDatabase.getInstance().getReference("MarketStall")
        val query = database.child(marketStallPerson.identification!!)

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    for(ds in dataSnapshot.child("orders").children){
                        for(dsq in ds.child("quantityPerProducts").children){
                            product= Product(dsq.child("product").child("nameProduct").getValue(String::class.java),
                                dsq.child("product").child("priceProduct").getValue(Double::class.java),
                                dsq.child("product").child("imageProduct").getValue(String::class.java),
                                dsq.child("product").child("salesUnitProduct").getValue(String::class.java))
                            quantityPerProduct= QuantityPerProduct(
                                dsq.child("quantity").getValue(Int::class.java),
                                product
                            )
                            QuantityPerProduct.add(quantityPerProduct)
                        }
                        order= Order(
                            ds.child("clientName").getValue(String::class.java),
                            QuantityPerProduct,
                            ds.child("deliveryType").getValue(String::class.java),
                            ds.child("clientAddress").getValue(String::class.java),
                            ds.child("clientCellphone").getValue(Long::class.java).toString(),
                            ds.child("clientEmail").getValue(String::class.java)
                        )
                        orders.add(order)
                    }
                    createExcel(orders)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("", databaseError.getMessage()) //Don't ignore errors!
            }
        }
        query.addValueEventListener(valueEventListener)
    }
    fun createExcel(orders: MutableList<Order>){
        var filePath: File = File(context?.getExternalFilesDir(null), "Orders.xls")
        try{
            var hssfWorkbook: HSSFWorkbook = HSSFWorkbook()
            var hssfSheet: HSSFSheet = hssfWorkbook.createSheet()
            var productText: String = ""

            var hssfRow: HSSFRow;
            var hssfCell0: HSSFCell;
            var hssfCell1: HSSFCell;
            var hssfCell2: HSSFCell;
            var hssfCell3: HSSFCell;
            var hssfCell4: HSSFCell;
            var hssfCell5: HSSFCell;

            var hssfRowIntrod: HSSFRow = hssfSheet.createRow(0)
            var clientName: HSSFCell = hssfRowIntrod.createCell(0)
            clientName.setCellValue("Nombre de cliente")
            var quantity: HSSFCell = hssfRowIntrod.createCell(1)
            quantity.setCellValue("Productos")
            var deliveryType: HSSFCell = hssfRowIntrod.createCell(2)
            deliveryType.setCellValue("Tipo de entrega")
            var clientAddress: HSSFCell = hssfRowIntrod.createCell(3)
            clientAddress.setCellValue("Dirección")
            var clientCellphone: HSSFCell = hssfRowIntrod.createCell(4)
            clientCellphone.setCellValue("Teléfono")
            var clientEmail: HSSFCell = hssfRowIntrod.createCell(5)
            clientEmail.setCellValue("Correo electrónico")

            for(i in 1..orders.size){
                hssfRow = hssfSheet.createRow(i)
                hssfCell0 = hssfRow.createCell(0)
                hssfCell0.setCellValue(orders.get(i-1).clientName)
                for(j in 0..orders.get(i-1).quantityPerProducts!!.size-1){
                    if(j!=0){
                        productText+="\n"
                    }
                    productText+= "Producto: ${orders.get(i-1).quantityPerProducts!!.get(j).product!!.nameProduct} "+
                            "| Precio: ${orders.get(i-1).quantityPerProducts!!.get(j).product!!.priceProduct} "+
                            "| Cantidad ${orders.get(i-1).quantityPerProducts!!.get(j).quantity} ${orders.get(i-1).quantityPerProducts!!.get(j).product!!.salesUnitProduct} "

                }
                hssfCell1 = hssfRow.createCell(1)
                hssfCell1.setCellValue(productText)
                hssfCell2 = hssfRow.createCell(2)
                hssfCell2.setCellValue(orders.get(i-1).deliveryType)
                hssfCell3 = hssfRow.createCell(3)
                hssfCell3.setCellValue(orders.get(i-1).clientAddress)
                hssfCell4 = hssfRow.createCell(4)
                hssfCell4.setCellValue(orders.get(i-1).clientCellphone)
                hssfCell5 = hssfRow.createCell(5)
                hssfCell5.setCellValue(orders.get(i-1).clientEmail)
            }

            if(!filePath.exists()){
                filePath.createNewFile()
            }
            var fileOutputStream: FileOutputStream = FileOutputStream(filePath)
            hssfWorkbook.write(fileOutputStream)
            if(fileOutputStream!=null){
                fileOutputStream.flush()
                fileOutputStream.close()
            }
            Thread {
                try {
                    SendEmailService().send("${context?.getExternalFilesDir(null)}/Orders.xls", marketStallPerson.email)
                    runOnUiThread(Runnable() {
                        run() {
                            var container: FrameLayout = findViewById(R.id.container)
                            Toast.makeText(container.context, "¡Se ha enviado los pedidos al correo!", Toast.LENGTH_LONG).show()
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }.start()
        }catch(e: Exception){
            Log.d(null,"$e")
        }
    }

}

