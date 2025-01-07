package com.al.client

import com.al.model.fromres.UserInfo
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class GoogleAuthService(private val httpClient: HttpClient) {
    suspend fun fetchUserInfo(accessToken: String): UserInfo {
        return httpClient.get("https://www.googleapis.com/oauth2/v2/userinfo") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $accessToken")
            }
        }.body()
    }
}