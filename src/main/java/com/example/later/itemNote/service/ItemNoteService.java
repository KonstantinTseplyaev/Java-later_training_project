package com.example.later.itemNote.service;

import com.example.later.itemNote.model.dto.ItemNoteCreationDto;
import com.example.later.itemNote.model.dto.ItemNoteDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface ItemNoteService {
    @Transactional
    ItemNoteDto addNewItemNote(long userId, ItemNoteCreationDto itemNoteDto);

    List<ItemNoteDto> searchNotesByUrl(String url, Long userId);

    List<ItemNoteDto> searchNotesByTag(long userId, String tag);

    List<ItemNoteDto> findAllItemNotes(long userId, int from, int size);
}
