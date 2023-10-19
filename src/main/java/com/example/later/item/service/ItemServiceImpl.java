package com.example.later.item.service;

import com.example.later.exception.ItemRetrieverException;
import com.example.later.item.model.GetItemRequest;
import com.example.later.item.model.Item;
import com.example.later.item.model.PatchItemRequest;
import com.example.later.item.model.QItem;
import com.example.later.item.model.dto.ItemCreationDto;
import com.example.later.item.model.dto.ItemDto;
import com.example.later.item.model.enums.ContentType;
import com.example.later.item.model.enums.SortParams;
import com.example.later.item.model.enums.State;
import com.example.later.item.repository.ItemRepository;
import com.example.later.mapper.MapperUtil;
import com.example.later.user.model.User;
import com.example.later.user.repository.UserRepository;
import com.example.later.item.service.UrlMetadataRetriever.UrlMetadata;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final UrlMetadataRetriever retriever;

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getItemsByOwnerId(Long ownerId) {
        if (!userRepository.existsById(ownerId)) throw new RuntimeException("юзера не существует");
        List<Item> items = itemRepository.findAllByOwnerId(ownerId);
        return MapperUtil.convertList(items, MapperUtil::convertToItemDto);
    }

    @Override
    @Transactional
    public ItemDto addNewItem(Long ownerId, ItemCreationDto itemDto) {
        Optional<User> ownerOpt = userRepository.findById(ownerId);
        if (ownerOpt.isEmpty()) throw new RuntimeException("юзер не найден");
        itemDto.setOwnerId(ownerId);
        Optional<Item> itemByResolved = itemRepository.findByResolvedUrlAndOwnerId(itemDto.getUrl(), ownerId);
        if (itemByResolved.isPresent()) {
            if (!itemDto.getTags().isEmpty()) {
                return MapperUtil.convertToItemDto(updateTags(itemDto, itemByResolved.get()));
            } else {
                throw new ItemRetrieverException("ссылка уже существует");
            }
        }
        UrlMetadata metadata = retriever.retrieve(itemDto.getUrl());
        Item newItem = itemRepository.save(MapperUtil.convertFromItemCreationDto(itemDto, ownerOpt.get(), metadata));
        return MapperUtil.convertToItemDto(newItem);
    }

    @Override
    @Transactional
    public void deleteItem(Long ownerId, Long itemId) {
        if (!itemRepository.existsByIdAndOwnerId(itemId, ownerId)) {
            throw new RuntimeException("у юзера нет такой ссылки");
        }
        itemRepository.deleteByIdAndOwnerId(itemId, ownerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getItems(GetItemRequest param) {
        BooleanExpression byUserId = QItem.item.owner.id.eq(param.getUserId());
        BooleanExpression byState = makeStateCondition(param.getState());
        BooleanExpression byContentType = makeContentTypeCondition(param.getContentType());
        BooleanExpression byTags = makeTagsCondition(param.getTags());
        Sort sorting = makeOrderByParam(param.getSort());
        Pageable pageable = PageRequest.of(0, param.getLimit(), sorting);
        Iterable<Item> items = itemRepository.findAll((byUserId.and(byState).and(byContentType).and(byTags)), pageable);
        List<Item> itemList = StreamSupport.stream(items.spliterator(), false).collect(Collectors.toList());
        return MapperUtil.convertList(itemList, MapperUtil::convertToItemDto);
    }

    @Override
    @Transactional
    public ItemDto updateItem(PatchItemRequest param) {
        if (!itemRepository.existsByIdAndOwnerId(param.getItemId(), param.getUserId())) {
            throw new RuntimeException("у юзера нет такой ссылки");
        }
        Optional<Item> itemOpt = itemRepository.findById(param.getItemId());
        Item item = itemOpt.orElseThrow();
        item.setUnread(param.getUnread());
        if (param.getTags() != null) {
            updateOrReplaceTags(item, param.getReplaceTags(), param.getTags());
        }
        return MapperUtil.convertToItemDto(itemRepository.save(item));
    }

    private BooleanExpression makeStateCondition(State state) {
        BooleanExpression byState = QItem.item.unread.isTrue();
        switch (state) {
            case READ:
                byState = QItem.item.unread.isFalse();
                break;
            case ALL:
                byState = QItem.item.unread.isNotNull();
                break;
        }
        return byState;
    }

    private BooleanExpression makeContentTypeCondition(ContentType content) {
        BooleanExpression byContent = QItem.item.mimeType.isNotNull();
        switch (content) {
            case ARTICLE:
                byContent = QItem.item.mimeType.eq("text");
                break;
            case IMAGE:
                byContent = QItem.item.mimeType.eq("image");
                break;
            case VIDEO:
                byContent = QItem.item.mimeType.eq("video");
        }
        return byContent;
    }

    private BooleanExpression makeTagsCondition(List<String> tags) {
        if (tags == null) return QItem.item.tags.isEmpty().or(QItem.item.tags.isNotEmpty());
        return QItem.item.tags.any().in(tags);
    }

    private Sort makeOrderByParam(SortParams sort) {
        if (sort == SortParams.OLDEST) return Sort.by(Sort.Direction.ASC, "dateResolved");
        if (sort == SortParams.NEWEST) return Sort.by(Sort.Direction.DESC, "dateResolved");
        if (sort == SortParams.TITLE) return Sort.by(Sort.Direction.ASC, "title");
        else throw new RuntimeException();
    }

    private Item updateTags(ItemCreationDto itemDto, Item item) {
        item.setTags(itemDto.getTags());
        return itemRepository.save(item);
    }

    private void updateOrReplaceTags(Item item, boolean replace, List<String> tags) {
        if (replace) {
            item.setTags(new HashSet<>(tags));
        } else {
            Set<String> oldTags = item.getTags();
            Set<String> newTags = tags.stream().filter(tag -> !oldTags.contains(tag)).collect(Collectors.toSet());
            oldTags.addAll(newTags);
            item.setTags(oldTags);
        }
    }
}
