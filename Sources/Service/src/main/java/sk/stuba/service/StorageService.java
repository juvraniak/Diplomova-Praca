package sk.stuba.service;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void init();

    Stream<Path> getAllLibraries();

    Resource getLibraryFromImport(String library);

    void saveLibrary(String libraryName, MultipartFile libraryFile);
}
