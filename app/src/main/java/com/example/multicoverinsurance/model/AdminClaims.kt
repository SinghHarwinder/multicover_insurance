package com.example.multicoverinsurance.model

import kotlinx.serialization.Serializable

@Serializable
data class AdminClaims ( val uid : String,val email : String,val desc : String,val image : String,val name : String, val reason : String, val date:  String, val status : String): java.io.Serializable