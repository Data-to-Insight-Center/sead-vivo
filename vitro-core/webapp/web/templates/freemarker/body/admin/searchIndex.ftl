<#--
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
-->

<#-- 
    Template for the page that controls the updating or rebuilding of the Search Index. 
-->

<h2>Search Index Status</h2>

<#if !indexIsConnected>
    <!-- Can't contact the Solr server. Indexing would be impossible. Show an error message. -->
    <section id="error-alert" role="alert">
        <img src="${urls.images}/iconAlert.png" width="24" height="24" alert="Error alert icon" />
        <p>The search index is not connected.</p>
        <p><tt>SolrServer.ping()</tt> failed.
        <p>Check startup status page and/or Tomcat logs for more information.</p>
    </section>
    
<#elseif worklevel == "IDLE">
    <!-- Solr indexer is idle. Show the button that rebuilds the index. -->
    <h3>The search indexer is idle.</h3>
    <#if hasPreviousBuild??>
        <p>The most recent update was at ${since?string("hh:mm:ss a, MMMM dd, yyyy")}</p>
    </#if>
    
    <form action="${actionUrl}" method="POST">
        <p>
            <input class="submit" type="submit" name="rebuild" value="Rebuild" role="button" />
            Reset the search index and re-populate it.
        </p>
    </form>
    
<#elseif totalToDo == 0>
    <!-- Solr indexer is preparing the list of records. Show elapsed time since request. -->
    <h3>Preparing to rebuild the search index. </h3>
    <p>since ${since?string("hh:mm:ss a, MMMM dd, yyyy")}, elapsed time ${elapsed}</p>
    
<#else>
    <!-- Solr indexer is re-building the index. Show the progress. -->
    <h3>${currentTask} the search index.</h3>
    <p>since ${since?string("hh:mm:ss a, MMMM dd, yyyy")}, elapsed time ${elapsed}, estimated total time ${expected}</p>
    <p>Completed ${completedCount} out of ${totalToDo} index records.</p>
    
</#if>
