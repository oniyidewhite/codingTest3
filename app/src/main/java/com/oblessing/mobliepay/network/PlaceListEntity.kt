package com.oblessing.mobliepay.network

import com.google.gson.annotations.SerializedName

data class PlaceListEntity(
    @SerializedName("count")
    val count: Int,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("places")
    val places: List<PlaceEntity>
)

data class PlaceEntity(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("address")
    val address: String?,
    @SerializedName("type")
    val type: String,
    @SerializedName("coordinates")
    val coordinates: LatLng?,
    @SerializedName("life-span")
    val lifeSpan: LifeSpan?
)

data class LifeSpan(
    @SerializedName("begin")
    val begin: String
)

data class LatLng(
    @SerializedName("latitude")
    val lat: String,
    @SerializedName("longitude")
    val lng: String
)
