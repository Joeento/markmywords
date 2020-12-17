package com.kudler.markmywords.response;

/**
 * Class of data that we need collected and serialized
 * into a JSON HTTP response when a Markov Chain is generated.
 */

public class MarkovChainResponse {
    private final String original;
    private final String result;

    public MarkovChainResponse(String original, String result) {
        this.original = original;
        this.result = result;
    }
    public String getOriginal() {
        return original;
    }
    public String getResult() {
        return result;
    }
}
