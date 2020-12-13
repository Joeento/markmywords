package com.kudler.markmywords.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

        /*
        for (Map.Entry<String, ArrayList<Character>> entry : prefixes.entrySet()) {
            String values = "";
            for (int i = 0; i < entry.getValue().size(); i++) {
                values += entry.getValue().get(i) + ",";
            }
            System.out.println("Key = '" + entry.getKey() + "', Value = '" + values +"'");
        }
         */
        return prefixes;
    }
}
