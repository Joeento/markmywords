package com.kudler.markmywords.controller;

import com.kudler.markmywords.exception.BadParameterException;
import com.kudler.markmywords.service.FileService;
import com.kudler.markmywords.response.TextFileResponse;
import com.kudler.markmywords.service.MarkovService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Map;

@RestController
public class UploadController {

    @Autowired
    FileService fileService;
    @Autowired
    MarkovService markovService;

    @PostMapping("/upload")
    public TextFileResponse upload(@RequestParam(value = "file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadParameterException("file", "file");
        }

        TextFileResponse textFile = fileService.uploadFile(file);
        Map<String, ArrayList<Character>> prefixes = markovService.buildPrefixTable("the theremin is theirs, ok? yes, it is, this is a theremin.", 3);
        return textFile;
    }
}
