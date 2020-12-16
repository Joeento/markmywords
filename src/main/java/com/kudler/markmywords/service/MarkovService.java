package com.kudler.markmywords.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MarkovService {

    public static final String NONWORD = "\n";
    public static final String DELIMITER_REGEX = "\\s+";

    public Map<String, ArrayList<String>> buildPrefixTable(String text, int size) {
        String[] words = text.split(DELIMITER_REGEX);
        Map<String, ArrayList<String>> prefixes = new HashMap<String, ArrayList<String>>();

        for (int i = 0; i <= words.length - size; i++) {
            String prefix = buildPrefixString(words, i, i + size);
            String suffix = (i + size < words.length) ? words[i + size] : NONWORD;
            if (!prefixes.containsKey(prefix)) {
                prefixes.put(prefix, new ArrayList<String>());
            }
            prefixes.get(prefix).add(suffix);
        }

        return prefixes;
    }

    public String generate(String text, int size, int maxWords) {
        String[] words = text.split(DELIMITER_REGEX);
        String prefix = buildPrefixString(words, 0, size);
        StringBuilder result = new StringBuilder();
        Random random = new Random();

        Map<String, ArrayList<String>> prefixes = buildPrefixTable(text, size);
        ArrayList<String> suffixes = prefixes.get(prefix);
        int randomIndex = random.nextInt(suffixes.size());
        String suffix = suffixes.get(randomIndex);
        result.append(prefix);
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
            }

            result.append(" ");
            result.append(suffix);
            wordsAdded++;

        }
        return result.toString();
    }

    public String chain(String text, int size, int length) {
        return generate(text, size, length);
    }

    public String buildPrefixString(String[] words, int start, int end) {
        return String.join(" ", Arrays.copyOfRange(words, start, end));
    }
}
