package com.example.oldguard_guardianver.Request.contact

import com.google.gson.annotations.SerializedName

data class DeleteContactRequest(
    @SerializedName("contactId") val contactId: Long,
    @SerializedName("id") val id: Long
)
