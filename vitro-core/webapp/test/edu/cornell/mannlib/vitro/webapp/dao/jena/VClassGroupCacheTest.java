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

package edu.cornell.mannlib.vitro.webapp.dao.jena;

import java.io.StringReader;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDFS;

public class VClassGroupCacheTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testIsVClassGroupNameChange() {
        
        
        //protected static boolean isVClassGroupNameChange(Statement stmt, OntModel jenaOntModel) {
        
        String rdf = 
            "core:Summer \n" +
            "      a       owl:Class ; \n" +
            "      rdfs:label \"Spring and toast 3\"@en-US ; \n" +
            "      rdfs:subClassOf owl:Thing ; \n" +
            "      vitro:descriptionAnnot \n" +
            "              \"cgaa\"^^xsd:string ; \n" +
            "      vitro:displayLimitAnnot \n" +
            "              \"-1\"^^xsd:int ; \n" +
            "      vitro:displayRankAnnot \n" +
            "              \"-1\"^^xsd:int ; \n" +
            "      vitro:exampleAnnot \"serefs\"^^xsd:string ; \n" +
            "      vitro:hiddenFromDisplayBelowRoleLevelAnnot \n" +
            "              <http://vitro.mannlib.cornell.edu/ns/vitro/role#public> ; \n" +
            "      vitro:inClassGroup  <http://vivoweb.org/ontology#vitroClassGroupequipment> ; \n" +
            "      vitro:prohibitedFromUpdateBelowRoleLevelAnnot \n" +
            "              <http://vitro.mannlib.cornell.edu/ns/vitro/role#public> ; \n" +
            "      vitro:shortDef \"sfsfe\"^^xsd:string ; \n" +
            "      owl:equivalentClass core:Summer . ";
        
        OntModel om = ModelFactory.createOntologyModel();
        om.read( new StringReader( prefixes + rdf) , null , "N3");
        
        Statement stmt = ResourceFactory.createStatement(
                ResourceFactory.createResource("http://vivoweb.org/ontology/core#Summer"),
                RDFS.label,
                ResourceFactory.createPlainLiteral("some old label"));
                
        boolean isNameChange = VClassGroupCache.isClassNameChange(stmt, om);
        Assert.assertTrue("Expected it to be a name change but it wasn't.", isNameChange);
        
        stmt = ResourceFactory.createStatement(
                ResourceFactory.createResource("http://vivoweb.org/ontology/core#bogus"),
                ResourceFactory.createProperty("http://example.com/nonLabelProperty"),
                ResourceFactory.createPlainLiteral("some old label"));
                
        boolean notNameChange = ! VClassGroupCache.isClassNameChange(stmt, om);
        Assert.assertTrue("Expected it to NOT be a name change but it was.",  notNameChange);
     
        stmt = ResourceFactory.createStatement(
                ResourceFactory.createResource("http://vivoweb.org/ontology/core#bogus"),
                RDFS.label,
                ResourceFactory.createPlainLiteral("some old label"));
                
        notNameChange = ! VClassGroupCache.isClassNameChange(stmt, om);
        Assert.assertTrue("Expected it to NOT be a name change but it was.",  notNameChange);
    }

    static final String prefixes = 
    "@prefix dc:      <http://purl.org/dc/elements/1.1/> . \n" +
    "@prefix pvs:     <http://vivoweb.org/ontology/provenance-support#> . \n" +
    "@prefix geo:     <http://aims.fao.org/aos/geopolitical.owl#> . \n" +
    "@prefix foaf:    <http://xmlns.com/foaf/0.1/> . \n" +
    "@prefix scires:  <http://vivoweb.org/ontology/scientific-research#> . \n" +
    "@prefix scripps:  <http://vivo.scripps.edu/> . \n" +
    "@prefix dcterms:  <http://purl.org/dc/terms/> . \n" +
    "@prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#> . \n" +
    "@prefix swrl:    <http://www.w3.org/2003/11/swrl#> . \n" +
    "@prefix vitro:   <http://vitro.mannlib.cornell.edu/ns/vitro/0.7#>  . \n" +
    "@prefix event:   <http://purl.org/NET/c4dm/event.owl#> . \n" +
    "@prefix bibo:    <http://purl.org/ontology/bibo/> . \n" +
    "@prefix xsd:     <http://www.w3.org/2001/XMLSchema#> . \n" +
    "@prefix owl:     <http://www.w3.org/2002/07/owl#> . \n" +
    "@prefix swrlb:   <http://www.w3.org/2003/11/swrlb#> . \n" +
    "@prefix rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#> . \n" +
    "@prefix core:    <http://vivoweb.org/ontology/core#> . \n" +
    "@prefix skos:    <http://www.w3.org/2004/02/skos/core#> . \n" +
    "@prefix vivo:    <http://vivo.library.cornell.edu/ns/0.1#> . \n" +
    "@prefix dcelem:  <http://purl.org/dc/elements/1.1/> . \n" +
    "@prefix ero:     <http://purl.obolibrary.org/obo/> . \n" +
    " \n" ;
    
   
}
