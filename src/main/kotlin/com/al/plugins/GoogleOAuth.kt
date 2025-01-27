package com.al.plugins

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory

object GoogleOAuth {
    private const val CLIENT_ID = "528000822377-uutrvsurskgdaj69d15od14n7smppska.apps.googleusercontent.com" // Your Google OAuth Client ID

    private val verifier: GoogleIdTokenVerifier = GoogleIdTokenVerifier.Builder(
        GoogleNetHttpTransport.newTrustedTransport(),
        JacksonFactory.getDefaultInstance()
    )
        .setAudience(listOf(CLIENT_ID))
        .setIssuer("https://accounts.google.com")
        .build()

    fun verifyIdToken(idToken: String): GoogleIdToken? {
        return verifier.verify(idToken)
    }
}