package com.example.agroecologico

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.example.agroecologico.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleClient: GoogleSignInClient
    private lateinit var database: DatabaseReference


    //Constants
    private companion object{
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }
    // Choose authentication providers
    val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.PhoneBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        auth = Firebase.auth

        // OnClickListener of sign in button
        viewBinding.btnSignIn.setOnClickListener{
            val tfNEmail = viewBinding.tfName.text.toString()
            val tfNPassword = viewBinding.tfPassword.text.toString()
            when{
                tfNEmail.isEmpty() || tfNPassword.isEmpty() -> {
                    Toast.makeText(baseContext, "Complete los campos correctamente para iniciar sesión.", Toast.LENGTH_SHORT).show()
                } else -> {
                    signIn(tfNEmail, tfNPassword)
                }
            }
        }

        // OnClickListener of sign up button
        viewBinding.btnSignUp.setOnClickListener {

            startActivity(Intent(this, SignUpActivity::class.java))
        }

        viewBinding.btnSignInGoogle.setOnClickListener{
            Log.d(TAG, "ENTRÉ!!!! AL BOTÓN")
            Log.d(TAG, "onCreate: begin Google SignIn")
            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()
            signInLauncher.launch(signInIntent)

        }

    }
    private val signInLauncher = registerForActivityResult(

        FirebaseAuthUIActivityResultContract()
    ) { res ->
        Log.d(TAG, "Pasé al metodo registerForAcitivityResult")
        this.onSignInResult(res)
    }

    private fun validateSalesPerson(){
        database = FirebaseDatabase.getInstance().getReference("MarketStall")
        val email = viewBinding.tfName.text.toString()
        val password = viewBinding.tfPassword.text.toString()
        val query = database.orderByChild("email").equalTo(email)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    for (ds in dataSnapshot.children){
                        if(ds.child("password").getValue(String::class.java) == password){
                            Toast.makeText(baseContext, "¡ Bienvenido ${ds.child("salesPersonName").getValue(String::class.java)}!",
                                Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@MainActivity, MenuActivitySalesPerson::class.java))
                        }else{
                            Toast.makeText(baseContext, "El usuario o la contraseña es incorrecta",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(baseContext, "El usuario o la contraseña es incorrecta",
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, databaseError.getMessage()) //Don't ignore errors!
            }
        }
        query.addListenerForSingleValueEvent(valueEventListener)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        Log.d(TAG, "Pasé al metodo onSignInResult")
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            Log.d(TAG, "Se pudo hacer la conexión con google")
            Log.d(TAG, "Pasé al metodo onSignInResult")
            Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
            val user = FirebaseAuth.getInstance().currentUser
            startActivity(Intent(this, MenuActivityAdmin::class.java))
            // ...
        } else {
            Log.d(TAG, "No se pudo hacer la conexión con google")

        }
    }

    private fun signIn (email: String, password : String ){
        auth.signOut()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "signInWithEmail:success")
                    val user = auth.currentUser
                    startActivity(Intent(this, MenuActivityAdmin::class.java))
                    //reload()
                } else {
                    validateSalesPerson()
                }
            }
    }
    /*
    private fun reload(){
        val intent = Intent(this, MenuActivityAdmin::class.java)
        this.startActivity(intent)
    }
    */
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            //reload();
        }
    }

}