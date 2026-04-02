package com.thitsaworks.operation_portal.component.misc.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.net.URI;
import java.time.Duration;
import java.util.Locale;

@Service
public class S3FileStorage implements FileStorage {

    private static final Logger LOG = LoggerFactory.getLogger(S3FileStorage.class);

    public static final String S3_SETTINGS_PATH = "s3/settings";

    private final boolean enabled;

    private final String bucket;

    private final String region;

    private final String accessKey;

    private final String secretKey;

    private final String prefix;

    private final String endpoint;

    private final boolean pathStyleAccess;

    private final Duration presignedUrlLifetime;

    private final S3Client s3Client;

    private final S3Presigner s3Presigner;

    public S3FileStorage(Settings settings) {

        this.enabled = settings.enabled;
        this.bucket = settings.bucket;
        this.region = settings.region;
        this.accessKey = settings.accessKey;
        this.secretKey = settings.secretKey;
        this.prefix = settings.prefix;
        this.endpoint = settings.endpoint;
        this.pathStyleAccess = settings.pathStyleAccess;
        this.presignedUrlLifetime = settings.presignedUrlLifetime;

        this.s3Client = this.enabled ? this.buildClient() : null;
        this.s3Presigner = this.enabled ? this.buildPresigner() : null;
    }

    public String upload(String fileLocation, byte[] fileBytes, String extension) {

        if (!this.enabled) {

            throw new IllegalStateException("S3 upload is disabled. Set REPORT_S3_ENABLED=true.");
        }

        if (this.bucket.isBlank()) {

            throw new IllegalStateException("S3 bucket is missing. Set REPORT_S3_BUCKET.");
        }

        var key = this.prefix + fileLocation;

        PutObjectRequest putObjectRequest = PutObjectRequest
                                                .builder()
                                                .bucket(this.bucket)
                                                .key(key)
                                                .contentType(this.contentType(extension))
                                                .contentLength((long) fileBytes.length)
                                                .build();

        this.s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileBytes));

        LOG.info("Uploaded to S3 [{}]", key);

        return key;
    }

    private S3Client buildClient() {

        var builder = S3Client
                          .builder()
                          .credentialsProvider(this.credentialsProvider())
                          .region(Region.of(this.region))
                          .serviceConfiguration(S3Configuration
                                                    .builder()
                                                    .pathStyleAccessEnabled(this.pathStyleAccess)
                                                    .build());

        if (!this.endpoint.isBlank()) {

            builder.endpointOverride(URI.create(this.endpoint));
        }

        return builder.build();
    }

    private S3Presigner buildPresigner() {

        S3Presigner.Builder builder = S3Presigner
                                          .builder()
                                          .credentialsProvider(this.credentialsProvider())
                                          .region(Region.of(this.region))
                                          .serviceConfiguration(S3Configuration
                                                                    .builder()
                                                                    .pathStyleAccessEnabled(
                                                                        this.pathStyleAccess)
                                                                    .build());

        if (!this.endpoint.isBlank()) {

            builder.endpointOverride(URI.create(this.endpoint));
        }

        return builder.build();
    }

    private AwsCredentialsProvider credentialsProvider() {

        if (!this.accessKey.isBlank() && !this.secretKey.isBlank()) {

            return StaticCredentialsProvider.create(
                AwsBasicCredentials.create(this.accessKey, this.secretKey));
        }

        return DefaultCredentialsProvider.create();
    }

    public String generatePreSignedDownloadUrl(String key) {

        LOG.info("Presigned URL Life time seconds : [{}]", this.presignedUrlLifetime.getSeconds());

        GetObjectRequest getObjectRequest = GetObjectRequest
                                                .builder()
                                                .bucket(this.bucket)
                                                .key(key)
                                                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest
                                                     .builder()
                                                     .signatureDuration(this.presignedUrlLifetime)
                                                     .getObjectRequest(getObjectRequest)
                                                     .build();

        PresignedGetObjectRequest presignedRequest = this.s3Presigner.presignGetObject(
            presignRequest);

        return presignedRequest.url().toString();
    }

    private String contentType(String extension) {

        return switch (extension.toLowerCase(Locale.ROOT)) {
            case "csv" -> "text/csv";
            case "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "zip" -> "application/zip";
            default -> "application/octet-stream";
        };
    }

    public record Settings(boolean enabled,
                           String bucket,
                           String region,
                           String accessKey,
                           String secretKey,
                           String prefix,
                           String endpoint,
                           boolean pathStyleAccess,
                           Duration presignedUrlLifetime) { }

}
