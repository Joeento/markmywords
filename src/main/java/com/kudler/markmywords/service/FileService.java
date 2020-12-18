package com.kudler.markmywords.service;

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

/**
 * Service designed to contain any actions
 * related to reading and writing to files.
 * Used by the UploadController to make a copy of
 * the uploaded file on the disc.
 */

@Service
public class FileService {
    //If app.upload.dir is set in application properties,
    //use the user's home directory.
    @Value("${app.upload.dir:${user.home}}")
    public String uploadDir;

    /**
     * Method for copying a file from a controller onto the disc,
     * and then opening it and returning the data.
     * @param file Any data in the uploaded file as well as it's temporary location.
     * @return Contents of file read into a String.
     * @throws FileUploadException When an error occurs with reading/writing the file on the disc
     */
    public String uploadFile(MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();

            // Only accept text files, otherwise exit with an error message
            if (filename == null || !filename.endsWith(".txt")) {
                throw new FileUploadException("Sorry, text files only!  Please upload a file ending with .txt.");
            }

            InputStream data = file.getInputStream();
            String destination = uploadDir + File.separator + StringUtils.cleanPath(filename);

            // Copy file from it's temporary location into the servers uploads directory
            Path copyLocation = Paths.get(destination);
            Files.copy(data, copyLocation, StandardCopyOption.REPLACE_EXISTING);

            return new String(file.getBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            // Instead of an IOException, we throw a custom-made
            // RuntimeException that's easier for the user to understand
            throw new FileUploadException(e.getMessage());
        }
    }
}
