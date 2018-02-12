package sk.stuba.entity;

import java.io.File;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Library {

    private int id;
    private String path;
    private String version;
    private File libraryFile;

}