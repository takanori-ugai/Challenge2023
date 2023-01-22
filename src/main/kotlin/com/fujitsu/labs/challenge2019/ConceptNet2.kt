package com.fujitsu.labs.challenge2019

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

fun main() {
    val format = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }
    val url = URL("https://api.conceptnet.io/query?start=/c/en/kill&rel=/r/MotivatedByGoal&limit=1000")
    val http = url.openConnection() as HttpURLConnection
    http.setRequestMethod("GET")
    http.setRequestProperty("Accept", "application/json")
    http.connect()

    val isr = InputStreamReader(http.getInputStream())
    val lines = isr.readText()
//    println(lines)
    val employeeObject = format.decodeFromString<Motivate>(lines)
    println(employeeObject)
    isr.close()
}
class ConceptNet2

@Serializable
data class Motivate(
    val version: String,
    val edges: List<Edge>,
)
