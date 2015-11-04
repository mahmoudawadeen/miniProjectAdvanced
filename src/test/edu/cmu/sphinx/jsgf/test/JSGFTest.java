/*
 * Copyright 1999-2004 Carnegie Mellon University.
 * Portions Copyright 2004 Sun Microsystems, Inc.
 * Portions Copyright 2004 Mitsubishi Electric Research Laboratories.
 * All Rights Reserved.  Use is subject to license terms.
 *
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 *
 */

package edu.cmu.sphinx.jsgf.test;

import edu.cmu.sphinx.jsgf.JSGFGrammar;
import edu.cmu.sphinx.jsgf.JSGFRuleGrammar;
import edu.cmu.sphinx.util.props.ConfigurationManager;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.speech.EngineException;
import javax.speech.recognition.GrammarException;
import javax.speech.recognition.RuleGrammar;
import javax.speech.recognition.RuleParse;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import com.sun.speech.engine.recognition.BaseRecognizer;
import com.sun.speech.engine.recognition.BaseRuleGrammar;

/**
 * A test program for JSGF grammars. This program will generate a number of
 * random sentences from a JSGF grammar and attempt to validate them via a rule
 * parse. A count of valid and invalid sentences is reported after the run.
 * 
 * This test will detect and reports only certain grammar failures. It will
 * detect when a JSGFGrammar generates invalid sentences. It will not detect the
 * case where a valid sentence can never be generated by the JSGFGrammar.
 * 
 * Note that there is a bug in the Rule Grammar parse where rules with nested
 * recursion cause a stack overflow error.
 * 
 */
public class JSGFTest {

    JSGFGrammar jsgfGrammar;
    
    @Before
    public void init() throws IOException {
        ConfigurationManager cm = new ConfigurationManager(
        "src/test/edu/cmu/sphinx/jsgf/test/jsgftest.config.xml");

        jsgfGrammar = (JSGFGrammar) cm.lookup("jsgfGrammar");
        jsgfGrammar.allocate();       
    }
    
    @Test
    public void testParser() throws IOException, GrammarException, EngineException {
        JSGFRuleGrammar jsgfRuleGrammar = jsgfGrammar.getRuleGrammar();
        BaseRecognizer recognizer = new BaseRecognizer(jsgfGrammar.getGrammarManager());  
        recognizer.allocate();
        RuleGrammar grammar = new BaseRuleGrammar(recognizer, jsgfRuleGrammar);
        Scanner scanner = new Scanner (new File ("src/test/edu/cmu/sphinx/jsgf/test/input.txt"));
        scanner.useDelimiter("\n");
        while (scanner.hasNext()) {
            String sentence = scanner.next();
            RuleParse rp = grammar.parse(sentence, null);
            Assert.assertNotNull(rp);
        }
    }
    
    @Test
    public void testSave () {
        JSGFRuleGrammar grammar = jsgfGrammar.getRuleGrammar();
        System.out.println (grammar.toString());
    }
}