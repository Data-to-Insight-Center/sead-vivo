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

package edu.cornell.mannlib.vitro.webapp.dao.jena;

import com.hp.hpl.jena.rdf.model.ModelChangedListener;

import edu.cornell.mannlib.vitro.webapp.servlet.setup.JenaDataSourceSetupBase;

public class ABoxJenaChangeListener extends JenaChangeListener {

    public ABoxJenaChangeListener(ModelChangedListener listener) {
        super(listener);
        ignoredGraphs.add(JenaDataSourceSetupBase.JENA_INF_MODEL);
        ignoredGraphs.add(JenaDataSourceSetupBase.JENA_TBOX_ASSERTIONS_MODEL);
        ignoredGraphs.add(JenaDataSourceSetupBase.JENA_TBOX_INF_MODEL);
    }
    
    @Override
    public void addedStatement(String serializedTriple, String graphURI) {
        if (isABoxGraph(graphURI)) {
            super.addedStatement(serializedTriple, graphURI);
        }
    }

    @Override
    public void removedStatement(String serializedTriple, String graphURI) {
        if (isABoxGraph(graphURI)) {
            super.removedStatement(serializedTriple, graphURI);
        }
    }
    
    private boolean isABoxGraph(String graphURI) {
        return (graphURI == null || 
                        JenaDataSourceSetupBase.JENA_DB_MODEL.equals(graphURI) 
                                || (!ignoredGraphs.contains(graphURI) 
                                        && !graphURI.contains("filegraph") 
                                                && !graphURI.contains("tbox")));
    }
    
}
