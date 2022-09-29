package com.megWorld.universal.network

import com.megWorld.universal.entities.ProductObject
import com.megWorld.universal.utils.ProductConstants.API_KEY
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//private const val BASE_URL = "http://megworld.in/"


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