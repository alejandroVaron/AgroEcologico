package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding:ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        auth = Firebase.auth
        viewBinding.btnSignUp.setOnClickListener {

            startActivity(Intent(this, signUpActivity::class.java))
        }
        viewBinding.btnSignIn.setOnClickListener{
            val tfNEmail = viewBinding.tfName.text.toString()
            val tfNPassword = viewBinding.tfPassword.text.toString()

            when{
                tfNEmail.isEmpty() || tfNPassword.isEmpty() -> {
                    Toast.makeText(baseContext, "correo o contraseña incorrecta.", Toast.LENGTH_SHORT).show()
                } else -> {
                    signIn(tfNEmail, tfNPassword)
                }
            }


        }
    }

    private fun signIn (email: String, password : String ){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "signInWithEmail:success")
                    val user = auth.currentUser
                    reload()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun reload(){
        val intent = Intent(this, pruebaActivity::class.java)
        this.startActivity(intent)
    }
}