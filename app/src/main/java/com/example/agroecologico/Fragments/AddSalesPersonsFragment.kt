package com.example.agroecologico.Fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.agroecologico.ItemViewModel
import com.example.agroecologico.MenuActivitySalesPerson
import com.example.agroecologico.Models.MarketStall
import com.example.agroecologico.Models.SalesPerson
import com.example.agroecologico.R
import com.example.agroecologico.databinding.FragmentAddSalesPersonBinding
import com.example.agroecologico.databinding.FragmentAddSalesPersonsBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.util.*


class AddSalesPersonsFragment : Fragment() {
    private var _binding: FragmentAddSalesPersonsBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var databaseMarket: DatabaseReference
    lateinit var marketStallPersonInFrag: MarketStall
    private val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1555
    private val RESULT_LOAD_IMG = 1
    private lateinit var photoSalesPerson: String
    private  var imageChange: Boolean = false
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
        _binding = FragmentAddSalesPersonsBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.ivSalesPersonPhoto.setOnClickListener{
            var alertDialog: AlertDialog

            alertDialog = AlertDialog.Builder(context).create()

            alertDialog.setButton(
                AlertDialog.BUTTON_POSITIVE, "Cámara",
                DialogInterface.OnClickListener{ dialog, id ->
                    val intentImp:Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    this.startActivityForResult(intentImp, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
                    dialog.cancel()
                })

            alertDialog.setButton(
                AlertDialog.BUTTON_NEUTRAL, "Galeria",
                DialogInterface.OnClickListener{ dialog, id ->
                    val intentGal:Intent = Intent(Intent.ACTION_PICK)
                    intentGal.setType("image/*")
                    startActivityForResult(intentGal, RESULT_LOAD_IMG)
                    dialog.cancel()
                })

            alertDialog.show()
            var layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.agroecologicoColor))
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(resources.getColor(R.color.agroecologicoColor))
            layoutParams.weight = 8f
            layoutParams.width= resources.getDimensionPixelSize(R.dimen.alertdialog_button_width)
            layoutParams.gravity = Gravity.CENTER
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).layoutParams = layoutParams
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).layoutParams = (layoutParams)
        }
        binding.btnAddSalesPerson.setOnClickListener{
           if(imageChange){
               sendPhotoStorage()
           }else{
               photoSalesPerson = "https://firebasestorage.googleapis.com/v0/b/agroecologico-6bd81.appspot.com/o/salesPersonPhotos%2Fdefault_worker.png?alt=media&token=b2a8fb09-303f-4daa-bb82-5e4e5973ff11"
               addSalesPersonInMarketStall()
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
                binding.ivSalesPersonPhoto.setImageBitmap(productImage)
                Log.d("aiuda", "Se guardo la foto!!!")




                imageChange = true
            }
        }
        if(requestCode== RESULT_LOAD_IMG){
            if(resultCode == Activity.RESULT_OK){
                val image: Uri? = data!!.data
                binding.ivSalesPersonPhoto.setImageURI(image)
                imageChange = true
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
                addSalesPersonInMarketStall()
                Log.d("aiuda", "El link es: $downloadUri")
            } else {

            }
        }
    }

    private fun addSalesPersonInMarketStall(){
        var name = binding.etSalesPersonName.text.toString()
        var salesPerson: SalesPerson = SalesPerson(photoSalesPerson, name)

        databaseMarket.child(marketStallPersonInFrag.identification!!).child("workers").child("${UUID.randomUUID().toString()}")
            .setValue(salesPerson).addOnSuccessListener {
                (activity as MenuActivitySalesPerson).hideSoftKeyboard()
                clear()
                Snackbar.make(binding.ivSalesPersonPhoto, "El vendedor ha sido añadido exitosamente", Snackbar.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Snackbar.make(binding.ivSalesPersonPhoto,"El vendedor no pudo ser añadido", Snackbar.LENGTH_SHORT).show()
            }
    }

    private fun clear() {
        binding.etSalesPersonName.text.clear()
        binding.ivSalesPersonPhoto.setImageResource(R.drawable.ic_editprofile)
    }

}