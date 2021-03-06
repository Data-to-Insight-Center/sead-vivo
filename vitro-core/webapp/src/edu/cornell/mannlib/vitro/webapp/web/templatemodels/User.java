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

package edu.cornell.mannlib.vitro.webapp.web.templatemodels;

import java.util.Collection;

import edu.cornell.mannlib.vedit.beans.LoginStatusBean;
import edu.cornell.mannlib.vitro.webapp.auth.identifier.IdentifierBundle;
import edu.cornell.mannlib.vitro.webapp.auth.identifier.RequestIdentifiers;
import edu.cornell.mannlib.vitro.webapp.auth.identifier.common.HasProfile;
import edu.cornell.mannlib.vitro.webapp.auth.policy.PolicyHelper;
import edu.cornell.mannlib.vitro.webapp.beans.UserAccount;
import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;
import edu.cornell.mannlib.vitro.webapp.controller.freemarker.RevisionInfoController;
import edu.cornell.mannlib.vitro.webapp.controller.freemarker.SiteAdminController;
import edu.cornell.mannlib.vitro.webapp.controller.freemarker.UrlBuilder;
import edu.cornell.mannlib.vitro.webapp.search.controller.IndexController;

public class User extends BaseTemplateModel {
    private final VitroRequest vreq;
    private final UserAccount currentUser;
    private final String profileUrl;
    
    public User(VitroRequest vreq) {
        this.vreq = vreq;
        this.currentUser = LoginStatusBean.getCurrentUser(vreq);
        this.profileUrl = figureAssociatedProfileUrl();
    }
    
	private String figureAssociatedProfileUrl() {
        IdentifierBundle ids = RequestIdentifiers.getIdBundleForRequest(vreq);
		Collection<String> uris = HasProfile.getProfileUris(ids);
        if (uris.isEmpty()) {
        	return "";
        }
        
        String uri = uris.iterator().next();
        String url = UrlBuilder.getIndividualProfileUrl(uri, vreq);
        if (url == null) {
        	return "";
        }
        
        return url;
	}
	
	/* Template properties */

	public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    public String getEmailAddress() {
		return (currentUser == null) ? "" : currentUser.getEmailAddress();
    }
    
    public String getLoginName() {
    	if (currentUser == null) {
    		return "";
    	} 

    	if (currentUser.getFirstName().isEmpty()) {
    		return currentUser.getEmailAddress();
    	}
    	
    	return currentUser.getFirstName();
    }
    
    public String getFirstName() {
        return currentUser == null ? "" : currentUser.getFirstName();
    }
    
    public String getLastName() {
        return currentUser == null ? "" : currentUser.getLastName();
    }
    
    public boolean getHasSiteAdminAccess() {
    	return PolicyHelper.isAuthorizedForActions(vreq, SiteAdminController.REQUIRED_ACTIONS);
    }
    
    public boolean getHasRevisionInfoAccess() {
    	return PolicyHelper.isAuthorizedForActions(vreq, RevisionInfoController.REQUIRED_ACTIONS);
    }
    
    public boolean isAuthorizedToRebuildSearchIndex() {
        return PolicyHelper.isAuthorizedForActions(vreq, IndexController.REQUIRED_ACTIONS);
    }
    
    public boolean getHasProfile() {
    	return !profileUrl.isEmpty();
    }
    
    public String getProfileUrl() {
    	return profileUrl;
    }
}
