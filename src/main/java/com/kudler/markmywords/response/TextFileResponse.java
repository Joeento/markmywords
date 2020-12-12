package com.kudler.markmywords.response;

public class TextFileResponse {

    private final String path;
    private final String content;

    public TextFileResponse(String path, String content) {
        this.path = path;
        this.content = content;
    }
    public String getPath() {
        return path;
    }
    public String getContent() {
        return content;
    }
}
