package com.example.agroecologico

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.agroecologico.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
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

            startActivity(Intent(this, signUpActivity::class.java))
        }

        viewBinding.btnSignInGoogle.setOnClickListener{
            Log.d(TAG, "ENTRÉ!!!! AL BOTÓN")
            Log.d(TAG, "onCreate: begin Google SignIn")
            //configure the google sign in
            val webApplicationClientId = "395831132438-6ktluduhgkh1ibflg3sb7unb7lvq4fff.apps.googleusercontent.com"
            val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webApplicationClientId)
                .requestEmail() // we need email from google account
                .build()
            googleClient = GoogleSignIn.getClient(this, googleSignInOptions)
            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent, RC_SIGN_IN)
        }

    }
    /*
    "client_id": "395831132438-6ktluduhgkh1ibflg3sb7unb7lvq4fff.apps.googleusercontent.com",
    * */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                Log.d(TAG, "FirebaseAuthWithGoggle: ${task.getResult(ApiException::class.java)}")
                val account = task.getResult(ApiException::class.java)
                Log.d(TAG, "FirebaseAuthWithGoggle: "+ account)
                firebaseAuthWithGoogle(account)
            }catch(e: ApiException){
                Log.w(TAG, "Google sign in failed", e)
                Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        Log.d(TAG, "Entré al metodo firsebaseauthwithgoogle")
        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                Log.d(TAG, "firebaseAuthWitchGoogleAccount: LoggedIn")
                val firebaseUser = auth.currentUser
                val uid = firebaseUser!!.uid!!
                Log.d(TAG, "firebaseAuthWitchGoogleAccount: uid: $uid")
                startActivity(Intent(this, MainActivity::class.java))
            }.addOnFailureListener{ e ->
                Log.d(TAG, "firebaseAuthWitchGoogleAccount: Loggin failed due to ${e.message}")
                Toast.makeText(this, "Loggin failed due to ${e.message}", Toast.LENGTH_SHORT).show()
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
/*
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload();
        }
    }

 */
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