package com.example.repository

import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

class MongoDB(private val connectionString: String) {
    val client: CoroutineClient by lazy {
        KMongo.createClient(connectionString).coroutine
    }
    
    fun getDatabase(dbName: String): CoroutineDatabase {
        return client.getDatabase(dbName)
    }
    
    fun close() {
        client.close()
    }
}

// package com.example.repository

// import org.litote.kmongo.coroutine.CoroutineClient
// import org.litote.kmongo.coroutine.CoroutineDatabase
// import org.litote.kmongo.reactivestreams.KMongo

// class MongoDB(private val connectionString: String) {
//     val client: CoroutineClient by lazy {
//         KMongo.createClient(connectionString).coroutine
//     }

//     fun getDatabase(dbName: String): CoroutineDatabase {
//         return client.getDatabase(dbName)
//     }

//     fun close() {
//         client.close()
//     }
// }
