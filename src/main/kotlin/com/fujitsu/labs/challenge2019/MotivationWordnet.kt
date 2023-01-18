package com.fujitsu.labs.challenge2019

import com.opencsv.CSVReader
import java.io.FileReader

fun main(args: Array<String>) {
    val filename =
        if (args.size > 0) {
            args[0]
        } else {
            "MotivationWords.txt"
        }
    val motivation = MotivationWordnet()
    motivation.makeHeader()
    motivation.makeList(filename)
}

class MotivationWordnet {

    val wordnet = WordnetChecker()

    fun makeHeader() {
        println("@prefix kgcf: <http://kgchallenge.github.io/ontology/#> .")
        println("@prefix kdf: <http://kgchallenge.github.io/data/#> .")
        println("@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .")
        println("@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .")
        println()
        println("kgcf:motivation a rdfs:Class .")
        println("kgcf:motivation_of_murder a rdfs:Class ;")
        println("       rdfs:subClassOf kgcf:motivation .")
        println("kgcf:relatedWord a rdf:Property .")
        println("")
    }

    fun makeList(fileName: String) {
        val reader = CSVReader(FileReader(fileName))
        var nextLine = reader.readNext()
        while (nextLine != null) {
            var words = wordnet.checker(nextLine)
            if (words.size > 0) {
                println("kdf:${nextLine[0]} a kgcf:motivation_of_murder .")
            }
            for (i in words) {
                println("kdf:${nextLine[0]} kgcf:relatedWord <$i> .")
            }
            nextLine = reader.readNext()
        }
    }
}
