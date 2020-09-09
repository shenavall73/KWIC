package org.bamford.KWIC;

import org.junit.Test;

import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class XMLSentenceParserTest {
    private static final String SIMPLE_SENTENCE_FILENAME = "simplesentence.xml";
    private static final String PUNCTUATED_SENTENCE = "punctuatedsentence.xml";
    private static final String SENTENCE_WITH_INDENTATION = "sentencewithindentedwords.xml";
    private static final String MULTIPLE_SENTENCES = "multiplesentences.xml";

    private XMLSentenceParser extractor = new XMLSentenceParser();

    private String[] simpleSentenceExpectedParts =  new String[]{"The","medical","aspects","can","be","cancer"};
    private String[] punctuatedSentenceExpectedParts =  new String[]{"The","medical","aspects","can","be","cancer",",","pneumonia",",",
        "sudden","blindness",",","dementia",",","dramatic","weight","loss","or","any","combination","of","these","."};
    private String[] indentedSentenceExpectedParts =  new String[]{"You","are","also","asked","to","keep","your","church","leaders","informed","of","your","involvement",
        "so","that","they","can","ensure","you","are","adequately","supported","."};

    @Test
    public void testSimpleSentenceWithMatch() throws Exception {
        final List<ParsedSentence> parsedSentences = extractor.parseSentencesFromFile("aspects", getResourceURL(SIMPLE_SENTENCE_FILENAME));
        assertEquals(1, parsedSentences.size());
        assertParsedSentenceAgainstExpectations(parsedSentences.get(0), simpleSentenceExpectedParts, 2);
    }

    @Test
    public void testSimpleSentenceWithoutMatch() throws Exception {
        final List<ParsedSentence> parsedSentences = extractor.parseSentencesFromFile("notaword", getResourceURL(SIMPLE_SENTENCE_FILENAME));
        assertEquals(0, parsedSentences.size());
    }

    @Test
    public void testPunctuatedSentenceWithMatch() throws Exception {
        final List<ParsedSentence> parsedSentences = extractor.parseSentencesFromFile("pneumonia", getResourceURL(PUNCTUATED_SENTENCE));
        assertEquals(1, parsedSentences.size());
        assertParsedSentenceAgainstExpectations(parsedSentences.get(0), punctuatedSentenceExpectedParts, 7);
    }

    @Test
    public void testPunctuatedSentenceWithoutMatch() throws Exception {
        final List<ParsedSentence> parsedSentences = extractor.parseSentencesFromFile("notaword", getResourceURL(PUNCTUATED_SENTENCE));
        assertEquals(0, parsedSentences.size());
    }

    @Test
    public void testIndentedSentenceWithMatch() throws Exception {
        final List<ParsedSentence> parsedSentences = extractor.parseSentencesFromFile("that", getResourceURL(SENTENCE_WITH_INDENTATION));
        assertEquals(1, parsedSentences.size());
        assertParsedSentenceAgainstExpectations(parsedSentences.get(0), indentedSentenceExpectedParts, 14);
    }

    @Test
    public void testIndentedSentenceWithoutMatch() throws Exception {
        final List<ParsedSentence> parsedSentences = extractor.parseSentencesFromFile("notaword", getResourceURL(SENTENCE_WITH_INDENTATION));
        assertEquals(0, parsedSentences.size());
    }

    @Test
    public void testMultipleSentencesWithBothMatches() throws Exception {
        final List<ParsedSentence> parsedSentences = extractor.parseSentencesFromFile("can", getResourceURL(MULTIPLE_SENTENCES));
        assertEquals(2, parsedSentences.size());
        assertParsedSentenceAgainstExpectations(parsedSentences.get(0), simpleSentenceExpectedParts, 3);
        assertParsedSentenceAgainstExpectations(parsedSentences.get(1), indentedSentenceExpectedParts, 16);
    }

    @Test
    public void testMultipleSentencesWithoutMatches() throws Exception {
        final List<ParsedSentence> parsedSentences = extractor.parseSentencesFromFile("notaword", getResourceURL(MULTIPLE_SENTENCES));
        assertEquals(0, parsedSentences.size());
    }

    @Test
    public void testMultipleSentencesWithOneMatch() throws Exception {
        final List<ParsedSentence> parsedSentences = extractor.parseSentencesFromFile("adequately", getResourceURL(MULTIPLE_SENTENCES));
        assertEquals(1, parsedSentences.size());
        assertParsedSentenceAgainstExpectations(parsedSentences.get(0), indentedSentenceExpectedParts, 20);
    }

    @Test
    public void testMultipleMatchesInSameSentence() throws Exception {
        final List<ParsedSentence> parsedSentences = extractor.parseSentencesFromFile("are", getResourceURL(MULTIPLE_SENTENCES));
        assertEquals(1, parsedSentences.size());
        assertParsedSentenceAgainstExpectations(parsedSentences.get(0), indentedSentenceExpectedParts, 1, 19);
    }

    private void assertParsedSentenceAgainstExpectations(final ParsedSentence ps, final String[] expectedParts, final Integer... expectedNodeIndices) {
        List<SentencePart> parts = ps.getSentenceParts();
        assertEquals(expectedParts.length, parts.size());
        for (int i=0; i<expectedParts.length; i++) {
            String expectedPart = expectedParts[i];
            SentencePart part = parts.get(i);
            assertEquals(expectedPart, part.getValue());
            // Simplification of what constitutes punctuation is sufficient for the test data used in this unit test
            if (expectedPart.equals(",") || expectedPart.equals(".")) {
                assertEquals(SentencePartType.PUNCTUATION, part.getSentencePartType());
            } else {
                assertEquals(SentencePartType.WORD, part.getSentencePartType());
            }
        }
        for (int i=0; i<expectedNodeIndices.length; i++) {
            assertEquals(expectedNodeIndices[i], ps.getNodeIndices().get(i));
        }
    }

    private String getResourceURL(final String filename) {
        final ClassLoader classLoader = getClass().getClassLoader();
        final URL url = classLoader.getResource(filename);
        return url.getPath();
    }
}
