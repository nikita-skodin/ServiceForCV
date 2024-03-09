package com.nikita.taskcreateserviceforcv.services;

import com.nikita.taskcreateserviceforcv.exceptions.ImageUploadException;
import com.nikita.taskcreateserviceforcv.services.interfaces.FileService;
import com.nikita.taskcreateserviceforcv.util.props.MinioProperties;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public String upload(final MultipartFile file) {
        try {
            createBucket();
        } catch (Exception e) {
            throw new ImageUploadException("File upload failed: "
                                           + e.getMessage());
        }

        String fileName = generateFileName(file);
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            throw new ImageUploadException("File upload failed: "
                                           + e.getMessage());
        }
        saveImage(inputStream, fileName);
        return fileName;
    }

    @SneakyThrows
    private void createBucket() {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(minioProperties.getBucket())
                .build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .objectLock(false)
                    .build());
        }
    }

    private String generateFileName(final MultipartFile file) {
        String extension = getExtension(file);
        return UUID.randomUUID() + "." + extension;
    }

    private String getExtension(final MultipartFile file) {
        return file.getOriginalFilename()
                .substring(file.getOriginalFilename()
                                   .lastIndexOf(".") + 1);
    }

    @SneakyThrows
    public boolean eqFile(String name, MultipartFile file) {
        GetObjectResponse images = null;
        try {
            images = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(name)
                    .build());
        } catch (Exception e) {
            throw new ImageUploadException("Exception when processing a file with the name %s".formatted(name));
        }

        byte[] bytes = images.readAllBytes();
        byte[] bytes1 = file.getBytes();

        return Arrays.equals(bytes1, bytes);
    }

    public void rmFile(String name) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(name)
                    .build());
        } catch (Exception e) {
            throw new ImageUploadException("Exception when processing a file with the name %s".formatted(name));
        }
    }

    @SneakyThrows
    private void saveImage(
            final InputStream inputStream,
            final String fileName
    ) {
        minioClient.putObject(PutObjectArgs.builder()
                .stream(inputStream, inputStream.available(), -1)
                .bucket(minioProperties.getBucket())
                .object(fileName)
                .build());
    }

}