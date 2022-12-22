package com.example.oldguard_guardianver.Request.guest

import com.google.gson.annotations.SerializedName

//어르신 생활패턴(문자주는 시간)
data class TimeLimitRequest(
    @SerializedName("id") val id: Long,
    @SerializedName("messageTime") val messageTime: Long,
    @SerializedName("callTime") val callTime: Long,
    @SerializedName("emergencyTime") val emergencyTime: Long
)
