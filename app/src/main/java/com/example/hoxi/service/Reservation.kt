package com.example.hoxi.service

import com.google.gson.annotations.SerializedName

data class Reservation (
    val name: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("hotelNumber")
    val hotelNumber: String
)