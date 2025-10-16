package com.example.Recycling.Repository;



import com.example.Recycling.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    // Find purchases by buyer email
    List<Purchase> findByBuyerEmail(String buyerEmail);

    // Find purchases by item owner email (seller)
    List<Purchase> findByItemOwnerEmail(String ownerEmail);

    // Find purchases by item ID
    List<Purchase> findByItemId(Long itemId);

    // Find purchases by status
    List<Purchase> findByStatus(String status);
}