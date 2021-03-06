<%--
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
--%>

<%@ page import="com.hp.hpl.jena.rdf.model.ModelMaker" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.net.URLEncoder" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@taglib prefix="vitro" uri="/WEB-INF/tlds/VitroUtils.tld" %>
<%@page import="edu.cornell.mannlib.vitro.webapp.auth.permissions.SimplePermission" %>
<% request.setAttribute("requestedActions", SimplePermission.USE_ADVANCED_DATA_TOOLS_PAGES.ACTION); %>
<vitro:confirmAuthorization />

<script type="text/javascript">
function init(){
	var infoLine = document.information.info.value;
	if(infoLine == "RDB models"){
		document.rdbform.submit.disabled="true";
	}
	else{
		document.sdbform.submit.disabled="true";
	}
}
</script>

    <h2><a class="ingestMenu" href="ingest">Ingest Menu</a> > Available Jena Models</h2>

    <table>
    <tr>
    <td>
     <form name="sdbform" action="ingest" method="get">
        <input type="hidden" name="action" value="sdbModels"/>
<% if ("SDB models".equals(request.getAttribute("infoLine"))) { %>
        <input type="submit" name="submit" value="SDB Models"/>
<% } else { %>
        <input type="submit" name="submit" value="Main Store Models"/>
<% } %>
    </form>
    </td>
    <td>
    <form name="rdbform" action="ingest" method="get">
        <input type="hidden" name="action" value="rdbModels"/>
        <input type="submit" name="submit" value="RDB Models"/>
    </form>
    </td>
    </tr>
    </table>
    <form action="ingest" method="get">
        <input type="hidden" name="action" value="createModel"/>
        <input type="hidden" name="modelType" value="${modelType}"/>
        <input type="submit" name="submit" value="Create Model"/>
    </form>
    <form name="information">
    <input type="hidden" name="info" value="${infoLine}"/>
    </form>
    
            Currently showing <font color="red">${infoLine}</font>
    <ul>
      <c:forEach var="modelName" items="${modelNames}">
          <li style="padding-bottom:2em;padding-top:2em;"> ${modelName}
            <table style="margin-left:2em;"><tr>
            <td>
            <form action="ingest" method="get">
                <input type="hidden" name="action" value="loadRDFData"/>
                <input type="hidden" name="modelName" value="${modelName}"/>
                <input type="hidden" name="modelType" value="${modelType}"/>
                <input type="submit" name="submit" value="load RDF data"/>
            </form>
            </td>
            <td>
            <c:url var="outputModelURL" value="ingest">
            	<c:param name="action" value="outputModel"/>
            	<c:param name="modelName" value="${modelName}"/>
            </c:url>
            <a href="${outputModelURL}">output model</a>
            </td>
            <td>
            <form action="ingest" method="post">
                <input type="hidden" name="action" value="clearModel"/>
                <input type="hidden" name="modelName" value="${modelName}"/>
                <input type="hidden" name="modelType" value="${modelType}"/>
                <input type="submit" name="submit" value="clear statements"/>
            </form>
            </td>
            <td>
            <form action="ingest" method="post">
                <input type="hidden" name="action" value="removeModel"/>
                <input type="hidden" name="modelName" value="${modelName}"/>
                <input type="hidden" name="modelType" value="${modelType}"/>
                <input type="submit" name="submit" value="remove"/>
            </form>
            </td>
            </tr>
            <tr>
            <td>
            <form action="ingest" method="post">
                <input type="hidden" name="action" value="attachModel"/>
                <input type="hidden" name="modelName" value="${modelName}"/>
                <input type="hidden" name="modelType" value="${modelType}"/>
                <input type="submit" name="submit" value="attach to TBox (ontology)"/>
            </form>
            </td>
            <td>
            <form action="ingest" method="post">
                <input type="hidden" name="action" value="detachModel"/>
                <input type="hidden" name="modelName" value="${modelName}"/>
                <input type="hidden" name="modelType" value="${modelType}"/>
                <input type="submit" name="submit" value="detach from TBox (ontology)"/>
            </form>
            </td>
            <td>
			<form action="ingest" method="get">
			    <input type="hidden" name="action" value="permanentURI" />
			    <input type="hidden" name="modelName" value="${modelName}" /> 
				<input type="hidden" name="modelType" value="${modelType}"/>
				<input type="submit" name="submit" value="generate permanent URIs" /></form>
			</td>
            <td>&nbsp;</td>
            </tr>
            </table>
        </li>
      </c:forEach>
    </ul>

