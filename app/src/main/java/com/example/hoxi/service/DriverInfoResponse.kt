package com.example.hoxi.service
data class Position(
    val x: Double,
    val y: Double
)

data class Info(
    val name: String,
    val phoneNumber: String,
    val carType: String,
    val carNumber: String,
    val image: String
)

data class Data(
    val position: Position,
    val info: Info
)

data class DriverInfoResponse(
    val statusCode: Int,
    val data: Data,
    val message: String
)