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

package edu.cornell.mannlib.vitro.webapp.controller.visualization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringEscapeUtils;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.Model;

import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.Actions;
import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;
import edu.cornell.mannlib.vitro.webapp.controller.freemarker.FreemarkerHttpServlet;
import edu.cornell.mannlib.vitro.webapp.controller.freemarker.responsevalues.ResponseValues;
import edu.cornell.mannlib.vitro.webapp.visualization.constants.VisConstants;
import edu.cornell.mannlib.vitro.webapp.visualization.exceptions.MalformedQueryParametersException;
import edu.cornell.mannlib.vitro.webapp.visualization.visutils.UtilityFunctions;
import edu.cornell.mannlib.vitro.webapp.visualization.visutils.VisualizationRequestHandler;

/**
 * Services a standard visualization request, which involves templates. This will return a simple 
 * error message and a 501 if there is no jena Model.
 *
 * @author cdtank
 */
@SuppressWarnings("serial")
public class ShortURLVisualizationController extends FreemarkerHttpServlet {

	public static final String URL_ENCODING_SCHEME = "UTF-8";

	private static final Log log = LogFactory.getLog(ShortURLVisualizationController.class.getName());
	
    protected static final Syntax SYNTAX = Syntax.syntaxARQ;
    
    public static ServletContext servletContext;
    
    @Override
    protected Actions requiredActions(VitroRequest vreq) {
    	/*
    	 * Based on the query parameters passed via URI get the appropriate visualization 
    	 * request handler.
    	 * */
    	VisualizationRequestHandler visRequestHandler = 
    			getVisualizationRequestHandler(vreq);
    	
    	if (visRequestHandler != null) {
    		
    		Actions requiredPrivileges = visRequestHandler.getRequiredPrivileges();
			if (requiredPrivileges != null) {
    			return requiredPrivileges;
    		}
    	}
    	return super.requiredActions(vreq);
    }
   
    @Override
    protected ResponseValues processRequest(VitroRequest vreq) {
        
    	/*
    	 * Based on the query parameters passed via URI get the appropriate visualization 
    	 * request handler.
    	 * */
    	VisualizationRequestHandler visRequestHandler = 
    			getVisualizationRequestHandler(vreq);
    	
    	servletContext = getServletContext();
    	
    	if (visRequestHandler != null) {
    	
    		/*
        	 * Pass the query to the selected visualization request handler & render the vis.
        	 * Since the visualization content is directly added to the response object we are side-
        	 * effecting this method.
        	 * */
            return renderVisualization(vreq, visRequestHandler);
            
    	} else {
    		return UtilityFunctions.handleMalformedParameters(
    									"Visualization Query Error", 
    									"Inappropriate query parameters were submitted.", 
    									vreq);
    	}
    	
    }


	private ResponseValues renderVisualization(VitroRequest vitroRequest,
									 VisualizationRequestHandler visRequestHandler) {
		
		Model model = vitroRequest.getJenaOntModel(); // getModel()
        if (model == null) {
            
            String errorMessage = "This service is not supporeted by the current " 
            			+ "webapp configuration. A jena model is required in the " 
            			+ "servlet context.";

            log.error(errorMessage);
            
            return UtilityFunctions.handleMalformedParameters("Visualization Query Error", 
            												  errorMessage, 
            												  vitroRequest);
        }
        
		Dataset dataset = setupJENADataSource(vitroRequest);
		
		if (dataset != null && visRequestHandler != null) {
        	
        	try {
        		List<String> matchedPatternGroups = extractShortURLParameters(vitroRequest);
        		
        		Map<String, String> parametersForVis = getParamatersForVis(matchedPatternGroups, vitroRequest);
        		
				return visRequestHandler.generateVisualizationForShortURLRequests(
								parametersForVis,
								vitroRequest,
								log,
								dataset);
				
			} catch (MalformedQueryParametersException e) {
				return UtilityFunctions.handleMalformedParameters(
						"Standard Visualization Query Error - Individual Publication Count", 
						e.getMessage(), 
						vitroRequest);
			}
        	
        } else {
        	
    		String errorMessage = "Data Model Empty &/or Inappropriate " 
    									+ "query parameters were submitted. ";
    		
    		log.error(errorMessage);
    		
    		return UtilityFunctions.handleMalformedParameters("Visualization Query Error", 
    														  errorMessage, 
    														  vitroRequest);
        }
	}

	private Map<String, String> getParamatersForVis(List<String> matchedPatternGroups, 
													VitroRequest vitroRequest) {
		
		Map<String, String> parameters = new HashMap<String, String>();
		
		/*
		 * We need to convert the short-form URI into a long form. So we use the 
		 * default namespace to construct one. 
		 * Since VIVO allows non-default namespaces, there are chances that short URLs 
		 * will have a "uri" parameter instead of individual uri being part of the formal
		 * url.
		 * */
		String subjectURI = null;
		if (matchedPatternGroups.size() <= 1) {
			
			subjectURI = vitroRequest.getParameter(VisualizationFrameworkConstants.INDIVIDUAL_URI_KEY);
			
		} else {
			
			subjectURI = vitroRequest.getWebappDaoFactory().getDefaultNamespace() 
							+ matchedPatternGroups.get(1);
		}
		
		subjectURI = StringEscapeUtils.escapeHtml(subjectURI);
		parameters.put(VisualizationFrameworkConstants.INDIVIDUAL_URI_KEY, subjectURI);

		if (VisualizationFrameworkConstants.COAUTHORSHIP_VIS_SHORT_URL
				.equalsIgnoreCase(matchedPatternGroups.get(0))) {
			
			parameters.put(VisualizationFrameworkConstants.VIS_MODE_KEY, 
						   VisualizationFrameworkConstants.COAUTHOR_VIS_MODE);
			
		} else if (VisualizationFrameworkConstants.COINVESTIGATOR_VIS_SHORT_URL
				.equalsIgnoreCase(matchedPatternGroups.get(0))) {
			
			parameters.put(VisualizationFrameworkConstants.VIS_MODE_KEY, 
					   VisualizationFrameworkConstants.COPI_VIS_MODE);
		} else {
			
			/*
			 * Currently temporal vis for both grants & publications do not require use of 
			 * vis_modes in their request handlers, so no need to provide anything other than 
			 * the URI.
			 * */
			
		}
		
		return parameters;
	}


	private VisualizationRequestHandler getVisualizationRequestHandler(
				VitroRequest vitroRequest) {
		
		String visType = null;
		
		VisualizationRequestHandler visRequestHandler = null;
		
		List<String> matchedPatternGroups = extractShortURLParameters(vitroRequest);

		if (matchedPatternGroups.size() > 0) {
		
//			System.out.println(matchedPatternGroups.get(0) + " --> " + matchedPatternGroups.get(1));
//			
//			System.out.println(vitroRequest.getRequestURI() 
//						+ " -- " + vitroRequest.getContextPath()
//						+ " -- " + vitroRequest.getContextPath().length()
//						+ " -- " + vitroRequest.getRequestURI().substring(vitroRequest.getContextPath().length()));
			
			visType = matchedPatternGroups.get(0);
 
	    	try {
	    		visRequestHandler = VisualizationsDependencyInjector
	    									.getVisualizationIDsToClassMap(getServletContext())
	    											.get(visType);
	    	} catch (NullPointerException nullKeyException) {
	    		/*
	    		 * Let the default flow take care of returning a null.
	    		 * */
			}
    	
		}
    	
		return visRequestHandler;
	}

	/**
	 * An ideal short url request would mimic,
	 * 		vivo.com/vis/author-network/shortURI
	 *  	vivo.com/vis/grant-graph/shortURI
	 * etc. So first we obtain the request url which can be used to extract the requested visualization
	 * and the subject of the visualization. So the below pattern matcher will take "/vis/<vis-name>/<shortURI>"
	 * as an input.
	 */
	private List<String> extractShortURLParameters(VitroRequest vitroRequest) {

		List<String> matchedGroups = new ArrayList<String>(); 
		String subURIString = vitroRequest.getRequestURI().substring(vitroRequest.getContextPath().length()+1);
		String[] urlParams = StringEscapeUtils.escapeHtml(subURIString).split("/");
		
		if (urlParams.length > 1 
				&& urlParams[0].equalsIgnoreCase("vis")) {
			for (int ii=1; ii < urlParams.length; ii++) {
				matchedGroups.add(urlParams[ii]);
			}
		}
		
		return matchedGroups;
	}

	private Dataset setupJENADataSource(VitroRequest vreq) {

        log.debug("rdfResultFormat was: " + VisConstants.RDF_RESULT_FORMAT_PARAM);

        return vreq.getDataset();
	}

}

