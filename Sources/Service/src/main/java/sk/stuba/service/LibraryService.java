package sk.stuba.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sk.stuba.dao.LibraryDao;
import sk.stuba.entity.Library;

@Service
public class LibraryService {

    @Autowired
    private LibraryDao libraryDao;

    public Collection<Library> getAllLibraries(){
        return libraryDao.getAllLibraries();
    }

    public Library getLibraryFromImport(Integer id){
        return libraryDao.getLibraryFromImport(id);
    }
}
