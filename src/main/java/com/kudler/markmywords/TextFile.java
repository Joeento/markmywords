package com.kudler.markmywords;

public class TextFile {

    private final String path;
    private final String content;

    public TextFile(String path, String content) {
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
