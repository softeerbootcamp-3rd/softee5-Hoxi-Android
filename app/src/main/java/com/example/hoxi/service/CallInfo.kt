package com.example.hoxi.service

import com.google.gson.annotations.SerializedName

data class CallInfo(
    @SerializedName("user_id")
    val userId: Int,
    val call: CallData,
    val reservation: ReservationData
)

data class CallData(
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

data class ReservationData(
    val name: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("hotelNumber")
    val hotelNumber: String
)