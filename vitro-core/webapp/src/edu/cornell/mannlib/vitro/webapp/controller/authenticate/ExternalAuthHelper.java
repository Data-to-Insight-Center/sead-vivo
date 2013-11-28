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

package edu.cornell.mannlib.vitro.webapp.controller.authenticate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.mannlib.vitro.webapp.config.ConfigurationProperties;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;


/**
 * Capture the properties used by the External Authorization system, and use
 * them to assist in the process.
 * 
 * The first time this bean is requested, it is created from the configuration
 * properties and cached in the session. After that, the cached version is used.
 */
public class ExternalAuthHelper {
	private static final Log log = LogFactory.getLog(ExternalAuthHelper.class);

	private static final ExternalAuthHelper DUMMY_HELPER = new ExternalAuthHelper(
			null, null, null, null, null );

	private static final String BEAN_ATTRIBUTE = ExternalAuthHelper.class
			.getName();

	/** This configuration property points to the external authorization server. */
	private static final String PROPERTY_EXTERNAL_AUTH_SERVER_URL = "externalAuth.serverUrl";

	/** This configuration property says which HTTP header holds the auth ID. */
	public static final String PROPERTY_EXTERNAL_AUTH_ID_HEADER = "externalAuth.netIdHeaderName";
	
	private static String PROPERTY_EXTERNAL_CALLBACK_URI = "externalAuth.callback_uri";
    private static final String PROPERTY_EXTERNAL_CLIENT_ID = "externalAuth.client_id";
    private static final String PROPERTY_EXTERNAL_CLIENT_SECRET = "externalAuth.cient_secret";
	
	private GoogleAuthorizationCodeFlow flow;
	private String stateToken;
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    
    private static final Iterable<String> SCOPE = Arrays.asList("https://www.googleapis.com/auth/userinfo.profile;https://www.googleapis.com/auth/userinfo.email".split(";"));
    private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo";

	// ----------------------------------------------------------------------
	// static methods
	// ----------------------------------------------------------------------

	/**
	 * Get the bean from the servlet context. If there is no bean, create one.
	 * 
	 * Never returns null.
	 */
	public static ExternalAuthHelper getHelper(ServletRequest request) {
		if (!(request instanceof HttpServletRequest)) {
			log.trace("Not an HttpServletRequest: " + request);
			return DUMMY_HELPER;
		}

		HttpSession session = ((HttpServletRequest) request).getSession(false);
		if (session == null) {
			log.trace("No session; no need to create one.");
			return DUMMY_HELPER;
		}

		ServletContext ctx = session.getServletContext();

		Object attr = ctx.getAttribute(BEAN_ATTRIBUTE);
		if (attr instanceof ExternalAuthHelper) {
			log.trace("Found a bean: " + attr);
			return (ExternalAuthHelper) attr;
		}

		ExternalAuthHelper bean = buildBean(ctx);
		log.debug("Created a bean: " + bean);
		setBean(ctx, bean);
		return bean;
	}

	/** It would be private, but we want to allow calls for faking. */
	protected static void setBean(ServletContext context,
			ExternalAuthHelper bean) {
		context.setAttribute(BEAN_ATTRIBUTE, bean);
	}

	private static ExternalAuthHelper buildBean(ServletContext ctx) {
		String externalAuthServerUrl = ConfigurationProperties.getBean(ctx)
				.getProperty(PROPERTY_EXTERNAL_AUTH_SERVER_URL);
		String externalAuthHeaderName = ConfigurationProperties.getBean(ctx)
				.getProperty(PROPERTY_EXTERNAL_AUTH_ID_HEADER);
		String externalAuthCallBackUri = ConfigurationProperties.getBean(ctx)
				.getProperty(PROPERTY_EXTERNAL_CALLBACK_URI);
		String externalAuthClientId = ConfigurationProperties.getBean(ctx)
				.getProperty(PROPERTY_EXTERNAL_CLIENT_ID);
		String externalAuthClientSecret = ConfigurationProperties.getBean(ctx)
				.getProperty(PROPERTY_EXTERNAL_CLIENT_SECRET);

		return new ExternalAuthHelper(externalAuthServerUrl,
				externalAuthHeaderName, externalAuthCallBackUri, externalAuthClientId, externalAuthClientSecret);
	}


	// ----------------------------------------------------------------------
	// the bean
	// ----------------------------------------------------------------------

	private final String externalAuthServerUrl;
	private final String externalAuthHeaderName;
	private final String externalAuthCallBackUri;
	private final String externalAuthClientId;
	private final String externalAuthClientSecret;

	/** It would be private, but we want to allow subclasses for faking. */
	protected ExternalAuthHelper(String externalAuthServerUrl,
			String externalAuthHeaderName, String externalAuthCallBackUri, String externalAuthClientId,
			String externalAuthClientSecret) {
		this.externalAuthServerUrl = trimThis(externalAuthServerUrl);
		this.externalAuthHeaderName = trimThis(externalAuthHeaderName);
		this.externalAuthCallBackUri = trimThis(externalAuthCallBackUri);
		this.externalAuthClientId = trimThis(externalAuthClientId);
		this.externalAuthClientSecret = trimThis(externalAuthClientSecret);
	}

	private String trimThis(String string) {
		if (string == null) {
			return null;
		} else {
			return string.trim();
		}
	}

	public String buildExternalAuthRedirectUrl(String returnUrl) {
		if (returnUrl == null) {
			log.error("returnUrl is null.");
			return null;
		}

		if (externalAuthServerUrl == null) {
			log.debug("deploy.properties doesn't contain a value for '"
					+ PROPERTY_EXTERNAL_AUTH_SERVER_URL
					+ "' -- sending directly to '" + returnUrl + "'");
			return returnUrl;
		}

		try {
			String encodedReturnUrl = URLEncoder.encode(returnUrl, "UTF-8");
			//String externalAuthUrl = externalAuthServerUrl + "?target="
				//	+ encodedReturnUrl;
			/*String externalAuthUrl = "https://accounts.google.com/AccountChooser?service=lso&continue=https%3A%2F%2Faccounts.google.com%2" +
					"Fo%2Foauth2%2Fauth%3Fscope%3Dhttps%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile%2Bhttps%3A%2F%2Fwww.googleapis.com%2" +
					"Fauth%2Fuserinfo.email%26response_type%3Dcode%26redirect_uri%3Dhttp%3A%2F%2Flocalhost%3A8080%2Fvivotom3%2Fcallback%26" +
					"client_id%3D240906802335.apps.googleusercontent.com%26hl%3Den%26from_login%3D1%26as%3D-525665c69297d86d&btmpl=authsub&hl=en";
			log.debug("externalAuthUrl is '" + externalAuthUrl + "'"); */
			
			System.out.println("Before Google Auth call ..");
			googleAuthHelper();
			System.out.println("After Google Auth call ..");
			String externalAuthUrl = buildLoginUrl();
			System.out.println("loginURL: "+externalAuthUrl);
			return externalAuthUrl;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e); // No UTF-8? Really?
		}
	}

	 public void googleAuthHelper() {
         flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT,
                         JSON_FACTORY, externalAuthClientId, externalAuthClientSecret, (Collection<String>) SCOPE).build();
         
         generateStateToken();
 }	
 
	public String buildLoginUrl() {
        
        final GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
        
        return url.setRedirectUri(externalAuthCallBackUri).setState(stateToken).build();
	}
	 
	 private void generateStateToken(){
         
         SecureRandom sr1 = new SecureRandom();
         
         stateToken = "google;"+sr1.nextInt();
         
 }
	 public String getUserInfoJson(final String authCode) throws IOException {

			final GoogleTokenResponse response = flow.newTokenRequest(authCode).setRedirectUri(externalAuthCallBackUri).execute();
			final Credential credential = flow.createAndStoreCredential(response, null);
			final HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(credential);
			// Make an authenticated request
			final GenericUrl url = new GenericUrl(USER_INFO_URL);
			final HttpRequest request = requestFactory.buildGetRequest(url);
			request.getHeaders().setContentType("application/json");
			final String jsonIdentity = request.execute().parseAsString();
			System.out.println("jsonIdentity: "+ jsonIdentity);
			JSONObject jsonIdentity1 = (JSONObject) JSONSerializer.toJSON(jsonIdentity);
			String userId = jsonIdentity1.getString("email");
			System.out.println("name retrieved: "+ userId);
			return userId;

		}
	 
	public String getExternalAuthId(HttpServletRequest request) {
		if (request == null) {
			log.error("request is null.");
			return null;
		}

		if (externalAuthHeaderName == null) {
			log.error("User asked for external authentication, "
					+ "but deploy.properties doesn't contain a value for '"
					+ PROPERTY_EXTERNAL_AUTH_ID_HEADER + "'");
			return null;
		}

		String externalAuthId = request.getHeader(externalAuthHeaderName);
		log.debug("externalAuthId=" + externalAuthId);
		return externalAuthId;
	}

	@Override
	public String toString() {
		return "ExternalAuthHelper[externalAuthServerUrl="
				+ externalAuthServerUrl + ", externalAuthHeaderName="
				+ externalAuthHeaderName + "]";
	}

}
