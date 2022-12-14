package com.megWorld.universal.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.megWorld.universal.ui.activities.LoginActivity
import com.megWorld.universal.ui.activities.RegisterActivity
import com.megWorld.universal.ui.activities.UserProfileActivity
import com.megWorld.universal.utils.Constants
import com.megWorld.universal.entities.User
import com.megWorld.universal.ui.activities.MainActivity
import com.megWorld.universal.ui.fragement.ProfileFragment

class FirestoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User){

        mFirestore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener {e ->
                activity.hideProgressBar()
                Log.e(activity.javaClass.simpleName,
                "Error while registering User.",
                    e
                    )

            }

    }

    private fun  getCurrentUserID(): String {
       val currentUser = FirebaseAuth.getInstance().currentUser
       var currentUserID= ""
       if (currentUser!= null){
            currentUserID = currentUser.uid
       }
       return currentUserID
   }


    fun getUserDetails(activity: Activity){
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName,document.toString())
                val user = document.toObject(User::class.java)!!
                val sharedPreference: SharedPreferences = activity.getSharedPreferences(
                    Constants.MEGWORLD_PREFERENCES,
                    Context.MODE_PRIVATE
                )

                val editor: SharedPreferences.Editor = sharedPreference.edit()
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    user.username
                )
                editor.putString(
                    Constants.LOGGED_IN_IMAGE,
                    user.image
                )
                editor.apply()
                when(activity){
                    is LoginActivity -> {
                        activity.userLoggedInService(user)
                    }
//                        is MainActivity ->{
//                            ProfileFragment().getProfileInfo(user) as MainActivity
//                        }

                }
            }
            .addOnFailureListener { e ->
                when(activity){
                    is LoginActivity -> {
                        activity.hideProgressBar()
                    }
//                    is MainActivity -> {
//                        activity.hideProgressBar()
//                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error While getting the User Details",
                    e
                )
            }
    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String,Any>){
        mFirestore.collection(Constants.USERS)
                .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when (activity){
                    is UserProfileActivity ->{
                        activity.userProfileUpdateSuccess()
                    }
                }

            }
            .addOnFailureListener {e ->
                when (activity){
                    is UserProfileActivity ->{
                        activity.hideProgressBar()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error While Updating the User Details.",
                    e
                )
            }
    }

    fun uploadImageToCloudStorage(activity: Activity,imageFileURI:Uri?){

        val sRef:StorageReference = FirebaseStorage.getInstance().reference.child(
            Constants.USER_PROFILE_IMAGE+System.currentTimeMillis()+"."
        +Constants.getFileExtension(
                activity,imageFileURI
        )
        )
        sRef.putFile(imageFileURI!!).addOnSuccessListener { taskSnapShot ->
            Log.e("Firebase Image Url",
                taskSnapShot.metadata!!.reference!!.downloadUrl.toString()
            )
            taskSnapShot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->
            Log.e("Downloadable Image URL", uri.toString())
                when (activity){
                    is UserProfileActivity ->{
                        activity.imageUploadSuccess(uri.toString())
                    }
                }
            }
        }
            .addOnFailureListener{exception->
                when (activity){
                    is UserProfileActivity ->{
                        activity.hideProgressBar()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }
}