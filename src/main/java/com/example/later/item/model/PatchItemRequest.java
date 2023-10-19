package com.example.later.item.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PatchItemRequest {
    private Long userId;
    private Long itemId;
    private Boolean replaceTags;
    private Boolean unread;
    private List<String> tags;

}
