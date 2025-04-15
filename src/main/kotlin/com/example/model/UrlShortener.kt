package com.example.model

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import kotlinx.serialization.Contextual


@Serializable
data class UrlShortener(
    @BsonId @Contextual val id: ObjectId? = null,
    val originalUrl: String,
    val shortCode: String
)