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
    public MarkovChainResponse upload(@RequestParam(value = "file", required = true) MultipartFile file, @RequestParam(required = true) Integer n) {
        if (file.isEmpty()) {
            throw new BadParameterException("file", "file");
        }

        String fileContent = fileService.uploadFile(file);
        String result = markovService.chain(fileContent, n);
        return new MarkovChainResponse(fileContent, result);
    }
}
