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

<#-- Template for adding a new individual from the Site Admin page: VIVO version -->

<#import "lib-vivo-form.ftl" as lvf>

<#--Retrieve certain edit configuration information-->
<#assign typeName = editConfiguration.pageData.typeName />
<#assign isPersonType = editConfiguration.pageData.isPersonType />

<#--Get existing value for specific data literals and uris-->
<#assign firstNameValue = lvf.getFormFieldValue(editSubmission, editConfiguration, "firstName")/>
<#assign lastNameValue = lvf.getFormFieldValue(editSubmission, editConfiguration, "lastName")/>
<#assign labelValue = lvf.getFormFieldValue(editSubmission, editConfiguration, "label")/>

<#--If edit submission exists, then retrieve validation errors if they exist-->
<#if editSubmission?has_content && editSubmission.submissionExists = true && editSubmission.validationErrors?has_content>
	<#assign submissionErrors = editSubmission.validationErrors/>
</#if>


<h2>Create a new ${typeName}</h2>


<#if submissionErrors?has_content >
    <section id="error-alert" role="alert">
        <img src="${urls.images}/iconAlert.png" width="24" height="24" alert="Error alert icon" />
        <p>
        <#list submissionErrors?keys as errorFieldName>
    	    <#if errorFieldName == "firstName">
    	        Please enter a First Name for this person.
    	    <#elseif  errorFieldName == "lastName">
    	        Please enter a Last Name for this person.
        	<#elseif  errorFieldName == "label">
        	    Please enter a value in the name field.
    	    </#if>
    	    <br />
    	</#list>
        </p>
    </section>
</#if>

<#assign requiredHint = "<span class='requiredHint'> *</span>" />

<section id="newIndividual" role="region">        
    
    <form id="newIndividual" class="customForm noIE67" action="${submitUrl}"  role="add new individual">
 
    <#if isPersonType = "true">       
        <p>
            <label for="firstName">First Name ${requiredHint}</label>
            <input size="30"  type="text" id="firstName" name="firstName" value="${firstNameValue}" />
        </p>

        <p>
            <label for="lastName">Last Name ${requiredHint}</label>
            <input size="30"  type="text" id="lastName" name="lastName" value="${lastNameValue}" />
        </p>
    <#else>       
        <p>
            <label for="name">Name ${requiredHint}</label>
            <input size="30"  type="text" id="label" name="label" value="${labelValue}" />
        </p>
    </#if>

    <p class="submit">
        <input type="hidden" name = "editKey" value="${editKey}"/>
        <input type="submit" id="submit" value="Create ${typeName}"/>
        <span class="or"> or </span><a class="cancel" href="${urls.base}/siteAdmin" title="Cancel">Cancel</a>
    </p>

    <p id="requiredLegend" class="requiredHint">* required fields</p>

    </form>


</section>

${stylesheets.add('<link rel="stylesheet" href="${urls.base}/templates/freemarker/edit/forms/css/customForm.css" />')}
