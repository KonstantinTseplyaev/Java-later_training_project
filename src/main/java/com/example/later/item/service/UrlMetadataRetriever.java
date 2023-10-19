package com.example.later.item.service;

import java.time.Instant;

public interface UrlMetadataRetriever {
    interface UrlMetadata {
        String getNormalUrl();
        String getResolvedUrl();
        String getMimeType();
        String getTitle();
        boolean isHasImage();
        boolean isHasVideo();
        Instant getDateResolved();
    }
    UrlMetadata retrieve(String url);
}
