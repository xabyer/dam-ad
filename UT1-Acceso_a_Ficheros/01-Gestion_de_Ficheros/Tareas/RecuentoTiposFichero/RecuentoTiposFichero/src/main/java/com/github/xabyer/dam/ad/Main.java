package com.github.xabyer.dam.ad;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/*
Haz un programa que cuente cuántos ficheros de cada tipo hay en una carpeta dada. Al terminar, debe mostrar un resumen
de qué extensiones de fichero había, cuántos ficheros había por cada una, y cuál ha sido su tamaño promedio;
por ejemplo:

TXT: 3 ficheros (tamaño medio 2323.23 bytes)
PDF: 2 ficheros (tamaño medio 86284.23 bytes)

...
 */
public class Main {
    static void main() {

        // 1. Pedir la ruta de trabajo
        var workingPath = Path.of(requestWorkingPath());

        // 2. Recorrer la ruta filtrando archivos devolviendo un mapa con la extension y la cantidad de tipos.
        collectFilesStats(workingPath);


    }

    private static void collectFilesStats(Path workingPath) {
        Map<String, Integer> filesTypeCounterMap = new HashMap<>();
        Map<String, Long> totalBytesFilesMap = new HashMap<>();
        Integer filesTypeCounter;
        Long totalBytesFiles;

        try (Stream<Path> directoryFiles = Files.list(workingPath).filter(Files::isRegularFile)) {

            for (var currentFile : directoryFiles.toList()) {
                var extensionKeyMap = getExtensionFile(currentFile);

                // Fill map to count type of files
                filesTypeCounter = filesTypeCounterMap.getOrDefault(extensionKeyMap, 0);
                filesTypeCounter++;
                filesTypeCounterMap.put(extensionKeyMap, filesTypeCounter);

                // Fill map to sum total bytes of a type of file.
                totalBytesFiles = totalBytesFilesMap.getOrDefault(extensionKeyMap, 0L);
                totalBytesFiles += Files.size(currentFile);
                totalBytesFilesMap.put(extensionKeyMap, totalBytesFiles);

            }
            if(!filesTypeCounterMap.isEmpty()) {
                IO.println(filesTypeCounterMap.entrySet());
                IO.println(totalBytesFilesMap.entrySet());

            }

            // 3. Mostrar la información de salida
            showResults(filesTypeCounterMap, totalBytesFilesMap);

        } catch (IOException e) {
            System.err.println("No se pudo encontrar el directorio: " + e.getMessage());

        } catch (Exception e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, e.getLocalizedMessage());
        }
    }

    private static void showResults(Map<String, Integer> filesTypeCounterMap, Map<String, Long> totalBytesFilesMap) {
        if(
                filesTypeCounterMap != null && totalBytesFilesMap!= null &&
                !filesTypeCounterMap.isEmpty() && !totalBytesFilesMap.isEmpty()
        ){
            for(String key: filesTypeCounterMap.keySet()){
                if(filesTypeCounterMap.containsKey(key) && totalBytesFilesMap.containsKey(key)){

                    IO.println(
                            key +
                            ":" +
                            filesTypeCounterMap.get(key) +
                            " ficheros (tamaño medio " +
                            totalBytesFilesMap.get(key) / filesTypeCounterMap.get(key) +
                            " bytes.)"
                    );
                }
            }
        } else {
            IO.println("No hay información que mostrar.");
        }
    }

    /**
     * Extract the file extension from the given path.
     * @param workingPath the path to the file.
     * @return A String representing the file extension
     * @throws IndexOutOfBoundsException if the path does not refer to a regular file with an extension;
     *          ensure this method is called after filtering with {@code isRegularFile}(NIO) or {@code isFile}
     *          (IO). You can also check if the path contains a dot by parsing it with {@code toString} and
     *          checking with {@code contains(".")} method.
     */
    private static String getExtensionFile(Path workingPath) {

        var fileLastDotPosition = workingPath.getFileName().toString().lastIndexOf(".");
        var fileExtension = workingPath.getFileName().toString().split("\\.", fileLastDotPosition)[1];

        IO.println(workingPath.getFileName().toString()); //to check the name of the working files.

        return fileExtension;

    }

    private static String requestWorkingPath() {
        return IO.readln("Introduce la ruta donde buscar cada tipo de archivo: ");
    }
}
