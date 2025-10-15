package com.example.Recycling.Controller;



import com.example.Recycling.entity.Purchase;
import com.example.Recycling.entity.Item;
import com.example.Recycling.Service.PurchaseService;
import com.example.Recycling.Service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/purchases")
@CrossOrigin(origins = "*")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private ItemService itemService;

    /**
     * Create a new purchase
     */
    @PostMapping
    public ResponseEntity<?> createPurchase(@RequestBody Map<String, Object> purchaseData) {
        try {
            // Extract purchase data from the request
            Long itemId = Long.valueOf(purchaseData.get("itemId").toString());
            String buyerName = (String) purchaseData.get("buyerName");
            String buyerEmail = (String) purchaseData.get("buyerEmail");
            String buyerPhone = (String) purchaseData.get("buyerPhone");
            String buyerAddress = (String) purchaseData.get("buyerAddress");
            String paymentMethod = (String) purchaseData.get("paymentMethod");

            // Get the item
            Item item = itemService.getItemById(itemId);
            if (item == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Item not found with ID: " + itemId);
            }

            // Check if item is already sold
            if ("sold".equals(item.getStatus())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Item is already sold");
            }

            // Create purchase object
            Purchase purchase = new Purchase(item, buyerName, buyerEmail, buyerPhone, buyerAddress, paymentMethod);
            purchase.setStatus("completed");

            // Save purchase (this will also send emails and update item status)
            Purchase savedPurchase = purchaseService.savePurchase(purchase);

            return ResponseEntity.ok(savedPurchase);

        } catch (Exception e) {
            System.err.println("❌ Error creating purchase: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating purchase: " + e.getMessage());
        }
    }

    /**
     * Get all purchases
     */
    @GetMapping
    public ResponseEntity<List<Purchase>> getAllPurchases() {
        try {
            List<Purchase> purchases = purchaseService.getAllPurchases();
            return ResponseEntity.ok(purchases);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get purchase by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Purchase> getPurchaseById(@PathVariable Long id) {
        try {
            Purchase purchase = purchaseService.getPurchaseById(id);
            if (purchase != null) {
                return ResponseEntity.ok(purchase);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get purchases by buyer email
     */
    @GetMapping("/buyer/{email}")
    public ResponseEntity<List<Purchase>> getPurchasesByBuyerEmail(@PathVariable String email) {
        try {
            List<Purchase> purchases = purchaseService.getPurchasesByBuyerEmail(email);
            return ResponseEntity.ok(purchases);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get purchases by seller email
     */
    @GetMapping("/seller/{email}")
    public ResponseEntity<List<Purchase>> getPurchasesBySellerEmail(@PathVariable String email) {
        try {
            List<Purchase> purchases = purchaseService.getPurchasesBySellerEmail(email);
            return ResponseEntity.ok(purchases);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update purchase status
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Purchase> updatePurchaseStatus(@PathVariable Long id, @RequestBody Map<String, String> statusData) {
        try {
            String status = statusData.get("status");
            Purchase updatedPurchase = purchaseService.updatePurchaseStatus(id, status);
            if (updatedPurchase != null) {
                return ResponseEntity.ok(updatedPurchase);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}