package com.jesusmar.covid19njstats.models

data class Auth0Token (
    val access_token :String,
    val expires_in:Int,
    val token_type: String
)

