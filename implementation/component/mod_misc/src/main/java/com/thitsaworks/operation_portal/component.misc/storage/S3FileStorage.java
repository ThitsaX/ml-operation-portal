package com.thitsaworks.operation_portal.component.misc.storage;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.Duration;
import java.util.Date;
import java.util.Locale;

@Service
public class S3FileStorage  implements FileStorage{

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

    private final AmazonS3 amazonS3;

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

        this.amazonS3 = this.enabled ? this.buildClient() : null;
    }

    public String upload(String fileLocation, byte[] fileBytes, String extension) {

        if (!this.enabled) {

            throw new IllegalStateException("S3 upload is disabled. Set REPORT_S3_ENABLED=true.");
        }

        if (this.bucket.isBlank()) {

            throw new IllegalStateException("S3 bucket is missing. Set REPORT_S3_BUCKET.");
        }

        var key = this.prefix + fileLocation;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileBytes.length);
        metadata.setContentType(this.contentType(extension));

        PutObjectRequest putObjectRequest = new PutObjectRequest(
            this.bucket, key, new ByteArrayInputStream(fileBytes), metadata);

        this.amazonS3.putObject(putObjectRequest);

        LOG.info("Uploaded to S3 [{}]", key);

        return key;
    }

    private AmazonS3 buildClient() {

        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder
                                            .standard()
                                            .withPathStyleAccessEnabled(this.pathStyleAccess);

        if (!this.accessKey.isBlank() && !this.secretKey.isBlank()) {

            builder.withCredentials(new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(this.accessKey, this.secretKey)));

        } else {

            builder.withCredentials(DefaultAWSCredentialsProviderChain.getInstance());
        }

        if (!this.endpoint.isBlank()) {

            builder.withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration(this.endpoint, this.region));
        } else {

            builder.withRegion(this.region);
        }

        return builder.build();
    }

    public String generatePreSignedDownloadUrl(String key) {

        Date expiration = new Date(
            System.currentTimeMillis() + this.presignedUrlLifetime.toMillis());
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(this.bucket, key)
                                                  .withMethod(HttpMethod.GET)
                                                  .withExpiration(expiration);

        return this.amazonS3.generatePresignedUrl(request).toString();
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
