package grpcservice

import annotation.AnnotationOrch
import com.hp.hpl.jena.ontology.OntModel
import com.hp.hpl.jena.ontology.OntModelSpec
import com.hp.hpl.jena.rdf.model.ModelFactory
import com.parser.proto.ParseRequest
import com.parser.proto.ParseResponse
import com.parser.proto.ParserGrpc
import init.initializer
import io.grpc.stub.StreamObserver
import phrase.phraseOrch
import phraseMerger.phraseMergerOrch
import queryConstructor.SparqlSelector
import question.quesOrch

object Parser: ParserGrpc.ParserImplBase() {

    init {
        initializer()
    }

    override fun parse(request: ParseRequest, responseObserver: StreamObserver<ParseResponse>) {

        val model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null)
        model.read("file:///C:/Files/YandexDisk/ПГУ/!Курсовая 3/Protege-5.2.0-win/moodle.owl", null)
        val query = request.query

        val sparql = execute(query, model, true)
        val response = ParseResponse.newBuilder().setResult(sparql).build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }

    fun execute(question: String, ontology: OntModel, verbose: Boolean): String {
        if (verbose) {
            println("The question is : $question")
        }

        val question_orch = quesOrch()
        val ques_annotation = question_orch.questionOrchestrator(question)
        val phrase = phraseOrch()
        val phraseList = phrase.startPhraseMerger(ques_annotation)
        val phraseMergerOrchestrator = phraseMergerOrch()
        val annotation = AnnotationOrch()
        annotation.startAnnotationOrch(phraseList, ques_annotation, ontology)
        phraseMergerOrchestrator.startPhraseMergerOrch(ques_annotation, phraseList)
        ques_annotation.phraseList = phraseList
        if (verbose) {
            println("the list of phrases are ")
            for (ph in phraseList) {
                for (tk in ph.phraseToken) {
                    print(tk.value + " ")
                }
            }
        }

        val askNow_sparql = SparqlSelector.sparqlSelector(ques_annotation)
        if (verbose) {
            println("The generated sparql is :$askNow_sparql")
        }

        return askNow_sparql
    }
}