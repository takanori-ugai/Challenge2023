package com.fujitsu.labs.challenge2019

import org.apache.jena.query.QueryExecutionFactory
import org.apache.jena.query.QueryFactory
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.util.FileManager

class WordnetChecker {

  val wordnet = ModelFactory.createDefaultModel()

  // use the FileManager to find the input file
  val wordnetStream = FileManager.get().open("dict/wordnet31.ttl")
    ?: throw IllegalArgumentException(
      "File: dict/wordnet31.ttl not found")

  init {
    wordnet.read(wordnetStream, null, "TURTLE")
  }

  fun checker(tokens: Array<String>): Array<String> {
    val ret: ArrayList<String> = arrayListOf()
    for (token in tokens) {
      val queryString = "select * where { ?S <http://www.w3.org/2006/03/wn/wn20/schema/lexicalForm> ?O . FILTER ( str(?O) = \"${token}\" )} limit 10"
      val query = QueryFactory.create(queryString)
      QueryExecutionFactory.create(query, wordnet).use { qexec ->
        val results = qexec.execSelect()
        if (results.hasNext()) {
          val soln = results.nextSolution()
          val r = soln.getResource("S") // Get a result variable - must be a resource
          ret.add(r.toString())
        }
      }
    }
    return ret.toTypedArray()
  }
}
