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

package edu.cornell.mannlib.vitro.webapp.visualization.persongrantcount;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;

import com.hp.hpl.jena.query.Dataset;

import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.Actions;
import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;
import edu.cornell.mannlib.vitro.webapp.controller.freemarker.responsevalues.ResponseValues;
import edu.cornell.mannlib.vitro.webapp.controller.freemarker.responsevalues.TemplateResponseValues;
import edu.cornell.mannlib.vitro.webapp.controller.visualization.DataVisualizationController;
import edu.cornell.mannlib.vitro.webapp.controller.visualization.VisualizationFrameworkConstants;
import edu.cornell.mannlib.vitro.webapp.visualization.exceptions.MalformedQueryParametersException;
import edu.cornell.mannlib.vitro.webapp.visualization.valueobjects.Activity;
import edu.cornell.mannlib.vitro.webapp.visualization.valueobjects.SparklineData;
import edu.cornell.mannlib.vitro.webapp.visualization.valueobjects.SubEntity;
import edu.cornell.mannlib.vitro.webapp.visualization.visutils.SelectOnModelUtilities;
import edu.cornell.mannlib.vitro.webapp.visualization.visutils.UtilityFunctions;
import edu.cornell.mannlib.vitro.webapp.visualization.visutils.VisualizationRequestHandler;


/**
 * 
 * This request handler is used to serve the content related to an individual's
 * grants over the years like,
 * 		1. Sparkline representing this
 * 		2. An entire page dedicated to the sparkline vis which will also have links to
 * download the data using which the sparkline was rendered & its tabular representation etc.
 * 		3. Downloadable CSV file containing number of grants over the years.
 * 		4. Downloadable PDf file containing the grant content, among other things. 
 * Currently this is disabled because the feature is half-baked. We plan to activate this in 
 * the next major release.  
 * 
 * @author bkoniden
 * Deepak Konidena
 */
public class PersonGrantCountRequestHandler implements VisualizationRequestHandler {
	
	@Override
	public Map<String, String> generateDataVisualization(
			VitroRequest vitroRequest, Log log, Dataset dataset)
			throws MalformedQueryParametersException {

		String personURI = vitroRequest
				.getParameter(VisualizationFrameworkConstants.INDIVIDUAL_URI_KEY);
		
		SubEntity person = new SubEntity(
									personURI,
									UtilityFunctions.getIndividualLabelFromDAO(vitroRequest, personURI));
		
		Map<String, Activity> grantsToURI = SelectOnModelUtilities.getGrantsForPerson(dataset, person, false);
		
		
		/*
    	 * Create a map from the year to number of grants. Use the Grant's
    	 * parsedGrantYear to populate the data.
    	 * */
    	Map<String, Integer> yearToGrantCount = 
			UtilityFunctions.getYearToActivityCount(grantsToURI.values());
	
    	return prepareDataResponse(person,
				yearToGrantCount);

	
	}
	
	@Override
	public ResponseValues generateVisualizationForShortURLRequests(
			Map<String, String> parameters, VitroRequest vitroRequest, Log log,
			Dataset dataSource) throws MalformedQueryParametersException {
		throw new UnsupportedOperationException("Person Grant Count Visualization does not provide " 
					+ "Short URL Response.");
	}

	@Override
	public Object generateAjaxVisualization(VitroRequest vitroRequest, Log log,
			Dataset dataset) throws MalformedQueryParametersException {

		String personURI = vitroRequest
		.getParameter(VisualizationFrameworkConstants.INDIVIDUAL_URI_KEY);

		String visMode = vitroRequest
				.getParameter(VisualizationFrameworkConstants.VIS_MODE_KEY);
		
		String visContainer = vitroRequest
				.getParameter(VisualizationFrameworkConstants.VIS_CONTAINER_KEY);
		
		SubEntity person = new SubEntity(
				personURI,
				UtilityFunctions.getIndividualLabelFromDAO(vitroRequest, personURI));

		Map<String, Activity> grantsToURI = SelectOnModelUtilities.getGrantsForPerson(dataset, person, false);
		
    	/*
    	 * Create a map from the year to number of grants. Use the Grant's
    	 * parsedGrantYear to populate the data.
    	 * */
    	Map<String, Integer> yearToGrantCount = 
			UtilityFunctions.getYearToActivityCount(grantsToURI.values());
    	

		boolean shouldVIVOrenderVis = yearToGrantCount.size() > 0 ? true : false;
			
			/*
	    	 * Computations required to generate HTML for the sparkline & related context.
	    	 * */
	    	PersonGrantCountVisCodeGenerator visualizationCodeGenerator = 
	    		new PersonGrantCountVisCodeGenerator(personURI,
	    									   visMode,
	    									   visContainer,
	    									   yearToGrantCount,
	    									   log);


	    	SparklineData sparklineData = visualizationCodeGenerator
			.getValueObjectContainer();
	    	
	    	return prepareDynamicResponse(vitroRequest, 
			   		  sparklineData, 
			   		shouldVIVOrenderVis);

		
	}
	
	@Override
	public ResponseValues generateStandardVisualization(
			VitroRequest vitroRequest, Log log, Dataset dataset)
			throws MalformedQueryParametersException {
		
		String personURI = vitroRequest
				.getParameter(VisualizationFrameworkConstants.INDIVIDUAL_URI_KEY);

		String visMode = vitroRequest
				.getParameter(VisualizationFrameworkConstants.VIS_MODE_KEY);

		String visContainer = vitroRequest
				.getParameter(VisualizationFrameworkConstants.VIS_CONTAINER_KEY);
		
		SubEntity person = new SubEntity(
				personURI,
				UtilityFunctions.getIndividualLabelFromDAO(vitroRequest, personURI));

		Map<String, Activity> grantsToURI = SelectOnModelUtilities.getGrantsForPerson(dataset, person, false);
		
    	/*
    	 * Create a map from the year to number of grants. Use the Grant's
    	 * parsedGrantYear to populate the data.
    	 * */
    	Map<String, Integer> yearToGrantCount = 
			UtilityFunctions.getYearToActivityCount(grantsToURI.values());
	
    	/*
    	 * Computations required to generate HTML for the sparkline & related context.
    	 * */
    	PersonGrantCountVisCodeGenerator visualizationCodeGenerator = 
    		new PersonGrantCountVisCodeGenerator(personURI,
    									   visMode,
    									   visContainer,
    									   yearToGrantCount,
    									   log);
    	
    	SparklineData sparklineData = visualizationCodeGenerator
											.getValueObjectContainer();
    	
			return prepareStandaloneResponse(vitroRequest, 
    							  sparklineData);
	}
	
	private String getGrantsOverTimeCSVContent(Map<String, Integer> yearToGrantCount) {
		
		StringBuilder csvFileContent = new StringBuilder();
		
		csvFileContent.append("Year, Grants\n");

		for (Entry<String, Integer> currentEntry : yearToGrantCount.entrySet()) {
			csvFileContent.append(StringEscapeUtils.escapeCsv(currentEntry.getKey()));
			csvFileContent.append(",");
			csvFileContent.append(currentEntry.getValue());
			csvFileContent.append("\n");
		}

		return csvFileContent.toString();
	}
	
	/**
	 * Provides response when csv file containing the grant count over the years
	 * is requested.
	 * @param investigator
	 * @param yearToGrantCount
	 * @return 
	 */
	private Map<String, String> prepareDataResponse(
						SubEntity investigator,
						Map<String, Integer> yearToGrantCount) {
		
		String piName = investigator.getIndividualLabel();
		
		String outputFileName = UtilityFunctions.slugify(piName) 
										+ "_grants-per-year" + ".csv";

		Map<String, String> fileData = new HashMap<String, String>();
		fileData.put(DataVisualizationController.FILE_NAME_KEY, 
					 outputFileName);
		fileData.put(DataVisualizationController.FILE_CONTENT_TYPE_KEY, 
					 "application/octet-stream");
		fileData.put(DataVisualizationController.FILE_CONTENT_KEY, 
					getGrantsOverTimeCSVContent(yearToGrantCount));

		return fileData;
	}
	
	/**
	 * Provides response when an entire page dedicated to grant sparkline is requested.
	 * @param vreq
	 * @param valueObjectContainer
	 * @return 
	 */
	private TemplateResponseValues prepareStandaloneResponse(VitroRequest vreq,
			SparklineData valueObjectContainer) {
        
        String standaloneTemplate = "personGrantCountStandaloneActivator.ftl";

        Map<String, Object> body = new HashMap<String, Object>();
        body.put("title", "Individual Grant Count visualization");
        body.put("sparklineVO", valueObjectContainer);

        return new TemplateResponseValues(standaloneTemplate, body);
        
	}
	
	/**
	 * Provides response when the grant sparkline has to be rendered in already existing 
	 * page, e.g. profile page.
	 * @param vreq
	 * @param valueObjectContainer
	 * @param yearToGrantCount
	 * @return 
	 */
	private TemplateResponseValues prepareDynamicResponse(
			VitroRequest vreq,
			SparklineData valueObjectContainer, 
			boolean shouldVIVOrenderVis) {

        String dynamicTemplate = "personGrantCountDynamicActivator.ftl";

        Map<String, Object> body = new HashMap<String, Object>();
        body.put("sparklineVO", valueObjectContainer);
        body.put("shouldVIVOrenderVis", shouldVIVOrenderVis);
        
        return new TemplateResponseValues(dynamicTemplate, body);
        
	}

	@Override
	public Actions getRequiredPrivileges() {
		// TODO Auto-generated method stub
		return null;
	}

}