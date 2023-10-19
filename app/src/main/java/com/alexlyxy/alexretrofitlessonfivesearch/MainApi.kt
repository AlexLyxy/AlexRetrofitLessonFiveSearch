package com.alexlyxy.alexretrofitlessonfivesearch

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

    interface MainApi {
        @GET("products/{id}")
        suspend fun getProductById(@Path("id") id: Int): Product

        @POST("auth/login")
        suspend fun auth(@Body authRequest: AuthRequest): User

        @GET("products")
        suspend fun getAllProducts(): Products

        @GET("products/search")
        suspend fun getProductsByName(@Query("q") name: String): Products
}