package com.megWorld.universal.network

import com.google.android.gms.common.api.internal.ApiKey
import com.megWorld.universal.entities.ProductObject
import com.megWorld.universal.utils.ProductConstants.API_KEY
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//private const val BASE_URL = "http://megworld.in/"
 //private val gson = Gson().fromJson(ProductList,ProductObject::class.java)
// private val retrofit = Retrofit
//     .Builder()
//     .baseUrl(BASE_URL)
//     .addConverterFactory(GsonConverterFactory.create())
//     .build()

interface ApiServices {
    @GET("wp-json/wp/v2/shop/")
     fun getProductDetails(
//        @Query("id")
//        id:String,
//        @Query("page")
//        pageNumber: Int = 1,
        @Query("apiKay")
        apiKey: String = API_KEY
    ):Call<ProductObject>
     fun searchProductDetails(
        @Query("search query")
        searchQuery: String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKay")
        apiKey: String = API_KEY
    ):Call<ProductObject>

    //fun getAllData(): Call<List<ProductObject>>
}
//object Api {
//        val  retrofitService: ApiServices by lazy { retrofit.create(ApiServices::class.java) }
//}