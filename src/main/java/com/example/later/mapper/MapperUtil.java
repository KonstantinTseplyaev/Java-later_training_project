package com.example.later.mapper;

import com.example.later.item.model.dto.ItemCreationDto;
import com.example.later.item.model.dto.ItemDto;
import com.example.later.item.model.Item;
import com.example.later.item.service.UrlMetadataRetriever.UrlMetadata;
import com.example.later.itemNote.model.ItemNote;
import com.example.later.itemNote.model.dto.ItemNoteCreationDto;
import com.example.later.itemNote.model.dto.ItemNoteDto;
import com.example.later.user.model.User;
import com.example.later.user.model.dto.UserCreationDto;
import com.example.later.user.model.dto.UserDto;
import com.example.later.user.model.UserState;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class MapperUtil {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static <R, E> List<R> convertList(List<E> list, Function<E, R> converter) {
        return list.stream().map(converter).collect(Collectors.toList());
    }

    public static UserDto convertToUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public static User convertFromUserCreationDto(UserCreationDto userDto) {
        return User.builder()
                .email(userDto.getEmail())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .registrationDate(Instant.now())
                .state(UserState.ACTIVE)
                .birthday(userDto.getBirthday())
                .build();
    }

    public static ItemDto convertToItemDto(Item item) {
        return modelMapper.map(item, ItemDto.class);
    }

    public static Item convertFromItemCreationDto(ItemCreationDto itemDto, User owner, UrlMetadata metadata) {
        return Item.builder()
                .owner(owner)
                .url(itemDto.getUrl())
                .tags(itemDto.getTags())
                .resolvedUrl(metadata.getResolvedUrl())
                .mimeType(metadata.getMimeType())
                .title(metadata.getTitle())
                .hasImage(metadata.isHasImage())
                .hasVideo(metadata.isHasVideo())
                .dateResolved(metadata.getDateResolved())
                .unread(true)
                .build();
    }

    public static ItemNoteDto convertToItemNoteDto(ItemNote itemNote) {
        return new ItemNoteDto(itemNote.getText(), itemNote.getCreationTime().toString(), itemNote.getItem().getUrl());
    }

    public static ItemNote convertFromItemNoteCreationDto(ItemNoteCreationDto itemNoteDto, Item item) {
        return ItemNote.builder()
                .text(itemNoteDto.getText())
                .authorId(itemNoteDto.getAuthorId())
                .item(item)
                .creationTime(Instant.now())
                .build();
    }
}
