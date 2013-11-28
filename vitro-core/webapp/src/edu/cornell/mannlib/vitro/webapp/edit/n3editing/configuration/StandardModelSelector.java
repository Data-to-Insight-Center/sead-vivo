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

package edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.hpl.jena.ontology.OntModel;

import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;
import edu.cornell.mannlib.vitro.webapp.dao.jena.OntModelSelector;

public class StandardModelSelector implements ModelSelector {

    private static final Log log = LogFactory.getLog(StandardModelSelector.class);
    
    public OntModel getModel(HttpServletRequest request, ServletContext context) {
        VitroRequest vreq = new VitroRequest( request );        
        
        Object sessionOntModel = null;
        if( vreq.getSession() != null) {
            OntModelSelector oms = (OntModelSelector) vreq.getSession()
            							.getAttribute("unionOntModelSelector");
            if (oms != null) {
            	sessionOntModel = oms.getABoxModel();
            }
        }
        if(sessionOntModel != null && sessionOntModel instanceof OntModel ) {
            log.debug("using OntModelSelector from session");
            return (OntModel)sessionOntModel;
        } else if (vreq.getOntModelSelector() != null) {
            log.debug("using OntModelSelector from request");
            return vreq.getOntModelSelector().getABoxModel();
        } else {
            log.debug("using OntModelSelector from context");
            return ((OntModelSelector) context
            			.getAttribute("unionOntModelSelector")).getABoxModel();
        }
    }
    
    public static final ModelSelector selector = new StandardModelSelector();

}