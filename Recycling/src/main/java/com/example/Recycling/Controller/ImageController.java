package com.example.Recycling.Controller;



import com.example.Recycling.Service.ItemService;
import com.example.Recycling.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "*")
public class ImageController {

    @Autowired
    private ItemService itemService;

    /**
     * Get image for a specific item
     */
    @GetMapping("/item/{itemId}")
    public ResponseEntity<byte[]> getItemImage(@PathVariable Long itemId) {
        try {
            // Get the item first to check if it exists
            Item item = itemService.getItemById(itemId);
            if (item == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // Get image data and content type
            byte[] imageData = itemService.getItemImage(itemId);
            String contentType = itemService.getImageContentType(itemId);

            if (imageData == null || imageData.length == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentLength(imageData.length);

            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);

        } catch (RuntimeException e) {
            System.err.println("❌ Error fetching image for item " + itemId + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            System.err.println("❌ Unexpected error fetching image for item " + itemId + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get thumbnail image for a specific item
     */
    @GetMapping("/item/{itemId}/thumbnail")
    public ResponseEntity<byte[]> getItemThumbnail(@PathVariable Long itemId) {
        try {
            // Get the item first to check if it exists
            Item item = itemService.getItemById(itemId);
            if (item == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // Get image data and content type (using same method as main image)
            byte[] imageData = itemService.getItemImage(itemId);
            String contentType = itemService.getImageContentType(itemId);

            if (imageData == null || imageData.length == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentLength(imageData.length);

            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);

        } catch (RuntimeException e) {
            System.err.println("❌ Error fetching thumbnail for item " + itemId + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            System.err.println("❌ Unexpected error fetching thumbnail for item " + itemId + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}