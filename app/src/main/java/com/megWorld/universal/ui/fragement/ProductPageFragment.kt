package com.megWorld.universal.ui.fragement

import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.megWorld.universal.R
import com.megWorld.universal.databinding.FragmentProductPageBinding
import kotlinx.android.synthetic.main.fragment_product_page.*


class ProductPageFragment : BaseFragment<FragmentProductPageBinding>(),View.OnClickListener {

    private var num = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductPageBinding.inflate(inflater, container, false)


        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                productDescription.justificationMode = JUSTIFICATION_MODE_INTER_WORD
            }
        }

        productCarousel()


        incrementBtn.setOnClickListener(this@ProductPageFragment)
        decrementBtn.setOnClickListener(this@ProductPageFragment)

    }
    private fun productCarousel(){
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.tomato, ScaleTypes.CENTER_INSIDE))
        imageList.add(SlideModel(R.drawable.bananas,ScaleTypes.CENTER_INSIDE))
        val imageSlider = activity?.findViewById(R.id.productCarousel) as ImageSlider
        imageSlider.setImageList(imageList,ScaleTypes.CENTER_INSIDE)
        imageSlider.stopSliding()
    }


    override fun getViewBinding(view: View): FragmentProductPageBinding {
        TODO("Not yet implemented")
    }

    override fun onClick(v: View?) {
    if (v!=null){
        when(v.id){
            R.id.incrementBtn ->{
                num++
                quantityInput.setText(num.toString())
            }
            R.id.decrementBtn -> {
                num--
                quantityInput.setText(num.toString())
            }
        }
    }
    }
    private fun setupActionBar(){
        val toolBar = activity?.findViewById(R.id.toolBarReg) as Toolbar
        if(activity is AppCompatActivity){
            (activity as AppCompatActivity).setSupportActionBar(toolBar)
        }
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.back)
        
    }

}