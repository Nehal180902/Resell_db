package com.example.Recycling.Config;

import com.example.Recycling.Repository.ItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * Data Initializer Configuration
 * Initializes database with sample data if empty
 */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(ItemRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                System.out.println("\n🌱 Initializing database with sample items...\n");

//                repository.save(new Item(
//                        "Vintage Wooden Chair",
//                        "furniture",
//                        "resell",
//                        "good",
//                        "A beautiful vintage wooden chair with minimal wear. Perfect for a reading nook.",
//                        45.00,
//                        "Sarah Johnson",
//                        "sarah@example.com",
//                        "1234567890",
//                        "123 Oak Street, Greenville",
//                        LocalDateTime.now(),
//                        LocalDateTime.now(),
//                        "available"
//                ));
//
//                repository.save(new Item(
//                        "Smartphone - Like New",
//                        "electronics",
//                        "resell",
//                        "like-new",
//                        "Latest model smartphone with all accessories. Used for only 2 months.",
//                        350.00,
//                        "Michael Chen",
//                        "michael@example.com",
//                        "2345678901",
//                        "456 Tech Avenue, Innovation City",
//                        LocalDateTime.now(),
//                        LocalDateTime.now(),
//                        "available"
//                ));
//
//                repository.save(new Item(
//                        "Children's Books Collection",
//                        "books",
//                        "donate",
//                        "good",
//                        "Collection of 20 children's books in excellent condition. Perfect for ages 3-8.",
//                        0.00,
//                        "Lisa Thompson",
//                        "lisa@example.com",
//                        "3456789012",
//                        "789 Story Lane, Booktown",
//                        LocalDateTime.now(),
//                        LocalDateTime.now(),
//                        "available"
//                ));
//
//                repository.save(new Item(
//                        "Designer Handbag",
//                        "clothing",
//                        "exchange",
//                        "like-new",
//                        "High-quality designer handbag. Looking to exchange for similar value electronics.",
//                        null,
//                        "Emma Wilson",
//                        "emma@example.com",
//                        "4567890123",
//                        "321 Fashion Street, Stylestown",
//                        LocalDateTime.now(),
//                        LocalDateTime.now(),
//                        "available"
//                ));
//
//                repository.save(new Item(
//                        "Bicycle - Mountain Bike",
//                        "other",
//                        "resell",
//                        "fair",
//                        "Mountain bike with some cosmetic wear but mechanically sound. Great for trails.",
//                        120.00,
//                        "David Rodriguez",
//                        "david@example.com",
//                        "5678901234",
//                        "654 Trail Road, Adventureville",
//                        LocalDateTime.now(),
//                        LocalDateTime.now(),
//                        "available"
//                ));
//
//                repository.save(new Item(
//                        "Coffee Table",
//                        "furniture",
//                        "resell",
//                        "good",
//                        "Modern coffee table with glass top and wooden legs. Minimal signs of use.",
//                        75.00,
//                        "James Miller",
//                        "james@example.com",
//                        "6789012345",
//                        "987 Modern Avenue, Design City",
//                        LocalDateTime.now(),
//                        LocalDateTime.now(),
//                        "available"
//                ));
//
//                repository.save(new Item(
//                        "Gaming Laptop",
//                        "electronics",
//                        "resell",
//                        "good",
//                        "High-performance gaming laptop with RTX graphics. Perfect for gaming and video editing.",
//                        899.00,
//                        "Alex Turner",
//                        "alex@example.com",
//                        "7890123456",
//                        "555 Tech Street, Silicon Valley",
//                        LocalDateTime.now(),
//                        LocalDateTime.now(),
//                        "available"
//                ));
//
//                repository.save(new Item(
//                        "Baby Clothes Bundle",
//                        "clothing",
//                        "donate",
//                        "like-new",
//                        "Bundle of 15 pieces of baby clothes (0-12 months). Gently used and washed.",
//                        0.00,
//                        "Maria Garcia",
//                        "maria@example.com",
//                        "8901234567",
//                        "222 Family Lane, Suburbia",
//                        LocalDateTime.now(),
//                        LocalDateTime.now(),
//                        "available"
//                ));

//                System.out.println("✅ Sample data initialized successfully!");
//                System.out.println("📊 Total items in database: " + repository.count());
//            } else {
//                System.out.println("\nℹ️ Database already contains " + repository.count() + " items. Skipping initialization.\n");
            }
        };
    }
}
