package org.bamford.KWIC;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides functionality to parse sentences from an XML file
 */
public class XMLSentenceParser {

    private static final String SENTENCE_TAG = "S";
    private static final String WORD_TAG = "W";
    private static final String PUNCTUATION_TAG = "C";

    /**
     * Parse all sentences from the passed file
     * For each sentence that contains one or more occurrences of the passed node, construct a {@link ParsedSentence} object
     * Return a collection of all such {@link ParsedSentence}s
     * @param node  The node of interest
     * @param filePath  The path to the file to be parsed
     * @return  a collection of {@link ParsedSentence} objects, each one representing a sentence that contains one or more occurrences of the node of interest
     */
    public List<ParsedSentence> parseSentencesFromFile(final String node, final String filePath) throws IOException, SAXException, ParserConfigurationException {
        final List<ParsedSentence> parsedSentences = new ArrayList<>();
        final SAXParserFactory factory = SAXParserFactory.newInstance();
        final SAXParser saxParser = factory.newSAXParser();

        DefaultHandler handler = new DefaultHandler() {
            ParsedSentence sentence;
            boolean foundSentence = false;
            boolean foundWord = false;
            boolean foundPunctuation = false;

            @Override
            public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
                if (qName.equalsIgnoreCase(SENTENCE_TAG)) {
                    foundSentence = true;
                }
                if (qName.equalsIgnoreCase(WORD_TAG)) {
                    foundWord = true;
                }
                if (qName.equalsIgnoreCase(PUNCTUATION_TAG)) {
                    foundPunctuation = true;
                }
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
                if (qName.equalsIgnoreCase(SENTENCE_TAG) && !sentence.getNodeIndices().isEmpty()) {
                    parsedSentences.add(sentence);
                }
            }

            @Override
            public void characters(char ch[], int start, int length) throws SAXException {
                if (foundSentence) {
                    sentence = new ParsedSentence();
                    foundSentence = false;
                }

                if (foundWord) {
                    final String word = new String(ch, start, length).trim();
                    sentence.addSentencePart(new SentencePart(SentencePartType.WORD, word));
                    if (word.equalsIgnoreCase(node)) {
                        sentence.addNodeIndex(sentence.getSentenceParts().size() - 1);
                    }
                    foundWord = false;
                }

                if (foundPunctuation) {
                    sentence.addSentencePart(new SentencePart(SentencePartType.PUNCTUATION, new String(ch, start, length).trim()));
                    foundPunctuation = false;
                }
            }

        };
        saxParser.parse(filePath, handler);
        return parsedSentences;
    }

}
