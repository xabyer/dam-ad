package com.github.xabyer.dam.ad;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Main {
    // 1. Pedir por teclado la ruta completa de un directorio
    // 2. Si no existe, indicarlo y terminar.
    // 3. sino es un directorio, indicarlo y terminar.
    // 4. Mostrar un listado de las entradas que contenga el directorio.
    // 4.1. En cada entrada que sea un fichero, se mostrará us tamaño.
    // 4.2. En cada entrada que sea un directorio, se mostrará cuántas entradas contiene.
    static void main() {

        Path workingPath = requestDirectoryPath();

        if (isNotADirectory(workingPath)) {
            IO.println(
                    "El directorio %s no existe".formatted(workingPath.toString())
            );
            return;
        }

        if (isNotDirectory(workingPath)) {
            IO.println(
                    "La ruta %s introducida no es un directorio.".formatted(workingPath.toString())
            );
            return;
        }

        showContentPath(workingPath);


    }

    //1. Pedir por teclado la ruta completa de un directorio
    private static Path requestDirectoryPath() {
        return Path.of(IO.readln("Introduzca el directorio de trabajo: "));
    }

    // 2. Si no existe, indicarlo y terminar.
    private static boolean isNotADirectory(Path path) {
        return Files.notExists(path);
    }

    // 3. sino es un directorio, indicarlo y terminar.
    private static boolean isNotDirectory(Path path) {
        return !Files.isDirectory(path);
    }

    // 4. Mostrar un listado de las entradas que contenga el directorio.
    private static void showContentPath(Path path) {
        try (
                Stream<Path> entries = Files.list(path)
        ) {
            entries.forEach(Main::checkContentPath);

        } catch (IOException e) {
            System.err.println("Error al mostrar el contenido: " + e.getMessage());
        }
    }

    private static void checkContentPath(Path path) {
        // 4.1. En cada entrada que sea un fichero, se mostrará us tamaño.
        try {
            if (Files.isRegularFile(path)) {
                IO.println(
                        "El tamaño del archivo %s es %d bytes".formatted(
                                path.getFileName(),
                                Files.size(path)
                        )
                );
                // 4.2. En cada entrada que sea un directorio, se mostrará cuántas entradas contiene.
            } else if (Files.isDirectory(path)) {
                try (
                        Stream<Path> registers = Files.list(path)
                ) {
                    long registerCount = registers.count();
                    IO.println(
                            "El directorio %s contiene %d entradas"
                                    .formatted(
                                            path.getFileName(),
                                            registerCount
                                    )
                    );
                }
            } else {
                System.out.println("No se reconoce el tipo de entrada.");
            }
        } catch (IOException e) {
            System.err.printf("No se pudo acceder a %s: %s%n", path.getFileName(), e.getMessage());

        }

    }

}
