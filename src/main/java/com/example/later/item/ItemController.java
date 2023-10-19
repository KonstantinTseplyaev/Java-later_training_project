package com.example.later.item;

import com.example.later.item.model.GetItemRequest;
import com.example.later.item.model.PatchItemRequest;
import com.example.later.item.model.dto.ItemCreationDto;
import com.example.later.item.model.dto.ItemDto;
import com.example.later.item.model.enums.ContentType;
import com.example.later.item.model.enums.SortParams;
import com.example.later.item.model.enums.State;
import com.example.later.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getItemsByOwnerId(@RequestHeader("X-Later-User-Id") long userId) {
        log.info("GET-запрос: получение всех ссылок пользователя {}", userId);
        return itemService.getItemsByOwnerId(userId);
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Later-User-Id") Long userId,
                           @RequestBody @Valid ItemCreationDto itemCreationDto) {
        log.info("POST-запрос: добавление новой ссылки {} пользователем {}", itemCreationDto, userId);
        return itemService.addNewItem(userId, itemCreationDto);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Later-User-Id") long userId,
                           @PathVariable long itemId) {
        log.info("DELETE-запрос: удаление ссылки {} пользователем {}", itemId, userId);
        itemService.deleteItem(userId, itemId);
    }

    @GetMapping("/param")
    public List<ItemDto> getAllItems(@RequestHeader("X-Later-User-Id") long userId,
                                     @RequestParam(defaultValue = "unread") String state,
                                     @RequestParam(defaultValue = "all") String contentType,
                                     @RequestParam(defaultValue = "newest") String sort,
                                     @RequestParam(defaultValue = "10") int limit,
                                     @RequestParam(required = false) List<String> tags) {
        GetItemRequest requestParam = GetItemRequest.builder()
                .userId(userId)
                .state(State.valueOf(state.toUpperCase(Locale.ROOT)))
                .contentType(ContentType.valueOf(contentType.toUpperCase(Locale.ROOT)))
                .sort(SortParams.valueOf(sort.toUpperCase(Locale.ROOT)))
                .limit(limit)
                .tags(tags)
                .build();
        log.info("GET-запрос: получение всех ссылок пользователя {} по заданным параметрам {}", userId, requestParam);
        return itemService.getItems(requestParam);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Later-User-Id") long userId,
                              @PathVariable long itemId,
                              @RequestParam(defaultValue = "false") boolean replaceTags,
                              @RequestParam(required = false) List<String> tags,
                              @RequestParam(defaultValue = "false") boolean unread) {
        PatchItemRequest requestParam = PatchItemRequest.builder()
                .itemId(itemId)
                .userId(userId)
                .replaceTags(replaceTags)
                .tags(tags)
                .unread(unread)
                .build();
        log.info("PATCH-запрос: редактирование ссылки пользователем {} по заданным параметрам {}", userId, requestParam);
        return itemService.updateItem(requestParam);
    }
}
