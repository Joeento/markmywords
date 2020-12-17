package com.kudler.markmywords.service;

import com.kudler.markmywords.exception.BadParameterException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service designed to take in text strings from any
 * source and create Markov Chains out of them.
 */
@Service
public class MarkovService {

    // Unlike C, Java does not have an END_OF_STRING character,
    // so I made my own variable to indicate there's no words left
    public static final String NONWORD = "NONWORD";

    // Since we are splitting our text into words, we need a regex
    // to determine which character denote the end of a word. "\\s+"
    // translates to "any number of whitespace characters in a row".
    public static final String DELIMITER_REGEX = "\\s+";
    
    /**
     * Generate a Markov Chain by creating a prefix, and then
     * appending suffixes to it one at a time until we reach
     * our word limit or we reach randomly choose the last word in
     * the source string.
     * @param text Source string that will be transformed
     * @param size Number of words to use as a prefix
     * @param maxWords Upper bound on when to terminate Markov string.
     * @param prefix optional string to start our transformation.
     * @return String of words representing our generated Markov Chain
     */
    public String chain(String text, int size, int maxWords, String prefix) {
        String[] words = text.split(DELIMITER_REGEX);
        StringBuilder result = new StringBuilder();
        Random random = new Random();

        prefix = (prefix == null) ? buildPrefixString(words, 0, size) :  prefix;
        Map<String, ArrayList<String>> prefixes = buildPrefixTable(text, size);

        if (!prefixes.containsKey(prefix)) {
            throw new BadParameterException("Sorry, your prefix needs to appear at least once in the text document " +
                    "and contain exactly " + size + " words.  To use a default prefix, leave out the 'prefix' field.");
        }

        ArrayList<String> suffixes = prefixes.get(prefix);
        int randomIndex = random.nextInt(suffixes.size());
        String suffix = suffixes.get(randomIndex);
        result.append(prefix);
        if (suffix.equals(NONWORD)) {
            return result.toString();
        }

        result.append(" ");
        result.append(suffix);
        int wordsAdded = size + 1;
        while (!suffix.equals(NONWORD) && (maxWords < 1 || wordsAdded < maxWords)) {
            String[] previousPrefixWords = prefix.split(DELIMITER_REGEX);
            String previousPrefixOffset = buildPrefixString(previousPrefixWords, 1, previousPrefixWords.length);
            StringBuilder prefixBuilder = new StringBuilder();
            prefixBuilder.append(previousPrefixOffset);

            if (size > 1) {
                prefixBuilder.append(" ");
            }

            prefixBuilder.append(suffix);
            prefix = prefixBuilder.toString();

            suffixes = prefixes.get(prefix);
            if (suffixes == null || suffixes.size() == 0) {
                break;
            }

            randomIndex = random.nextInt(suffixes.size());
            suffix = suffixes.get(randomIndex);
            if (suffix.equals(NONWORD)) {
                break;
    /**
     * Helper method to create data structure containing
     * every adjacent word grouping - or "prefix" in our
     * text string, then matching them with their
     * following word - or "suffix".  Traditionally,
     * prefixes and suffixes are mapped via matrix, but
     * we will keep our prefixes indexed in a hashtable, so that
     * access to a suffix list will be O(1).
     *
     * @param text The text that will be broken down into a HashMap
     *             of prefixes as keys, and a list of suffixes
     *             as values.
     * @param size The size of each prefix.  For example, if
     *             size=2, every 2 adjacent words will grouped
     *             and used as keys in the table.
     * @return A HashMap containing every possible of word
     *              grouping in "text" of size "size".  Each key in the
     *              table points to an array list containing the succeeding word
     *              from the original "text" string.
     */
    public Map<String, ArrayList<String>> buildPrefixTable(String text, int size) {
        String[] words = text.split(DELIMITER_REGEX);
        Map<String, ArrayList<String>> prefixes = new HashMap<>();

        for (int i = 0; i <= words.length - size; i++) {
            // Build the prefix string by grouping every word from "i" to the end of the prefix size
            String prefix = buildPrefixString(words, i, i + size);
            // The suffix will be the word following the end of the prefix.
            // If there is not one, use NONWORD to denote the termination of the string.
            String suffix = (i + size < words.length) ? words[i + size] : NONWORD;
            if (!prefixes.containsKey(prefix)) {
                prefixes.put(prefix, new ArrayList<>());
            }

            result.append(" ");
            result.append(suffix);
            wordsAdded++;

        }
        return result.toString();
    }

    /**
     * Helper method to abstract grouping words together to form a prefix.
     * @param words Array of every word in a text string, separated by whitespace.
     * @param start First index in subarray that we will be concatenating, inclusive
     * @param end Last index of subarray, exclusive
     * @return String concatenating all words from start to end with a whitespace.
     */
    public String buildPrefixString(String[] words, int start, int end) {
        return String.join(" ", Arrays.copyOfRange(words, start, end));
    }
}
