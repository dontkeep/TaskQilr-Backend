package com.al.model.fromres

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val id: String,
    val name: String,
    @SerialName("given_name") val givenName: String,
    @SerialName("family_name") val familyName: String,
    val email: String,
    val picture: String,
    val locale: String = "",
    @SerialName("verified_email") val verifiedEmail: Boolean?
)
