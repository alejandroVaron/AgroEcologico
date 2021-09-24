package com.example.agroecologico.Fragments

import android.R
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.agroecologico.ItemViewModel
import com.example.agroecologico.Models.MarketStall
import com.example.agroecologico.Models.SalesPerson
import com.example.agroecologico.databinding.FragmentAddSalesPersonBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.util.*

class AddSalesPersonFragment() : Fragment() {
    private var _binding: FragmentAddSalesPersonBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var databaseMarket: DatabaseReference
    lateinit var marketStallPersonInFrag: MarketStall
    private val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1555
    private lateinit var photoSalesPerson: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().getReference("Unit")
        databaseMarket = FirebaseDatabase.getInstance().getReference("MarketStall")
        val model: ItemViewModel by activityViewModels()
        // Method to bring the marketStall from the activity
        model.getMarketStall().observe(this, Observer<MarketStall>{ marketStall ->
            marketStallPersonInFrag = marketStall
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddSalesPersonBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.ivSalesPersonPhoto.setOnClickListener{
            val intentImp = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            this.startActivityForResult(intentImp, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
        }
        binding.btnAddSalesPerson.setOnClickListener{
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
                binding.ivSalesPersonPhoto.setImageBitmap(productImage)

            }
        }
    }

    fun sendPhotoStorage(){
        var storageRef = Firebase.storage.reference
        var imagesRef: StorageReference = storageRef.child("salesPersonPhotos/salesPerson${UUID.randomUUID().toString()}")
        imagesRef.name == imagesRef.name
        binding.ivSalesPersonPhoto.isDrawingCacheEnabled = true
        binding.ivSalesPersonPhoto.buildDrawingCache()
        val bitmap = (binding.ivSalesPersonPhoto.drawable as BitmapDrawable).bitmap
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
                photoSalesPerson = downloadUri.toString()
                addProductInMarketStall()
                Log.d("aiuda", "El link es: $downloadUri")
            } else {

            }
        }
    }

    private fun addProductInMarketStall(){
        var name = binding.etSalesPersonName.text.toString()
        var salesPerson: SalesPerson = SalesPerson(name, photoSalesPerson)

        databaseMarket.child(marketStallPersonInFrag.identification!!).child("workers").child("${UUID.randomUUID().toString()}")
            .setValue(salesPerson).addOnSuccessListener {
                clear()
                Snackbar.make(binding.ivSalesPersonPhoto, "El vendedor ha sido añadido exitosamente", Snackbar.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(this@AddSalesPersonFragment.requireActivity(),"El vendedor no pudo ser añadido", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clear() {
        binding.etSalesPersonName.text.clear()
        binding.ivSalesPersonPhoto.setImageResource(android.R.drawable.ic_menu_gallery)
    }
}