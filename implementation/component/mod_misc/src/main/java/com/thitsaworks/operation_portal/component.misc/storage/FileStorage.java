package com.thitsaworks.operation_portal.component.misc.storage;

public interface FileStorage {

    String upload(String fileLocation, byte[] fileBytes, String extension);

    String generatePreSignedDownloadUrl(String key);

}
