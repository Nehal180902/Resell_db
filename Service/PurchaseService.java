package com.example.Recycling.Service;


import com.example.Recycling.entity.Purchase;
import com.example.Recycling.entity.Item;
import com.example.Recycling.Repository.PurchaseRepository;
import com.example.Recycling.Repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Comparator;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private EmailService emailService;

    /**
     * Save purchase and send email notifications
     */
    public Purchase savePurchase(Purchase purchase) {
        // Save the purchase
        Purchase savedPurchase = purchaseRepository.save(purchase);

        // Update item status to sold
        Item item = purchase.getItem();
        if (item != null) {
            item.setStatus("sold");
            itemRepository.save(item);
        }

        // Send email notifications
        emailService.sendPurchaseNotifications(savedPurchase);

        return savedPurchase;
    }

    /**
     * Get purchase by ID
     */
    public Purchase getPurchaseById(Long id) {
        return purchaseRepository.findById(id).orElse(null);
    }

    /**
     * Get all purchases
     */
    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    /**
     * Get purchases by buyer email - UPDATED METHOD NAME
     */
    public List<Purchase> getPurchasesByBuyerEmail(String email) {
        List<Purchase> purchases = purchaseRepository.findByBuyerEmail(email);
        // Sort by purchase date descending manually
        purchases.sort(Comparator.comparing(Purchase::getPurchaseDate).reversed());
        return purchases;
    }

    /**
     * Get purchases by seller email - UPDATED METHOD NAME
     */
    public List<Purchase> getPurchasesBySellerEmail(String email) {
        List<Purchase> purchases = purchaseRepository.findByItemOwnerEmail(email);
        // Sort by purchase date descending manually
        purchases.sort(Comparator.comparing(Purchase::getPurchaseDate).reversed());
        return purchases;
    }

    /**
     * Get purchases by item ID
     */
    public List<Purchase> getPurchasesByItemId(Long itemId) {
        return purchaseRepository.findByItemId(itemId);
    }

    /**
     * Update purchase status
     */
    public Purchase updatePurchaseStatus(Long id, String status) {
        Purchase purchase = purchaseRepository.findById(id).orElse(null);
        if (purchase != null) {
            purchase.setStatus(status);
            return purchaseRepository.save(purchase);
        }
        return null;
    }
}