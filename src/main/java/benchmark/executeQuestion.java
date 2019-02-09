package benchmark;

import java.io.File;
import java.util.ArrayList;

import annotation.AnnotationOrch;
import annotation.relationAnnotationToken;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import init.initializer;
import phrase.phrase;
import phrase.phraseOrch;
import phraseMerger.phraseMergerOrch;
import queryConstructor.SparqlSelector;
import question.quesOrch;
import question.questionAnnotation;
import token.token;
import utils.qaldQuery;

public class executeQuestion {


	public static void main(String[] args) {
		initializer init = new initializer();
		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null);
		model.read("file:///C:/Files/YandexDisk/ПГУ/!Курсовая 3/Protege-5.2.0-win/moodle.owl", null);
		 execute("What is the capital of Russian Federation?", model, true);
        execute("What are courses?", model, true);
        execute("What students enrolled in the databases?", model, true);
        execute("What are students that enrolled in the databases?", model, true);
        execute("What are students enrolled in the databases?", model, true);
        execute("What courses ivanov ivan is enrolled in?", model, true);
        execute("What are courses that ivanov ivan enrolled in?", model, true);
	}

	/*
	 * This starts the processing pipelines. Also has a verbose variable which prints accordingly. This feature has not been implemnted
	 * properly.
	 * 
	 * 
	 * */
	
	public static ArrayList<String> execute(String question, OntModel ontology, Boolean verbose){
		Integer counter = 10;
		if(verbose){
			System.out.println("The question is : " + question );
		}
		
		
		ArrayList<String> askNow_answer = null;	//This will store the answer generated after the question is executed.
		
		/*
		 * Question orchestrator: Takes input as straing (question) and returns annotated question.
		 * 
		 * General Note: FOr more information about any class check the class specific documentation.
		 * */
		quesOrch question_orch = new quesOrch(); 
		questionAnnotation ques_annotation = question_orch.questionOrchestrator(question);
		phraseOrch phrase = new phraseOrch();
		ArrayList<phrase> phraseList = phrase.startPhraseMerger(ques_annotation);
		phraseMergerOrch phraseMergerOrchestrator = new phraseMergerOrch();
		AnnotationOrch annotation = new AnnotationOrch();
		ArrayList<ArrayList<relationAnnotationToken>> relAnnotation = annotation.startAnnotationOrch(phraseList,ques_annotation, ontology);
		ArrayList<ArrayList<phrase>> conceptList = phraseMergerOrchestrator.startPhraseMergerOrch(ques_annotation, phraseList);
		ques_annotation.setPhraseList(phraseList);
		if(verbose){
			System.out.println("the list of phrases are ");
			for(phrase ph: phraseList){
				for(token tk: ph.getPhraseToken()){
					System.out.print(tk.getValue() + "sasas ");
				}
//				if(ph.getUri() != null){
//					System.out.println("");
//					System.out.println(" The list of proabable relations are");
//					Integer temp_counter = 0;
//					for (relationAnnotationToken relTk : ph.getListOfProbableRelation()){
//						
//						System.out.println(relTk.getPropertyLabel());
//						
//						if(relTk.getPh() == null){
//							System.out.println("**");
//							System.out.println("\t" + relTk.getTok().getValue() + " : " + relTk.getPropertyLabel() + " :" + relTk.getScore() );
//						}
//						else{
//							System.out.println("yo");
//							for(token tk:relTk.getPh().getPhraseToken()){
//								System.out.print(tk.getValue() + " ");
//							}
//							System.out.println(" ");
//						}
//						
//						if(temp_counter == counter){
//							break;
//						}
//						temp_counter = temp_counter+1;
//					}
//				}
//				else{
//					System.out.println("");
//				}
			}
		}
		
		String askNow_sparql = SparqlSelector.sparqlSelector(ques_annotation);
		if(verbose){
			System.out.println("@CHECK!!");
			System.out.println("The generated sparql is :" + askNow_sparql);
		}
		
//		if (!askNow_sparql.equals("")){
//			 askNow_answer = qaldQuery.returnResultsQald(askNow_sparql);
//		}
		return askNow_answer;
	}
}
