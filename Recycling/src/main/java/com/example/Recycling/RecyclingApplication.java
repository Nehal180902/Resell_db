package com.example.Recycling;

import com.example.Recycling.Service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class RecyclingApplication {
    @Autowired
    private EmailService senderService;
	public static void main(String[] args) {
		SpringApplication.run(RecyclingApplication.class, args);
	}

   // @EventListener(ApplicationReadyEvent.class)
//    public void triggerMail() {
//        senderService.sendSimpleEmail(
//                "Care&Share@gmail.com",
//                "✅ Your order is confirmed - Care & Share",
//                "Dear Customer,\n\n" +
//                        "Greetings from Care & Share!\n\n" +
//                        "Your order from our website has been successfully confirmed.\n" +
//                        "We appreciate your support in promoting sustainable reuse.\n\n" +
//                        "Warm regards,\n" +
//                        "Care & Share Team"
//        );
//    }
//
}
//
