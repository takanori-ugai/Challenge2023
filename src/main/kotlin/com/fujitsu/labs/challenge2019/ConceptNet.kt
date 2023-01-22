package com.fujitsu.labs.challenge2019

import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNames

fun main() {
    val format = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }
    val url = URL("https://api.conceptnet.io/query?end=/c/en/kill&rel=/r/UsedFor&limit=1000")
    val http = url.openConnection() as HttpURLConnection
    http.setRequestMethod("GET")
    http.setRequestProperty("Accept", "application/json")
    http.connect()

    val isr = InputStreamReader(http.getInputStream())
    val lines = isr.readText()
//    println(lines)
    val employeeObject = format.decodeFromString<Means>(lines);
    println(employeeObject);
    isr.close()
}
class ConceptNet

@Serializable
data class Means(
    val version: String,
    val edges: List<Edge>,
)

@Serializable
data class Edge(
    val start: Map<String, String>,
    val end: Map<String, String>
)