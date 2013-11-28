<#--
Copyright (c) 2012, Cornell University
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

<@widget name="login" include="assets" />
<#include "browse-classgroups.ftl">

<!DOCTYPE html>
<html lang="en">
    <head>
        <#include "head.ftl">
    </head>
    
    <body class="${bodyClasses!}" onload="${bodyOnload!}">
        <#include "identity.ftl">

        <#include "menu.ftl">

        <section id="intro" role="region">
            <h2>Welcome to SEAD Research Network</h2>

            <p>SEAD Research Network is a database of experts in sustainability science across many disciplines and universities. Use this network to explore the profiles, publications and data collection and release activities of thousands of researchers to find collaborators, discover data sets or publications or visualize connections between people and their research.</p>
                        
            <section id="search-home" role="region">
                <h3>Search SEAD Research Network</h3>
                
                <fieldset>
                    <legend>Search form</legend>
                    <form id="search-home-vivo" action="${urls.search}" method="post" name="search-home" role="search">
                        <div id="search-home-field">
                            <input type="text" name="querytext" class="search-home-vivo" value="${querytext!}" />
                            <input type="submit" value="Search" class="search">
                        </div>
                    </form>
                </fieldset>
            </section> <!-- #search-home -->
        </section> <!-- #intro -->
        
        <@widget name="login" />
        
        <@allClassGroups vClassGroups! />
    
    <#include "footer.ftl">
    </body>
</html>
