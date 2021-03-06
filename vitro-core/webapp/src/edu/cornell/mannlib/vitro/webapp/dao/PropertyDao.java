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

package edu.cornell.mannlib.vitro.webapp.dao;

import java.util.List;

import edu.cornell.mannlib.vitro.webapp.beans.Property;
import edu.cornell.mannlib.vitro.webapp.beans.VClass;

public interface PropertyDao {
	
    void addSuperproperty(Property property, Property superproperty);
    
    void addSuperproperty(String propertyURI, String superpropertyURI);
    
    void removeSuperproperty(Property property, Property superproperty);
    
    void removeSuperproperty(String propertyURI, String superpropertyURI);
    
    void addSubproperty(Property property, Property subproperty);
    
    void addSubproperty(String propertyURI, String subpropertyURI);
    
    void removeSubproperty(Property property, Property subproperty);
    
    void removeSubproperty(String propertyURI, String subpropertyURI);
    
    void addEquivalentProperty(String propertyURI, String equivalentPropertyURI);
    
    void addEquivalentProperty(Property property, Property equivalentProperty);
    
    void removeEquivalentProperty(String propertyURI, String equivalentPropertyURI);
    
    void removeEquivalentProperty(Property property, Property equivalentProperty);
    
    List <String> getSubPropertyURIs(String propertyURI);

    List <String> getAllSubPropertyURIs(String propertyURI);

    List <String> getSuperPropertyURIs(String propertyURI, boolean direct);

    List <String> getAllSuperPropertyURIs(String propertyURI);
    
    List <String> getEquivalentPropertyURIs(String propertyURI);
    
    List <VClass> getClassesWithRestrictionOnProperty(String propertyURI);
    
}
