package com.example.oldguard_guardianver.Request

import com.google.gson.annotations.SerializedName

//임시 데이터 클래스. 삭제필요, ReceivedRecordFragment의 recyclerView에서 사용 예정
data class ReceivedRecordData(
    @SerializedName("name") val name: String,
    @SerializedName("time") val time: String,
    @SerializedName("type") val type: String
)
