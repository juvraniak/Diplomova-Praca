package sk.stuba.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import sk.stuba.configuration.StorageConfiguration;
import sk.stuba.exception.LibraryNotFoundException;
import sk.stuba.exception.LibraryStorageException;

@Slf4j
@Service
public class LibraryStorageService implements StorageService {

    private final Path librariesLocation;


    @Autowired
    public LibraryStorageService(StorageConfiguration storageConfiguration) {
        librariesLocation = Paths.get(storageConfiguration.getLocation());
    }


    @Override
    public void init() {
        try {
            Files.createDirectories(librariesLocation);
        } catch (IOException e) {
            throw new LibraryStorageException("Could not initialize storage", e);
        }
    }

    @Override
    public Stream<Path> getAllLibraries() {
        try {
            return Files.walk(this.librariesLocation, 1)
                .filter(path -> !path.equals(this.librariesLocation))
                .map(path -> this.librariesLocation.relativize(path));
        } catch (IOException e) {
            throw new LibraryStorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Resource getLibraryFromImport(String library) {
        try {
            Path file = load(library);
            Resource resource = new UrlResource(file.toUri());
//            if (resource.exists() || resource.isReadable()) {
            if (resource != null) {
                return resource;
            } else {
                throw new LibraryStorageException("Could not read file: " + library);
            }
        } catch (MalformedURLException e) {
            throw new LibraryNotFoundException("Could not read file: " + library, e);
        }
    }

    public Path load(String filename) {
        return librariesLocation.resolve(filename);
    }

    @Override
    public void saveLibrary(String libraryName, MultipartFile libraryFile) {
        String filename = StringUtils.cleanPath(libraryFile.getOriginalFilename());
        try {
            if (libraryFile.isEmpty()) {
                throw new LibraryStorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new LibraryStorageException("Cannot store file with relative path outside current directory " + filename);
            }
            Files.copy(libraryFile.getInputStream(), this.librariesLocation.resolve(filename),
                StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new LibraryStorageException("Failed to store file " + filename, e);
        }
    }

    public Optional<Stream<Path>> getAllPaths() {
        try {
            return Optional.of(Files.walk(this.librariesLocation, 1)
                .filter(path -> !path.equals(this.librariesLocation))
                .map(path -> this.librariesLocation.relativize(path)));
        } catch (IOException e) {
            log.info("Could not retrieve file {}", e.getMessage());
        }
        return Optional.empty();
    }
}
