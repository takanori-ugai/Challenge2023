package com.fujitsu.labs.challenge2019

import com.fujitsu.labs.transe.LoadRDF
import org.apache.jena.query.QueryExecutionFactory
import org.apache.jena.query.QueryFactory
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.util.FileManager

fun main(args: Array<String>) {
    // args[0] = RDF
    // args[1] = Vector
    val predictor = AddMotivation(args[0])
    predictor.printTriples()
}

class AddMotivation(rdf: String) {
    val rdf = LoadRDF(rdf)
    val wordnet = ModelFactory.createDefaultModel()

    // use the FileManager to find the input file
    val wordnetStream = FileManager.get().open(rdf)
        ?: throw IllegalArgumentException(
            "File: $rdf not found"
        )

    init {
        wordnet.read(wordnetStream, null, "TURTLE")
    }

    fun printTriples() {
        val queryString = """
prefix rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> 
prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> 
prefix owl: <http://www.w3.org/2002/07/owl#> 
prefix cc:   <http://creativecommons.org/ns#> 
prefix xsd:  <http://www.w3.org/2001/XMLSchema#> 
prefix kgc: <http://kgc.knowledge-graph.jp/ontology/kgc.owl#>  
#prefix kd: <http://kgc.knowledge-graph.jp/data/SpeckledBand/>  
prefix kdcf: <http://kgchallenge.github.io/ontology/#>

select DISTINCT ?mean ?subject ?S where {
  ?S ?p ?subject .
  ?S kdcf:relatedWord ?word .
  ?mean kdcf:relatedWord ?word .
  ?mean a kdcf:motivation_of_murder .
  ?subject a kgc:Person .
}
        """.trimIndent()
        val query = QueryFactory.create(queryString)
        QueryExecutionFactory.create(query, wordnet).use { qexec ->
            val results = qexec.execSelect()
            while (results.hasNext()) {
                val soln = results.nextSolution()
                val motivation = soln.getResource("mean")
                val subject = soln.getResource("subject")
                val sentence = soln.getResource("S")
                println("<$subject> <http://kgchallenge.github.io/ontology/#relatedMotivation> <$motivation> .")
                println("<$sentence> <http://kgchallenge.github.io/ontology/#relatedMotivation> <$motivation> .")
            }
        }
    }
}
