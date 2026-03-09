package com.thitsaworks.operation_portal.core.reporting.download.generator.support;

import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGeneratedFile;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class PagedZipSupport {

    public ReportGeneratedFile generatePagedZip(String entryPrefix,
                                                String fileType,
                                                int totalRowCount,
                                                int pageSize,
                                                ChunkProvider chunkProvider)
        throws ReportException, IOException {

        ByteArrayOutputStream zipBytes = new ByteArrayOutputStream();

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(zipBytes)) {
            int chunkNo = 1;
            for (int offset = 0; offset < totalRowCount; offset += pageSize) {
                int limit = Math.min(pageSize, totalRowCount - offset);
                byte[] chunkBytes = chunkProvider.load(offset, limit);

                ZipEntry entry = new ZipEntry(entryPrefix + chunkNo + "." + fileType);
                zipOutputStream.putNextEntry(entry);
                zipOutputStream.write(chunkBytes);
                zipOutputStream.closeEntry();
                chunkNo++;
            }
        }

        return new ReportGeneratedFile(zipBytes.toByteArray(), "zip");
    }

    @FunctionalInterface
    public interface ChunkProvider {

        byte[] load(int offset, int limit) throws ReportException;
    }
}
