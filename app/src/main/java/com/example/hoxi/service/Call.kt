package com.example.hoxi.service

import com.google.gson.annotations.SerializedName

data class Call(
    val source: String,
    val destination: String,
    val distance: Double,
    @SerializedName("arrivalTime")
    val arrivalTime: String,
    @SerializedName("carrierNum")
    val carrierNum: Int,
    val requirement: String,
    @SerializedName("deliveryFee")
    val deliveryFee: Int,
    val isCargo: Boolean
)