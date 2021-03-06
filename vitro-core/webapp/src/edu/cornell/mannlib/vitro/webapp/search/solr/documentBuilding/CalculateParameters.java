/*
Copyright (c) 2013, Cornell University
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice,
      this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright notice,
      this list of conditions and the following disclaimer in the documentation
      and/or other materials provided with the distribution.
    * Neither the name of Cornell University nor the names of its contributors
      may be used to endorse or promote products derived from this software
      without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package edu.cornell.mannlib.vitro.webapp.search.solr.documentBuilding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.shared.Lock;

import edu.cornell.mannlib.vitro.webapp.beans.Individual;
import edu.cornell.mannlib.vitro.webapp.search.VitroSearchTermNames;


public class CalculateParameters implements DocumentModifier {

    private boolean shutdown = false;
	private Dataset dataset;
   // public static int totalInd=1;
    
    private static final String prefix = "prefix owl: <http://www.w3.org/2002/07/owl#> "
		+ " prefix vitroDisplay: <http://vitro.mannlib.cornell.edu/ontologies/display/1.1#>  "
		+ " prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>  "
		+ " prefix core: <http://vivoweb.org/ontology/core#>  "
		+ " prefix foaf: <http://xmlns.com/foaf/0.1/> "
		+ " prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> "
		+ " prefix localNav: <http://vitro.mannlib.cornell.edu/ns/localnav#>  "
		+ " prefix bibo: <http://purl.org/ontology/bibo/>  ";
    
    private static final String betaQuery = prefix + " SELECT count(distinct ?inLinks) " +
    		" WHERE { " +
    		" ?uri rdf:type owl:Thing . " +
    		" ?inLinks ?prop ?uri . " +
    		" } ";
    
    private static final String totalCountQuery = prefix + " SELECT count(distinct ?ind) " +
	" WHERE { " +
	" ?ind rdf:type owl:Thing . " +
	" } ";
     
    private static Log log = LogFactory.getLog(CalculateParameters.class);
    
	public CalculateParameters(Dataset dataset){
		 this.dataset =dataset;
		// new Thread(new TotalInd(this.dataset,totalCountQuery)).start();
	}
	
	public CalculateParameters(){
		super();
	}
	
	public float calculateBeta(String uri){
		float beta=0;
		int Conn=0; 
		Query query;
		QuerySolutionMap initialBinding = new QuerySolutionMap();
		QuerySolution soln = null;
		Resource uriResource = ResourceFactory.createResource(uri);
		initialBinding.add("uri", uriResource);
		dataset.getLock().enterCriticalSection(Lock.READ);		
		QueryExecution qexec=null;
		try{
			query = QueryFactory.create(betaQuery,Syntax.syntaxARQ);
			qexec = QueryExecutionFactory.create(query,dataset,initialBinding);
			ResultSet results = qexec.execSelect();
			List<String> resultVars = results.getResultVars();
			if(resultVars!=null && resultVars.size()!=0){
				soln = results.next();
				Conn = Integer.parseInt(soln.getLiteral(resultVars.get(0)).getLexicalForm());
			}
		}catch(Throwable t){
		    if( ! shutdown )
		        log.error(t,t);
		}finally{
		    if( qexec != null ) 
		        qexec.close();		    
			dataset.getLock().leaveCriticalSection();
		}

		beta = (float)Conn;
		//beta *= 100;
		beta += 1;
		
		// sigmoid function to keep beta between 0 to 1;
		
		beta = (float) (1 / ( 1 + Math.pow(Math.E,(-beta))));
		
		if(beta > 1)
			log.info("Beta higher than 1 : " + beta);
		else if(beta <= 0)
			log.info("Beta lower < = 0 : " + beta);
		return beta; 
    }
	
	
    public String[] getAdjacentNodes(String uri){
		
    	List<String> queryList = new ArrayList<String>();
    	Set<String> adjacentNodes = new HashSet<String>();
    	Set<String> coauthorNames = new HashSet<String>();
    	String[] info = new String[]{"",""};
    	StringBuffer adjacentNodesConcat = new StringBuffer();
    	StringBuffer coauthorBuff = new StringBuffer();
    	adjacentNodesConcat.append("");
    	coauthorBuff.append("");
    	
    	queryList.add(prefix + 
    			" SELECT ?adjobj (str(?adjobjLabel) as ?coauthor) " +
    			" WHERE { " +
    			" ?uri rdf:type <http://xmlns.com/foaf/0.1/Person> . " +
    			" ?uri ?prop ?obj . " +
    			" ?obj rdf:type <http://vivoweb.org/ontology/core#Relationship> . " +
    			" ?obj ?prop2 ?obj2 . " +
    			" ?obj2 rdf:type <http://vivoweb.org/ontology/core#InformationResource> . " +
    			" ?obj2 ?prop3 ?obj3 . " +
    			" ?obj3 rdf:type <http://vivoweb.org/ontology/core#Relationship> . " +
    			" ?obj3 ?prop4 ?adjobj . " +
    			" ?adjobj rdfs:label ?adjobjLabel . " +
    			" ?adjobj rdf:type <http://xmlns.com/foaf/0.1/Person> . " +

    			" FILTER (?prop !=rdf:type) . " +
    			" FILTER (?prop2!=rdf:type) . " +
    			" FILTER (?prop3!=rdf:type) . " +
    			" FILTER (?prop4!=rdf:type) . " +
    			" FILTER (?adjobj != ?uri) . " +
    	"}");

    	queryList.add(prefix +
    			" SELECT ?adjobj " +
    			" WHERE{ " +

    			" ?uri rdf:type foaf:Agent . " +
    			" ?uri ?prop ?obj . " +
    			" ?obj ?prop2 ?adjobj . " +


    			" FILTER (?prop !=rdf:type) . " +
    			" FILTER isURI(?obj) . " +

    			" FILTER (?prop2!=rdf:type) . " +
    			" FILTER (?adjobj != ?uri) . " +
    			" FILTER isURI(?adjobj) . " +

    			" { ?adjobj rdf:type <http://xmlns.com/foaf/0.1/Organization> . } " +
    			" UNION " +
    			" { ?adjobj rdf:type <http://xmlns.com/foaf/0.1/Person> . } " +
    			" UNION " +
    			" { ?adjobj rdf:type <http://vivoweb.org/ontology/core#InformationResource> . } " +
    			" UNION " +
    			" { ?adjobj rdf:type <http://vivoweb.org/ontology/core#Location> . } ." +
    	"}");
	
    	Query query;
    	
    	QuerySolution soln;
    	QuerySolutionMap initialBinding = new QuerySolutionMap();
		Resource uriResource = ResourceFactory.createResource(uri);
		
		initialBinding.add("uri", uriResource);
    	
    	Iterator<String> queryItr = queryList.iterator();
    	
    	dataset.getLock().enterCriticalSection(Lock.READ);
    	Resource adjacentIndividual = null;
    	RDFNode coauthor = null;
    	try{
    		while(queryItr.hasNext()){
    			/*if(!isPerson){
    				queryItr.next(); // we don't want first query to execute if the ind is not a person. 
    			}*/
    			query = QueryFactory.create(queryItr.next(),Syntax.syntaxARQ);
    			QueryExecution qexec = QueryExecutionFactory.create(query,dataset,initialBinding);
    			try{
    					ResultSet results = qexec.execSelect();
    					while(results.hasNext()){
    						soln = results.nextSolution();

    						adjacentIndividual = (Resource)soln.get("adjobj");
    						if(adjacentIndividual!=null){
    							adjacentNodes.add(adjacentIndividual.getURI());
    						}	

    						coauthor = soln.get("coauthor");
    						if(coauthor!=null){
    							coauthorNames.add(" co-authors " + coauthor.toString() + " co-authors ");
    						}	
    					}
    			}catch(Exception e){
    			    if( ! shutdown )
    			        log.error("Error found in getAdjacentNodes method of SearchQueryHandler");
    			}finally{
    				qexec.close();
    			}	
    		}
    		queryList = null;	
    		Iterator<String> itr = adjacentNodes.iterator();
    		while(itr.hasNext()){
    			adjacentNodesConcat.append(itr.next() + " ");
    		}
    		
    		info[0] = adjacentNodesConcat.toString();
    		
    		itr = coauthorNames.iterator();
    		while(itr.hasNext()){
    			coauthorBuff.append(itr.next());
    		}
    		
    		info[1] = coauthorBuff.toString();
    		
    	}
    	catch(Throwable t){
    	    if( ! shutdown )
    	        log.error(t,t);
    	}finally{
    		dataset.getLock().leaveCriticalSection();
    		adjacentNodes = null;
    		adjacentNodesConcat = null;
    		coauthorBuff = null;
    	}
    	return info;
	}
   
	@Override
	public void modifyDocument(Individual individual, SolrInputDocument doc, StringBuffer addUri) {
		// TODO Auto-generated method stub
		 // calculate beta value.  
        log.debug("Parameter calculation starts..");
        float beta = calculateBeta(individual.getURI());
        doc.addField(VitroSearchTermNames.BETA, beta);
        doc.setDocumentBoost(beta + doc.getDocumentBoost() );   
        log.debug("Parameter calculation is done");
	}
	
	
	public void shutdown(){
        shutdown=true;
    }
}

class TotalInd implements Runnable{
	private Dataset dataset;
	private String totalCountQuery;
	private static Log log = LogFactory.getLog(TotalInd.class);
	
	public TotalInd(Dataset dataset,String totalCountQuery){
		this.dataset = dataset;
		this.totalCountQuery = totalCountQuery;
		
	}
	public void run(){
		    int totalInd=0;
	        Query query;
	    	QuerySolution soln = null;
			dataset.getLock().enterCriticalSection(Lock.READ);
			QueryExecution qexec = null;
			
			try{
				query = QueryFactory.create(totalCountQuery,Syntax.syntaxARQ);
				qexec = QueryExecutionFactory.create(query,dataset);
				ResultSet results = qexec.execSelect();
				List<String> resultVars = results.getResultVars();
				
				if(resultVars!=null && resultVars.size()!=0){
					soln = results.next();
					totalInd = Integer.parseInt(soln.getLiteral(resultVars.get(0)).getLexicalForm());
				}
				//CalculateParameters.totalInd = totalInd;
				//log.info("Total number of individuals in the system are : " + CalculateParameters.totalInd);
			}catch(Throwable t){
				log.error(t,t);
			}finally{
			    if( qexec != null ) 
			        qexec.close();
				dataset.getLock().leaveCriticalSection();
			}
		
	}
}
