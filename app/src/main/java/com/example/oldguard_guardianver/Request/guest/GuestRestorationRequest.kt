package com.example.oldguard_guardianver.Request.guest

import com.google.gson.annotations.SerializedName

data class GuestRestorationRequest(
    @SerializedName("id") val id: Long
)
