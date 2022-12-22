package com.example.oldguard_guardianver.Request.record

import com.google.gson.annotations.SerializedName

data class SendRecordResponse(
    @SerializedName("localDateTime") val localDateTime: String,
    @SerializedName("name") val name: String
)
