package sk.stuba.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import sk.stuba.entity.Library;

@Repository
public class LibraryDao {
    private static Map<Integer, Library> libraries;

    static {
        libraries = new HashMap<Integer, Library>(){
            {
                put(1, new Library(1,"path1", "version1"));
                put(2, new Library(2,"path2", "version2"));
                put(3, new Library(3,"path3", "version3"));
                put(4, new Library(4,"path4", "version4"));
            }
        };
    }

    public Collection<Library> getAllLibraries(){
        return libraries.values();
    }

    public Library getLibraryFromImport(Integer id){
        return libraries.get(id);
    }
}
