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

package edu.cornell.mannlib.vitro.webapp.auth.requestedAction.propstmt;

import com.hp.hpl.jena.ontology.OntModel;

import edu.cornell.mannlib.vitro.webapp.beans.ObjectPropertyStatement;

/**
 * A base class for requested actions that involve adding, editing, or deleting
 * object property statements from a model.
 */
public abstract class AbstractObjectPropertyStatementAction extends
		AbstractPropertyStatementAction {
	private final String subjectUri;
	private final String predicateUri;
	private final String objectUri;

	public AbstractObjectPropertyStatementAction(OntModel ontModel, String subjectUri,
			String predicateUri, String objectUri) {
		super(ontModel);
		this.subjectUri = subjectUri;
		this.predicateUri = predicateUri;
		this.objectUri = objectUri;
	}

	public AbstractObjectPropertyStatementAction(OntModel ontModel, ObjectPropertyStatement ops) {
		super(ontModel);
		this.subjectUri = (ops.getSubject() == null) ? ops.getSubjectURI()
				: ops.getSubject().getURI();
		this.predicateUri = (ops.getProperty() == null) ? ops.getPropertyURI()
				: ops.getProperty().getURI();
		this.objectUri = (ops.getObject() == null) ? ops.getObjectURI() : ops
				.getObject().getURI();
	}

	public String getSubjectUri() {
		return subjectUri;
	}

	public String getObjectUri() {
		return objectUri;
	}
	
	@Override
	public String getPredicateUri() {
		return predicateUri;
	}

	@Override
	public String[] getResourceUris() {
		return new String[] {subjectUri, objectUri};
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": <" + subjectUri + "> <"
				+ predicateUri + "> <" + objectUri + ">";
	}
}
