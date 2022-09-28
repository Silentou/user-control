package com.megWorld.universal.ui.activities

import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.megWorld.universal.databinding.ActivityRegisterBinding
import com.megWorld.universal.entities.User
import com.megWorld.universal.firestore.FirestoreClass


class RegisterActivity : BaseActivity() {
    private lateinit var binding:ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
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

        setupActionBar()

        binding.userRegisterButton.setOnClickListener{
            registerUser()
        }
    }


    private fun registerUser() {

        if (validateRegisterUser()){

            showProgressBar()

            val email: String  = binding.rEmailInputTextField.text.toString().trim{it <= ' '}
            val password: String  = binding.cPasswordInputTextField.text.toString().trim{it <= ' '}

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this) { task->
                    hideProgressBar()
                    if (task.isSuccessful) {
                        auth.currentUser?.sendEmailVerification()
                            ?.addOnSuccessListener {
                                val firebaseUser: FirebaseUser = task.result!!.user!!
                                val user = User(
                                    firebaseUser.uid,
                                    binding.usernameInputTextField.text.toString(),
                                    binding.rEmailInputTextField.text.toString().trim{it <= ' '}

                                )

                                FirestoreClass().registerUser(this,user)
                                Toast.makeText(this,"Please Verify Your Email", Toast.LENGTH_LONG).show()
                                finish()
                            }
                            ?.addOnFailureListener {
                                showErrorMessage("Error occurred $it", true)
                            }
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
//                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        showErrorMessage("Registration Failed", true )
//                        updateUI(null)
                    }

                }
                .addOnFailureListener {
                    showErrorMessage("error Occurred ${it.localizedMessage}", true)
                }
        }
    }

//    private fun updateUI(user: FirebaseUser?) {
//        if (user != null) {
//            binding.emailTextField.setText(getString("R.string.google_status_fmt", user.email))
//            mDetailTextView.setText(getString(com.megWorld.universal.R.string.firebase_status_fmt, user.uid))
//            findViewById<View>(com.megWorld.universal.R.id.sign_in_button).visibility = View.GONE
//            findViewById<View>(R.id.sign_out_and_disconnect).visibility = View.VISIBLE
//        } else {
//            mStatusTextView.setText(R.string.signed_out)
//            mDetailTextView.setText(null)
//            findViewById<View>(R.id.sign_in_button).visibility = View.VISIBLE
//            findViewById<View>(R.id.sign_out_and_disconnect).visibility = View.GONE
//        }
//    }

    private fun validateRegisterUser(): Boolean {
        return when{
            TextUtils.isEmpty(binding.usernameInputTextField.text.toString()) -> {
                showErrorMessage("Please Enter your Username",true)
                false
            }
            TextUtils.isEmpty(binding.rEmailInputTextField.text.toString().trim{it <= ' '})
                    || !Patterns.EMAIL_ADDRESS.matcher(binding.rEmailInputTextField.text.toString()).matches()
            -> {
                showErrorMessage("Please Enter your Valid Email Address",true)
                false
            }
            TextUtils.isEmpty(binding.passwordInputTextField.text.toString().trim{it <= ' '}) || binding.passwordInputTextField.length() <= 8
                    || !binding.passwordInputTextField.text.toString().matches(".*[A-Z].*".toRegex())
                    || !binding.passwordInputTextField.text.toString().matches(".*[0-9]*.".toRegex())
                    || !binding.passwordInputTextField.text.toString().matches(".*[!@#$%^&*()_+=-]*.".toRegex())
            -> {
                showErrorMessage("Password Should contain One Capital Letter, One Number and Special Character ",true)
                false
            }
            TextUtils.isEmpty(binding.cPasswordInputTextField.text.toString().trim{it <= ' '})  -> {
                showErrorMessage("Please Enter your Confirm Password",true)
                false
            }

            binding.passwordInputTextField.text.toString().trim{it <= ' '} != binding.cPasswordInputTextField.text.toString().trim{it <= ' '} ->{
                showErrorMessage("Password Mismatch",true)
                false
            }

            !binding.checkTermsRegister.isChecked ->{
                showErrorMessage("Please Agree The Terms & Conditions and Privacy Policy",true)
                false
            }

            else -> {
//                showErrorMessage("Your Details are Saved", false)
                true
            }
        }
    }


private fun setupActionBar(){
    val toolBar = findViewById<View>(com.megWorld.universal.R.id.toolBarReg) as Toolbar
    setSupportActionBar(toolBar)
    val actionBar = supportActionBar
  if (actionBar!=null) {
    actionBar.setHomeAsUpIndicator(com.megWorld.universal.R.drawable.back)
    actionBar.setDisplayHomeAsUpEnabled(true)
    }
    toolBar.setNavigationOnClickListener{onBackPressed()}
}

    fun userRegistrationSuccess(){
        hideProgressBar()
        Toast.makeText(this,"Registered Successfully",Toast.LENGTH_SHORT).show()
    }
}
