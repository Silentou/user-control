package com.megWorld.universal.ui.fragement

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.SnackbarContentLayout
import com.megWorld.universal.R
import com.megWorld.universal.databinding.FragmentBaseBinding
import com.megWorld.universal.databinding.FragmentCartBinding
import com.megWorld.universal.databinding.FragmentProfileBinding
import com.megWorld.universal.utils.Constants
import com.megWorld.universal.utils.GlideLoader
import kotlinx.android.synthetic.main.fragment_profile.*
import me.itangqi.waveloadingview.WaveLoadingView


abstract class BaseFragment<VB: ViewBinding> : Fragment() {

    private lateinit var dialog: Dialog
    var binding: VB? = null

    fun showErrorMessage( message: String, errorMessage: Boolean){
        val snackBar = Snackbar.make(activity?.findViewById(android.R.id.content) as SnackbarContentLayout, message, Snackbar.LENGTH_SHORT)
        val  snackBarView = snackBar.view

        if (errorMessage){
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.error_color
                )
            )
        }
        else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.successColor
                )
            )
        }
        snackBar.show()
    }
    fun showProgressBar(){

        dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.dialog_wait)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }
    fun hideProgressBar() {
        dialog.dismiss()
    }

abstract fun getViewBinding(view: View): VB


}