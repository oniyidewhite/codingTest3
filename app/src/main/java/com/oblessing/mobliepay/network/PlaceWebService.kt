package com.oblessing.mobliepay.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface PlaceWebService {
    @GET("place")
    @Headers(value = ["Content-Type: application/json", "User-Agent: MobilePay/0.0.1 (oniyideinc@gmail.com)"])
    suspend fun search(
        @Query("query") query: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("fmt") fmt: String = "json",
    ): PlaceListEntity?
}