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

package edu.cornell.mannlib.vitro.webapp.controller.ajax;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.DatasetFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.Model;

import edu.cornell.mannlib.vitro.webapp.auth.permissions.SimplePermission;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.Actions;
import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;
import edu.cornell.mannlib.vitro.webapp.controller.ajax.SparqlUtils.AjaxControllerException;
import edu.cornell.mannlib.vitro.webapp.dao.jena.OntModelSelector;

/**
 * Handle an AJAX request for a SPARQL query. On entry, the "query" parameter
 * contains the query string.
 * 
 * The result is delivered in JSON format.
 */
public class SparqlQueryAjaxController extends VitroAjaxController {
	private static final Log log = LogFactory
			.getLog(SparqlQueryAjaxController.class);

	public static final String PARAMETER_QUERY = "query";
	public static final String RESPONSE_MIME_TYPE = "application/javascript";

	public static final String PARAMETER_MODEL = "model";
	public static final String OPTION_MODEL_FULL = "full";
	public static final String OPTION_MODEL_USER_ACCOUNTS = "userAccounts";

	@Override
	protected Actions requiredActions(VitroRequest vreq) {
		String modelParam = getModelParam(vreq);
		if (OPTION_MODEL_USER_ACCOUNTS.equals(modelParam)) {
			return SimplePermission.QUERY_USER_ACCOUNTS_MODEL.ACTIONS;
		} else {
			return SimplePermission.QUERY_FULL_MODEL.ACTIONS;
		}
	}

	@Override
	protected void doRequest(VitroRequest vreq, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String modelParam = getModelParam(vreq);
			Model model = locateModel(vreq, modelParam);
			String queryParam = locateQueryParam(vreq);
			Query query = SparqlUtils.createQuery(queryParam);
			SparqlUtils.executeQuery(response, query, model);
			return;
		} catch (AjaxControllerException e) {
			log.error(e.getMessage());
			response.sendError(e.getStatusCode());
		}
	}

	private String getModelParam(HttpServletRequest req) {
		String modelParam = req.getParameter(PARAMETER_MODEL);
		log.debug("modelParam was: " + modelParam);
		if ((modelParam != null) && (!modelParam.isEmpty())) {
			return modelParam;
		} else {
			return OPTION_MODEL_FULL;
		}

	}

	private Model locateModel(VitroRequest vreq, String modelParam)
			throws AjaxControllerException {
		Object o = getServletContext().getAttribute("baseOntModelSelector");
		if (!(o instanceof OntModelSelector)) {
			throw new AjaxControllerException(SC_INTERNAL_SERVER_ERROR,
					"OntModelSelector not found");
		}
		OntModelSelector oms = (OntModelSelector) o;

		Model model = null;
		if (OPTION_MODEL_USER_ACCOUNTS.equals(modelParam)) {
			model = oms.getUserAccountsModel();
		} else {
			// TODO What is the appropriate way to do this?
			// model = oms.getFullModel();
			model = vreq.getJenaOntModel();
		}
		if (model == null) {
			throw new AjaxControllerException(SC_INTERNAL_SERVER_ERROR,
					"Model '' not found.");
		}

		return model;
	}

	private String locateQueryParam(VitroRequest vreq)
			throws AjaxControllerException {
		String queryParam = vreq.getParameter(PARAMETER_QUERY);
		log.debug("queryParam was: " + queryParam);
		if ((queryParam != null) && (!queryParam.isEmpty())) {
			return queryParam;
		} else {
			throw new AjaxControllerException(SC_NOT_FOUND, "'"
					+ PARAMETER_QUERY + "' parameter is required");
		}
	}
	
}
