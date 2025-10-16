package com.example.Recycling.Controller;



import com.example.Recycling.DTO.ItemRequestDto;
import com.example.Recycling.DTO.ItemResponseDto;
import com.example.Recycling.Service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "*")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping("/with-images")
    public ResponseEntity<ItemResponseDto> createItemWithImages(
            @RequestParam("title") String title,
            @RequestParam("type") String type,
            @RequestParam("category") String category,
            @RequestParam("itemCondition") String itemCondition,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("ownerName") String ownerName,
            @RequestParam("ownerEmail") String ownerEmail,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("address") String address,
            @RequestParam("location") String location,
            @RequestParam(value = "shippingAvailable", defaultValue = "false") Boolean shippingAvailable,
            @RequestParam("images") List<MultipartFile> images) {

        System.out.println("Received item creation request for: " + title);
        System.out.println("Number of images received: " + images.size());

        try {
            ItemRequestDto itemRequest = new ItemRequestDto();
            itemRequest.setTitle(title);
            itemRequest.setType(type);
            itemRequest.setCategory(category);
            itemRequest.setItemCondition(itemCondition);
            itemRequest.setDescription(description);
            itemRequest.setPrice(price);
            itemRequest.setOwnerName(ownerName);
            itemRequest.setOwnerEmail(ownerEmail);
            itemRequest.setPhoneNumber(phoneNumber);
            itemRequest.setAddress(address);
            itemRequest.setLocation(location);
            itemRequest.setShippingAvailable(shippingAvailable);
            itemRequest.setImages(images);

            ItemResponseDto response = itemService.createItemWithImages(itemRequest);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            System.err.println("Error creating item with images: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/images/item/{itemId}")
    public ResponseEntity<byte[]> getItemImage(@PathVariable Long itemId) {
        try {
            byte[] imageData = itemService.getItemImage(itemId);
            String contentType = itemService.getImageContentType(itemId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentLength(imageData.length);
            headers.setCacheControl("max-age=3600"); // Cache for 1 hour

            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error fetching image for item " + itemId + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ItemResponseDto>> getItemsByStatus(@PathVariable String status) {
        try {
            List<ItemResponseDto> items = itemService.getItemsByStatus(status);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/available")
    public ResponseEntity<List<ItemResponseDto>> getAvailableItems() {
        return getItemsByStatus("available");
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ItemResponseDto>> getPendingItems() {
        return getItemsByStatus("pending");
    }

    @GetMapping("/rejected")
    public ResponseEntity<List<ItemResponseDto>> getRejectedItems() {
        return getItemsByStatus("rejected");
    }

    @PutMapping("/{itemId}/approve")
    public ResponseEntity<ItemResponseDto> approveItem(@PathVariable Long itemId) {
        try {
            ItemResponseDto response = itemService.approveItem(itemId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{itemId}/reject")
    public ResponseEntity<ItemResponseDto> rejectItem(@PathVariable Long itemId) {
        try {
            ItemResponseDto response = itemService.rejectItem(itemId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Backend is running successfully! Image storage is working.");
    }
}