package com.megWorld.universal.ui.fragement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.megWorld.universal.R
import com.megWorld.universal.databinding.FragmentCartBinding

class CartFragment : Fragment(R.layout.fragment_cart) {
    private lateinit var binding: FragmentCartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentCartBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val textView: TextView = binding.textNotification
        textView.text = "Menu"
        return root
    }


}