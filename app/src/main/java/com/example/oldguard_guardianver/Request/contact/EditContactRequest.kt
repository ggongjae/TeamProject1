package com.example.oldguard_guardianver.Request.contact

import com.google.gson.annotations.SerializedName

data class EditContactRequest (
    @SerializedName("contact") val contact: String?,
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String?
)