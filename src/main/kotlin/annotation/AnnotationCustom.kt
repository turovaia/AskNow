package annotation
import com.hp.hpl.jena.ontology.OntModel
import ontologyUtils.OntologyEndPoint
import phrase.phrase
import utils.spotlight
import java.util.regex.Pattern


fun AnnotationCustom(phraseList: ArrayList<phrase>, ontology: OntModel) {
        val spot = spotlight()
        val posTags = Pattern.compile("NER|NNP")
        for (ph in phraseList) {
            var tempString = String()

            for (tk in ph.getPhraseToken()) {
                tempString = tempString + tk.getValue() + " "
            }
            println("check for the phrase $tempString")

            ph.uri = OntologyEndPoint.getOntologyEquivalent(tempString, ontology)
            println("MY FOUND URI: " + ph.uri)

            // JSONArray DBpEquivalent = spot.getDBLookup(tempString, "0.0");

            /*		 try{

			 JSONObject obj2 = (JSONObject) DBpEquivalent.get(0);
			 System.out.println(obj2.get("uri") );
			 ph.setUri(obj2.get("uri").toString());
		 }

		 catch (Exception e){
			 //sometimes spotlight does not catch simple URI.
			 if(posTags.matcher(ph.getPosTag()).find()){
				 String uri = forcedSpotlight.getDbpEntity(tempString);
				 if(!uri.equals("notFound")){
					 ph.setUri(uri);
				 }
			 }
			 continue;
		 }*/
        }
    }