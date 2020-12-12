package com.kudler.markmywords.service;

import com.kudler.markmywords.response.TextFileResponse;
import com.kudler.markmywords.exception.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    @Value("${app.upload.dir:${user.home}}")
    public String uploadDir;

    public TextFileResponse uploadFile(MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();

            if (filename == null || !filename.endsWith(".txt")) {
                throw new FileUploadException("Sorry, text files only!  Please upload a file ending with .txt.");
            }

            InputStream data = file.getInputStream();
            String destination = uploadDir + File.separator + StringUtils.cleanPath(filename);

            Path copyLocation = Paths.get(destination);
            Files.copy(data, copyLocation, StandardCopyOption.REPLACE_EXISTING);

            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            return new TextFileResponse(destination, content);
        } catch (IOException e) {
            throw new FileUploadException(e.getMessage());
        }
    }
}
