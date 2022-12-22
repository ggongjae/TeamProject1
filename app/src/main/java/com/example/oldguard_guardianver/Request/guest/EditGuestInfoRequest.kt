package com.example.oldguard_guardianver.Request.guest

import com.google.gson.annotations.SerializedName

data class EditGuestInfoRequest(
    @SerializedName("callTime") val callTime: Long,
    @SerializedName("emergencyTime") val emergencyTime: Long,
    @SerializedName("id") val id: Long,
    @SerializedName("messageTime") val messageTime: Long,
    @SerializedName("sleepEndTime") val sleepEndTime: String?,
    @SerializedName("sleepStartTime") val sleepStartTime: String?
)
