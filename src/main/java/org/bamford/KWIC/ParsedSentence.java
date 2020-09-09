package org.bamford.KWIC;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates all parsed data from a sentence, ie the collection of symbols and the indices of each node of interest
 */
public class ParsedSentence {
    private List<SentencePart> sentenceParts = new ArrayList<>();
    private List<Integer> nodeIndices = new ArrayList<>();

    /**
     * Add a {@link SentencePart} to the ordered collection of {@link SentencePart}s
     * @param sp    The {@link SentencePart}
     */
    public void addSentencePart(final SentencePart sp) {
        sentenceParts.add(sp);
    }

    /**
     * Add a node index of interest to the ordered collection of node indices for this sentence
     * @param ni    The node index
     */
    public void addNodeIndex(final Integer ni) {
        nodeIndices.add(ni);
    }

    public List<SentencePart> getSentenceParts() {
        return sentenceParts;
    }

    public List<Integer> getNodeIndices() {
        return nodeIndices;
    }

    public void setSentenceParts(List<SentencePart> sentenceParts) {
        this.sentenceParts = sentenceParts;
    }
}
