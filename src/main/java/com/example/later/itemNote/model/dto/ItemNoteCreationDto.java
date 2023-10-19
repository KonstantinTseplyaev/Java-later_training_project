package com.example.later.itemNote.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class ItemNoteCreationDto {
    private Long authorId;
    @NotNull
    @Positive
    private Long itemId;
    @NotBlank
    private String text;
}
