package com.example.oldguard_guardianver.Request.guest

import com.google.gson.annotations.SerializedName
import java.time.LocalTime

//어르신 수면시간(수면시간, 기상시간)
data class ClockRequest(
    @SerializedName("sleepStartTime") val startTime: String?,
    @SerializedName("id")val id: Long,
    @SerializedName("sleepEndTime") val endTime: String?
)
