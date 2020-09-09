package org.bamford.KWIC;

/**
 * Encapsulates the value and type of a single symbol in a sentence
 */
public class SentencePart {
    private SentencePartType sentencePartType;
    private String value;

    /**
     * Construct a new {@link SentencePart}
     * @param sentencePartType  The {@link SentencePartType}
     * @param value The text value of the symbol, eg "the" or ","
     */
    public SentencePart(final SentencePartType sentencePartType, final String value) {
        this.sentencePartType = sentencePartType;
        this.value = value;
    }

    public SentencePartType getSentencePartType() {
        return sentencePartType;
    }

    public void setSentencePartType(final SentencePartType sentencePartType) {
        this.sentencePartType = sentencePartType;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
