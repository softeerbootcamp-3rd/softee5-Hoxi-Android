package com.example.hoxi.service

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("/api/recent/1")
    fun fetchRecentDestination(): Call<RecentDestination>

    @POST("/api/create/call")
    fun makeCall(@Body callRequestDto : CallInfo) : Call<CallResponseDto>

    @GET("/api/call/driver-position/3")
    fun getDriverInfo(): Response<DriverInfoResponse>
}