package com.example.later.itemNote.service;

import com.example.later.itemNote.repository.ItemNoteRepository;
import com.example.later.itemNote.model.ItemNote;
import com.example.later.itemNote.model.dto.ItemNoteCreationDto;
import com.example.later.itemNote.model.dto.ItemNoteDto;
import com.example.later.mapper.MapperUtil;
import com.example.later.item.model.Item;
import com.example.later.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemNoteServiceImpl implements ItemNoteService {
    private final ItemNoteRepository itemNoteRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemNoteDto addNewItemNote(long authorId, ItemNoteCreationDto itemNoteDto) {
        if (!itemRepository.existsByIdAndOwnerId(itemNoteDto.getItemId(), authorId)) {
            throw new RuntimeException("итема нет");
        }
        itemNoteDto.setAuthorId(authorId);
        Optional<Item> item = itemRepository.findById(itemNoteDto.getItemId());
        ItemNote itemNote = itemNoteRepository
                .save(MapperUtil.convertFromItemNoteCreationDto(itemNoteDto, item.get()));
        return MapperUtil.convertToItemNoteDto(itemNote);
    }

    @Override
    public List<ItemNoteDto> searchNotesByUrl(String url, Long userId) {
        List<ItemNote> itemNotes = itemNoteRepository.findByAuthorIdAndItemUrlContainingIgnoreCase(userId, url);
        return MapperUtil.convertList(itemNotes, MapperUtil::convertToItemNoteDto);
    }

    @Override
    public List<ItemNoteDto> searchNotesByTag(long userId, String tag) {
        List<ItemNote> itemNotes = itemNoteRepository.findByAuthorIdAndTag(tag, userId);
        return MapperUtil.convertList(itemNotes, MapperUtil::convertToItemNoteDto);
    }

    @Override
    public List<ItemNoteDto> findAllItemNotes(long userId, int from, int size) {
        Page<ItemNote> itemNotes = itemNoteRepository.findByAuthorId(userId, Pageable.ofSize(size).withPage(from));
        return MapperUtil.convertList(itemNotes.toList(), MapperUtil::convertToItemNoteDto);
    }
}
