# 🔍 Explicación de BasicFileAttributes, readAttributes y similares

En Java NIO.2 (java.nio.file.attribute), las attributes classes son interfaces que representan metadatos de un archivo o directorio.

Se usan junto con el método Files.readAttributes() para obtener información de forma eficiente.

## 1. BasicFileAttributes

- Es la interfaz más genérica y está disponible en todos los sistemas.
- Contiene información como:
  - creationTime() → fecha de creación
  - lastModifiedTime() → última modificación
  - lastAccessTime() → último acceso
  - size() → tamaño en bytes
  - isRegularFile() → si es un archivo normal
  - isDirectory() → si es un directorio
  - isSymbolicLink() → si es un enlace simbólico
  - isOther() → otro tipo de entrada

**Ejemplo:**
````java
BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
System.out.println("Tamaño: " + attrs.size());
System.out.println("Última modificación: " + attrs.lastModifiedTime());
````
___

## 2. PosixFileAttributes

- Extiende BasicFileAttributes y añade información específica de sistemas POSIX (Linux, macOS, Unix).
- Permite obtener:
  - Propietario (owner())
  - Grupo (group())
  - Permisos (permissions() → devuelve un Set<PosixFilePermission>)

**Ejemplo:**
````java
PosixFileAttributes posixAttrs = Files.readAttributes(path, PosixFileAttributes.class);
System.out.println("Permisos: " + PosixFilePermissions.toString(posixAttrs.permissions()));
````
⚠️ En Windows normalmente lanza UnsupportedOperationException.
___

## 3. DosFileAttributes

- Extiende BasicFileAttributes y añade atributos específicos de sistemas Windows/DOS:
  - isHidden()
  - isReadOnly()
  - isArchive()
  - isSystem()

**Ejemplo:**
````java
DosFileAttributes dosAttrs = Files.readAttributes(path, DosFileAttributes.class);
System.out.println("Oculto: " + dosAttrs.isHidden());
````
___

## 4. Files.readAttributes()

- Es un método genérico que recibe:
  - La ruta (Path)
  - La clase de atributos que quieres (BasicFileAttributes.class, PosixFileAttributes.class, etc.)
- Devuelve una instancia de esa interfaz con los datos cargados.

**Ventajas:**
- Eficiencia: obtiene todos los atributos de una sola vez, evitando múltiples llamadas al sistema de archivos.
- Portabilidad: puedes pedir BasicFileAttributes en cualquier sistema, y atributos más específicos solo si el sistema lo soporta.
___

### Resumen:
- Siempre puedes usar BasicFileAttributes → info básica y universal.
- Usa PosixFileAttributes si quieres permisos/propietario en Linux/macOS.
- Usa DosFileAttributes si quieres atributos especiales en Windows.
- Files.readAttributes() es la puerta de entrada para todos.
___


# 📊 Tabla comparativa de atributos de ficheros en Java NIO.2

| Interfaz / Clase de atributos | Disponible en | Métodos clave | Información que devuelve | Notas |
|--------------------------------|---------------|--------------|--------------------------|-------|
| **`BasicFileAttributes`** | Todos los sistemas | `creationTime()`, `lastModifiedTime()`, `lastAccessTime()`, `size()`, `isRegularFile()`, `isDirectory()`, `isSymbolicLink()`, `isOther()` | Metadatos básicos: fechas, tamaño, tipo de entrada | Es la más genérica y segura de usar en cualquier plataforma |
| **`PosixFileAttributes`** | Sistemas POSIX (Linux, macOS, Unix) | Hereda todos los de `BasicFileAttributes` + `owner()`, `group()`, `permissions()` | Propietario, grupo y permisos POSIX (`rwxr-xr-x`) | Lanza `UnsupportedOperationException` en Windows |
| **`DosFileAttributes`** | Windows y compatibles | Hereda todos los de `BasicFileAttributes` + `isHidden()`, `isReadOnly()`, `isArchive()`, `isSystem()` | Atributos especiales de Windows/DOS | No disponible en sistemas POSIX |
| **`FileOwnerAttributeView`** | Todos los sistemas | `getOwner()`, `setOwner()` | Propietario del archivo | Vista para leer/cambiar propietario |
| **`PosixFilePermission`** (enum) | POSIX | — | Constantes para permisos (`OWNER_READ`, `GROUP_WRITE`, etc.) | Se usa con `PosixFileAttributes.permissions()` |
| **`UserPrincipal`** | Todos | — | Representa un usuario del sistema | Usado para propietario/grupo |

___

## 💡 Cómo leer atributos
````java
// Atributos básicos (funciona en cualquier sistema)
BasicFileAttributes basic = Files.readAttributes(path, BasicFileAttributes.class);
System.out.println("Tamaño: " + basic.size());
System.out.println("Última modificación: " + basic.lastModifiedTime());

// Atributos POSIX (solo en Linux/macOS)
try {
    PosixFileAttributes posix = Files.readAttributes(path, PosixFileAttributes.class);
    System.out.println("Permisos: " + PosixFilePermissions.toString(posix.permissions()));
    System.out.println("Propietario: " + posix.owner().getName());
} catch (UnsupportedOperationException e) {
    System.out.println("Atributos POSIX no soportados en este sistema");
}

// Atributos DOS (solo en Windows)
try {
    DosFileAttributes dos = Files.readAttributes(path, DosFileAttributes.class);
    System.out.println("Oculto: " + dos.isHidden());
} catch (UnsupportedOperationException e) {
    System.out.println("Atributos DOS no soportados en este sistema");
}
````
___

## 🚀 Consejos prácticos

- Siempre empieza con BasicFileAttributes si quieres compatibilidad total.
- Usa PosixFileAttributes o DosFileAttributes solo si necesitas datos específicos de esa plataforma.
- Si no sabes si el sistema soporta un tipo de atributos, envuélvelo en un try/catch para UnsupportedOperationException.
- Files.readAttributes() es más eficiente que llamar a Files.size(), Files.getLastModifiedTime(), etc. por separado, porque obtiene todo en una sola operación.
___

```
flowchart TD
    A[Files.readAttributes(Path, Class<T>)] --> B[BasicFileAttributes]
    A --> C[PosixFileAttributes]
    A --> D[DosFileAttributes]

    B -->|Disponible en todos los sistemas| B1[Fechas: creationTime, lastModifiedTime, lastAccessTime]
    B --> B2[Tamaño: size()]
    B --> B3[Tipo: isRegularFile, isDirectory, isSymbolicLink, isOther]

    C -->|Extiende BasicFileAttributes| C1[owner()]
    C --> C2[group()]
    C --> C3[permissions() -> Set<PosixFilePermission>]

    D -->|Extiende BasicFileAttributes| D1[isHidden()]
    D --> D2[isReadOnly()]
    D --> D3[isArchive()]
    D --> D4[isSystem()]

    C3 --> C4[PosixFilePermissions.toString(Set)]
```
___

### 🔍 Cómo leer este diagrama
- **Nodo A**: `Files.readAttributes()` es el punto de entrada.
- **Nodo B**: `BasicFileAttributes` es la interfaz base, siempre disponible.
- **Nodo C**: `PosixFileAttributes` extiende `BasicFileAttributes` y añade info de sistemas POSIX.
- **Nodo D**: `DosFileAttributes` extiende `BasicFileAttributes` y añade info de sistemas Windows/DOS.
- Las flechas muestran herencia o relación directa.

---

# 📂 Ejemplo práctico: lector universal de atributos

````java
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.Set;

public class FileAttributesInspector {

    public static void main(String[] args) {
        Path path = Path.of(args.length > 0 ? args[0] : ".");

        if (!Files.exists(path)) {
            System.err.println("La ruta no existe: " + path);
            return;
        }

        System.out.println("=== Información de: " + path + " ===");

        // --- Atributos básicos ---
        try {
            BasicFileAttributes basic = Files.readAttributes(path, BasicFileAttributes.class);
            System.out.println("Tipo: " +
                    (basic.isDirectory() ? "Directorio" :
                     basic.isRegularFile() ? "Archivo" :
                     basic.isSymbolicLink() ? "Enlace simbólico" : "Otro"));
            System.out.println("Tamaño: " + basic.size() + " bytes");
            System.out.println("Creación: " + basic.creationTime());
            System.out.println("Última modificación: " + basic.lastModifiedTime());
            System.out.println("Último acceso: " + basic.lastAccessTime());
        } catch (IOException e) {
            System.err.println("No se pudieron leer atributos básicos: " + e.getMessage());
        }

        // --- Atributos POSIX ---
        try {
            PosixFileAttributes posix = Files.readAttributes(path, PosixFileAttributes.class);
            System.out.println("Propietario: " + posix.owner().getName());
            System.out.println("Grupo: " + posix.group().getName());
            Set<PosixFilePermission> perms = posix.permissions();
            System.out.println("Permisos POSIX: " + PosixFilePermissions.toString(perms));
        } catch (UnsupportedOperationException e) {
            System.out.println("Atributos POSIX no soportados en este sistema");
        } catch (IOException e) {
            System.err.println("Error leyendo atributos POSIX: " + e.getMessage());
        }

        // --- Atributos DOS ---
        try {
            DosFileAttributes dos = Files.readAttributes(path, DosFileAttributes.class);
            System.out.println("Oculto: " + dos.isHidden());
            System.out.println("Solo lectura: " + dos.isReadOnly());
            System.out.println("Archivo de sistema: " + dos.isSystem());
            System.out.println("Archivo de archivo: " + dos.isArchive());
        } catch (UnsupportedOperationException e) {
            System.out.println("Atributos DOS no soportados en este sistema");
        } catch (IOException e) {
            System.err.println("Error leyendo atributos DOS: " + e.getMessage());
        }

        // --- Propietario usando FileOwnerAttributeView ---
        try {
            FileOwnerAttributeView ownerView = Files.getFileAttributeView(path, FileOwnerAttributeView.class);
            if (ownerView != null) {
                UserPrincipal owner = ownerView.getOwner();
                System.out.println("Propietario (via FileOwnerAttributeView): " + owner.getName());
            }
        } catch (IOException e) {
            System.err.println("Error obteniendo propietario: " + e.getMessage());
        }
    }
}
````
___

## 🔹 Cómo usarlo
1. Guarda el archivo como FileAttributesInspector.java.
2. Compílalo:
````java
javac FileAttributesInspector.java
````
3. Ejecútalo pasando una ruta como argumento:
````java
java FileAttributesInspector /ruta/al/archivo_o_directorio
````
Si no pasas argumento, inspecciona el directorio actual (".").
___

## 💡 Características clave
- Portátil: No falla si el sistema no soporta POSIX o DOS, simplemente lo indica.
- Completo: Lee atributos básicos, POSIX, DOS y propietario.
- Seguro: Captura excepciones por tipo (UnsupportedOperationException y IOException).
- Extensible: Puedes añadir más vistas de atributos (AclFileAttributeView, UserDefinedFileAttributeView, etc.).


# 📂 Explorador de directorios avanzado
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
    // Capturamos IOException aquí para no propagarla y poder seguir ejecutando.
    private static void showContentPath(Path path) {
        try (Stream<Path> entries = Files.list(path)) {
            entries.forEach(Main::checkContentPath);
        } catch (IOException e) {
            System.err.println("Error al listar el directorio: " + e.getMessage());
        }
    }

    private static void checkContentPath(Path path) {
        try {
            // --- Atributos básicos ---
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

            // --- Atributos POSIX ---
            try {
                PosixFileAttributes posixAttrs = Files.readAttributes(path, PosixFileAttributes.class);
                Set<PosixFilePermission> perms = posixAttrs.permissions();
                IO.println("    Permisos POSIX: " + PosixFilePermissions.toString(perms));
                IO.println("    Propietario: " + posixAttrs.owner().getName());
                IO.println("    Grupo: " + posixAttrs.group().getName());
            } catch (UnsupportedOperationException e) {
                IO.println("    Permisos POSIX: No soportado en este sistema");
            }

            // --- Atributos DOS ---
            try {
                DosFileAttributes dosAttrs = Files.readAttributes(path, DosFileAttributes.class);
                IO.println("    Oculto: " + dosAttrs.isHidden());
                IO.println("    Solo lectura: " + dosAttrs.isReadOnly());
                IO.println("    Archivo de sistema: " + dosAttrs.isSystem());
                IO.println("    Archivo de archivo: " + dosAttrs.isArchive());
            } catch (UnsupportedOperationException e) {
                IO.println("    Atributos DOS: No soportado en este sistema");
            }

        } catch (IOException e) {
            System.err.printf("No se pudo acceder a %s: %s%n", path.getFileName(), e.getMessage());
        }
    }
}
````
___

## 📌 Notas clave
- BasicFileAttributes: siempre disponible, devuelve tamaño, fechas y tipo de entrada.
- PosixFileAttributes: solo en sistemas POSIX (Linux, macOS, Unix).
- DosFileAttributes: solo en Windows/DOS.
- try-with-resources: asegura que los Stream<Path> se cierren automáticamente.
- Gestión de excepciones:
  - Se capturan errores globales en showContentPath para no detener todo el programa.
  - Se capturan errores puntuales en checkContentPath para que un fallo en un archivo no interrumpa el resto.
___