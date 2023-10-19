package com.example.later.itemNote.repository;

import com.example.later.itemNote.model.ItemNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemNoteRepository extends JpaRepository<ItemNote, Long> {
    List<ItemNote> findByAuthorIdAndItemUrlContainingIgnoreCase(long userId, String url);

    @Query(value = "select * from item_notes " +
            "where item_id in " +
            "(select distinct it.id from items as it " +
            "join tags as t on t.item_id = it.id " +
            "where t.name = ?1) " +
            "and author_id = ?2", nativeQuery = true)
    List<ItemNote> findByAuthorIdAndTag(String tag, long authorId);

    Page<ItemNote> findByAuthorId(long userId, Pageable pageable);


}
