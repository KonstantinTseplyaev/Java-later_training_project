package com.example.later.item.model.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Positive;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class ItemCreationDto {
    @Positive
    private Long ownerId;
    @URL
    private String url;
    @Builder.Default
    Set<String> tags = new HashSet<>();
}
