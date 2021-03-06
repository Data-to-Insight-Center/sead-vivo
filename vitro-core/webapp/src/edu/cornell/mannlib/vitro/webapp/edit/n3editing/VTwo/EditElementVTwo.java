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

package edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo;

import java.util.Map;
import java.util.List;
import com.hp.hpl.jena.rdf.model.Literal;

import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.EditConfigurationVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.MultiValueEditSubmission;
import freemarker.template.Configuration;
/**
 * All classes that implement this interface must have a public constructor that 
 * takes a edu.cornell.mannlib.vitro.webapp.edit.n3editing.Field.  It will be 
 * called with using reflection.
 */
public interface EditElementVTwo {  
    /**
     * This is a method to get a map of variable name to Literal value from the submitted form. 
     */
    public Map<String,List<Literal>> 
        getLiterals(String fieldName, EditConfigurationVTwo editConfig, Map<String,String[]> queryParameters );
    
    /**
     * This is a method to get a map of variable name to URI values from the submitted form. 
     */
    public Map<String,List<String>> 
        getURIs(String fieldName, EditConfigurationVTwo editConfig, Map<String,String[]> queryParameters );
    
    /**
     * Gets validation error messages.  Returns an empty list if there are no errors.
     */
    public Map<String,String>
        getValidationMessages(String fieldName, EditConfigurationVTwo editConfig, Map<String,String[]> queryParameters);
    
    /**
     * This is a method to generate the HTML output for a form element.  It should use a freemarker template
     * to produce the output.  
     */
    public String draw(String fieldName, EditConfigurationVTwo editConfig, MultiValueEditSubmission editSub, Configuration fmConfig);
    
    /**
     * This method gets the map with the data that can then be passed to the template
     */
    public Map getMapForTemplate(EditConfigurationVTwo editConfig, MultiValueEditSubmission editSub);            
    
    /* in the future, we may need to get existing values */
    /*
    public Map<String,Literal> getExistingLiterals(???)
    public Map<String,String> getExistingURIs(???);
    */
}
