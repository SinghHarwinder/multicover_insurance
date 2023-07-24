package com.example.multicoverinsurance.model

import kotlinx.serialization.Serializable

@Serializable
data class Claims ( val name : String, val reason : String, val date:  String, val status : String): java.io.Serializable