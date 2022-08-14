package com.hectorfortuna.marvelproject.util

import com.hectorfortuna.marvelproject.BuildConfig
import java.math.BigInteger
import java.security.MessageDigest
import java.sql.Timestamp

fun hash(ts: String): String {
    val input = "${ts}${privateKey()}${apiKey()}"
    val md = MessageDigest.getInstance("md5")
    return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
}

fun baseUrl() = BuildConfig.BASE_URL

fun ts() = Timestamp(System.currentTimeMillis()).time.toString()

fun apiKey() = BuildConfig.API_KEY

fun privateKey() = BuildConfig.PRIVATE_KEY
