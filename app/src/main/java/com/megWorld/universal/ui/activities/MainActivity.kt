package com.megWorld.universal.ui.activities



import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.*
import com.google.firebase.auth.FirebaseAuth
import com.megWorld.universal.R
import com.megWorld.universal.databinding.ActivityMainBinding
import com.megWorld.universal.entities.User
import com.megWorld.universal.firestore.FirestoreClass
import com.megWorld.universal.ui.fragement.*
import com.megWorld.universal.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*
import nl.joery.animatedbottombar.AnimatedBottomBar


@Suppress("DEPRECATION")
class MainActivity : BaseActivity(){
    lateinit var auth:FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private val homeFragment = HomeFragment()
    private val orderFragment = ProductPageFragment()
    private val profileFragment = ProfileFragment()
    private val cartFragment = CartFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }
        else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

//        val crashButton = Button(this)
//        addContentView(crashButton, ViewGroup.LayoutParams(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT))
//        crashButton.text = "Test Crash"
//        crashButton.setOnClickListener {
//            throw RuntimeException("Test Crash") // Force a crash
//        }

        // TODO: Bottom Navigation



        makeCurrentFragment(homeFragment)
            binding.bottomNavigation.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
                override fun onTabSelected(
                    lastIndex: Int,
                    lastTab: AnimatedBottomBar.Tab?,
                    newIndex: Int,
                    newTab: AnimatedBottomBar.Tab
                ) {

                    //redirecting fragments
                    when(newIndex){
                        0 -> makeCurrentFragment(homeFragment)
                        1 -> makeCurrentFragment(orderFragment)
                        2 -> makeCurrentFragment(cartFragment)
                        3 -> makeCurrentFragment(profileFragment)
                        else -> makeCurrentFragment(homeFragment)
                    }

                    Log.d("bottom_bar", "Selected index: $newIndex, title: ${newTab.title}")


                }
                override fun onTabReselected(index: Int, tab: AnimatedBottomBar.Tab) {
                    Log.d("bottom_bar", "Reselected index: $index, title: ${tab.title}")
                }
        })

        auth = FirebaseAuth.getInstance()
        checkUser()
        val currentUser = auth.currentUser
        if (currentUser == null){
           startActivity(Intent(this, MainActivity::class.java))
            finish()
        }


    }


    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container_fragment, fragment)
                .commit()
        }
    }

    private fun checkUser() {
        val firebaseUser = auth.currentUser
        if (firebaseUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }


    override fun onBackPressed() {
        doubleBackToExit()
    }



}