package com.example.oldguard_guardianver.Request.record

import com.google.gson.annotations.SerializedName

data class RecordRequest(
    @SerializedName("sort") val sort: String
)
