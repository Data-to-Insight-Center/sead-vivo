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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.shared.Lock;
import com.hp.hpl.jena.vocabulary.OWL;

import edu.cornell.mannlib.vitro.webapp.beans.DataProperty;
import edu.cornell.mannlib.vitro.webapp.beans.Individual;
import edu.cornell.mannlib.vitro.webapp.beans.VClass;
import edu.cornell.mannlib.vitro.webapp.beans.VClassGroup;
import edu.cornell.mannlib.vitro.webapp.controller.Controllers;
import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;
import edu.cornell.mannlib.vitro.webapp.controller.freemarker.IndividualListController;
import edu.cornell.mannlib.vitro.webapp.controller.freemarker.IndividualListController.PageRecord;
import edu.cornell.mannlib.vitro.webapp.controller.freemarker.UrlBuilder;
import edu.cornell.mannlib.vitro.webapp.controller.json.JsonServlet;
import edu.cornell.mannlib.vitro.webapp.dao.DisplayVocabulary;
import edu.cornell.mannlib.vitro.webapp.dao.VitroVocabulary;
import edu.cornell.mannlib.vitro.webapp.dao.WebappDaoFactory;
import edu.cornell.mannlib.vitro.webapp.dao.jena.VClassGroupCache;


public class ExecuteDataRetrieval {
    
    final static Log log = LogFactory.getLog(ExecuteDataRetrieval.class);
    Individual individual = null;
    VitroRequest vreq = null;
    Model displayModel = null;

    /**
     * Constructor with display model and data getter URI that will be called by reflection.
     */
    public ExecuteDataRetrieval(VitroRequest vreq, Model displayModel, Individual individual){
        //not sure if we need display model or vreq for this?
    	this.individual = individual;
    	this.vreq = vreq;
    	this.displayModel = displayModel;
    }   
    
    public List<DataGetter> retrieveDataGetters() {
    	//Using a hashset to prevent duplicates
    	//Would this work with interfaces? In this case, all of them would be interfaces?
    	HashSet<DataGetter> dataGetters = new HashSet<DataGetter>();
    	List<VClass> vclasses = this.individual.getVClasses();
    	//For any of the vclasses that apply to this individual, check whether
    	//there are any datagetter assigned for that class
    	try {
	    	for(VClass v: vclasses) {
	    		String classURI = v.getURI();
	    		//How to handle duplicates?
	    		dataGetters.addAll(DataGetterUtils.getDataGettersForClass(vreq, displayModel, classURI));
	    	} 
	    }
    	catch(Exception ex) {
    		log.error("Error occurred in retrieving datagetters for vclasses", ex);
    	}
    	List<DataGetter> dgList = new ArrayList<DataGetter>(dataGetters);
    	return dgList;
    }
    
    //retrieve data getters for the classes that apply to this individual
    //execute the data getters, and return the data in a map
    public void executeDataGetters(Map<String, Object> mapForTemplate) 
    throws Exception {
        List<DataGetter> dgList = retrieveDataGetters();
        //Put in individual URI in map for template - e.g. sparql query can then use that information and add bindings    
        mapForTemplate.put("individualURI", this.individual.getURI());
        for( DataGetter dg : dgList){            
            Map<String,Object> moreData = dg.getData(mapForTemplate);            
            if( moreData != null ){
                mapForTemplate.putAll(moreData);
            }
        }                       
    }
    
	
	
    
}
