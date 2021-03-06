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

package edu.cornell.mannlib.vitro.webapp.controller.accounts.manageproxies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.mannlib.vitro.webapp.beans.Individual;
import edu.cornell.mannlib.vitro.webapp.beans.SelfEditingConfiguration;
import edu.cornell.mannlib.vitro.webapp.beans.UserAccount;
import edu.cornell.mannlib.vitro.webapp.controller.AbstractPageHandler;
import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;

/**
 * Create a new relationship, or set of relationships, between zero or more
 * proxies and zero or more profiles.
 * 
 * Note that this is additive, so if a proxy already has some profiles, they
 * will be preserved, even if they are not listed here.
 * 
 * It is possible that one or more profiles might be the "self" pages of one or
 * more proxies, so as we do each proxy, we exclude any profile which is the
 * "self" for that proxy.
 * 
 * If there are zero proxies here, or zero profiles, it doesn't hurt anything,
 * it just doesn't accomplish anything either.
 * 
 * This is not really a page, in that it doesn't display anything. It's just a
 * way to separate out some of the logic of the ManageProxies list page.
 */
public class ManageProxiesCreatePage extends AbstractPageHandler {
	private static final Log log = LogFactory
			.getLog(ManageProxiesCreatePage.class);

	private static final String PARAMETER_PROXY_URI = "proxyUri";
	private static final String PARAMETER_PROFILE_URI = "profileUri";

	private final SelfEditingConfiguration selfEditingConfiguration;
	private List<String> proxyUris;
	private List<String> profileUris;
	private List<UserAccount> proxyAccounts;

	private boolean valid = true;

	public ManageProxiesCreatePage(VitroRequest vreq) {
		super(vreq);
		this.selfEditingConfiguration = SelfEditingConfiguration.getBean(vreq);

		parseParameters();
	}

	private void parseParameters() {
		try {
			proxyUris = getStringParameters(PARAMETER_PROXY_URI);
			profileUris = getStringParameters(PARAMETER_PROFILE_URI);

			proxyAccounts = findProxyAccounts();
			validateProfileUris();
		} catch (InvalidParametersException e) {
			log.error(e.getMessage());
			valid = false;
		}

	}

	private List<UserAccount> findProxyAccounts()
			throws InvalidParametersException {
		List<UserAccount> accounts = new ArrayList<UserAccount>();
		for (String proxyUri : proxyUris) {
			UserAccount proxy = userAccountsDao.getUserAccountByUri(proxyUri);
			if (proxy == null) {
				throw new InvalidParametersException(
						"Found no User Account for proxyUri='" + proxyUri + "'");
			}
			accounts.add(proxy);
		}
		return accounts;
	}

	private void validateProfileUris() throws InvalidParametersException {
		for (String profileUri : profileUris) {
			Individual ind = indDao.getIndividualByURI(profileUri);
			if (ind == null) {
				throw new InvalidParametersException(
						"Found no Individual for profileUri='" + profileUri
								+ "'");
			}
		}
	}

	public boolean isValid() {
		return valid;
	}

	/**
	 * We don't remove any existing relationships, we just add new ones. But we
	 * won't add a relationship to one's self.
	 */
	public void createRelationships() {
		for (UserAccount proxyAccount : proxyAccounts) {
			Set<String> profiles = new HashSet<String>();
			profiles.addAll(figureNonSelfProfileUris(proxyAccount));
			profiles.addAll(proxyAccount.getProxiedIndividualUris());

			proxyAccount.setProxiedIndividualUris(profiles);
			userAccountsDao.updateUserAccount(proxyAccount);
		}
	}

	/* Look at the desired profiles, and remove any that are this proxy's self. */
	private Collection<String> figureNonSelfProfileUris(UserAccount proxyAccount) {
		List<String> myProfiles = new ArrayList<String>(profileUris);
		for (Individual self : selfEditingConfiguration
				.getAssociatedIndividuals(indDao, proxyAccount)) {
			myProfiles.remove(self.getURI());
		}
		return myProfiles;
	}

}
