package com.fujitsu.labs.challenge2019

import opennlp.tools.lemmatizer.LemmatizerME
import opennlp.tools.lemmatizer.LemmatizerModel
import opennlp.tools.postag.POSModel
import opennlp.tools.postag.POSTaggerME
import opennlp.tools.tokenize.TokenizerME
import opennlp.tools.tokenize.TokenizerModel
import org.apache.jena.query.QueryExecutionFactory
import org.apache.jena.query.QueryFactory
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.util.FileManager
import java.io.FileInputStream

fun main(args: Array<String>) {
    val fileName = if (args.size >= 0) {
        args[0]
    } else {
        "data/SpeckledBand.ttl"
    }
    val openNLP = OpenNLP()
    openNLP.makeHeader()
    openNLP.lemmatize(fileName)
}

class OpenNLP {

    fun makeHeader() {
        println("@prefix kgcf: <http://kgchallenge.github.io/ontology/#> .")
        println("@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .")
        println()
        println("kgcf:relatedWord a rdf:Property .")
        println()
    }

    fun lemmatize(fileName: String) {
        val modelStream = FileInputStream("dict/en-lemmatizer.bin")
        val lemmatizerModel = LemmatizerModel(modelStream!!)
        val lemmatizer = LemmatizerME(lemmatizerModel)

        val posModelStream = FileInputStream("dict/en-pos-maxent.bin")
        val posModel = POSModel(posModelStream)
        val posTagger = POSTaggerME(posModel)

        val tokenStream = FileInputStream("dict/en-token.bin")
        val tokenModel = TokenizerModel(tokenStream)
        val tokenizer = TokenizerME(tokenModel)

        val wordnet = WordnetChecker()

        val model = ModelFactory.createDefaultModel()
        val inputStream = FileManager.get().open(fileName)
            ?: throw IllegalArgumentException(
                "File: data/SpeckledBand.ttl not found"
            )
        model.read(inputStream, null, "TURTLE")
        val queryString = "select * where { ?S <http://kgc.knowledge-graph.jp/ontology/kgc.owl#source> ?O . \n FILTER ( lang(?O) = \"en\" )}\n"
        val query = QueryFactory.create(queryString)
        QueryExecutionFactory.create(query, model).use { qexec ->
            val results = qexec.execSelect()
            while (results.hasNext()) {
                val soln = results.nextSolution()
                val r = soln.getResource("S") // Get a result variable - must be a resource
                val l = soln.getLiteral("O") // Get a result variable - must be a literal
                val token = tokenizer.tokenize(l.string)
                val lemmas = lemmatizer.lemmatize(token, posTagger.tag(token))
                for (i in wordnet.checker(lemmas)) {
                    println("<$r> kgcf:relatedWord <$i> .")
                }
            }
        }
        val queryString2 = """
        select * where {
          ?S <http://www.w3.org/2000/01/rdf-schema#label> ?O .
          FILTER ( lang(?O) = "en" )
        }
        """.trimIndent()
        val query2 = QueryFactory.create(queryString2)
        QueryExecutionFactory.create(query2, model).use { qexec ->
            val results = qexec.execSelect()
            while (results.hasNext()) {
                val soln = results.nextSolution()
                val r = soln.getResource("S") // Get a result variable - must be a resource
                val l = soln.getLiteral("O") // Get a result variable - must be a literal
                val words = wordnet.checker(arrayOf(l.string))
                if (words.size > 0) {
                    println("<$r> kgcf:relatedWord <${words[0]}> .")
                }
            }
        }
    }
}
