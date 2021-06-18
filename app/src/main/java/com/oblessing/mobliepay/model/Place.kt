package com.oblessing.mobliepay.model

data class Place(
    val id: String,
    val name: String,
    val address: String,
    val type: String,
    val latitude: String,
    val longitude: String,
    val expiresAt: Long
)