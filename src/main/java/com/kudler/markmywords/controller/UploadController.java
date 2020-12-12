package com.kudler.markmywords.controller;

import com.kudler.markmywords.exception.BadParameterException;
import com.kudler.markmywords.service.FileService;
import com.kudler.markmywords.TextFile;
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
    public TextFile uploadFile(@RequestParam(value = "file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadParameterException("file", "file");
        }

        TextFile textFile = fileService.uploadFile(file);
        return textFile;
    }
}