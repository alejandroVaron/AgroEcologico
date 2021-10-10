package com.example.agroecologico.Fragments

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.agroecologico.ItemViewModel
import com.example.agroecologico.MenuActivitySalesPerson
import com.example.agroecologico.Models.MarketStall
import com.example.agroecologico.SendEmailService
import com.example.agroecologico.databinding.FragmentMarketStallPersonBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.util.jar.Manifest

class marketStallPersonFragment : Fragment() {

    // En este objeto se encuentra el puesto de venta del trabajador
    lateinit var marketStallPersonInFrag: MarketStall

    private var _binding: FragmentMarketStallPersonBinding? = null

    private val binding get() = _binding!!

    private var firebaseSto: FirebaseStorage = FirebaseStorage.getInstance()

    private var filePath: File = File(context?.getExternalFilesDir(null), "Orders.xls")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE), PackageManager.PERMISSION_GRANTED )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMarketStallPersonBinding.inflate(inflater, container, false)
        val view = binding.root
        val model: ItemViewModel by activityViewModels()
        // Method to bring the marketStall from the activity
        model.getMarketStall().observe(this, Observer<MarketStall>{ marketStall ->
            Log.d("aiuda", "Entré en el viewModel: ")
            marketStallPersonInFrag = marketStall
            bringData()
            downloadImage()
        })

        binding.btnSendMail.setOnClickListener {

            Thread {
                try {
                    (activity as MenuActivitySalesPerson).bringOrders()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }.start()

        }

        return view
    }

    private fun bringData(){
        binding.tvMarketStallName.setText(marketStallPersonInFrag.nameMarketStall)
        binding.tvWorkerId.setText(marketStallPersonInFrag.identification)
        binding.tvCellphoneWorker.setText(marketStallPersonInFrag.cellphone)
        binding.tvEmailWorker.setText(marketStallPersonInFrag.email)
    }

    private fun downloadImage(){
        var photo: String = marketStallPersonInFrag.terrainPhoto!!
        try{
            Glide.with(this)
                .load(photo)
                .centerCrop()
                .into(binding.ivTerrainPhoto)
        }catch (err: IOException){
            Log.d("aiuda", "$err")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}