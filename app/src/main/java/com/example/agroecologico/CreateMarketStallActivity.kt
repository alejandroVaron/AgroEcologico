package com.example.agroecologico

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.agroecologico.Models.MarketStall
import com.example.agroecologico.Models.Product
import com.example.agroecologico.databinding.ActivityCreateMarketStallBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateMarketStallActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityCreateMarketStallBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCreateMarketStallBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        database = FirebaseDatabase.getInstance().getReference("MarketStall")
        viewBinding.btnCreateMarketStall.setOnClickListener{
            validate()
        }
    }

    private fun validate(){

        val marketStallName = viewBinding.etMarketStallName.text.toString()
        val cellphone = viewBinding.etCellphoneMarketStall.text.toString()
        val email = viewBinding.etEmailMarketStall.text.toString()
        val password = viewBinding.etPasswordMarketStall.text.toString()
        val identification = viewBinding.etUsetIdentification.text.toString()

        val marketStall = MarketStall(marketStallName, email, password, identification, cellphone, mutableListOf<Product>(),"", "", "")

        database.child(identification).setValue(marketStall).addOnSuccessListener {

            clear()
            Toast.makeText(baseContext,"El puesto de venta se ha creado exitosamente", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MenuActivityAdmin::class.java))

        }.addOnFailureListener{
            Toast.makeText(baseContext,"El puesto de venta no pudo ser creado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clear(){
        viewBinding.etCellphoneMarketStall.text.clear()
        viewBinding.etEmailMarketStall.text.clear()
        viewBinding.etPasswordMarketStall.text.clear()
        viewBinding.etUsetIdentification.text.clear()
        viewBinding.etMarketStallName.text.clear()
    }


}