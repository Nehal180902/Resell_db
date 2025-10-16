package com.example.Recycling.Service;



import com.example.Recycling.entity.Purchase;
import com.example.Recycling.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    /**
     * Send purchase notifications to both buyer and seller
     */
    public void sendPurchaseNotifications(Purchase purchase) {
        try {
            Item item = purchase.getItem();

            if (item == null) {
                System.err.println("❌ Cannot send emails: Item is null in purchase");
                return;
            }

            // Send email to buyer
            sendBuyerConfirmation(purchase);

            // Send email to seller
            sendSellerNotification(purchase);

            System.out.println("✅ Email notifications sent successfully!");

        } catch (Exception e) {
            System.err.println("❌ Failed to send email notifications: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Send confirmation email to buyer
     */
    private void sendBuyerConfirmation(Purchase purchase) {
        try {
            Item item = purchase.getItem();

            String subject = "Purchase Confirmation - Order #" + purchase.getId();
            String message = "Dear " + purchase.getBuyerName() + ",\n\n" +
                    "Thank you for your purchase on Resell!\n\n" +
                    "📦 ORDER DETAILS:\n" +
                    "• Item: " + item.getTitle() + "\n" +
                    "• Price: $" + String.format("%.2f", item.getPrice()) + "\n" +
                    "• Order ID: #" + purchase.getId() + "\n" +
                    "• Payment Method: " + purchase.getPaymentMethod() + "\n" +
                    "• Purchase Date: " + purchase.getPurchaseDate() + "\n\n" +
                    "👤 SELLER CONTACT INFORMATION:\n" +
                    "• Name: " + item.getOwnerName() + "\n" +
                    "• Email: " + item.getOwnerEmail() + "\n" +
                    "• Phone: " + item.getPhoneNumber() + "\n" +
                    "• Location: " + item.getLocation() + "\n\n" +
                    "📞 NEXT STEPS:\n" +
                    "Please contact the seller within 24 hours to arrange pickup or delivery.\n\n" +
                    "Thank you for choosing ResaleExchange!\n\n" +
                    "Best regards,\n" +
                    "ResaleExchange Team";

            sendEmail(purchase.getBuyerEmail(), subject, message);
            System.out.println("✅ Buyer confirmation email sent to: " + purchase.getBuyerEmail());

        } catch (Exception e) {
            System.err.println("❌ Failed to send buyer email: " + e.getMessage());
        }
    }

    /**
     * Send notification email to seller
     */
    private void sendSellerNotification(Purchase purchase) {
        try {
            Item item = purchase.getItem();

            String subject = "Congratulations! Your Item Sold - " + item.getTitle();
            String message = "Dear " + item.getOwnerName() + ",\n\n" +
                    "Great news! Your item has been sold on Resell.\n\n" +
                    "💰 SALE DETAILS:\n" +
                    "• Item: " + item.getTitle() + "\n" +
                    "• Price: $" + String.format("%.2f", item.getPrice()) + "\n" +
                    "• Order ID: #" + purchase.getId() + "\n" +
                    "• Sale Date: " + purchase.getPurchaseDate() + "\n\n" +
                    "👤 BUYER INFORMATION:\n" +
                    "• Name: " + purchase.getBuyerName() + "\n" +
                    "• Email: " + purchase.getBuyerEmail() + "\n" +
                    "• Phone: " + purchase.getBuyerPhone() + "\n" +
                    "• Shipping Address: " + purchase.getBuyerAddress() + "\n\n" +
                    "📞 NEXT STEPS:\n" +
                    "Please contact the buyer within 24 hours to arrange the transaction.\n" +
                    "Shipping will start Soon\n\n" +
                    "Thank you for using Care & Share - Resell\n\n" +
                    "Best regards,\n" +
                    "Resell Team";

            sendEmail(item.getOwnerEmail(), subject, message);
            System.out.println("✅ Seller notification email sent to: " + item.getOwnerEmail());

        } catch (Exception e) {
            System.err.println("❌ Failed to send seller email: " + e.getMessage());
        }
    }

    /**
     * Core email sending method
     */
    private void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom("noreply@resaleexchange.com");

            emailSender.send(message);
            System.out.println("📧 Email successfully sent to: " + to);

        } catch (Exception e) {
            System.err.println("❌ Failed to send email to " + to + ": " + e.getMessage());
            throw new RuntimeException("Email sending failed: " + e.getMessage());
        }
    }
}