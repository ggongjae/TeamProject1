package com.example.oldguard_guardianver.Request.guest

import com.google.gson.annotations.SerializedName

//어르신 정보(로그인코드, 성함)
//일단 elder Manager화면에 이 데이터 클래스 썼어요
data class GuestLoginRequest(
    @SerializedName("code") val code: String?,
    @SerializedName("address") val address : String?,
    @SerializedName("name") val name: String?
)