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

package edu.cornell.mannlib.vitro.webapp.auth.policy;

import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import stubs.edu.cornell.mannlib.vitro.webapp.auth.policy.bean.PropertyRestrictionPolicyHelperStub;
import stubs.javax.servlet.ServletContextStub;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.impl.RDFDefaultErrorHandler;

import edu.cornell.mannlib.vitro.testing.AbstractTestClass;
import edu.cornell.mannlib.vitro.webapp.auth.identifier.ArrayIdentifierBundle;
import edu.cornell.mannlib.vitro.webapp.auth.identifier.Identifier;
import edu.cornell.mannlib.vitro.webapp.auth.identifier.IdentifierBundle;
import edu.cornell.mannlib.vitro.webapp.auth.identifier.common.HasProfile;
import edu.cornell.mannlib.vitro.webapp.auth.policy.bean.PropertyRestrictionPolicyHelper;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.Authorization;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.PolicyDecision;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.propstmt.AddObjectPropertyStatement;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.propstmt.EditDataPropertyStatement;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.propstmt.EditObjectPropertyStatement;
import edu.cornell.mannlib.vitro.webapp.beans.Individual;
import edu.cornell.mannlib.vitro.webapp.beans.IndividualImpl;
import edu.cornell.mannlib.vitro.webapp.dao.VitroVocabulary;

public class SelfEditingPolicy_2_Test extends AbstractTestClass {
	private static final Log log = LogFactory
			.getLog(SelfEditingPolicy_2_Test.class);

	/** We may edit objects in this arbitrary namespace. */
	private static final String SAFE_NS = "http://test.mannlib.cornell.edu/ns/01#";

	/** We are not allowed to edit objects in the administrative namespace. */
	private static final String ADMIN_NS = VitroVocabulary.vitroURI;

	/** The URI of a SelfEditor. */
	private static final String SELFEDITOR_URI = SAFE_NS + "individual000";

	/** Some things that are safe to edit. */
	private static final String SAFE_RESOURCE = SAFE_NS + "individual123";
	private static final String SAFE_PREDICATE = SAFE_NS + "hasHairStyle";

	/** Some things that are not safe to edit. */
	private static final String ADMIN_RESOURCE = ADMIN_NS + "individual666";
	private static final String ADMIN_PREDICATE_1 = ADMIN_NS + "hasSuperPowers";
	private static final String ADMIN_PREDICATE_2 = ADMIN_NS + "mayPrintMoney";
	private static final String ADMIN_PREDICATE_3 = ADMIN_NS
			+ "getsOutOfJailFree";
	private static final String ADMIN_PREDICATE_4 = ADMIN_NS + "canDeleteModel";

	/** The policy we are testing. */
	SelfEditingPolicy policy;

	/** A SelfEditing individual identifier. */
	Individual seIndividual;

	/** A bundle that contains a SelfEditing individual. */
	IdentifierBundle ids;

	/**
	 * An empty model that acts as a placeholder in the requested actions. The
	 * SelfEditingPolicy does not base its decisions on the contents of the
	 * model.
	 */
	private OntModel ontModel;

	
	@Before
	public void setUp() throws Exception {
		InputStream is = getClass().getResourceAsStream(
				"./SelfEditingPolicy_2_Test.xml");
		Assert.assertNotNull(is);

		// suppress the warning messages from loading the model.
		setLoggerLevel(RDFDefaultErrorHandler.class, Level.OFF);

		// TODO This doesn't appear to be used for anything. Can it go away, along with the data file?
		OntModel model = ModelFactory.createOntologyModel();
		model.read(is, "");
		Assert.assertNotNull(model);
		Assert.assertTrue(model.size() > 0);

		ServletContextStub ctx = new ServletContextStub();
		PropertyRestrictionPolicyHelper.setBean(ctx,
				PropertyRestrictionPolicyHelperStub
						.getInstance(new String[] { ADMIN_NS }));

		policy = new SelfEditingPolicy(ctx);
		Assert.assertNotNull(policy);

		seIndividual = new IndividualImpl();
		seIndividual.setURI(SELFEDITOR_URI);

		ids = new ArrayIdentifierBundle(new HasProfile(SELFEDITOR_URI));

		ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

		// setLoggerLevel(SelfEditingPolicySetupTest.class, Level.DEBUG);
	}

	// ----------------------------------------------------------------------
	// General tests
	// ----------------------------------------------------------------------

	@Test
	public void nullRequestedAction() {
		PolicyDecision dec = policy.isAuthorized(ids, null);
		Assert.assertNotNull(dec);
		Assert.assertEquals(Authorization.INCONCLUSIVE, dec.getAuthorized());
	}

	@Test
	public void nullIdentifierBundle() {
		AddObjectPropertyStatement whatToAuth = new AddObjectPropertyStatement(
				ontModel, SELFEDITOR_URI, SAFE_PREDICATE, SAFE_RESOURCE);
		PolicyDecision dec = policy.isAuthorized(null, whatToAuth);
		Assert.assertNotNull(dec);
		Assert.assertEquals(Authorization.INCONCLUSIVE, dec.getAuthorized());
	}

	@Test
	public void noSelfEditorIdentifier() {
		ids.clear();
		ids.add(new Identifier() { /* empty identifier */
		});
		assertAddObjectPropStmt(SELFEDITOR_URI, SAFE_PREDICATE, SAFE_RESOURCE,
				Authorization.INCONCLUSIVE);
	}

	// ----------------------------------------------------------------------
	// Tests against AddObjectPropStmt
	// ----------------------------------------------------------------------

	@Test
	public void addObjectPropStmtSuccess1() {
		assertAddObjectPropStmt(SELFEDITOR_URI, SAFE_PREDICATE, SAFE_RESOURCE,
				Authorization.AUTHORIZED);
	}

	@Test
	public void addObjectPropStmtSuccess2() {
		assertAddObjectPropStmt(SAFE_RESOURCE, SAFE_PREDICATE, SELFEDITOR_URI,
				Authorization.AUTHORIZED);
	}

	@Test
	public void addObjectPropStmtUnsafePredicate1() {
		assertAddObjectPropStmt(SELFEDITOR_URI, ADMIN_PREDICATE_1,
				SAFE_RESOURCE, Authorization.INCONCLUSIVE);
	}

	@Test
	public void addObjectPropStmtUnsafePredicate2() {
		assertAddObjectPropStmt(SAFE_RESOURCE, ADMIN_PREDICATE_1,
				SELFEDITOR_URI, Authorization.INCONCLUSIVE);
	}

	@Test
	public void addObjectPropStmtUnsafePredicate3() {
		assertAddObjectPropStmt(SELFEDITOR_URI, ADMIN_PREDICATE_2,
				SAFE_RESOURCE, Authorization.INCONCLUSIVE);
	}

	@Test
	public void addObjectPropStmtUnsafePredicate4() {
		assertAddObjectPropStmt(SELFEDITOR_URI, ADMIN_PREDICATE_3,
				SAFE_RESOURCE, Authorization.INCONCLUSIVE);
	}

	@Test
	public void addObjectPropStmtUnsafePredicate5() {
		assertAddObjectPropStmt(SELFEDITOR_URI, ADMIN_PREDICATE_4,
				SAFE_RESOURCE, Authorization.INCONCLUSIVE);
	}

	// ----------------------------------------------------------------------
	// Tests against EditObjPropStmt
	// ----------------------------------------------------------------------

	@Test
	public void editObjectPropStmtSuccess1() {
		assertEditObjPropStmt(SELFEDITOR_URI, SAFE_PREDICATE, SAFE_RESOURCE,
				Authorization.AUTHORIZED);
	}

	@Test
	public void editObjectPropStmtSuccess2() {
		assertEditObjPropStmt(SAFE_RESOURCE, SAFE_PREDICATE, SELFEDITOR_URI,
				Authorization.AUTHORIZED);
	}

	@Test
	public void editObjectPropStmtEditorNotInvolved() {
		// this is the case where the editor is not part of the stmt
		assertEditObjPropStmt(SAFE_RESOURCE, SAFE_PREDICATE, SAFE_RESOURCE,
				Authorization.INCONCLUSIVE);
	}

	@Test
	public void editObjectPropStmtUnsafeResource() {
		assertEditObjPropStmt(SELFEDITOR_URI, SAFE_PREDICATE, ADMIN_RESOURCE,
				Authorization.INCONCLUSIVE);
	}

	@Test
	public void editObjectPropStmtUnsafePredicate1() {
		assertEditObjPropStmt(SELFEDITOR_URI, ADMIN_PREDICATE_4, SAFE_RESOURCE,
				Authorization.INCONCLUSIVE);
	}

	@Test
	public void editObjectPropStmtUnsafePredicate2() {
		assertEditObjPropStmt(SAFE_RESOURCE, ADMIN_PREDICATE_4, SELFEDITOR_URI,
				Authorization.INCONCLUSIVE);
	}

	@Test
	public void editObjectPropStmtUnsafeBoth() {
		assertEditObjPropStmt(SELFEDITOR_URI, ADMIN_PREDICATE_4,
				ADMIN_RESOURCE, Authorization.INCONCLUSIVE);
	}

	// ----------------------------------------------------------------------
	// Tests against EditDataPropStmt
	// ----------------------------------------------------------------------

	@Test
	public void editDataPropSuccess() {
		assertEditDataPropStmt(SELFEDITOR_URI, SAFE_PREDICATE, "junk",
				Authorization.AUTHORIZED);
	}

	@Test
	public void editDataPropUnsafePredicate() {
		assertEditDataPropStmt(SELFEDITOR_URI, ADMIN_PREDICATE_1, "junk",
				Authorization.INCONCLUSIVE);
	}

	@Test
	public void editDataPropUnsafeResource() {
		assertEditDataPropStmt(ADMIN_RESOURCE, SAFE_PREDICATE, null,
				Authorization.INCONCLUSIVE);
	}

	@Test
	public void editDataPropNoCloseRelation() {
		assertEditDataPropStmt(SAFE_RESOURCE, SAFE_PREDICATE, null,
				Authorization.INCONCLUSIVE);
	}

	@Test
	public void editDataPropModelProhibited() {
		// model prohibited
		assertEditDataPropStmt(SAFE_RESOURCE, ADMIN_PREDICATE_1, null,
				Authorization.INCONCLUSIVE);
	}

	// ------------------------------------------------------------------------
	// Support methods
	// ------------------------------------------------------------------------

	/**
	 * Create an {@link AddObjectPropertyStatement}, test it, and compare to
	 * expected results.
	 */
	private void assertAddObjectPropStmt(String uriOfSub, String uriOfPred,
			String uriOfObj, Authorization expectedAuthorization) {
		AddObjectPropertyStatement whatToAuth = new AddObjectPropertyStatement(
				ontModel, uriOfSub, uriOfPred, uriOfObj);
		PolicyDecision dec = policy.isAuthorized(ids, whatToAuth);
		log.debug(dec);
		Assert.assertNotNull(dec);
		Assert.assertEquals(expectedAuthorization, dec.getAuthorized());
	}

	/**
	 * Create an {@link EditObjectPropertyStatement}, test it, and compare to
	 * expected results.
	 */
	private void assertEditObjPropStmt(String uriOfSub, String uriOfPred,
			String uriOfObj, Authorization expectedAuthorization) {
		EditObjectPropertyStatement whatToAuth = new EditObjectPropertyStatement(
				ontModel, uriOfSub, uriOfPred, uriOfObj);
		PolicyDecision dec = policy.isAuthorized(ids, whatToAuth);
		log.debug(dec);
		Assert.assertNotNull(dec);
		Assert.assertEquals(expectedAuthorization, dec.getAuthorized());
	}

	/**
	 * Create an {@link EditDataPropertyStatement}, test it, and compare to
	 * expected results.
	 */
	private void assertEditDataPropStmt(String individualURI,
			String datapropURI, String data, Authorization expectedAuthorization) {
		EditDataPropertyStatement whatToAuth = new EditDataPropertyStatement(
				ontModel, individualURI, datapropURI);
		PolicyDecision dec = policy.isAuthorized(ids, whatToAuth);
		log.debug(dec);
		Assert.assertNotNull(dec);
		Assert.assertEquals(expectedAuthorization, dec.getAuthorized());
	}
}
