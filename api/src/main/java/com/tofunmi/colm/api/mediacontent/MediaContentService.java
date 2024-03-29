package com.tofunmi.colm.api.mediacontent;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created By tofunmi on 12/12/2022
 */
@Service
public class MediaContentService {
    private final MediaContentRepository repository;

    public MediaContentService(MediaContentRepository repository) {
        this.repository = repository;
    }

    public List<SavedMediaContent> save(MultipartFile[] mediaContents) {
        List<SavedMediaContent> savedMediaContents = new ArrayList<>();
        if (mediaContents != null) {
            for (MultipartFile fileContent : mediaContents) {
                try {
                    savedMediaContents.add(save(fileContent));
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save media content", e.getCause());
                }
            }
        }
        return savedMediaContents;
    }

    private SavedMediaContent save(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        Optional<String> fileExtension = getFileExtension(file);
        validateFileMetadata(contentType);

        final MediaContent mediaContent = new MediaContent();
        mediaContent.setMimeType(contentType);
        mediaContent.setFileName(UUID.randomUUID().toString());
        mediaContent.setContent(file.getBytes());
        fileExtension.ifPresent(mediaContent::setExtension);

        String id = repository.save(mediaContent).getId();
        return new SavedMediaContent(id, contentType);
    }

    private void validateFileMetadata(String contentType) {
        Assert.isTrue(StringUtils.hasText(contentType), "Content type not present on image");
        Assert.isTrue(contentType.startsWith("image/"), "Invalid content type");
    }

    private Optional<String> getFileExtension(MultipartFile multipartFile) {
        String originalFileName = multipartFile.getOriginalFilename();

        if (!StringUtils.hasText(originalFileName) || originalFileName.lastIndexOf(".") < 0) {
            return Optional.empty();
        }

        int lastIndexOf = originalFileName.lastIndexOf(".");
        return Optional.of(originalFileName.substring(lastIndexOf));
    }

    public byte[] getForId(String id) {
        return repository.findById(id).orElseThrow().getContent();
    }
}
