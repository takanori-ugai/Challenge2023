package com.fujitsu.labs.challenge2019

import com.fujitsu.labs.transe.TransE
import com.fujitsu.labs.transe.LoadRDF
import org.apache.jena.query.QueryExecutionFactory
import org.apache.jena.query.QueryFactory
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.util.FileManager
import smile.clustering.KMeans

fun main(args: Array<String>) {
  // args[0] = RDF
  // args[1] = Vector
  val predictor = Clustering(args[0], args[1])
  predictor.printTriples()
}

class Clustering(rdf: String, vector: String) {
  val rdf = LoadRDF(rdf)
  val embedding = TransE()
  val vectorFile = vector
  val wordnet = ModelFactory.createDefaultModel()

  // use the FileManager to find the input file
  val wordnetStream = FileManager.get().open(rdf)
    ?: throw IllegalArgumentException(
      "File: $rdf not found")

  init {
    wordnet.read(wordnetStream, null, "TURTLE")
  }

  fun printTriples() {
    embedding.prepare(rdf.entities, rdf.relations, rdf.triples)
    embedding.loadVectors(vectorFile)
    // prefix kgcf: <http://kgchallenge.github.io/ontology/#> .
    // select ?motivation { ?motivation a kgcf:motivation_of_murder . }
    val queryString = """
       prefix kgc: <http://kgc.knowledge-graph.jp/ontology/kgc.owl#> 
       select DISTINCT ?person {
          ?person a kgc:Person .
       }
    """.trimIndent()
    val query = QueryFactory.create(queryString)
    val persons = arrayListOf<String>()
    val personVec = arrayListOf<DoubleArray>()
    QueryExecutionFactory.create(query, wordnet).use { qexec ->
      val results = qexec.execSelect()
      while (results.hasNext()) {
        val soln = results.nextSolution()
        val r = soln.getResource("person")
//        println(r.toString())
        persons.add(r.toString())
        personVec.add(embedding.entityVec[embedding.entity2id[r.toString()]!!])
      }
    }
    val kMeans = KMeans(personVec.toTypedArray(), 3)
    val cluster = kMeans.clusterLabel
//    println(cluster.size)
    for (i in cluster.indices) {
      println("${persons[i]}, $i, ${cluster[i]}")
    }


  }
}
