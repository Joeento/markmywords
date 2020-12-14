package com.kudler.markmywords.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MarkovService {

    public static final char NONWORD = '\n';

    public Map<String, ArrayList<Character>> buildPrefixTable(String text, int size) {
        Map<String, ArrayList<Character>> prefixes = new HashMap<String, ArrayList<Character>>();
        for (int i = 0; i <= text.length() - size; i++) {
            String prefix = text.substring(i, i + size);
            char suffix = (i + size < text.length()) ? text.charAt(i + size) : NONWORD;
            if (!prefixes.containsKey(prefix)) {
                prefixes.put(prefix, new ArrayList<Character>());
            }
            prefixes.get(prefix).add(suffix);
        }

        return prefixes;
    }

    public String generate(Map<String, ArrayList<Character>> prefixes, int size, String text) {
        String prefix = text.substring(0, size);
        StringBuilder result = new StringBuilder();

        Random random = new Random();
        int randomIndex = random.nextInt(prefixes.get(prefix).size());
        char suffix = prefixes.get(prefix).get(randomIndex);
        result.append(prefix);
        result.append(suffix);

        while (suffix != NONWORD) {
            prefix = result.substring(result.length() - size, result.length());
            ArrayList suffixes = prefixes.get(prefix);
            if (suffixes == null || suffixes.size() == 0) {
                break;
            }

            randomIndex = random.nextInt(prefixes.get(prefix).size());
            suffix = prefixes.get(prefix).get(randomIndex);
            result.append(suffix);
        }

        /*
        result.append(result);

        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            List<Character> suffixes = prefixes.get(prefix);
            if (suffixes == null  || suffixes.size() == 0) {
                break;
            }
            char suffix = suffixes.get(random.nextInt(suffixes.size()));

        }
         */
        return result.toString();
    }

    public String chain(String text, int size) {
        return generate(buildPrefixTable(text, size), size, text);
    }

    public void printPrefixTable(Map<String, ArrayList<Character>> prefixes) {
        for (Map.Entry<String, ArrayList<Character>> entry : prefixes.entrySet()) {
            String values = "";
            for (int i = 0; i < entry.getValue().size(); i++) {
                values += entry.getValue().get(i) + ",";
            }
            System.out.println("Key = '" + entry.getKey() + "', Value = '" + values +"'");
        }
    }
}
