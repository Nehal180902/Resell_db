package com.example.Recycling.Repository;

import com.example.Recycling.entity.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {

    // Make sure this method exists
    List<ItemImage> findByItemId(Long itemId);

    Optional<ItemImage> findFirstByItemId(Long itemId);

    // Add this for better debugging
    @Query("SELECT COUNT(i) FROM ItemImage i WHERE i.item.id = :itemId")
    Long countImagesByItemId(@Param("itemId") Long itemId);
}