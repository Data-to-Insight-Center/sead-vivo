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
package edu.cornell.mannlib.vitro.webapp.sparql;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.mannlib.vedit.controller.BaseEditController;
import edu.cornell.mannlib.vitro.webapp.auth.permissions.SimplePermission;
import edu.cornell.mannlib.vitro.webapp.beans.ObjectProperty;
import edu.cornell.mannlib.vitro.webapp.beans.PropertyInstance;
import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;
import edu.cornell.mannlib.vitro.webapp.dao.ObjectPropertyDao;
import edu.cornell.mannlib.vitro.webapp.dao.PropertyInstanceDao;
import edu.cornell.mannlib.vitro.webapp.dao.VClassDao;

/**
 * This servlet gets all the object properties for a given subject.
 * 
 * @param vClassURI
 * @author yuysun
 */

public class GetClazzObjectProperties extends BaseEditController {
	private static final Log log = LogFactory.getLog(GetClazzObjectProperties.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isAuthorizedToDisplayPage(request, response,
				SimplePermission.USE_MISCELLANEOUS_PAGES.ACTIONS)) {
        	return;
		}

		VitroRequest vreq = new VitroRequest(request);

		String vClassURI = vreq.getParameter("vClassURI");
		if (vClassURI == null || vClassURI.trim().equals("")) {
			return;
		}

		String respo = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		respo += "<options>";

		ObjectPropertyDao odao = vreq.getFullWebappDaoFactory()
				.getObjectPropertyDao();
		PropertyInstanceDao piDao = vreq.getFullWebappDaoFactory()
				.getPropertyInstanceDao();
		VClassDao vcDao = vreq.getFullWebappDaoFactory().getVClassDao();

		// incomplete list of classes to check, but better than before
		List<String> superclassURIs = vcDao.getAllSuperClassURIs(vClassURI);
		superclassURIs.add(vClassURI);
		superclassURIs.addAll(vcDao.getEquivalentClassURIs(vClassURI));

		Map<String, PropertyInstance> propInstMap = new HashMap<String, PropertyInstance>();
		for (String classURI : superclassURIs) {
			Collection<PropertyInstance> propInsts = piDao
					.getAllPropInstByVClass(classURI);
			try {
				for (PropertyInstance propInst : propInsts) {
					propInstMap.put(propInst.getPropertyURI(), propInst);
				}
			} catch (NullPointerException ex) {
				continue;
			}
		}
		List<PropertyInstance> propInsts = new ArrayList<PropertyInstance>();
		propInsts.addAll(propInstMap.values());
		Collections.sort(propInsts);

		Iterator propInstIt = propInsts.iterator();
		HashSet opropURIs = new HashSet();
		while (propInstIt.hasNext()) {
			PropertyInstance pi = (PropertyInstance) propInstIt.next();
			if (!(opropURIs.contains(pi.getPropertyURI()))) {
				opropURIs.add(pi.getPropertyURI());
				ObjectProperty oprop = (ObjectProperty) odao
						.getObjectPropertyByURI(pi.getPropertyURI());
				if (oprop != null) {
					respo += "<option>" + "<key>" + oprop.getLocalName()
							+ "</key>" + "<value>" + oprop.getURI()
							+ "</value>" + "</option>";
				}
			}
		}
		respo += "</options>";
		response.setContentType("text/xml");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		out.println(respo);
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}
}
