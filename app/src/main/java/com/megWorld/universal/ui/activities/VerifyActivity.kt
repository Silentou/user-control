package com.megWorld.universal.ui.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.megWorld.universal.R
import com.megWorld.universal.databinding.ActivityVerifyBinding


class VerifyActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private  lateinit var binding: ActivityVerifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        @Suppress("DEPRECATION")
        supportActionBar?.hide()
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = resources.getColor(R.color.white)

        setupActionBar()
        auth= FirebaseAuth.getInstance()

        binding.submitBtn.setOnClickListener{
            val fgtEmail: String  = binding.fgtEmailInputTextField.text.toString().trim{it <= ' '}
            if (fgtEmail.isEmpty()){
                showErrorMessage("Please Enter Your Email",true)
            }else{
                showProgressBar()
                auth.sendPasswordResetEmail(fgtEmail)
                    .addOnCompleteListener{task ->
                        hideProgressBar()
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@VerifyActivity,
                                "Email Sent Successfully to reset password",
                                Toast.LENGTH_LONG)
                                .show()
                            finish()
                        }else{
                            showErrorMessage(task.exception!!.message.toString(),true)
                        }
                    }
                    .addOnFailureListener{
                        showErrorMessage("Something not Working",false)
                    }
            }
        }


    }
    private fun setupActionBar(){
        val toolBar = findViewById<View>(R.id.toolBar) as Toolbar
        setSupportActionBar(toolBar)
        val actionBar = supportActionBar
        if (actionBar!=null) {
            actionBar.setHomeAsUpIndicator(R.drawable.back)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = null
        }
        toolBar.setNavigationOnClickListener{onBackPressed()}




    }

}