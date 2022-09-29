package com.megWorld.universal.ui.fragement

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.jama.carouselview.CarouselView
import com.jama.carouselview.enums.IndicatorAnimationType
import com.jama.carouselview.enums.OffsetType
import com.megWorld.universal.R
import com.megWorld.universal.adapter.CategoryAdapter
import com.megWorld.universal.adapter.ProductAdapter
import com.megWorld.universal.databinding.FragmentHomeBinding
import com.megWorld.universal.entities.DataObject
import com.megWorld.universal.entities.ProductObject
import com.megWorld.universal.network.ApiServices
import com.megWorld.universal.network.RetrofitInstance
import com.megWorld.universal.utils.Constants
import com.megWorld.universal.utils.GlideLoader
import com.megWorld.universal.utils.ProductConstants
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private lateinit var recyclerView: RecyclerView
private lateinit var mCatList: ArrayList<DataObject>
private lateinit var catAdapter: CategoryAdapter
private lateinit var gridLayoutManager: GridLayoutManager
private lateinit var mProductList: ArrayList<ProductObject>
private lateinit var myAdapter: RecyclerView.Adapter<*>



class HomeFragment : Fragment() {

    private  lateinit var binding: FragmentHomeBinding
    private lateinit var productAdapter: ProductAdapter


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =   FragmentHomeBinding.inflate(layoutInflater)


        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = activity?.getSharedPreferences(Constants.MEGWORLD_PREFERENCES, Context.MODE_PRIVATE)
        val username = sharedPreferences?.getString(Constants.LOGGED_IN_USERNAME, "")
        binding.profileName.text = username
        val imageHome = sharedPreferences?.getString(Constants.LOGGED_IN_IMAGE, "")!!
        GlideLoader(requireActivity()).loadUserPicture(imageHome,userPic)


        //getAllData()
        categoryInit()
        //productInit()
        carouselView()
    }



@SuppressLint("UseCompatLoadingForDrawables")
private fun carouselView(){
    val images = arrayListOf(R.drawable.banner_image1,R.drawable.banner_image2)
    val carouselView = activity?.findViewById(com.megWorld.universal.R.id.carouselView) as CarouselView

    carouselView.apply {
        size = images.size
        resource = R.layout.image_carousel_item
        autoPlay = true
        indicatorAnimationType = IndicatorAnimationType.THIN_WORM
        carouselOffset = OffsetType.CENTER
        setCarouselViewListener { view, position ->
            // Example here is setting up a full image carousel
            val imageView = view.findViewById<ImageView>(R.id.imageView)
            imageView.setImageDrawable(resources.getDrawable(images[position]))
        }

        show()
    }
}

//    private fun productLoad(){
//        RetrofitInstance.getApiInterface()?.getProductDetails()?.enqueue(object :
//        Callback<ProductObject>{
//            override fun onResponse(call: Call<ProductObject>, response: Response<ProductObject>) {
//                if (response.isSuccessful){
//                    mProductList = response.body()?.acf as ArrayList<ProductObject>
//                }
//            }
//
//            override fun onFailure(call: Call<ProductObject>, t: Throwable) {
//                TODO("Not yet implemented")
//            }
//
//        })
//    }

    private fun productInit() {
        gridLayoutManager =  GridLayoutManager(activity, 2)
        recyclerView = activity?.findViewById<RecyclerView>(R.id.productList)?.apply {
            recyclerView.layoutManager = gridLayoutManager
            recyclerView.setHasFixedSize(true)
            myAdapter = ProductAdapter(mProductList,context)
            recyclerView.adapter = myAdapter
        }!!

//        recyclerView.layoutManager = gridLayoutManager
//        val snapHelper: SnapHelper = LinearSnapHelper()
//        snapHelper.attachToRecyclerView(recyclerView)
//        mProductList = ArrayList()
//        addProductDataToList()
//
//        productAdapter = activity?.let { ProductAdapter(mProductList, it) }!!
//        recyclerView.adapter = productAdapter
    }

    private fun addProductDataToList() {

//        mProductList.add(ProductObject(R.drawable.tomato,"Fresh Tomato","1kg","₹28"))
//        mProductList.add(ProductObject(R.drawable.cashew,"Cashew","1kg","₹180"))
//        mProductList.add(ProductObject(R.drawable.bananas,"Banana","1kg","₹52"))
//        mProductList.add(ProductObject(R.drawable.cauliflower,"Cauliflower","1kg","₹30"))
//        mProductList.add(ProductObject(R.drawable.green_chillies,"Green Chilli","500gm","₹15"))
//        mProductList.add(ProductObject(R.drawable.onion,"Onion","1kg","₹48"))
//        mProductList.add(ProductObject(R.drawable.pineapple,"Pineapple","Each","₹60"))
//        mProductList.add(ProductObject(R.drawable.orange,"Orange","1kg","₹83"))

    }

    private fun addDataToList() {
        mCatList.add(DataObject(R.drawable.fruits,"Fruits"))
        mCatList.add(DataObject(R.drawable.beverages,"Beverages"))
        mCatList.add(DataObject(R.drawable.meat,"Meat"))
        mCatList.add(DataObject(R.drawable.spices,"Spices"))
        mCatList.add(DataObject(R.drawable.vegetables,"Vegetables"))
        mCatList.add(DataObject(R.drawable.dairy,"Dairy"))
    }

    private fun categoryInit() {
        recyclerView = activity?.findViewById(R.id.catList) as RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        mCatList = ArrayList()
        addDataToList()
        catAdapter = CategoryAdapter(mCatList)
        recyclerView.adapter = catAdapter
    }
//    private fun getAllData(){
//        val product = ApiServices.n
//        Api.retrofitService.getAllData().enqueue(object : Callback<List<ProductObject>>{
//            override fun onResponse(
//                call: Call<List<ProductObject>>,
//                response: Response<List<ProductObject>>
//            ) {
//                if (response.isSuccessful){
////                   recyclerView = activity?.findViewById<RecyclerView>(R.id.productList)?.apply {
////                       gridLayoutManager =  GridLayoutManager(activity, 2)
////                       recyclerView.layoutManager = gridLayoutManager
////                       myAdapter = response.body()?.let { ProductAdapter(mProductList,it) }!!
////                       recyclerView.adapter = myAdapter
//                    productInit()
//                    Toast.makeText(activity,"Working",Toast.LENGTH_LONG).show()
//
//                }
//                }
//
//            override fun onFailure(call: Call<List<ProductObject>>, t: Throwable) {
//                t.printStackTrace()
//                Toast.makeText(activity,"Not Working",Toast.LENGTH_LONG).show()
//            }
//
//
//        })
//    }

}