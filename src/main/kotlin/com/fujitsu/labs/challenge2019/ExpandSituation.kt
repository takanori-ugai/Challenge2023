package com.fujitsu.labs.challenge2019

import com.fujitsu.labs.transe.LoadRDF
import com.fujitsu.labs.transe.TransE
import org.apache.jena.query.QueryExecutionFactory
import org.apache.jena.query.QueryFactory
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.util.FileManager

fun main(args: Array<String>) {
  // args[0] = RDF
  // args[1] = Vector
  val predictor = ExpandSituation(args[0], args[1])
  predictor.printTriples()
}

class ExpandSituation(rdf: String, vector: String) {
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
       prefix kgcf: <http://kgchallenge.github.io/ontology/#> 
       select DISTINCT ?situation {
          ?situation a kgc:Situation .
       }
    """.trimIndent()
    val query = QueryFactory.create(queryString)
    QueryExecutionFactory.create(query, wordnet).use { qexec ->
      val results = qexec.execSelect()
      while (results.hasNext()) {
        val soln = results.nextSolution()
        val r = soln.getResource("situation")
        val vec = embedding.predictTail(r.toString(), "http://kgchallenge.github.io/ontology/#relatedWord", 0.4)
        for (tail in vec) {
          if (tail.key.startsWith("http://www.w3.org/2006/03/wn/wn31/instances/word-")) {
            println("<$r>  <http://kgchallenge.github.io/ontology/#relatedWord> <${tail.key}> .")
          }
        }
      }
    }
  }
}
