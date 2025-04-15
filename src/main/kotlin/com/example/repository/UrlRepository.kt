package com.example.repository

import com.example.model.UrlShortener
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.bson.types.ObjectId

class UrlRepository(private val database: CoroutineDatabase) {
    private val collection = database.getCollection<UrlShortener>("url-shortener")

    suspend fun getAllUrls(): List<UrlShortener> = collection.find().toList()
    
    suspend fun getUrlById(id: String): UrlShortener? = 
        collection.findOne(UrlShortener::id eq ObjectId(id))
    
    suspend fun getUrlByShortCode(shortCode: String): UrlShortener? =
        collection.findOne(UrlShortener::shortCode eq shortCode)
    
    suspend fun createUrl(url: UrlShortener): UrlShortener? {
        collection.insertOne(url)
        return url
    }
    
    suspend fun updateUrl(id: String, url: UrlShortener): Boolean = 
        collection.replaceOne(UrlShortener::id eq ObjectId(id), url).wasAcknowledged()
    
    suspend fun deleteUrl(id: String): Boolean = 
        collection.deleteOne(UrlShortener::id eq ObjectId(id)).wasAcknowledged()
}