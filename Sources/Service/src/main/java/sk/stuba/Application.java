package sk.stuba;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import sk.stuba.configuration.StorageConfiguration;
import sk.stuba.service.LibraryStorageService;


@SpringBootApplication
@EnableConfigurationProperties(StorageConfiguration.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner init(LibraryStorageService libraryStorageService) {
        return args -> libraryStorageService.init();
    }
}
