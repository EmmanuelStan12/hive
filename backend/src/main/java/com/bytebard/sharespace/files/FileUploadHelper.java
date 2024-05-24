package com.bytebard.sharespace.files;

import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileUploadHelper {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }

    public Pair<String, String> upload(MultipartFile multipartFile) throws IOException {
        try {
            String filename = multipartFile.getOriginalFilename();

            if (filename == null || filename.isEmpty()) {
                throw new IllegalArgumentException("Filename cannot be null or empty");
            }

            String uuid = UUID.randomUUID().toString();

            filename = uuid.concat(getExtension(filename));

            File file = convertToFile(multipartFile, filename);
            String fileUrl = FirebaseStorageHelper.uploadFile(file, filename);
            Pair<String, String> fileProps = new Pair<>(filename, fileUrl);
            file.delete();
            return fileProps;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public void delete(String filename) throws IOException {
        if (filename == null || !FirebaseStorageHelper.exists(filename)) {
            return;
        }
        try {
            filename = filename.concat(getExtension(filename));
            FirebaseStorageHelper.deleteFile(filename);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

}
