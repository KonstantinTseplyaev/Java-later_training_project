package com.example.later.item.repository;

import com.example.later.item.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;


public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item> {
    Boolean existsByIdAndOwnerId(long id, long ownerId);

    void deleteByIdAndOwnerId(long id, long ownerId);

    List<Item> findAllByOwnerId(long ownerId);

    Optional<Item> findByResolvedUrlAndOwnerId(String url, long ownerId);
}
