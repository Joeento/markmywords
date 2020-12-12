package com.kudler.markmywords;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    @Value("${app.upload.dir:${user.home}}")
    public String uploadDir;

    public TextFile uploadFile(MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();

            if (filename == null || !filename.endsWith(".txt")) {
                throw new FileUploadException("Sorry, text files only!  Please upload a file ending with .txt.");
            }

            InputStream data = file.getInputStream();
            String destination = uploadDir + File.separator + StringUtils.cleanPath(filename);

            Path copyLocation = Paths.get(destination);
            Files.copy(data, copyLocation, StandardCopyOption.REPLACE_EXISTING);
            return new TextFile(destination, "this is placeholder text");
        } catch (IOException e) {
            throw new FileUploadException("Customer not found with id ");
        }
    }
}
