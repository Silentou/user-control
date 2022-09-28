package com.megWorld.universal.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.megWorld.universal.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.megWorld.universal.databinding.ActivityLoginBinding
import com.megWorld.universal.entities.User
import com.megWorld.universal.firestore.FirestoreClass
import com.megWorld.universal.utils.Constants


@Suppress("DEPRECATION")
class  LoginActivity : BaseActivity(),View.OnClickListener{
    private companion object {
        private const val CONST_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        auth = Firebase.auth
        // TODO: button links
        binding.fgtPassword.setOnClickListener(this@LoginActivity)
        binding.loginBtn.setOnClickListener(this@LoginActivity)
        binding.registerLink.setOnClickListener(this@LoginActivity)

        // TODO: Google Sign in Link
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.clientId))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = FirebaseAuth.getInstance()
        checkUser()

        binding.googleButton.setOnClickListener {
            Log.d(TAG, "onCreate: Begin google Sign In")
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, CONST_SIGN_IN)
        }

        val w = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    private fun checkUser() {
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CONST_SIGN_IN) {
            Log.d(TAG, "onActivityResult: Google Sign In Intent")
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = accountTask.getResult(ApiException::class.java)
                firebaseAuthWithGoogleAccount(account)

            } catch (e: Exception) {
                Log.d(TAG, "onActivityResult:${e.message}")
                Toast.makeText(
                    this@LoginActivity,
                    "onActivityResult:${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        Log.d(TAG, "firebaseAuthWithGoogleAccount: Begin Firebase Auth With Google Account")
        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Logged In")
                val firebaseUser = auth.currentUser
                val uid = firebaseUser!!.uid
                val email = firebaseUser.email

                Log.d(TAG, "firebaseAuthWithGoogleAccount: uid: $uid")
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Email:$email")

                if (authResult.additionalUserInfo!!.isNewUser) {
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: Account Created... \n$email")
                    Toast.makeText(
                        this@LoginActivity,
                        "Account Created... \n$email",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: Existing User... \n$email")
                    Toast.makeText(
                        this@LoginActivity,
                        "Logged In... \n$email",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Log In Failed Due to ${e.message}")
                Toast.makeText(
                    this@LoginActivity,
                    "Log In Failed Due to ${e.message}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
    }

    private fun loginRegisterUser() {
        if (validateLoginDetails()){
            showProgressBar()
            val lEmail= binding.lEmailInputTextField.text.toString().trim{it <= ' '}
            val lPassword = binding.lPassInputTextField.text.toString().trim{it <= ' '}

            auth.signInWithEmailAndPassword(lEmail,lPassword)
                .addOnCompleteListener(this) { task ->
                    hideProgressBar()
                    if (task.isSuccessful) {
                        val verification = auth.currentUser?.isEmailVerified
                        if (verification == true){
                            Toast.makeText(this,"Welcome",Toast.LENGTH_LONG).show()
                            FirestoreClass().getUserDetails(this@LoginActivity)
                        }else{
                            showErrorMessage("Email is not registered or verified ",true)
                        }

                    } else {

                        showErrorMessage( "Authentication failed ${task.exception}.",
                            true)
                    }
                }
                .addOnFailureListener {
                    showErrorMessage("Login Failed ${it.localizedMessage}", true)
                }
        }
    }

    private fun validateLoginDetails(): Boolean {
        return when{
            TextUtils.isEmpty(binding.lEmailInputTextField.text.toString().trim{it <= ' '})  -> {
                showErrorMessage("Please Enter Your Email",true)
                false
            }
            TextUtils.isEmpty(binding.lPassInputTextField.text.toString().trim{it <= ' '}) -> {
                showErrorMessage("Please Enter Your Valid Password",true)
                false
            }
            else -> {
                true
            }
        }
    }

    fun userLoggedInService(user: User) {

        hideProgressBar()
        if (user.profileCompleted == 0){
            val intent =Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS,user)
            startActivity(intent)
        }
        else{
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
        finish()
    }

    override fun onClick(view: View?) {
        if (view!=null){
            when(view.id){
                R.id.fgtPassword ->{
                    val intent = Intent(this@LoginActivity, VerifyActivity::class.java)
                    startActivity(intent)
                }
                R.id.loginBtn ->{
                    loginRegisterUser()
                }
                R.id.registerLink ->{
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }


}
