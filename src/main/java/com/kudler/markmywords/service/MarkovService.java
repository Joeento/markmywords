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

    public String generate(Map<String, ArrayList<String>> prefixes, int size, String text) {
        String[] words = text.split(DELIMITER_REGEX);
        String prefix = buildPrefixString(words, 0, size);
        StringBuilder result = new StringBuilder();
        Random random = new Random();

        ArrayList<String> suffixes = prefixes.get(prefix);
        int randomIndex = random.nextInt(suffixes.size());
        String suffix = suffixes.get(randomIndex);
        result.append(prefix);
        result.append(" ");
        result.append(suffix);
        while (!suffix.equals(NONWORD)) {
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
        }

        return result.toString();
    }

    public String chain(String text, int size) {
        return generate(buildPrefixTable(text, size), size, text);
    }

    public void printPrefixTable(Map<String, ArrayList<String>> prefixes) {
        for (Map.Entry<String, ArrayList<String>> entry : prefixes.entrySet()) {
            String values = "";
            for (int i = 0; i < entry.getValue().size(); i++) {
                values += entry.getValue().get(i) + ",";
            }
            System.out.println("Key = '" + entry.getKey() + "', Value = '" + values +"'");
        }
    }

    public String buildPrefixString(String[] words, int start, int end) {
        return String.join(" ", Arrays.copyOfRange(words, start, end));
    }
}
