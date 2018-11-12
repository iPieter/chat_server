package be.ipieter.chat.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Pieter
 * @version 1.0
 */

@Configuration
public class AppConfig {

    @Bean
    public StorageStrategy emojiStorage() {
        return new LocalFSStorageStrategy("/chat/emoji/");
    }

    @Bean
    public StorageStrategy imageStorage() {
        return new LocalFSStorageStrategy("/chat/image/");
    }

}