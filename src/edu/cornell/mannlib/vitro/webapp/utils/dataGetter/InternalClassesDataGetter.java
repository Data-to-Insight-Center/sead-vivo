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

package edu.cornell.mannlib.vitro.webapp.utils.dataGetter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;
import edu.cornell.mannlib.vitro.webapp.dao.DisplayVocabulary;
import edu.cornell.mannlib.vitro.webapp.dao.VitroVocabulary;
import edu.cornell.mannlib.vitro.webapp.dao.jena.ModelContext;

/**
 * This will pass these variables to the template:
 * classGroupUri: uri of the classgroup associated with this page.
 * vClassGroup: a data structure that is the classgroup associated with this page.     
 */
public class InternalClassesDataGetter extends IndividualsForClassesDataGetter{
    private static final Log log = LogFactory.getLog(InternalClassesDataGetter.class);
    
    /**
     * Constructor with display model and data getter URI that will be called by reflection.
     */
    public InternalClassesDataGetter(VitroRequest vreq, Model displayModel, String dataGetterURI){
        super(vreq, displayModel, dataGetterURI);
    }   
    
    
    //Use different template name for internal class template
    @Override
    protected void setTemplateName() {
    	super.restrictClassesTemplateName = "internalClass";
    }
    
    //Retrieve classes and check whether or not page to be filtered by internal class only 
    @Override
    protected Map<String, Object> getClassIntersectionsMap(Model displayModel) {

    	Map<String, Object> classesAndRestrictions = new HashMap<String, Object>();
    	QuerySolutionMap initialBindings = new QuerySolutionMap();
        initialBindings.add("dataGetterUri", ResourceFactory.createResource(this.dataGetterURI));
        List<String> classes = new ArrayList<String>();
       
        displayModel.enterCriticalSection(false);
        try{
        	Query individualsForClassesInternalQuery = QueryFactory.create(individualsForClassesInternalQueryString);
            QueryExecution qexec = QueryExecutionFactory.create( individualsForClassesInternalQuery, displayModel , initialBindings);
            try{
                ResultSet resultSet = qexec.execSelect();        
                while(resultSet.hasNext()){
                    QuerySolution soln = resultSet.next();
                    String dg = DataGetterUtils.nodeToString(soln.get("dg"));
                    classes.add(DataGetterUtils.nodeToString(soln.get("class")));
                    //node to string will convert null to empty string
                    String isInternal = DataGetterUtils.nodeToString(soln.get("isInternal"));
                    if(!isInternal.isEmpty()) {
                    	log.debug("Internal value is "+ isInternal);
                    	//Retrieve and add internal class
                    	classesAndRestrictions.put("isInternal", isInternal);
                    }
                }
                
                if( classes.size() == 0 ){
                    log.debug("No classes  defined in display model for "+ this.dataGetterURI);
                    return null;
                }
                classesAndRestrictions.put("classes", classes);  
                return classesAndRestrictions;
            }finally{
                qexec.close();
            }
        }finally{
            displayModel.leaveCriticalSection();
        }
	}


    //Retrieve current internal class uri to restrict by
	@Override
	protected List<String> retrieveRestrictClasses(
			ServletContext context, Map<String, Object> classIntersectionsMap) {
		List<String> restrictClasses = new ArrayList<String>();
		String internalClass = (String) classIntersectionsMap.get("isInternal");
		//if internal class restriction specified and is true
		if(internalClass != null && internalClass.equals("true")) {
			//Get internal class
			Model mainModel = ModelContext.getBaseOntModelSelector(context).getTBoxModel();;
			StmtIterator internalIt = mainModel.listStatements(null, ResourceFactory.createProperty(VitroVocabulary.IS_INTERNAL_CLASSANNOT), (RDFNode) null);
			//Checks for just one statement 
			if(internalIt.hasNext()){
				Statement s = internalIt.nextStatement();
				//The class IS an internal class so the subject is what we're looking for
				String internalClassUri = s.getSubject().getURI();
				log.debug("Found internal class uri " + internalClassUri);
				restrictClasses.add(internalClassUri);
			}
		}
			
		return restrictClasses;
	}        
	
	@Override
    public String getType(){
        return DataGetterUtils.generateDataGetterTypeURI(InternalClassesDataGetter.class.getName());
    } 
	
    static final protected String individualsForClassesInternalQueryString = 
    	DataGetterUtils.prefixes + "\n" + 
    	 "SELECT?class ?isInternal WHERE {\n" +
         " ?dataGetterUri <" + DisplayVocabulary.GETINDIVIDUALS_FOR_CLASS + "> ?class . \n" +
         " OPTIONAL {  ?dataGetterUri <"+ DisplayVocabulary.RESTRICT_RESULTS_BY_INTERNAL + "> ?isInternal } .\n" +    
         "} \n" ;
    
}