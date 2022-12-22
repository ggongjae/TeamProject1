package com.example.oldguard_guardianver.Request.contact

import com.google.gson.annotations.SerializedName
import java.io.Serializable

//보호자 정보(성함, 전화번호 data)
data class AddInfoRequest(
    @SerializedName("name") val name: String?,
    @SerializedName("id") val id: Long,
    @SerializedName("contact") val number: String?
): Serializable
