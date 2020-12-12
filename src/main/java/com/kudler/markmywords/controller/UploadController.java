package com.kudler.markmywords.controller;

import com.kudler.markmywords.exception.BadParameterException;
import com.kudler.markmywords.service.FileService;
import com.kudler.markmywords.response.TextFileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    @Autowired
    FileService fileService;

    @PostMapping("/upload")
    public TextFileResponse uploadFile(@RequestParam(value = "file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadParameterException("file", "file");
        }

        TextFileResponse textFile = fileService.uploadFile(file);
        return textFile;
    }
}
