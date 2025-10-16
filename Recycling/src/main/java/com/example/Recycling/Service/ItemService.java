package com.example.Recycling.Service;



import com.example.Recycling.DTO.ItemRequestDto;
import com.example.Recycling.DTO.ItemResponseDto;
import com.example.Recycling.Repository.ItemImageRepository;
import com.example.Recycling.Repository.ItemRepository;
import com.example.Recycling.entity.Item;
import com.example.Recycling.entity.ItemImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemImageRepository itemImageRepository;

    public ItemResponseDto createItemWithImages(ItemRequestDto itemRequest) throws IOException {
        System.out.println("🚀 === STARTING ITEM CREATION ===");
        System.out.println("📝 Creating new item with title: " + itemRequest.getTitle());

        if (itemRequest.getImages() != null) {
            System.out.println("🖼️ Number of images received: " + itemRequest.getImages().size());
            itemRequest.getImages().forEach(file -> {
                System.out.println("   - " + file.getOriginalFilename() + " (" + file.getSize() + " bytes)");
            });
        } else {
            System.out.println("⚠️ No images received in request");
        }

        // Create and save item
        Item item = new Item();
        item.setTitle(itemRequest.getTitle());
        item.setType(itemRequest.getType());
        item.setCategory(itemRequest.getCategory());
        item.setItemCondition(itemRequest.getItemCondition());
        item.setDescription(itemRequest.getDescription());
        item.setPrice(itemRequest.getPrice());
        item.setOwnerName(itemRequest.getOwnerName());
        item.setOwnerEmail(itemRequest.getOwnerEmail());
        item.setPhoneNumber(itemRequest.getPhoneNumber());
        item.setAddress(itemRequest.getAddress());
        item.setLocation(itemRequest.getLocation());
        item.setShippingAvailable(itemRequest.getShippingAvailable() != null ? itemRequest.getShippingAvailable() : false);
        item.setStatus("pending");

        // Save item first to get ID
        Item savedItem = itemRepository.save(item);
        System.out.println("✅ Item saved with ID: " + savedItem.getId());

        // Save images if provided
        if (itemRequest.getImages() != null && !itemRequest.getImages().isEmpty()) {
            System.out.println("💾 Saving images to database...");

            for (MultipartFile file : itemRequest.getImages()) {
                if (!file.isEmpty() && file.getSize() > 0) {
                    try {
                        System.out.println("📸 Processing: " + file.getOriginalFilename());
                        System.out.println("   Type: " + file.getContentType());
                        System.out.println("   Size: " + file.getSize() + " bytes");

                        // Create image entity
                        ItemImage itemImage = new ItemImage();
                        itemImage.setFileName(file.getOriginalFilename());
                        itemImage.setFileType(file.getContentType());
                        itemImage.setImageData(file.getBytes()); // This stores as BLOB
                        itemImage.setFileSize(file.getSize());
                        itemImage.setItem(savedItem);

                        // Save image to database
                        ItemImage savedImage = itemImageRepository.save(itemImage);
                        System.out.println("✅ Image saved with ID: " + savedImage.getId());

                        // Verify the image was saved
                        if (savedImage.getImageData() != null) {
                            System.out.println("✅ Image data stored: " + savedImage.getImageData().length + " bytes");
                        } else {
                            System.out.println("❌ Image data is NULL after save!");
                        }

                    } catch (IOException e) {
                        System.err.println("❌ Error processing image: " + file.getOriginalFilename());
                        e.printStackTrace();
                        throw new IOException("Failed to process image: " + file.getOriginalFilename(), e);
                    }
                } else {
                    System.out.println("⚠️ Skipping empty file: " + file.getOriginalFilename());
                }
            }
        }

        System.out.println("🎉 === ITEM CREATION COMPLETE ===");

        // Return the saved item with images
        return convertToResponse(savedItem);
    }

    public List<ItemResponseDto> getItemsByStatus(String status) {
        System.out.println("🔍 Fetching items with status: " + status);
        List<Item> items = itemRepository.findByStatus(status);

        System.out.println("📊 Found " + items.size() + " items");

        // Debug each item
        for (Item item : items) {
            System.out.println("📦 Item: " + item.getTitle() + " (ID: " + item.getId() + ")");

            // Explicitly load images for this item
            List<ItemImage> images = itemImageRepository.findByItemId(item.getId());
            System.out.println("   Images in database: " + images.size());

            for (ItemImage img : images) {
                System.out.println("   🖼️  " + img.getFileName() + " - " + img.getFileType() +
                        " - " + (img.getImageData() != null ? img.getImageData().length + " bytes" : "NULL data"));
            }
        }

        return items.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private ItemResponseDto convertToResponse(Item item) {
        ItemResponseDto response = new ItemResponseDto();
        response.setId(item.getId());
        response.setTitle(item.getTitle());
        response.setType(item.getType());
        response.setCategory(item.getCategory());
        response.setItemCondition(item.getItemCondition());
        response.setDescription(item.getDescription());
        response.setPrice(item.getPrice());
        response.setOwnerName(item.getOwnerName());
        response.setOwnerEmail(item.getOwnerEmail());
        response.setPhoneNumber(item.getPhoneNumber());
        response.setAddress(item.getAddress());
        response.setLocation(item.getLocation());
        response.setShippingAvailable(item.getShippingAvailable());
        response.setStatus(item.getStatus());
        response.setCreatedAt(item.getCreatedAt());
        response.setUpdatedAt(item.getUpdatedAt());

        // FIXED: Load images directly from repository to avoid LazyLoading issues
        List<ItemImage> images = itemImageRepository.findByItemId(item.getId());
        System.out.println("🔄 Converting " + images.size() + " images for item: " + item.getTitle());

        if (!images.isEmpty()) {
            List<ItemResponseDto.ImageInfo> imageInfos = images.stream()
                    .map(this::convertImageToInfo)
                    .collect(Collectors.toList());
            response.setImages(imageInfos);
            System.out.println("✅ Successfully converted " + imageInfos.size() + " images to Base64");
        } else {
            System.out.println("⚠️ No images found for item: " + item.getTitle());
        }

        return response;
    }

    private ItemResponseDto.ImageInfo convertImageToInfo(ItemImage image) {
        ItemResponseDto.ImageInfo imageInfo = new ItemResponseDto.ImageInfo();
        imageInfo.setId(image.getId());
        imageInfo.setFileName(image.getFileName());
        imageInfo.setFileType(image.getFileType());
        imageInfo.setFileSize(image.getFileSize());

        // Convert image data to Base64 for frontend
        if (image.getImageData() != null && image.getImageData().length > 0) {
            try {
                String base64Image = Base64.getEncoder().encodeToString(image.getImageData());
                imageInfo.setImageBase64(base64Image);
                imageInfo.setImageType(image.getFileType());
                System.out.println("✅ Base64 conversion successful for: " + image.getFileName());
            } catch (Exception e) {
                System.err.println("❌ Error converting image to Base64: " + image.getFileName());
                e.printStackTrace();
            }
        } else {
            System.out.println("❌ No image data available for: " + image.getFileName());
        }

        return imageInfo;
    }

    /**
     * Get item by ID
     */
    public Item getItemById(Long id) {
        System.out.println("🔍 Fetching item by ID: " + id);
        Item item = itemRepository.findById(id).orElse(null);
        if (item != null) {
            System.out.println("✅ Found item: " + item.getTitle() + " (Status: " + item.getStatus() + ")");
        } else {
            System.out.println("❌ Item not found with ID: " + id);
        }
        return item;
    }

    public ItemResponseDto approveItem(Long itemId) {
        System.out.println("✅ Approving item with ID: " + itemId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemId));

        item.setStatus("available");
        Item updatedItem = itemRepository.save(item);

        return convertToResponse(updatedItem);
    }

    public ItemResponseDto rejectItem(Long itemId) {
        System.out.println("❌ Rejecting item with ID: " + itemId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemId));

        item.setStatus("rejected");
        Item updatedItem = itemRepository.save(item);

        return convertToResponse(updatedItem);
    }

    public List<ItemResponseDto> getAllAvailableItems() {
        return getItemsByStatus("available");
    }

    public byte[] getItemImage(Long itemId) {
        ItemImage itemImage = itemImageRepository.findFirstByItemId(itemId)
                .orElseThrow(() -> new RuntimeException("No image found for item id: " + itemId));
        return itemImage.getImageData();
    }

    public String getImageContentType(Long itemId) {
        ItemImage itemImage = itemImageRepository.findFirstByItemId(itemId)
                .orElseThrow(() -> new RuntimeException("No image found for item id: " + itemId));
        return itemImage.getFileType();
    }
}