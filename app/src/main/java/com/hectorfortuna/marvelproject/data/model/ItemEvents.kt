package com.hectorfortuna.marvelproject.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ItemEvents(
    @SerializedName("resourceURI")
    val resourceURI: String,
    @SerializedName("name")
    val name: String
): Serializable
