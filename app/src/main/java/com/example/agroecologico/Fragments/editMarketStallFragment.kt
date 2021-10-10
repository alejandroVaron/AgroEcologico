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
import android.widget.ImageView
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.drawToBitmap
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.agroecologico.ItemViewModel
import com.example.agroecologico.MenuActivitySalesPerson
import com.example.agroecologico.Models.MarketStall
import com.example.agroecologico.Models.Product
import com.example.agroecologico.R
import com.example.agroecologico.databinding.FragmentEditMarketStallBinding
import com.example.agroecologico.databinding.FragmentMarketStallPersonBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*

class editMarketStallFragment : Fragment() {
    private var _binding: FragmentEditMarketStallBinding? = null
    private val binding get() = _binding!!
    lateinit var marketStallPersonInFrag: MarketStall
    private val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1555
    private lateinit var photoMarketStall: String
    private lateinit var lastImage: Bitmap
    private var boolImage: Boolean = false
    private lateinit var databaseMarket: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseMarket = FirebaseDatabase.getInstance().getReference("MarketStall")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditMarketStallBinding.inflate(inflater, container, false)
        val view = binding.root

        val model: ItemViewModel by activityViewModels()
        // Method to bring the marketStall from the activity
        model.getMarketStall().observe(this, Observer<MarketStall>{ marketStall ->
            marketStallPersonInFrag = marketStall
            downloadImage()
        })

        binding.ivImageMarketStall.setOnClickListener{
            val intentImp = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            this.startActivityForResult(intentImp, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
        }

        binding.btnChangeMarketStall.setOnClickListener{
            if(boolImage){
                sendPhotoStorage()
            }else{
                photoMarketStall = marketStallPersonInFrag.terrainPhoto!!
                editMarketStall()
            }
        }

        return view
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                val image:Bundle? = data?.extras
                val productImage: Bitmap? = image?.getParcelable<Bitmap>("data")
                binding.ivImageMarketStall.setImageBitmap(productImage)
                boolImage = true
            }
        }
    }

    private fun editMarketStall(){
        var name: String = binding.etChangeName.text.toString()
        var identification: String = marketStallPersonInFrag.identification!!

        val marketStallP: MarketStall = MarketStall(name,
            marketStallPersonInFrag.email,
            marketStallPersonInFrag.password,
            marketStallPersonInFrag.identification,
            marketStallPersonInFrag.cellphone,
            marketStallPersonInFrag.products,
            marketStallPersonInFrag.workers,
            marketStallPersonInFrag.orders,
            photoMarketStall,
            marketStallPersonInFrag.salesPersonName,
            marketStallPersonInFrag.salesPersonPhoto)

        Log.d("aiuda", "1- Estos son los productos: ${marketStallP.products}")

        databaseMarket.child(identification).child("nameMarketStall").setValue(marketStallP.nameMarketStall).addOnSuccessListener {
            databaseMarket.child(identification).child("terrainPhoto").setValue(marketStallP.terrainPhoto).addOnSuccessListener {
                binding.etChangeName.text.clear()
                sendMarketStall(marketStallP.nameMarketStall!!, marketStallP.terrainPhoto!!)

            }.addOnFailureListener{
                Snackbar.make(binding.ivImageMarketStall, "No se ha podido editar el puesto de venta", Snackbar.LENGTH_SHORT).show()
            }
            sendMarketStall(marketStallP.nameMarketStall!!, marketStallP.terrainPhoto!!)
        }.addOnFailureListener{
            Snackbar.make(binding.ivImageMarketStall, "No se ha podido editar el puesto de venta", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun sendMarketStall(name: String, photoMarketStall: String){
        Log.d("aiuda","2- Estos son los productos: ${marketStallPersonInFrag.products}")
        marketStallPersonInFrag.nameMarketStall = name
        marketStallPersonInFrag.terrainPhoto = photoMarketStall
        (activity as MenuActivitySalesPerson).setMarketStall(marketStallPersonInFrag)
    }

    fun sendPhotoStorage(){
        var storageRef = Firebase.storage.reference
        var imagesRef: StorageReference = storageRef.child("terrainPhotos/product${UUID.randomUUID().toString()}")
        imagesRef.name == imagesRef.name
        binding.ivImageMarketStall.isDrawingCacheEnabled = true
        binding.ivImageMarketStall.buildDrawingCache()
        val bitmap = (binding.ivImageMarketStall.drawable as BitmapDrawable).bitmap
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
                photoMarketStall = downloadUri.toString()
                editMarketStall()
                Log.d("aiuda", "El link es: $downloadUri")
            } else {

            }
        }
    }


    private fun downloadImage(){
        var photo: String = marketStallPersonInFrag.terrainPhoto!!
        try{
            Glide.with(this)
                .load(photo)
                .centerCrop()
                .into(binding.ivImageMarketStall)

        }catch (err: IOException){
            Log.d("aiuda", "$err")
        }
    }

}