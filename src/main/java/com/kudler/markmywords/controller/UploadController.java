package com.kudler.markmywords.controller;

import com.kudler.markmywords.exception.BadParameterException;
import com.kudler.markmywords.response.MarkovChainResponse;
import com.kudler.markmywords.service.FileService;
import com.kudler.markmywords.service.MarkovService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    @Autowired
    FileService fileService;
    @Autowired
    MarkovService markovService;

    @PostMapping("/upload")
    public MarkovChainResponse upload(@RequestParam(value = "file", required = true) MultipartFile file, @RequestParam(defaultValue = "3") Integer n) {
        if (file.isEmpty()) {
            throw new BadParameterException("Sorry, one of your parameters was invalid.  " +
                    "Please make sure you have a 'file' field of type 'File'");
        }

        if (n < 1) {
            throw new BadParameterException("Sorry, your prefix size must be one or greater.");
        }

        String fileContent = fileService.uploadFile(file);
        String[] words = fileContent.split(markovService.DELIMITER_REGEX);
        if (n > words.length) {
            throw new BadParameterException("Sorry, you can't have a prefix larger than the size of your text.");
        }

        String result = markovService.chain(fileContent, n);
        return new MarkovChainResponse(fileContent, result);
    }
}
