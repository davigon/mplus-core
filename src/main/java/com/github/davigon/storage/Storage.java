package com.github.davigon.storage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.StorageOptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Storage {
    private static final String PROYECT_ID = "mplus-802b4";
    private static final String BUCKET_NAME = "mplus-802b4.appspot.com";
    private final com.google.cloud.storage.Storage storage;

    public Storage() throws IOException {
        storage = StorageOptions.newBuilder()
                .setProjectId(PROYECT_ID)
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .build()
                .getService();
    }

    public Blob uploadFile(Path filePath, String contentType, String fileName) throws IOException {
        BlobId blobId = BlobId.of(BUCKET_NAME, fileName);
        BlobInfo blobInfo = BlobInfo
                .newBuilder(blobId)
                .setContentType(contentType)
                .build();
        return storage.create(blobInfo, Files.readAllBytes(filePath));
    }

}
