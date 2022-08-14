package com.hectorfortuna.marvelproject.data.model.comics

data class Creators(
    val available: Int,
    val collectionURI: String,
    val items: List<ItemX>,
    val returned: Int
)