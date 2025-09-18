# Ejercicio ampliado

## Carácteristicas:

- Fecha de última modificación
- Permisos POSIX (si el sistema lo soporta)
- Manteniendo tu estilo y estructura, pero con las mejoras que comentamos:
    - Nombres más claros
    - Si pongo un try dentro del método, no lanzo la excepción hacia fuera (la trato ahí mismo)
    - Uso de try-with-resources para cerrar Stream<Path>
    - 
````java
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.Set;
import java.util.stream.Stream;

public class Main {

    static void main() {
        Path workingPath = requestDirectoryPath();

        if (doesNotExist(workingPath)) {
            IO.println("El directorio %s no existe".formatted(workingPath));
            return;
        }

        if (isNotADirectory(workingPath)) {
            IO.println("La ruta %s introducida no es un directorio.".formatted(workingPath));
            return;
        }

        showContentPath(workingPath);
    }

    // 1. Pedir por teclado la ruta completa de un directorio
    private static Path requestDirectoryPath() {
        return Path.of(IO.readln("Introduzca el directorio de trabajo: "));
    }

    // 2. Si no existe, indicarlo y terminar.
    private static boolean doesNotExist(Path path) {
        return Files.notExists(path);
    }

    // 3. Si no es un directorio, indicarlo y terminar.
    private static boolean isNotADirectory(Path path) {
        return !Files.isDirectory(path);
    }

    // 4. Mostrar un listado de las entradas que contenga el directorio.
    // Aquí capturamos IOException dentro, porque si hay un error listando
    // no queremos propagarlo: simplemente mostramos el error y seguimos.
    private static void showContentPath(Path path) {
        try (Stream<Path> entries = Files.list(path)) {
            entries.forEach(Main::checkContentPath);
        } catch (IOException e) {
            System.err.println("Error al listar el directorio: " + e.getMessage());
        }
    }

    private static void checkContentPath(Path path) {
        try {
            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);

            if (attrs.isRegularFile()) {
                IO.println("Fichero: %-30s Tamaño: %d bytes | Última modificación: %s"
                        .formatted(path.getFileName(), attrs.size(), attrs.lastModifiedTime()));
            } else if (attrs.isDirectory()) {
                try (Stream<Path> subEntries = Files.list(path)) {
                    long count = subEntries.count();
                    IO.println("Directorio: %-30s Contiene: %d entradas | Última modificación: %s"
                            .formatted(path.getFileName(), count, attrs.lastModifiedTime()));
                }
            } else {
                IO.println("Otro tipo: %-30s Última modificación: %s"
                        .formatted(path.getFileName(), attrs.lastModifiedTime()));
            }

            // Intentar mostrar permisos POSIX si el sistema lo soporta
            try {
                PosixFileAttributes posixAttrs = Files.readAttributes(path, PosixFileAttributes.class);
                Set<PosixFilePermission> perms = posixAttrs.permissions();
                IO.println("    Permisos POSIX: " + PosixFilePermissions.toString(perms));
            } catch (UnsupportedOperationException e) {
                IO.println("    Permisos POSIX: No soportado en este sistema");
            }

        } catch (IOException e) {
            System.err.printf("No se pudo acceder a %s: %s%n", path.getFileName(), e.getMessage());
        }
    }
}
````