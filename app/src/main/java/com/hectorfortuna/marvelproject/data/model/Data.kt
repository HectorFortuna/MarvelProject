package com.hectorfortuna.marvelproject.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Data(
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("limit")
    val limit: Long,
    @SerializedName("total")
    val total: Long,
    @SerializedName("count")
    val count: Long,
    @SerializedName("results")
    val results: List<Results>
): Serializable
