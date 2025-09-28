package com.github.xabyer.dam.ad.explorador;

import java.io.File;
import java.nio.file.Path;

public class ExploradorDirectorios {

    public static final char SEPARADOR = File.separatorChar;
    public static final String PARENT_PATH = ".." + SEPARADOR;
    public static final String REGEX_GENERAL = "(\\w ?)+(-)(\\(\\d{5}\\))";
    public static final String REGEX_ESTRICTA = "([a-z0-9] ?)+(-)(\\(\\d{5}\\))";

    private final String workingPath;

    public ExploradorDirectorios(String workingPath) {
        this.workingPath = workingPath;
    }

    public void mostrarDirectorioPadre() {
        var parentPath = Path.of(workingPath).getParent().toString();
        IO.println(parentPath);
    }
}
