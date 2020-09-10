package org.bamford.KWIC;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Main class for the application
 * Produces a text file with all specified concordances in a passed file or (recursively) in a passed directory
 */
public class KeyWordInContextProcessor {
    private final XMLSentenceParser parser = new XMLSentenceParser();
    private final ConcordanceFormatter formatter = new ConcordanceFormatter();
    private String node;
    private int windowSize;
    private BufferedWriter br;

    /**
     * Entry point to the application
     * @param args expected to contain...
     *            [0]   The path of an xml file or directory containing xml files (including subdirectories) which will be processed
     *            [1]   The node to search for
     *            [2]   The window size
     */
    public static void main(String[] args) {
        KeyWordInContextProcessor processor = new KeyWordInContextProcessor();
        if (args.length < 3) {
            System.out.println("Key Word in context application requires 3 parameters.  (1) File or directory path (2) node (3) Windows size");
            return;
        }
        int windowSize;
        try {
            windowSize = Integer.parseInt(args[2]);
        } catch (final NumberFormatException nfe) {
            System.out.println("Third parameter Window Size must be numeric");
            return;
        }
        try {
            processor.produceConcordances(args[0], args[1], windowSize);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    void produceConcordances(final String filePath, final String node, final int windowSize) throws ParserConfigurationException, SAXException, IOException {
        this.node = node;
        this.windowSize = windowSize;

        File file = new File("concordances.txt");
        FileWriter fr = new FileWriter(file);
        br = new BufferedWriter(fr);

        File path = new File(filePath);
        processFileOrDirectory(path);

        br.close();
        fr.close();
    }

    private void processFileOrDirectory(final File file) throws ParserConfigurationException, SAXException, IOException {
        if (file.isFile()) {
            if ("xml".equalsIgnoreCase(getFileExtension(file))) {
                produceConcordancesForFile(file.getPath());
            }
        } else {
            processDirectory(file);
        }
    }

    private void processDirectory(final File directory) throws ParserConfigurationException, SAXException, IOException {
        if (directory.listFiles() != null) {
            for (final File file : directory.listFiles()) {
                processFileOrDirectory(file);
            }
        }
    }

    private void produceConcordancesForFile(final String filePath) throws ParserConfigurationException, SAXException, IOException {
        br.write("*** Processing File : " + filePath);
        br.newLine();
        final List<ParsedSentence> parsedSentences = parser.parseSentencesFromFile(node, filePath);
        final List<String> concordances = formatter.formatConcordances(parsedSentences, windowSize);
        for (String concordance : concordances) {
            br.write(concordance);
            br.newLine();
        }
    }

    private String getFileExtension(File file) {
        final String name = file.getName();
        final int lastIndexOf = name.lastIndexOf('.');
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf + 1);
    }
}
