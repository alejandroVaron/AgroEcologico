package com.example.agroecologico

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.agroecologico.databinding.ActivityAddWeightUnitBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AddWeightUnit : AppCompatActivity() {
    private lateinit var viewBinding: ActivityAddWeightUnitBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityAddWeightUnitBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.btnAddUnit.setOnClickListener{
            val unitValue = viewBinding.tfUnit.text.toString()

            database = Firebase.database.getReference("Unit")

            database.push().setValue(unitValue).addOnSuccessListener {
                viewBinding.tfUnit.text.clear()

                Toast.makeText(baseContext,"La unidad de venta se almacenó exitosamente", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MenuActivityAdmin::class.java))

            }.addOnFailureListener{

                Toast.makeText(baseContext,"La unidad de venta no pudo ser almacenada", Toast.LENGTH_SHORT).show()

            }
        }
    }
}