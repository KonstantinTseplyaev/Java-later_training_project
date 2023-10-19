package com.example.later.item.service;

import com.example.later.item.model.GetItemRequest;
import com.example.later.item.model.PatchItemRequest;
import com.example.later.item.model.dto.ItemCreationDto;
import com.example.later.item.model.dto.ItemDto;

import java.util.List;

public interface ItemService {
    List<ItemDto> getItemsByOwnerId(Long userId);

    ItemDto addNewItem(Long userId, ItemCreationDto itemCreationDto);

    void deleteItem(Long userId, Long itemId);

    List<ItemDto> getItems(GetItemRequest param);

    ItemDto updateItem(PatchItemRequest param);
}
