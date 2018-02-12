package sk.stuba.controller;


import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import sk.stuba.entity.Library;
import sk.stuba.service.LibraryService;

@Slf4j
@RestController
@RequestMapping("/libs")
public class LibraryController {

    @Autowired
    private LibraryService libraryService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Collection<Library> getAllLibraries() {
        return libraryService.getAllLibraries();
    }

    @RequestMapping(value = "/{import}", method = RequestMethod.GET)
    public Library getLibraryFromImport(@PathVariable(value = "import") String imp) {
        log.info("passed param {}", imp);
        return libraryService.getLibraryFromImport(1);
    }
}
