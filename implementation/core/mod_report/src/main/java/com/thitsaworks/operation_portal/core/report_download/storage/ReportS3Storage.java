package com.thitsaworks.operation_portal.core.report_download.storage;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.thitsaworks.operation_portal.core.report_download.model.ReportDownloadRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class ReportS3Storage {

    private static final Logger LOG = LoggerFactory.getLogger(ReportS3Storage.class);

    private final boolean enabled;
    private final String bucket;
    private final String region;
    private final String prefix;
    private final String endpoint;
    private final boolean pathStyleAccess;
    private final String publicBaseUrl;
    private final AmazonS3 amazonS3;

    public ReportS3Storage() {

        this.enabled = Boolean.parseBoolean(this.readConfig("REPORT_S3_ENABLED", "false"));
        this.bucket = this.readConfig("REPORT_S3_BUCKET", "");
        this.region = this.readConfig("REPORT_S3_REGION", "ap-southeast-1");
        this.prefix = this.readConfig("REPORT_S3_PREFIX", "report-downloads");
        this.endpoint = this.readConfig("REPORT_S3_ENDPOINT", "");
        this.pathStyleAccess = Boolean.parseBoolean(this.readConfig("REPORT_S3_PATH_STYLE_ACCESS", "true"));
        this.publicBaseUrl = this.readConfig("REPORT_S3_PUBLIC_BASE_URL", "");

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
        String uploadedUrl = this.objectUrl(key);
        LOG.info("Uploaded report request [{}] to S3 [{}]", request.getId().getEntityId(), uploadedUrl);
        return uploadedUrl;
    }

    private AmazonS3 buildClient() {

        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard()
                                                             .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                                                             .withPathStyleAccessEnabled(this.pathStyleAccess);

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
