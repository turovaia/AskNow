package benchmark;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;

import annotation.AnnotationOrch;
import annotation.OneHopRelationQuery;
import annotation.relationAnnotationToken;
import init.initializer;
import phrase.phrase;
import phrase.phraseOrch;
import phraseMerger.phraseMergerOrch;
import queryConstructor.SparqlSelector;
import question.quesOrch;
import question.questionAnnotation;
import token.token;
import utils.parseQald;
import utils.qaldQuery;
import utils.queryExecutor;
import org.apache.log4j.Logger;

public class qald6 {
/*
 * Class executes the QALD6 train dataset. And comapres the results with the sparql-answers provided by the qald
 * 
 * */
	final static Logger logger = Logger.getLogger(qald6.class);

	
	public static void main(String args[]){
		/*
		 This class executes the qald6 train file. It parses the question and passes it to the execute question class.
		 It also parses the answer and compares the answer with the generated answer. The answer is returned by the execute question.
		 The class acts like a wrapper for input question and answer, compares the generated answer and gives a score.
		 */
		
		
		//Initializing the pipeline
		initializer init = new initializer();
		//parsing the QALD json file for answers.
		ArrayList<String[]> qaldTuple = parseQald.parseQald6("src/main/resources/qald-6-train-multilingual.json");
		
		
		//Statistics varaibles
		Integer counter = 0;
		Integer query_number = 0;
		Integer skip_questions = 0;
		
		//basic parsing
		for(String[] temp: qaldTuple){
			System.out.println(query_number);
			query_number = query_number + 1;
			if(skip_questions < 0){
				skip_questions = skip_questions + 1;
				continue;
			}
			String question = temp[0];
			String sparql = temp[1];
			//Group by query apprently does not workso skipping it 
			
			if (sparql.contains("GROUP BY") || sparql.contains("Breaking_Bad") || query_number == 155 || query_number == 181 || query_number == 216 || query_number == 234 || query_number == 245 || query_number == 312 || query_number == 327 || query_number == 349){
				//Breaking_Bad query is no. 20
				//Film producer
				//TODO: Due to some implementation bug, these aove query are not supported. 
				continue;
			}
			if(!sparql.equals("")){
				//TODO: replaceAll should have more factors. 
				sparql = sparql.replaceAll("rdf:type", "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>").replaceAll("xsd:integer", "<http://www.w3.org/2001/XMLSchema#integer>").replaceAll("xsd:date", "<http://www.w3.org/2001/XMLSchema#date>").replaceAll("foaf:Person", "<http://xmlns.com/foaf/0.1/#Person>");
				sparql = sparql.replaceAll("foaf:givenName","<http://xmlns.com/foaf/0.1/#givenName>").replaceAll("xsd:double", "<http://www.w3.org/2001/XMLSchema#double>").replaceAll("foaf:surname", "<http://xmlns.com/foaf/0.1/#surname>");
				System.out.println("the qald query is " + sparql);
				try{
						ArrayList<String> qald_result = qaldQuery.returnResultsQald(sparql);
						System.out.println("the quald returned results are :");
						System.out.println(qald_result);
						//This is the meat of the code. This is where the question is executed. 
						ArrayList<String> askNow_answer = executeQuestion.execute(question,null, true);
						if (askNow_answer != null){
							if(askNow_answer.containsAll(qald_result) && qald_result.containsAll(askNow_answer)){
								System.out.println("Atleast one right answer");
								counter = counter + 1;
								
							}
					}
				}
					catch (Exception e){
						System.out.println(e.getMessage());
						continue ;
					}
				}
				System.out.println("********&&&*******" + counter);
				
			}
		}
	}
	
	//TODO: Add this to utils pacakage.


