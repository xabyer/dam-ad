# Versión alternativa del ejercicio

```java
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {

    static void main() {
        try {
            Path ruta = pedirRutaDirectorio();       // Paso 1
            comprobarExistencia(ruta);               // Paso 2
            comprobarEsDirectorio(ruta);             // Paso 3
            mostrarContenidoDirectorio(ruta);        // Paso 4
        } catch (IllegalArgumentException e) {
            // Capturamos aquí errores de validación de entrada
            System.err.println("Error: " + e.getMessage());
        } catch (IOException e) {
            // Capturamos aquí errores globales de E/S
            System.err.println("Error de entrada/salida: " + e.getMessage());
        }
    }

    /**
     * 1. Pedir por teclado la ruta completa de un directorio.
     * No lanzamos excepción aquí porque la entrada de datos no es crítica
     * y no queremos propagar errores de Scanner.
     */
    static Path pedirRutaDirectorio() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Introduce la ruta completa del directorio: ");
        String ruta = sc.nextLine().trim();
        return Paths.get(ruta);
    }

    /**
     * 2. Si no existe, indicarlo y terminar.
     * Lanzamos IllegalArgumentException para que el main decida si continuar.
     */
    static void comprobarExistencia(Path ruta) {
        if (!Files.exists(ruta)) {
            throw new IllegalArgumentException("La ruta no existe.");
        }
    }

    /**
     * 3. Si no es un directorio, indicarlo y terminar.
     * También lanzamos excepción para que el main lo gestione.
     */
    static void comprobarEsDirectorio(Path ruta) {
        if (!Files.isDirectory(ruta)) {
            throw new IllegalArgumentException("La ruta no es un directorio.");
        }
    }

    /**
     * 4. Mostrar un listado de las entradas que contenga el directorio.
     * 4.1. Si es fichero, mostrar tamaño.
     * 4.2. Si es directorio, mostrar cuántas entradas contiene.
     *
     * Aquí lanzamos IOException al main si hay un fallo global al listar el directorio,
     * pero tratamos errores puntuales de cada entrada dentro del bucle para no interrumpir
     * todo el proceso.
     */
    static void mostrarContenidoDirectorio(Path ruta) throws IOException {
        try (Stream<Path> entradas = Files.list(ruta)) {
            entradas.forEach(p -> {
                try {
                    BasicFileAttributes attrs = Files.readAttributes(p, BasicFileAttributes.class);
                    if (attrs.isRegularFile()) {
                        System.out.printf("Fichero: %-40s Tamaño: %d bytes%n",
                                p.getFileName(), attrs.size());
                    } else if (attrs.isDirectory()) {
                        try (Stream<Path> subEntradas = Files.list(p)) {
                            long count = subEntradas.count();
                            System.out.printf("Directorio: %-40s Contiene: %d entradas%n",
                                    p.getFileName(), count);
                        }
                    } else {
                        System.out.printf("Otro tipo: %s%n", p.getFileName());
                    }
                } catch (IOException e) {
                    // Tratamos aquí para que un error en un archivo no detenga todo el listado
                    System.err.println("No se pudo acceder a: " + p.getFileName() + " -> " + e.getMessage());
                }
            });
        }
    }
}
```

## 🔍 Decisiones clave

1. **Separación de responsabilidades**
   
2. **Gestión de excepciones**
    - Validaciones → lanzan IllegalArgumentException para que el main decida.
    - Lectura de directorio → IOException se lanza al main si es global, pero se captura dentro del bucle para errores puntuales.

3. **Uso de NIO.2**
    - Paths.get() para crear rutas.
    - Files.exists() y Files.isDirectory() para validaciones.
    - Files.list() y Files.readAttributes() para obtener información.
    - Stream para recorrer entradas de forma eficiente.

4. **Optimización**
    - try-with-resources para cerrar automáticamente Stream<Path>.
    - Lectura de atributos con BasicFileAttributes para evitar múltiples accesos al sistema de archivos.
    - Conteo de entradas de subdirectorios con Stream.count().