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

    @Bean
    public Endpoints endpoints() { return  new Endpoints(new Broker("35.166.188.212", 5672, 15674, -1));}

}