package com.example.later.itemNote.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemNoteDto {
    private String text;
    private String dateOfNote;
    private String itemUrl;
}
