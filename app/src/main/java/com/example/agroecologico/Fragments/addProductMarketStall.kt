package com.example.agroecologico.Fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.agroecologico.Models.MarketStall
import androidx.lifecycle.Observer
import com.example.agroecologico.*
import com.example.agroecologico.Models.Product
import com.example.agroecologico.R
import com.example.agroecologico.databinding.FragmentAddProductMarketStallBinding
import com.example.agroecologico.databinding.FragmentMarketStallPersonBinding
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.util.*

class addProductMarketStall : Fragment() {
    private var _binding: FragmentAddProductMarketStallBinding? = null
    private val binding get() = _binding!!
    private lateinit var spinner : Spinner
    private lateinit var database: DatabaseReference
    private val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1555
    private lateinit var photoProduct: String
    lateinit var marketStallPersonInFrag: MarketStall
    private lateinit var databaseMarket: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().getReference("Unit")
        databaseMarket = FirebaseDatabase.getInstance().getReference("MarketStall")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddProductMarketStallBinding.inflate(inflater, container, false)
        val view = binding.root
        spinner = binding.spinnerUnitSaler

        val model: ItemViewModel by activityViewModels()
        // Method to bring the marketStall from the activity
        model.getMarketStall().observe(this, Observer<MarketStall>{ marketStall ->
            Log.d("aiuda", "Entré en el viewModel: ")
            marketStallPersonInFrag = marketStall
        })

        val adapter = activity?.let {
            ArrayAdapter<String>(
                it,
                android.R.layout.simple_spinner_dropdown_item,
                getUnitSaler()
            )
        }
        spinner.adapter = adapter
        binding.ivImageProduct.setOnClickListener{
            val intentImp = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            this.startActivityForResult(intentImp, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
        }
        binding.btnAddProduct.setOnClickListener{
            sendPhotoStorage()
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                val image:Bundle? = data?.extras
                val productImage: Bitmap? = image?.getParcelable<Bitmap>("data")
                binding.ivImageProduct.setImageBitmap(productImage)

            }
        }
    }

    fun sendPhotoStorage(){
        var storageRef = Firebase.storage.reference
        var imagesRef: StorageReference = storageRef.child("productsPhotos/product${UUID.randomUUID().toString()}")
        imagesRef.name == imagesRef.name
        binding.ivImageProduct.isDrawingCacheEnabled = true
        binding.ivImageProduct.buildDrawingCache()
        val bitmap = (binding.ivImageProduct.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = imagesRef.putBytes(data)
        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    Log.d("aiuda", "$it")
                    throw it
                }
            }
            imagesRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                photoProduct = downloadUri.toString()
                addProductInMarketStall()
                Log.d("aiuda", "El link es: $downloadUri")
            } else {

            }
        }
    }
    private fun addProductInMarketStall(){
        var name = binding.etNameProduct.text.toString()
        var price = binding.etPriceProduct.text.toString().toDouble()
        var unit = binding.spinnerUnitSaler.selectedItem.toString()
        var product: Product = Product(name, price, photoProduct, unit)

        databaseMarket.child("${marketStallPersonInFrag.identification}").child("products").child("${UUID.randomUUID().toString()}")
            .setValue(product).addOnSuccessListener {
                clear()
                Toast.makeText(activity!!.applicationContext,"El puesto de venta se ha añadido exitosamente", Toast.LENGTH_SHORT).show()
                //(activity as MenuActivitySalesPerson).toastManual("holaaa")
            }.addOnFailureListener{
                Toast.makeText(this@addProductMarketStall.requireActivity(),"El producto no pudo ser añadido", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clear() {
        binding.etNameProduct.text.clear()
        binding.etPriceProduct.text.clear()
        binding.spinnerUnitSaler.setSelection(0)
        binding.ivImageProduct.setImageResource(android.R.drawable.ic_menu_gallery)
    }

    private fun getUnitSaler(): MutableList<String>{
        var list: MutableList<String> = mutableListOf<String>()
        list.add(0, "Seleccione una unidad")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(ds in dataSnapshot.children){
                    list.add(ds.getValue(String::class.java)!!)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        database.addValueEventListener(postListener)
        return list
    }

}