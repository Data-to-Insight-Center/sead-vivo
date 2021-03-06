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

<%

request.setAttribute("dwrDisabled", new Boolean(false));
String context = request.getContextPath();

%>


<%
if (!(Boolean)request.getAttribute("dwrDisabled")) {
%>
<script type="text/javascript">
    // relinquish jQuery's control of the $ variable to avoid conflicts with DWR
    jQuery.noConflict();
</script>

<script type="text/javascript" xml:space="preserve">
    var gEntityURI="${entity.URI}";
</script> <!-- There has got to be a better way to pass this to the js -->

<script type="text/javascript" src="<%=context%>/dwr/interface/PropertyDWR.js"></script>
<script type="text/javascript" src="<%=context%>/dwr/interface/EntityDWR.js"></script>
<script type="text/javascript" src="<%=context%>/dwr/interface/VClassDWR.js"></script>
<script type="text/javascript" src="<%=context%>/dwr/engine.js"></script>
<script type="text/javascript" src="<%=context%>/dwr/util.js"></script>
<script type="text/javascript" src="<%=context%>/js/betterDateInput.js"></script>
<script type="text/javascript" src="<%=context%>/js/vitro.js"></script>
<script type="text/javascript" src="<%=context%>/dojo.js"></script>
<script type="text/javascript" src="<%=context%>/js/ents_edit.js"></script>
<script type="text/javascript" src="<%=context%>/js/detect.js"></script>
<script type="text/javascript" src="<%=context%>/js/toggle.js"></script>

<%
} %>

