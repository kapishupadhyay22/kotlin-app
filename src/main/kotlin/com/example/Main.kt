package com.example

import com.example.model.UrlShortener
import com.example.repository.MongoDB
import com.example.repository.UrlRepository
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.collect
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }

        val database = try {
            val connectionString = "mongodb://localhost:27017"
            val mongoDB = MongoDB(connectionString)

            // Just try listing collections to confirm connectivity
            runBlocking {
                try {
                    val collections = mongoDB.getDatabase("keploy").listCollectionNames().toList()
                    println("✅ Connected to MongoDB. Found collections: $collections")
                } catch (e: Exception) {
                    mongoDB.close()
                    println("❌ MongoDB ping/list failed: ${e.message}")
                    throw e
                }
            }

            mongoDB.getDatabase("keploy")
        } catch (e: Exception) {
            println("❌ Failed to create MongoDB connection: ${e.message}")
            throw e
        }
        val repository = UrlRepository(database)
        routing {
            route("/urls") {
                get {
                    call.respond(repository.getAllUrls())
                }

                get("{id}") {
                    val id = call.parameters["id"] ?: return@get call.respondText(
                        "Missing id",
                        status = HttpStatusCode.BadRequest
                    )
                    val url = repository.getUrlById(id)
                    if (url == null) call.respond(HttpStatusCode.NotFound)
                    else call.respond(url)
                }

                get("/short/{code}") {
                    val code = call.parameters["code"] ?: return@get call.respondText(
                        "Missing short code",
                        status = HttpStatusCode.BadRequest
                    )
                    val url = repository.getUrlByShortCode(code)
                    if (url == null) call.respond(HttpStatusCode.NotFound)
                    else call.respond(url)
                }

                post {
                    val url = call.receive<UrlShortener>()
                    call.respond(repository.createUrl(url)!!)
                }

                put("{id}") {
                    val id = call.parameters["id"] ?: return@put call.respondText(
                        "Missing id",
                        status = HttpStatusCode.BadRequest
                    )
                    val url = call.receive<UrlShortener>()
                    if (repository.updateUrl(id, url)) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }

                delete("{id}") {
                    val id = call.parameters["id"] ?: return@delete call.respondText(
                        "Missing id",
                        status = HttpStatusCode.BadRequest
                    )
                    if (repository.deleteUrl(id)) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }
            }
        }
    }.start(wait = true)
}