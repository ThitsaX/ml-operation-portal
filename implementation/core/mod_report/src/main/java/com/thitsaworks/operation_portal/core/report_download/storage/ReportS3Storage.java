package com.thitsaworks.operation_portal.core.report_download.storage;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.thitsaworks.operation_portal.core.report_download.model.ReportDownloadRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.nio.charset.StandardCharsets;

@Service
public class ReportS3Storage {

    private static final Logger LOG = LoggerFactory.getLogger(ReportS3Storage.class);

    private final boolean enabled;
    private final String bucket;
    private final String region;
    private final String accessKey;
    private final String secretKey;
    private final String prefix;
    private final String endpoint;
    private final boolean pathStyleAccess;
    private final String publicBaseUrl;
    private final Duration presignedUrlLifetime;
    private final AmazonS3 amazonS3;

    public ReportS3Storage() {

        this.enabled = Boolean.parseBoolean(this.readConfig("REPORT_S3_ENABLED", "true"));
        this.bucket = this.readConfig("REPORT_S3_BUCKET", "tw-mcix-test");
        this.region = this.readConfig("REPORT_S3_REGION", "ap-southeast-1");
        this.accessKey = this.readConfig("REPORT_S3_ACCESS_KEY", "AKIAVJP4O2TSYIKUNH6Z");
        this.secretKey = this.readConfig("REPORT_S3_SECRET_KEY", "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");
        this.prefix = this.readConfig("REPORT_S3_PREFIX", "report-downloads");
        this.endpoint = this.readConfig("REPORT_S3_ENDPOINT", "");
        this.pathStyleAccess = Boolean.parseBoolean(this.readConfig("REPORT_S3_PATH_STYLE_ACCESS", "true"));
        this.publicBaseUrl = this.readConfig("REPORT_S3_PUBLIC_BASE_URL", "");
        this.presignedUrlLifetime =
                Duration.ofSeconds(Long.parseLong(this.readConfig("REPORT_S3_PRESIGNED_TTL_SECONDS", "3600")));

        this.amazonS3 = this.enabled ? this.buildClient() : null;
    }

    public String upload(ReportDownloadRequest request, byte[] fileBytes, String extension) {

        if (!this.enabled) {

            throw new IllegalStateException("S3 upload is disabled. Set REPORT_S3_ENABLED=true.");
        }

        if (this.bucket.isBlank()) {

            throw new IllegalStateException("S3 bucket is missing. Set REPORT_S3_BUCKET.");
        }

        String key = this.objectKey(request, extension);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileBytes.length);
        metadata.setContentType(this.contentType(extension));

        PutObjectRequest putObjectRequest =
                new PutObjectRequest(this.bucket, key, new ByteArrayInputStream(fileBytes), metadata);

        this.amazonS3.putObject(putObjectRequest);
        String uploadedUrl = this.generatePreSignedDownloadUrl(key);
        LOG.info("Uploaded report request [{}] to S3 [{}]", request.getId().getEntityId(), uploadedUrl);
        return uploadedUrl;
    }

    public String regeneratePreSignedDownloadUrl(String storedFileLocationOrUrl) {

        String key = this.extractObjectKey(storedFileLocationOrUrl);
        return this.generatePreSignedDownloadUrl(key);
    }

    private AmazonS3 buildClient() {

        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard()
                                                             .withPathStyleAccessEnabled(this.pathStyleAccess);

        if (!this.accessKey.isBlank() && !this.secretKey.isBlank()) {

            builder.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(this.accessKey, this.secretKey)));

        } else {

            builder.withCredentials(DefaultAWSCredentialsProviderChain.getInstance());
        }

        if (!this.endpoint.isBlank()) {

            builder.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(this.endpoint, this.region));
        } else {

            builder.withRegion(this.region);
        }

        return builder.build();
    }

    private String objectKey(ReportDownloadRequest request, String extension) {

        String normalizedType = request.getReportType().toLowerCase(Locale.ROOT);
        String timestamp = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
                                            .format(LocalDateTime.now(ZoneOffset.UTC));
        String normalizedExt = extension.toLowerCase(Locale.ROOT);

        return this.prefix + "/" + normalizedType + "/" + request.getId().getEntityId() + "_" + timestamp + "." + normalizedExt;
    }

    private String objectUrl(String key) {

        if (!this.publicBaseUrl.isBlank()) {

            return this.publicBaseUrl.endsWith("/") ? this.publicBaseUrl + key : this.publicBaseUrl + "/" + key;
        }

        if (!this.endpoint.isBlank()) {

            String normalizedEndpoint = this.endpoint.endsWith("/") ? this.endpoint.substring(0, this.endpoint.length() - 1) : this.endpoint;
            return normalizedEndpoint + "/" + this.bucket + "/" + key;
        }

        return "s3://" + this.bucket + "/" + key;
    }

    private String generatePreSignedDownloadUrl(String key) {

        Date expiration = new Date(System.currentTimeMillis() + this.presignedUrlLifetime.toMillis());
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(this.bucket, key)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);

        return this.amazonS3.generatePresignedUrl(request).toString();
    }

    private String extractObjectKey(String storedFileLocationOrUrl) {

        if (storedFileLocationOrUrl == null || storedFileLocationOrUrl.isBlank()) {

            throw new IllegalArgumentException("Stored file location/url is empty");
        }

        String value = storedFileLocationOrUrl.trim();

        if (value.startsWith("s3://")) {

            String withoutScheme = value.substring("s3://".length());
            int slashIndex = withoutScheme.indexOf('/');

            if (slashIndex < 0) {

                throw new IllegalArgumentException("Invalid s3 location: " + value);
            }

            String bucketFromLocation = withoutScheme.substring(0, slashIndex);
            if (!this.bucket.equals(bucketFromLocation)) {

                throw new IllegalArgumentException("Bucket mismatch in s3 location: " + value);
            }

            return withoutScheme.substring(slashIndex + 1);
        }

        URI uri = URI.create(value);
        String rawPath = uri.getPath();

        if (rawPath == null || rawPath.isBlank() || "/".equals(rawPath)) {

            throw new IllegalArgumentException("URL does not contain object key path: " + value);
        }

        String decodedPath = URLDecoder.decode(rawPath, StandardCharsets.UTF_8);
        String normalizedPath = decodedPath.startsWith("/") ? decodedPath.substring(1) : decodedPath;

        if (normalizedPath.startsWith(this.bucket + "/")) {

            return normalizedPath.substring(this.bucket.length() + 1);
        }

        return normalizedPath;
    }

    private String contentType(String extension) {

        return switch (extension.toLowerCase(Locale.ROOT)) {
            case "csv" -> "text/csv";
            case "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "zip" -> "application/zip";
            default -> "application/octet-stream";
        };
    }

    private String readConfig(String key, String defaultValue) {

        String value = System.getenv(key);

        if (value == null || value.isBlank()) {

            value = System.getProperty(key);
        }

        return value == null || value.isBlank() ? defaultValue : value.trim();
    }
}
