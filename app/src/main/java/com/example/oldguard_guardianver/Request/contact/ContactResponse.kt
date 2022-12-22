package com.example.oldguard_guardianver.Request.contact

import com.google.gson.annotations.SerializedName

data class ContactResponse(
    @SerializedName("name") val contact :String,
    @SerializedName("contactId") val contactId : Long,
    @SerializedName("contact") val name : String
)
