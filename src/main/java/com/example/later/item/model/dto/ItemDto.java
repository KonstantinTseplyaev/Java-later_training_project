package com.example.later.item.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long id;
    private String url;
    private Set<String> tags;
    private String mimeType;
    private String title;
    private Boolean unread;
}
