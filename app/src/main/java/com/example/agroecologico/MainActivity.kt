package com.example.agroecologico

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.example.agroecologico.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.json.JSONObject
import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager

import androidx.core.content.ContextCompat




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
        AuthUI.IdpConfig.GoogleBuilder().build())
    //AuthUI.IdpConfig.FacebookBuilder().build()

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

    private fun validateSalesPerson(email: String?){
        database = FirebaseDatabase.getInstance().getReference("MarketStall")
        Log.d("dd", "El email es: $email")
        val query = database.orderByChild("email").equalTo(email)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    for (ds in dataSnapshot.children){
                        Log.d("dd", "Entré al validateSalesPerson")
                            var menuActivity = Intent(this@MainActivity, MenuActivitySalesPerson::class.java)
                            menuActivity.putExtra("cellphone", ds.child("cellphone").getValue(String::class.java))
                            menuActivity.putExtra("email", ds.child("email").getValue(String::class.java))
                            menuActivity.putExtra("identification", ds.child("identification").getValue(String::class.java))
                            menuActivity.putExtra("nameMarketStall", ds.child("nameMarketStall").getValue(String::class.java))
                            menuActivity.putExtra("password", ds.child("password").getValue(String::class.java))
                            menuActivity.putExtra("salesPersonName", ds.child("salesPersonName").getValue(String::class.java))
                            menuActivity.putExtra("salesPersonPhoto", ds.child("salesPersonPhoto").getValue(String::class.java))
                            menuActivity.putExtra("terrainPhoto", ds.child("terrainPhoto").getValue(String::class.java))
                            startActivity(menuActivity)
                            Toast.makeText(baseContext, "¡ Bienvenido ${ds.child("salesPersonName").getValue(String::class.java)}!",
                                Toast.LENGTH_SHORT).show()
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

    private fun validateTypeUser(email: String?): Int?{
        database = FirebaseDatabase.getInstance().getReference("User")
        var type : Int? = 3
        var dsEmail : String? = ""
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(ds in dataSnapshot.children){
                    dsEmail = ds.child("email").getValue(String::class.java)
                    if(email == dsEmail){
                        type = ds.child("typeUser").getValue(Int::class.java)
                        Log.d("aiuda", "El tipo de usuario del auth es: $type")
                    }
                }
                startActivityUser(type)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        database.addValueEventListener(postListener)
        return type
    }
    private fun startActivityUser(typeUser : Int?){
        if(typeUser == 0){
            startActivity(Intent(this, MenuActivityAdmin::class.java))
        }else if(typeUser == 1){
            validateSalesPerson(auth.currentUser?.email)
        }else if(typeUser == 2){

        }
    }

    private fun signIn (email: String, password : String ){
        auth.signOut()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "signInWithEmail:success")
                    val user = auth.currentUser
                    validateTypeUser(auth.currentUser?.email)
                    //reload()
                } else {
                    Toast.makeText(this, "El usuario o la contraseña es incorrecto", Toast.LENGTH_SHORT).show()
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