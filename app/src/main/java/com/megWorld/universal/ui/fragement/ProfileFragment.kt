package com.megWorld.universal.ui.fragement


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.megWorld.universal.R
import com.megWorld.universal.databinding.FragmentProfileBinding
import com.megWorld.universal.entities.User
import com.megWorld.universal.ui.activities.LoginActivity
import com.megWorld.universal.ui.activities.UserProfileActivity
import com.megWorld.universal.utils.Constants
import com.megWorld.universal.utils.GlideLoader
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : BaseFragment<FragmentProfileBinding>(),View.OnClickListener {

private lateinit var mUserDetails: User
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding!!.root
    }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // TODO: logout function
        binding?.logout?.setOnClickListener(this@ProfileFragment)
        binding?.icForwardAccount5?.setOnClickListener(this@ProfileFragment)
        binding?.icLogout?.setOnClickListener(this@ProfileFragment)
       binding?.editToProfile?.setOnClickListener(this@ProfileFragment)

    }

    override fun onClick(v: View?) {
        if(v!=null){
            when(v.id){
                R.id.logout ->{
                    logoutFun()
                }
                R.id.ic_forwardAccount5 -> {
                    logoutFun()
                }
                R.id.ic_logout -> {
                    logoutFun()
                }
                R.id.editToProfile -> {
                        val intent = Intent(activity,UserProfileActivity::class.java)
                        intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
                        startActivity(intent)
                }
            }
        }
    }

    private fun logoutFun() {
        FirebaseAuth.getInstance().signOut()
        activity?.let{
            val intent = Intent(it, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            it.startActivity(intent)
            it.finish()
        }
    }


    private fun getUserDetails(){
        showProgressBar()
        userFunctional(this)
    }

    override fun onResume() {

        super.onResume()
        getUserDetails()
    }
    override fun getViewBinding(view: View): FragmentProfileBinding {
        TODO("Not yet implemented")
    }
private fun userFunctional(fragment: Fragment){
    hideProgressBar()
    val rootRef = FirebaseFirestore.getInstance().collection(Constants.USERS)
    val userRef = rootRef.document(getCurrentUserIDs()).get()
        userRef.addOnSuccessListener { document ->
            Log.i(activity?.javaClass?.simpleName,document.toString())
            val user = document.toObject(User::class.java)!!
            when(fragment){
                is ProfileFragment -> {
                    fragment.userDetailsSuccess(user)
                 }
           }
        }
        .addOnFailureListener{
            when(fragment){
                is ProfileFragment ->{
                    fragment.hideProgressBar()
                }
            }
        }
    }
        @SuppressLint("SetTextI18n")
        private fun userDetailsSuccess(user: User) {
            mUserDetails = user
            hideProgressBar()
            GlideLoader(requireActivity()).loadUserPicture(user.image, profileSettingImage)
            profileUsername.text = user.username
            emailSettings.text = user.email
            phoneSettings.text = "${user.mobile}"
            addressSettings.text = "${user.address} ${user.city} ${user.state} ${user.pinCode}"
    }
    private fun  getCurrentUserIDs(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID= ""
        if (currentUser!= null){
            currentUserID = currentUser.uid
        }
        return currentUserID
    }
}

