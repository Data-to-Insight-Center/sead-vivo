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

package edu.cornell.mannlib.vitro.webapp.visualization.tools;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;

import com.hp.hpl.jena.query.Dataset;

import edu.cornell.mannlib.vitro.webapp.auth.permissions.SimplePermission;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.Actions;
import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;
import edu.cornell.mannlib.vitro.webapp.controller.freemarker.responsevalues.ResponseValues;
import edu.cornell.mannlib.vitro.webapp.controller.freemarker.responsevalues.TemplateResponseValues;
import edu.cornell.mannlib.vitro.webapp.visualization.exceptions.MalformedQueryParametersException;
import edu.cornell.mannlib.vitro.webapp.visualization.visutils.VisualizationRequestHandler;

public class ToolsRequestHandler implements VisualizationRequestHandler {
	
	public static final Actions REQUIRED_ACTIONS = SimplePermission.REFRESH_VISUALIZATION_CACHE.ACTIONS;

	@Override
	public Object generateAjaxVisualization(VitroRequest vitroRequest, Log log,
			Dataset dataSource) throws MalformedQueryParametersException {
		throw new UnsupportedOperationException("Visualization Tool does not provide Ajax Response.");
	}

	@Override
	public Map<String, String> generateDataVisualization(
			VitroRequest vitroRequest, Log log, Dataset dataset)
			throws MalformedQueryParametersException {
		throw new UnsupportedOperationException("Visualization Tool does not provide Data Response.");
	}

	@Override
	public ResponseValues generateStandardVisualization(
			VitroRequest vitroRequest, Log log, Dataset dataSource)
			throws MalformedQueryParametersException {
		return renderToolsMenu(vitroRequest, log, dataSource);
	}

	@Override
	public ResponseValues generateVisualizationForShortURLRequests(
			Map<String, String> parameters, VitroRequest vitroRequest, Log log,
			Dataset dataSource) throws MalformedQueryParametersException {
		
		return renderToolsMenu(vitroRequest, log, dataSource);
	}

	@Override
	public Actions getRequiredPrivileges() {
		return REQUIRED_ACTIONS;
	}
	
	private ResponseValues renderToolsMenu(VitroRequest vitroRequest,
			Log log, Dataset dataSource) {

		String standaloneTemplate = "tools.ftl";

		Map<String, Object> body = new HashMap<String, Object>();
		body.put("title", "Visualization Tools");

		return new TemplateResponseValues(standaloneTemplate, body);
	} 

}
