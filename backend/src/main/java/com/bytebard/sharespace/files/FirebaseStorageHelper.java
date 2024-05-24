package com.bytebard.sharespace.files;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.Future;

public class FirebaseStorageHelper {

    private static final String BUCKET_NAME = "hive-d69c6.appspot.com";

    private static final String ADMIN_SDK_PATH = "hive.json";

    private static final String MEDIA_TYPE = "media";

    private static Storage storage = null;

    private FirebaseStorageHelper() {}

    private static Storage getStorage() throws IOException {
        if (storage == null) {
            InputStream inputStream = FirebaseStorageHelper.class.getClassLoader().getResourceAsStream(ADMIN_SDK_PATH);

            if (inputStream == null) {
                throw new FileNotFoundException("Sdk file configuration not found");
            }

            Credentials credentials = GoogleCredentials.fromStream(inputStream);
            storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        }
        return storage;
    }

    public static String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of(BUCKET_NAME, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(MEDIA_TYPE).build();

        getStorage().create(blobInfo, Files.readAllBytes(file.toPath()));

        String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media";
        return String.format(DOWNLOAD_URL, BUCKET_NAME, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    public static boolean deleteFile(String fileName) throws IOException {
        BlobId blobId = BlobId.of(BUCKET_NAME, fileName);
        return getStorage().delete(blobId);
    }

    public static boolean exists(String fileName) {
        Blob blob = storage.get(BUCKET_NAME, fileName);
        return blob != null;
    }
}
