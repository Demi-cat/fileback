package fileback.util;

import java.io.*;
import java.nio.file.*;

public final class FileUtils {

    public static void copyRecursive(Path src, Path dst, boolean verbose) throws IOException {
        if (Files.isDirectory(src)) {
            Files.createDirectories(dst);
            try (DirectoryStream<Path> ds = Files.newDirectoryStream(src)) {
                for (Path p : ds) {
                    copyRecursive(p, dst.resolve(p.getFileName()), verbose);
                }
            }
        } else {
            Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
            if (verbose) System.out.println("Copied " + src);
        }
    }

    public static void deleteRecursive(Path path) throws IOException {
        if (!Files.exists(path)) return;
        if (Files.isDirectory(path)) {
            try (DirectoryStream<Path> ds = Files.newDirectoryStream(path)) {
                for (Path p : ds) deleteRecursive(p);
            }
        }
        Files.deleteIfExists(path);
    }
}