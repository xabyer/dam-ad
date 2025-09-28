package com.github.xabyer.dam.ad;

import com.github.xabyer.dam.ad.explorador.ExploradorDirectorios;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
Nota: Mañana definir bien los requisitos y necesidades del ejercicio. Realizar análisis para su resolución y plantear
la resolución del ejercicio.
 */

public class Main {
    static void main() {
        String rutaTrabajo = pedirRutaTrabajo();
        ExploradorDirectorios pathStr = new ExploradorDirectorios(rutaTrabajo);
        pathStr.mostrarDirectorioPadre();
        Pattern pattern = Pattern.compile(ExploradorDirectorios.REGEX_GENERAL);
        List<Path> directoryMatchList= new ArrayList<>();
        List<Path> directoryNotMatchList= new ArrayList<>();

        var ruta = Path.of(rutaTrabajo);
        try(
            var files = Files.walk(ruta).filter(Files::isDirectory)
        ) {
            for(var file : files.toList()) {
                var directoryName = file.getFileName().toString();
                Matcher patternMatcher = pattern.matcher(directoryName);
                if(patternMatcher.matches()) {

                    directoryMatchList.add(file);
                }
                else {

                    directoryNotMatchList.add(file);
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        IO.println("Directory matches: ");
        for (var path : directoryMatchList){
            IO.println(path);
            IO.println(path.getFileName());
        }
        IO.println("Directory not matches: ");
        for (var path : directoryNotMatchList){
            IO.println(path);
            IO.println(path.getFileName());
        }

    }

    private static String pedirRutaTrabajo() {
        return IO.readln("Introduce el directorio de trabajo: ");
    }
}
