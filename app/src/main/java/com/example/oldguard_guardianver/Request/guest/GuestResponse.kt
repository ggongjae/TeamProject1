package com.example.oldguard_guardianver.Request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

//ActivityElderlyManager에서 사용하는 전체
data class GuestResponse(
    @SerializedName("id") val id: Long, //id
    @SerializedName("guestName") val guestName: String, //어르신 이름
    @SerializedName("contacts")  val contacts : List<Contact>, //보호자 이름, 전화번호
    @SerializedName("messageTime") val messageTime: Long,
    @SerializedName("callTime") val callTime: Long,
    @SerializedName("emergencyTime") val emergencyTime: Long,
    @SerializedName("sleepStartTime") val sleepStartTime: String,
    @SerializedName("sleepEndTime") val sleepEndTime: String
): Serializable

data class Contact(
    @SerializedName("id") val id : Long,
    @SerializedName("contact") val contact: String,
    @SerializedName("name") val name :String
): Serializable
