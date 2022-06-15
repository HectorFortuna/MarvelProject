package com.hectorfortuna.marvelproject.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Events(
    @SerializedName("available")
    val available: Int,
    @SerializedName("items")
    val items: List<ItemEvents>,
    @SerializedName("returned")
    val returned: Int
): Serializable
