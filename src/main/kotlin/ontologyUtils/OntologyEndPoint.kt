package ontologyUtils

import com.hp.hpl.jena.ontology.OntModel
import com.hp.hpl.jena.vocabulary.RDFS
import org.apache.commons.text.similarity.LongestCommonSubsequenceDistance

object OntologyEndPoint {

    fun getOntologyEquivalent(phrase: String, ontology: OntModel): String? {
        val ontologyLabelsScores = ontology.listResourcesWithProperty(RDFS.label).toList().map { resource ->
            resource.listProperties(RDFS.label).toList().map { Pair(it.literal.toString(), resource.uri) }
        }.flatten().map {
            Pair(LongestCommonSubsequenceDistance().apply(phrase, it.first), it.second).apply {
                //println("res: " + it.first + " " + this.first)
            }
        }

        val closestLabel = ontologyLabelsScores.filter { it.first < 5 }.minBy { it.first }

        return closestLabel?.second
    }

}