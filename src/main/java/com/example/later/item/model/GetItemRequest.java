package com.example.later.item.model;

import com.example.later.item.model.enums.ContentType;
import com.example.later.item.model.enums.SortParams;
import com.example.later.item.model.enums.State;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetItemRequest {
    private long userId;
    private State state;
    private ContentType contentType;
    private List<String> tags;
    private SortParams sort;
    private Integer limit;
}
