package com.megWorld.universal.ui.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.megWorld.universal.R
import com.megWorld.universal.databinding.ActivityUserProfileBinding
import com.megWorld.universal.entities.User
import com.megWorld.universal.firestore.FirestoreClass
import com.megWorld.universal.utils.Constants
import com.megWorld.universal.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException


class UserProfileActivity : BaseActivity(),View.OnClickListener {
    private lateinit var binding: ActivityUserProfileBinding
    private  var mSelectedImageFileUri: Uri? = null
    private lateinit var mUserDetails: User
    private var mUserProfileImageURL: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        @Suppress("DEPRECATION")
        supportActionBar?.hide()
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = resources.getColor(R.color.white)

        binding.editBackButton.setOnClickListener(this@UserProfileActivity)
        binding.profileEditBtn.setOnClickListener(this@UserProfileActivity)
        binding.userSaveButton.setOnClickListener(this@UserProfileActivity)

        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }
        binding.usernameInputTextField.setText(mUserDetails.username)
        binding.emailInputTextField.setText(mUserDetails.email)
        binding.phoneInputTextField.setText(mUserDetails.mobile.toString())
        binding.addressInputTextField.setText(mUserDetails.address)
        binding.cityInputTextField.setText(mUserDetails.city)
        binding.stateInputTextField.setText(mUserDetails.state)
        binding.pinCodeInputTextField.setText(mUserDetails.pinCode.toString())

        if (mUserDetails.profileCompleted == 0){

            binding.textView.text = resources.getString(R.string.edit_profile)
            binding.usernameInputTextField.isEnabled = false
            binding.emailInputTextField.isEnabled = false


        }else{
            //setupActionBar()
            binding.textView.text = resources.getString(R.string.title_profile_completed)

            GlideLoader(this@UserProfileActivity).loadUserPicture(mUserDetails.image,profile_image)

            if (mUserDetails.mobile!= 0L){
                binding.phoneInputTextField.setText(mUserDetails.mobile.toString())
            }

        }



        binding.termsCondition.movementMethod = LinkMovementMethod.getInstance()

    }



    private fun validateUser(): Boolean {
        return when{
//            TextUtils.isEmpty(binding.usernameInputTextField.text.toString()) -> {
//                showErrorMessage("Please Enter your Username",true)
//                false
//            }
//            TextUtils.isEmpty(binding.emailInputTextField.text.toString().trim{it <= ' '})
//                    || !Patterns.EMAIL_ADDRESS.matcher(binding.emailInputTextField.text.toString()).matches()
//            -> {
//                showErrorMessage("Please Enter your Valid Email",true)
//                false
//            }
            TextUtils.isEmpty(binding.phoneInputTextField.text.toString().trim{it <= ' '})
                    || binding.phoneInputTextField.length() >= 13


            -> {
                showErrorMessage("Please Enter your Valid Mobile Number",true)
                false
            }
            TextUtils.isEmpty(binding.addressInputTextField.text.toString()) -> {
                showErrorMessage("Please Enter your Address",true)
                false
            }
            TextUtils.isEmpty(binding.cityInputTextField.text.toString().trim{it <= ' '}) -> {
                showErrorMessage("Please Enter your City / Town",true)
                false
            }
            TextUtils.isEmpty(binding.stateInputTextField.text.toString()) -> {
                showErrorMessage("Please Enter your State",true)
                false
            }
            TextUtils.isEmpty(binding.pinCodeInputTextField.text.toString().trim{it <= ' ' } )->{
                showErrorMessage("Please Enter your Pin Code",true)
                false
            }
            !binding.checkTerms.isChecked ->{
                showErrorMessage("Please Agree The Conditions",true)
                false
            }

            else -> {
                true
            }
        }
    }

    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.profileEditBtn -> {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED
                        ){
                        //showErrorMessage("You already have storage permission",false)
                        Constants.showImageChooser(this)
                    }
                    else{
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                                Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }
                R.id.userSaveButton ->{

                    if (validateUser()){

                        showProgressBar()
                        if (mSelectedImageFileUri != null)
                        FirestoreClass().uploadImageToCloudStorage(this,mSelectedImageFileUri)
                        else{
                                updateUserProfileDetails()
                        }
                    }
                }
                R.id.editBackButton ->{
                    onBackPressed()
                }
            }

        }
    }
//    private fun setupActionBar(){
//        val toolBar = findViewById<View>(R.id.toolBarReg) as Toolbar
//        setSupportActionBar(toolBar)
//        val actionBar = supportActionBar
//        if (actionBar!=null) {
//            actionBar.setHomeAsUpIndicator(R.drawable.back)
//            actionBar.setDisplayHomeAsUpEnabled(true)
//        }
//        toolBar.setNavigationOnClickListener{onBackPressed()}
//    }

    private fun updateUserProfileDetails(){

        val userHashMap = HashMap<String,Any>()
        val mobileNumber = binding.phoneInputTextField.text.toString().trim{it <= ' '}
        val address = binding.addressInputTextField.text.toString()
        val city = binding.cityInputTextField.text.toString().trim{it <= ' '}
        val state = binding.stateInputTextField.text.toString()
        val pinCode = binding.pinCodeInputTextField.text.toString().trim { it <= ' ' }

        if (mUserProfileImageURL.isNotEmpty()){
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
        }
        if (mobileNumber.isNotEmpty() && mobileNumber != mUserDetails.mobile.toString()){
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }
        if (address.isNotEmpty() && address != mUserDetails.address){
            userHashMap[Constants.ADDRESS] = address
        }
        if (city.isNotEmpty() && city != mUserDetails.city){
            userHashMap[Constants.CITY] = city
        }
        if (state.isNotEmpty() && state != mUserDetails.state){
            userHashMap[Constants.STATE] = state
        }
        if (pinCode.isNotEmpty() && pinCode != mUserDetails.pinCode.toString()){
            userHashMap[Constants.PIN_CODE] = pinCode.toLong()
        }

        userHashMap[Constants.PROFILE_COMPLETED] = 1

        FirestoreClass().updateUserProfileData(this,userHashMap)
    }

    fun userProfileUpdateSuccess(){
        hideProgressBar()
        Toast.makeText(
            this@UserProfileActivity,
            "Your Profile Updated SuccessFully",
            Toast.LENGTH_SHORT
        ).show()
        startActivity(Intent(this@UserProfileActivity,MainActivity::class.java))
        finish()
    }

    fun imageUploadSuccess(imageURL:String){
        mUserProfileImageURL = imageURL
        updateUserProfileDetails()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE){
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //showErrorMessage("Storage Permission Granted", false)
                    Constants.showImageChooser(this)
                }else{
                    Toast.makeText(
                        this,
                        resources.getString(R.string.Storage_permission_denied),
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE){
                if (data != null){
                    try {
                        mSelectedImageFileUri = data.data!!
                        //binding.profileImage.setImageURI(Uri.parse(selectedImageFileUri.toString()))
                        GlideLoader(this).loadUserPicture(mSelectedImageFileUri!!,profile_image)
                    }catch (e:IOException){
                         e.printStackTrace()
                        Toast.makeText(
                            this@UserProfileActivity,
                            "Image Selection failed",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }else if (requestCode == Activity.RESULT_CANCELED){
                Log.e("Request Cancelled","Image selection cancelled")
        }
    }
}






