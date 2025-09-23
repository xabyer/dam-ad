package com.github.xabyer.dam.ad;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/*
    Escribe un programa que pida por teclado la ruta de una carpeta en el disco duro, y después pida una cadena de
    texto. El programa deberá buscar si en la ruta introducida hay ficheros cuyo nombre contenga la cadena introducida.
    Para cada fichero encontrado que sí la contenga, se deberá mostrar por consola:

        - El nombre completo de ese fichero
        - Su tamaño
        - Si se puede ejecutar o no

 */
public class Main {
    static void main() {
        List<Path> foundFileList = new ArrayList<>();
        // 1. Pedir ruta y el archivo por teclado por teclado
        Path searchPath = Path.of(requestPath());
        String targetFileName = requestFileName();

        // 2. Recorrer ruta buscando archivos filtrando por nombre
        try {
            foundFileList = searchTargetFiles(searchPath, targetFileName);
        } catch (IOException e) {
            System.err.println("No se pudo acceder al recurso en la ruta: " + e.getMessage());
        }

        // 3. Recorrer la lista mostrando los atributos de los archivos.
        if(foundFileList.isEmpty()) {
            IO.println("No se encontró ningún archivo que contenga" + targetFileName + " en el nombre");

        } else {

            showFilesAttributes(foundFileList);
        }
    }

    private static void showFilesAttributes(List<Path> foundFileList) {
        for(Path file : foundFileList) {
            IO.println("Nombre de archivo: " + file.getFileName());
            try {
                IO.println("Tamaño del archivo: " + Files.size(file) + " bytes");
            } catch (IOException e) {
                System.err.println("Error al acceder al fichero " + file + ":" + e.getMessage());
            }
            IO.println("Es ejecutable por el sistema: " +Files.isExecutable(file));
        }
    }

    private static List<Path> searchTargetFiles(Path searchPath, String targetFileName) throws IOException {
        try(Stream<Path> searchedTargetFiles = Files.list(searchPath)) {
            return searchedTargetFiles
                    .filter(Files::isRegularFile)
                    .filter(currentPath ->
                            currentPath.getFileName().toString().contains(targetFileName)
                    )
                    .toList();
        }
    }

    private static String requestPath() {
        return IO.readln("Introduzca la ruta de búsqueda de archivos: ");
    }

    private static String requestFileName() {
        return IO.readln("Introduzca el nombre del archivo a buscar: ");
    }
}
