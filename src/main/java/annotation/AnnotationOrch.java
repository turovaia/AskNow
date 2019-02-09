package annotation;

import java.util.ArrayList;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Ontology;
import org.apache.log4j.Logger;

import phrase.phrase;
import question.questionAnnotation;

public class AnnotationOrch {
	
	/*
	 * This class orchestrates the annotation. It first calls spotlight (entity linker) 
	 * and then moves to relation annotation (one-hop strategy and patty and other )
	 * 
	 * 
	 * */
	
	final static Logger logger = Logger.getLogger(AnnotationOrch.class);
	
	public ArrayList<ArrayList<relationAnnotationToken>> startAnnotationOrch(ArrayList<phrase> phraseList, questionAnnotation ques_annotation, OntModel ontology){
			//call spotlight annotation. 
			//for each phrase in the list pass it through the spotlight and check for its annotation.
		
		ArrayList<ArrayList<relationAnnotationToken>> finalRelList = new ArrayList<ArrayList<relationAnnotationToken>>();

		annotation.AnnotationCustomKt.AnnotationCustom(phraseList, ontology);
		
		try {
			
			finalRelList = relationAnnotation.relAnnotation(phraseList, ques_annotation);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		
		return finalRelList;
	}
	
	
	
}
