package com.example.agroecologico

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.agroecologico.databinding.ActivityAddWeightUnitBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class addWeightUnit : AppCompatActivity() {
    private lateinit var viewBinding: ActivityAddWeightUnitBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityAddWeightUnitBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.btnAddUnit.setOnClickListener{
            val unitValue = viewBinding.tfUnit.text.toString()

            database = Firebase.database.getReference("Unit")

            database.setValue(unitValue).addOnSuccessListener {
                viewBinding.tfUnit.text.clear()

                Toast.makeText(this,"La unidad de venta se almacenó exitosamente", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener{

                Toast.makeText(this,"La unidad de venta no pudo ser almacenada", Toast.LENGTH_SHORT).show()

            }
        }
    }
}