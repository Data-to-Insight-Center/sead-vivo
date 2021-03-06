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

<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%/* this odd thing points to something in web.xml */ %>
<%@ page errorPage="/error.jsp"%>
<%  /***********************************************
        alphaIndex.jsp will just display the just the index, no entites.
         
        request attributres:
        'alpha' - set to currently displaying alpha, 'none' or 'all'
        'controllerParam' - parameter for controller
        'count' - count of entites in the index
        'letters' - List of STrings, letters for index.
        'servlet' - name of servlet to put in links.
        
        put something like this in for debuggin: < % =  MiscWebUtils.getReqInfo(request) % >
         bdc34 2006-02-06
        **********************************************/
       
%>

<c:if test="${ requestScope.showAlpha == 1 }">
<div class='alphaIndex'>
    <c:forEach items='${requestScope.letters}' var='letter'>
        <c:if test="${letter == requestScope.alpha}"> ${requestScope.alpha }&nbsp;</c:if>
        <c:if test="${letter != requestScope.alpha}"> 
            <c:url var="url" value=".${requestScope.servlet}">
                <c:param name="alpha">${letter}</c:param>
            </c:url>
            <a href='<c:url value="${url}&amp;${requestScope.controllerParam}"/>'>${letter} </a>
         </c:if> 
    </c:forEach> 
    
    <%  if( request.getAttribute("alpha") != null && ! "all".equalsIgnoreCase((String)request.getAttribute("alpha"))) {  %>         
        <a href='<c:url value=".${requestScope.servlet}?&amp;alpha=all&amp;${requestScope.controllerParam}"/>'>all </a>
        <c:if test='${not empty requestScope.count }'>
            (${requestScope.count} that start with ${requestScope.alpha })
        </c:if>    
     <% }else{ %>
        (${requestScope.count})      
     <% } %>             
</div>
</c:if>