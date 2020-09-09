package org.bamford.KWIC;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ConcordanceFormatterTest {
    private ConcordanceFormatter instance = new ConcordanceFormatter();
    private ParsedSentence simpleSentence;
    private ParsedSentence punctuatedSentence;

    @Before
    public void setUp() {
        simpleSentence = constructParsedSentence(
                new String[]{"You","are","also","asked","to","keep","your","church","leaders","informed","of","your","involvement"});
        punctuatedSentence = constructParsedSentence(
                new String[]{"The","medical","aspects","can","be","cancer",",","pneumonia",",","sudden","blindness",",","dementia", "and", "dramatic", "weight", "loss", "."});
    }

    @Test
    public void expectedConcordanceIsCompleteSentence() {
        simpleSentence.addNodeIndex(6);
        final List<String> results = instance.formatConcordances(Collections.singletonList(simpleSentence), 6);
        assertEquals(1, results.size());
        assertEquals("You are also asked to keep your church leaders informed of your involvement", results.get(0));
    }

    @Test
    public void expectedConcordanceIsPartSentence() {
        simpleSentence.addNodeIndex(6);
        final List<String> results = instance.formatConcordances(Collections.singletonList(simpleSentence), 3);
        assertEquals(1, results.size());
        assertEquals("asked to keep your church leaders informed", results.get(0));
    }

    @Test
    public void windowsSizeExceedsSentenceBounds() {
        simpleSentence.addNodeIndex(6);
        final List<String> results = instance.formatConcordances(Collections.singletonList(simpleSentence), 10);
        assertEquals(1, results.size());
        assertEquals("You are also asked to keep your church leaders informed of your involvement", results.get(0));
    }

    @Test
    public void sentenceHasMultipleExpectedConcordances() {
        simpleSentence.addNodeIndex(6);
        simpleSentence.addNodeIndex(11);
        final List<String> results = instance.formatConcordances(Collections.singletonList(simpleSentence), 3);
        assertEquals(2, results.size());
        assertEquals("asked to keep your church leaders informed", results.get(0));
        assertEquals("leaders informed of your involvement", results.get(1));
    }

    @Test
    public void punctuationWithinWindowIncludedButNotCounted() {
        punctuatedSentence.addNodeIndex(5);
        final List<String> results = instance.formatConcordances(Collections.singletonList(punctuatedSentence), 5);
        assertEquals(1, results.size());
        assertEquals("The medical aspects can be cancer, pneumonia, sudden blindness, dementia and", results.get(0));
    }

    @Test
    public void entirePunctuatedSentenceExpected() {
        punctuatedSentence.addNodeIndex(5);
        final List<String> results = instance.formatConcordances(Collections.singletonList(punctuatedSentence), 10);
        assertEquals(1, results.size());
        assertEquals("The medical aspects can be cancer, pneumonia, sudden blindness, dementia and dramatic weight loss.", results.get(0));
    }

    private ParsedSentence constructParsedSentence(String[] parts) {
        final List<SentencePart> sps = new ArrayList<>();
        for (String s : parts) {
            // This simplification of what constitutes punctuation is sufficient for the test data used in this unit test
            if (s.equals(",") || s.equals(".")) {
                sps.add(new SentencePart(SentencePartType.PUNCTUATION, s));
            } else {
                sps.add(new SentencePart(SentencePartType.WORD, s));
            }
        }
        final ParsedSentence ps = new ParsedSentence();
        ps.setSentenceParts(sps);
        return ps;
    }
}
