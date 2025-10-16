package com.example.Recycling.Repository;



import com.example.Recycling.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface  ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByStatus(String status);
}