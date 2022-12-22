package com.example.oldguard_guardianver.Request.guest

import com.google.gson.annotations.SerializedName
import java.io.Serializable

//이름 받기
data class GuestListResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String
): Serializable
