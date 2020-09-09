package org.bamford.KWIC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides functionality to format parsed sentences to display concordance in a human-readable format
 */
public class ConcordanceFormatter {

    /**
     * Given a collection of {@link ParsedSentence}s, then for each node index of interest in the {@link ParsedSentence}
     * construct a concordance centered on that node using the given window of words before and after
     * Punctuation is included in the result but not counted towards the window size
     * If window size extends beyond the beginning/end of the sentence theh the concordance will be bounded by the sentence bounds
     * @param sentences A collection of {@link ParsedSentence}s
     * @param windowSize    The window size for the concordance
     * @return  A collection of constructed concordances
     */
    public List<String> formatConcordances(final List<ParsedSentence> sentences, final int windowSize) {
        final List<String> results = new ArrayList<>();
        for (ParsedSentence ps : sentences) {
            final List<SentencePart> sentenceParts = ps.getSentenceParts();
            for (Integer nodeIndex : ps.getNodeIndices()) {
                final List<SentencePart> partsAfter = getSentencePartsAfterNode(sentenceParts, nodeIndex, windowSize);
                final List<SentencePart> partsBefore = getSentencePartsBeforeNode(sentenceParts, nodeIndex, windowSize);
                results.add(formatResult(partsBefore, sentenceParts.get(nodeIndex), partsAfter));
            }
        }
        return results;
    }

    private List<SentencePart> getSentencePartsAfterNode(final List<SentencePart> sentenceParts, final int nodeIndex, final int windowSize) {
        final List<SentencePart> postSentenceParts = new ArrayList<>();
        int currentIndex = nodeIndex;
        int wordsAdded = 0;
        while(sentenceParts.size() > currentIndex + 1 && wordsAdded < windowSize) {
            currentIndex++;
            final SentencePart currentPart = sentenceParts.get(currentIndex);
            postSentenceParts.add(currentPart);
            if (currentPart.getSentencePartType().equals(SentencePartType.WORD)) {
                wordsAdded++;
            }
        }
        return postSentenceParts;
    }

    private List<SentencePart> getSentencePartsBeforeNode(final List<SentencePart> sentenceParts, final int nodeIndex, final int windowSize) {
        final List<SentencePart> copySentenceParts = new ArrayList<>(sentenceParts);
        Collections.reverse(copySentenceParts);
        final List<SentencePart> wordsBefore = getSentencePartsAfterNode(copySentenceParts, copySentenceParts.size() - 1 - nodeIndex, windowSize);
        Collections.reverse(wordsBefore);
        return wordsBefore;
    }



    private String formatResult(List<SentencePart> partsBefore, SentencePart node, List<SentencePart> partsAfter) {
        final List<SentencePart> all = new ArrayList<>();
        all.addAll(partsBefore);
        all.add(node);
        all.addAll(partsAfter);
        boolean first = true;
        StringBuilder sb = new StringBuilder();
        for (SentencePart part : all) {
            if (!first && part.getSentencePartType().equals(SentencePartType.WORD)) {
                sb.append(" ");
            }
            sb.append(part.getValue());
            first = false;
        }
        return sb.toString();
    }
}
