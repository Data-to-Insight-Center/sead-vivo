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

package edu.cornell.mannlib.vitro.webapp.controller.freemarker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;
import edu.cornell.mannlib.vitro.webapp.controller.freemarker.responsevalues.ResponseValues;
import edu.cornell.mannlib.vitro.webapp.controller.freemarker.responsevalues.TemplateResponseValues;

/**
 * Freemarker controller and template samples.
 * @author rjy7
 *
 */
public class SamplesController extends FreemarkerHttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Log log = LogFactory.getLog(SamplesController.class);
    private static final String TEMPLATE_DEFAULT = "samples.ftl";

    @Override
    protected ResponseValues processRequest(VitroRequest vreq) {
        
        Map<String, Object> body = new HashMap<String, Object>();
        // Test of #list directive in template on undefined, null, and empty values.
        // Basic idea: empty list okay, null or undefined value not okay.
        List<String> apples = new ArrayList<String>();  // no error
        // List<String> apples = null; // error
        body.put("apples", apples); // without this: error        
        
        // You can add to a collection AFTER putting it in the template data model.
        // The data model contains a reference to the collection, not a copy.
        List<String> fruit = new ArrayList<String>();
        fruit.add("apples");
        fruit.add("bananas");
        body.put("fruit", fruit);
        fruit.add("oranges");
        
        // But you cannot modify a scalar after putting it in the data model - the
        // template still gets the old value
        String animal = "elephant";
        body.put("animal", animal);
        animal = "camel";
        
        // Because the data model contains a reference to the collection, changing
        // one also changes the other.
        List<String> animals = new ArrayList<String>();
        animals.add("elephant");
        animals.add("tiger");
        Map<String, List> zoo1 = new HashMap<String, List>();
        Map<String, List> zoo2 = new HashMap<String, List>();
        zoo1.put("animals", animals);
        zoo2.put("animals", animals);
        zoo1.get("animals").add("monkey");       
        body.put("zoo1", zoo1);
        body.put("zoo2", zoo2);
        
        // Test recursive dump - array of arrays
//        String[] fruitArray = { "apples", "bananas", "strawberries" };
//        String[] animalArray = { "cat", "dog", "mouse" };
//        String[] dayArray = { "Monday", "Tuesday", "Wednesday" };
//        String[][] arrays = { fruitArray, animalArray, dayArray };
//        body.put("arrays", arrays);
        
        body.put("trueStatement", true);
        body.put("falseStatement", false);
        
        getBerries(body);
        
        body.put("bookTitle", "Pride and Prejudice");
        body.put("bookTitle", "Persuasion");
        
        body.put("year", "2001");
        
        body.put("xsddatetime", "1983-12-07T17:15:28Z");
        
        body.put("title", "Freemarker Samples");
        
        return new TemplateResponseValues(TEMPLATE_DEFAULT, body);
    }
    
    @Override
    protected String getTitle(String siteName, VitroRequest vreq) {
        return "Freemarker Samples";
    }
    
    private void getBerries(Map<String, Object> body) {
        body.put("berries", "strawberries, raspberries, blueberries");
    }

}

