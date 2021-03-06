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

package edu.cornell.mannlib.vitro.webapp.dao;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;


public class DisplayVocabulary {
    /** <p>The ontology model that holds the vocabulary terms</p> */
    private static OntModel m_model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM, null );

    /*Uris for Models for Display*/
    
    public static final String DISPLAY_TBOX_MODEL_URI = "http://vitro.mannlib.cornell.edu/default/vitro-kb-displayMetadataTBOX";
    public static final String DISPLAY_DISPLAY_MODEL_URI = "http://vitro.mannlib.cornell.edu/default/vitro-kb-displayMetadata-displayModel";
    /* Namespace for display vocabulary */
    public static final String DISPLAY_NS = "http://vitro.mannlib.cornell.edu/ontologies/display/1.1#";
    private static final String NS = DISPLAY_NS;
    
    /* Individuals */
    public static final String DEFAULT_MENU = NS + "DefaultMenu";
    public static final String HOME_PAGE_URI = NS + "Home";
    public static final String SEARCH_INDEX_URI = NS + "SearchIndex";
    
    //bk392 for extracting properties beyond context nodes.
    public static final String CONTEXT_NODES_URI = NS + "QueryForContextNodes";
    
    /* Page types */
    //Corresponding to statements in menu management that define class of data getter to be used
    public static final String PAGE_TYPE = NS + "Page";
    public static final String HOME_PAGE_TYPE = NS + "HomePage";
   
    
    public static final String CLASSGROUP_PAGE_TYPE = NS + "ClassGroupPage";
    public static final String CLASSINDIVIDUALS_PAGE_TYPE = NS + "IndividualsForClassesPage";
    public static final String CLASSINDIVIDUALS_INTERNAL_TYPE = NS + "InternalClassesPage";
   
    /* Object Properties */
    public static final String FOR_CLASSGROUP = NS + "forClassGroup";
    public static final String CLASS_INTERSECTION = NS + "intersectsWithClass";
    public static final String HAS_CLASS_INTERSECTION = NS + "hasClassIntersection";
    public static final String ITEM_TO_PAGE = NS + "toPage";
    public static final String HAS_ELEMENT = NS + "hasElement";
    public static final String USES_DATAGETTER_CLASS = NS + "usesDataGetterClass";

    /**Data Getter object properties **/
    public static final String HAS_DATA_GETTER = NS + "hasDataGetter";
    public static final String GETINDIVIDUALS_FOR_CLASS = NS + "getIndividualsForClass";
    public static final String 	RESTRICT_RESULTS_BY = NS + "restrictResultsByClass";
    //specific case for internal class, value is true or false
    public static final String 	RESTRICT_RESULTS_BY_INTERNAL = NS + "restrictResultsByInternalClass";


    /* Data Properties */
    public static final DatatypeProperty URL_MAPPING = m_model.createDatatypeProperty(NS + "urlMapping");
    public static final String TITLE = NS + "title";
    public static final DatatypeProperty REQUIRES_BODY_TEMPLATE = m_model.createDatatypeProperty(NS + "requiresBodyTemplate");
    //bk392 for extracting properties beyond context nodes.
    public static final DatatypeProperty QUERY_FOR_EDUCATIONAL_TRAINING = m_model.createDatatypeProperty(NS + "queryForEducationalTraining");
    public static final DatatypeProperty LINK_TEXT = m_model.createDatatypeProperty(NS + "linkText");

    /* URIs for storing menu.n3 */
    public static final String MENU_TEXT_RES = NS + "MenuText";    
    public static final String HAS_TEXT_REPRESENTATION = NS + "hasMenuText";
    
    
    
    /** <p>The namespace of the vocabulary as a string</p>
     *  @see #NS */
    public static String getURI() {return NS;}
    
    /** <p>The namespace of the vocabulary as a resource</p> */
    public static final Resource NAMESPACE = m_model.createResource( NS );
    
    public static final ObjectProperty REQUIRES_VALUES = m_model.createObjectProperty( NS + "requiresValues" );
    
    public static final ObjectProperty TO_PAGE = m_model.createObjectProperty( NS + "toPage" );
    
    public static final ObjectProperty EXCLUDE_CLASS = m_model.createObjectProperty( NS + "excludeClass" );
    
    public static final ObjectProperty INCLUDE_CLASS = m_model.createObjectProperty( NS + "includeClass" );
    
    /** <p>Java package and class name. ex edu.cornell.mannlib.vitro.webapps.functions.ExampleFunction</p> */
    public static final DatatypeProperty JAVA_CLASS_NAME = m_model.createDatatypeProperty( NS + "javaClassName" );
    
    public static final DatatypeProperty MENU_POSITION = m_model.createDatatypeProperty( NS + "menuPosition" );
    
    public static final DatatypeProperty PARAMETER_NAME = m_model.createDatatypeProperty( NS + "parameterName" );
    
    public static final DatatypeProperty PARAMETER_VALUE = m_model.createDatatypeProperty( NS + "parameterValue" );
    
    //public static final DatatypeProperty REQUIRES_BODY_TEMPLATE = m_model.createDatatypeProperty( NS + "requiresBodyTemplate" );
    
    /** <p>Values from HttpRequest.getPathInfo() will be mapped to values from urlMapping.</p> */
    //public static final DatatypeProperty URL_MAPPING = m_model.createDatatypeProperty( NS + "urlMapping" );
        
    
    
    /** <p>This represents a menu item or other general navigation item.</p> */
    public static final OntClass NAVIGATION_ELEMENT = m_model.createClass( NS + "NavigationElement" );
    
    /** <p>Class of pages.</p> */
    public static final OntClass PAGE = m_model.createClass( NS + "Page" );
    
    
    //Parameters to switch to menu editing - or to switch models
    public static final String SWITCH_TO_DISPLAY_MODEL = "switchToDisplayModel";
    public static final String USE_MODEL_PARAM = "useThisModel";
    public static final String USE_TBOX_MODEL_PARAM = "useThisTboxModel";
    public static final String USE_DISPLAY_MODEL_PARAM = "useThisDisplayModel";
    
    //Attribute values used to store display tbox/display display model in servlet context
    public static final String CONTEXT_DISPLAY_TBOX = "displayOntModelTBOX";
    public static final String CONTEXT_DISPLAY_DISPLAY = "displayOntModelDisplayModel";
    
    /** Key for display model in request, session or context attributes */
    public static final String DISPLAY_ONT_MODEL = "displayOntModel";
    
    //URL for menu management
    public static final String PROCESS_MENU_MANAGEMENT_URL = "/menuManagementEdit";
    public static final String REORDER_MENU_URL = PROCESS_MENU_MANAGEMENT_URL + "?cmd=Reorder&" + SWITCH_TO_DISPLAY_MODEL + "=true";

    /* URIs for some individuals in the dispaly ontology */

	public static final String HOME_MENU_ITEM = DISPLAY_NS + "HomeMenuItem";
        
	/* some URIs of properties used with the SPARQL DataGetter, and save to var is saved for fixedHtml */
    public static final String SAVE_TO_VAR = DISPLAY_NS + "saveToVar" ;
    public static final String QUERY_MODEL = DISPLAY_NS + "queryModel";    
    public static final String QUERY = DISPLAY_NS + "query";
    
    /* URI of property for Fixed HTML Generator */
    public static final String FIXED_HTML_VALUE = DISPLAY_NS + "htmlValue";
    
    //public static final Individual EVENTS = m_model.createIndividual( NS + "Events", PAGE );
    
    //public static final Individual EVENTS_MENU_ITEM = m_model.createIndividual( NS + "EventsMenuItem", NAVIGATION_ELEMENT );
    
    //public static final Individual HOME = m_model.createIndividual( NS + "Home", PAGE );
    
    //public static final Individual HOME_MENU_ITEM = m_model.createIndividual( NS + "HomeMenuItem", NAVIGATION_ELEMENT );
    
    //public static final Individual ORGANIZATIONS = m_model.createIndividual( NS + "Organizations", PAGE );
    
    //public static final Individual ORGANIZATIONS_MENU_ITEM = m_model.createIndividual( NS + "OrganizationsMenuItem", NAVIGATION_ELEMENT );
    
    //public static final Individual PEOPLE = m_model.createIndividual( NS + "People", PAGE );
    
    //public static final Individual PEOPLE_MENU_ITEM = m_model.createIndividual( NS + "PeopleMenuItem", NAVIGATION_ELEMENT );
    
    //public static final Individual PUBLICATIONS = m_model.createIndividual( NS + "Publications", PAGE );
    
    //public static final Individual PUBLICATIONS_MENU_ITEM = m_model.createIndividual( NS + "PublicationsMenuItem", NAVIGATION_ELEMENT );
    
}
