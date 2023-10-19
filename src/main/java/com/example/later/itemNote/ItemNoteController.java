package com.example.later.itemNote;

import com.example.later.itemNote.model.dto.ItemNoteCreationDto;
import com.example.later.itemNote.model.dto.ItemNoteDto;
import com.example.later.itemNote.service.ItemNoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
@Slf4j
public class ItemNoteController {
    private final ItemNoteService itemNoteService;

    @GetMapping(params = "url")
    public List<ItemNoteDto> searchByUrl(@RequestHeader("X-Later-User-Id") long userId,
                                         @RequestParam String url) {
        log.info("GET-запрос: получение списка заметок к ссылкам с URL {} пользователем {}", url, userId);
        return itemNoteService.searchNotesByUrl(url, userId);
    }

    @GetMapping(params = "tag")
    public List<ItemNoteDto> searchByTags(@RequestHeader("X-Later-User-Id") long userId,
                                          @RequestParam String tag) {
        log.info("GET-запрос: получение списка заметок к ссылкам с тегом {} пользователем {}", tag, userId);
        return itemNoteService.searchNotesByTag(userId, tag);
    }

    @GetMapping
    public List<ItemNoteDto> listAllNotes(@RequestHeader("X-Later-User-Id") long userId,
                                          @RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "10") int size) {
        log.info("GET-запрос: постраничное получение списка заметок пользователя {} с параметрами from {}, size {}"
                , userId, from, size);
        return itemNoteService.findAllItemNotes(userId, from, size);
    }

    @PostMapping
    public ItemNoteDto addNewNode(@RequestHeader("X-Later-User-Id") Long userId,
                                  @RequestBody @Valid ItemNoteCreationDto itemNote) {
        log.info("POST-запрос: добавление новой заметки {} пользователем {}", itemNote, userId);
        return itemNoteService.addNewItemNote(userId, itemNote);
    }
}
