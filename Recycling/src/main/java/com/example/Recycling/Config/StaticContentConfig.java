package com.example.Recycling.Config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Static Content Configuration
 * This configuration ensures that the root URL "/" serves the index.html file
 * from the static resources folder
 */
@Configuration
public class StaticContentConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Forward root URL to index.html
        registry.addViewController("/").setViewName("forward:/index.html");
    }
}