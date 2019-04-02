package edu.stanford.nlp.pipeline;

import edu.stanford.nlp.util.Timing;
import org.junit.Test;

import java.util.Properties;
import java.io.*;
import java.util.*;

import static org.junit.Assert.*;

import edu.stanford.nlp.io.FileSequentialCollection;

/**
 * Test some of the utility functions in {@link StanfordCoreNLP}.
 *
 * @author Gabor Angeli
 */
public class StanfordCoreNLPTest {

  //ChaeHunPark
  @Test
  public void annotationpoolTest() {
    Properties props1 = new Properties();
    props1.setProperty("annotators", "tokenize");
    Properties props2 = new Properties();
    props2.setProperty("annotators", "tokenize");
    AnnotatorImplementations impl = new AnnotatorImplementations();
    AnnotatorPool pool1 = StanfordCoreNLP.getDefaultAnnotatorPool(props1,impl);
    AnnotatorPool pool2 = StanfordCoreNLP.getDefaultAnnotatorPool(props2,impl);
    assertEquals(pool1,pool2);
  }

  @Test
  public void testPrereqAnnotatorsBasic() {
    assertEquals("tokenize,ssplit,pos,parse",
        StanfordCoreNLP.ensurePrerequisiteAnnotators(new String[]{"parse"}, new Properties()));
    assertEquals("tokenize,ssplit,pos,depparse",
        StanfordCoreNLP.ensurePrerequisiteAnnotators(new String[]{"depparse"}, new Properties()));
    assertEquals("tokenize,ssplit,pos,depparse",
        StanfordCoreNLP.ensurePrerequisiteAnnotators(new String[]{"depparse", "tokenize"}, new Properties()));
    assertEquals("tokenize,ssplit,pos,lemma,depparse,natlog",
        StanfordCoreNLP.ensurePrerequisiteAnnotators(new String[]{"natlog", "tokenize"}, new Properties()));
  }

  @Test
  public void testPrereqAnnotatorsOrderPreserving() {
    assertEquals("tokenize,ssplit,pos,lemma,depparse,natlog",
        StanfordCoreNLP.ensurePrerequisiteAnnotators(new String[]{"lemma", "depparse", "natlog"}, new Properties()));
    assertEquals("tokenize,ssplit,pos,depparse,lemma,natlog",
        StanfordCoreNLP.ensurePrerequisiteAnnotators(new String[]{"depparse", "lemma", "natlog"}, new Properties()));
    assertEquals("tokenize,ssplit,pos,lemma,ner,regexner",
        StanfordCoreNLP.ensurePrerequisiteAnnotators(new String[]{"ner", "regexner"}, new Properties()));
    assertEquals("tokenize,ssplit,pos,lemma,ner,depparse",
        StanfordCoreNLP.ensurePrerequisiteAnnotators(new String[]{"ner", "depparse"}, new Properties()));
    assertEquals("tokenize,ssplit,pos,lemma,depparse,ner",
        StanfordCoreNLP.ensurePrerequisiteAnnotators(new String[]{"depparse", "ner"}, new Properties()));
  }


  @Test
  public void testPrereqAnnotatorsRegexNERAfterNER() {
    assertEquals("tokenize,ssplit,pos,lemma,ner,regexner",
        StanfordCoreNLP.ensurePrerequisiteAnnotators(new String[]{"regexner", "ner"}, new Properties()));
  }

  @Test
  public void testBasicAnnotatorwithOrdering() {
	    assertEquals("tokenize,ssplit,pos",
	        StanfordCoreNLP.ensurePrerequisiteAnnotators(new String[]{"pos"}, new Properties()));
	    assertEquals("tokenize,ssplit,pos,lemma,depparse,natlog",
	        StanfordCoreNLP.ensurePrerequisiteAnnotators(new String[]{"natlog"}, new Properties())); 
  }
  
  @Test
  public void testPrereqAnnotatorsCorefBeforeOpenIE() {
    assertEquals("tokenize,ssplit,pos,lemma,depparse,natlog,ner,coref,openie",
        StanfordCoreNLP.ensurePrerequisiteAnnotators(new String[]{"openie", "coref"}, new Properties()));
    assertEquals("tokenize,ssplit,pos,lemma,ner,depparse,natlog,coref,openie",
        StanfordCoreNLP.ensurePrerequisiteAnnotators(new String[]{"coref", "openie"}, new Properties()));
  }


  @Test
  public void testPrereqAnnotatorsCoref() {
    Properties props = new Properties();
    assertEquals("tokenize,ssplit,pos,lemma,ner,depparse,coref",
        StanfordCoreNLP.ensurePrerequisiteAnnotators(new String[]{"coref"}, props));
    assertEquals("dep", props.getProperty("coref.md.type", ""));
  }


  @Test
  public void testPrereqAnnotatorsCorefWithParse() {
    Properties props = new Properties();
    assertEquals("tokenize,ssplit,pos,lemma,ner,parse,coref",
        StanfordCoreNLP.ensurePrerequisiteAnnotators(new String[]{"parse","coref"}, props));
    assertEquals("__empty__", props.getProperty("coref.md.type", "__empty__"));
  }

}
