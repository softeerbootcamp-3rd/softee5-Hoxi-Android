package com.example.hoxi.service

import android.provider.ContactsContract.RawContacts.Data

data class RecentDestination (
    val statusCode: Int,
    val data: Names,
    val message: String
)