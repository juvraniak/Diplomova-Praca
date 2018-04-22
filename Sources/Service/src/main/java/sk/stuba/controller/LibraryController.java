package sk.stuba.controller;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sk.stuba.service.LibraryStorageService;

@Slf4j
@RestController
@RequestMapping("/libraries")
public class LibraryController {

  @Autowired
  private LibraryStorageService libraryStorageService;

  @GetMapping("/")
  public String listUploadedFiles(Model model) throws IOException {

    List<String> allLibs = libraryStorageService.getAllPaths().get()
        .map(path -> path.getFileName().toString())
        .collect(Collectors.toList());

    return allLibs.toString();
  }

  @GetMapping(value = "/download/{name}")
  public ResponseEntity<Resource> getLibraryFromImport(@PathVariable(value = "name") String name) {
    log.info("passed param name : {}", name);
    Resource file = libraryStorageService.getLibraryFromImport(name + ".jar");

    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

  @PostMapping(value = "/upload/{name}")
  public ResponseEntity<?> uploadLibrary(@PathVariable(value = "name") String name,
      @RequestParam("file") MultipartFile jarFile) {
    libraryStorageService.saveLibrary(name, jarFile);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
}
