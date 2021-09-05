package com.example.agroecologico

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.agroecologico.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleClient: GoogleSignInClient

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
                    Toast.makeText(baseContext, "correo o contraseña incorrecta.", Toast.LENGTH_SHORT).show()
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

    //Google  395831132438-cepm768f39k4kbt26qijeet6lf1njsd9.apps.googleusercontent.com

    //Propia
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
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
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


/*
    override fun onDataChange(dataSnapshot: DataSnapshot) {
        // This method is called once with the initial value and again
        // whenever data at this location is updated.
        val value = dataSnapshot.getValue<String>()
        Log.d("TAG", "Value is: $value")
    }

    override fun onCancelled(error: DatabaseError) {
        // Failed to read value
        Log.w("TAG", "Failed to read value.", error.toException())
    }
*/

}