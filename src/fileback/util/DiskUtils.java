package fileback.util;

import java.nio.file.*;
import java.util.List;
import java.io.IOException;

public final class DiskUtils {
    private DiskUtils() {}

    public static long calculateSize(Path path) throws IOException {
        if (!Files.exists(path)) return 0;
        if (Files.isDirectory(path)) {
            long size = 0;
            try (DirectoryStream<Path> ds = Files.newDirectoryStream(path)) {
                for (Path p : ds) {
                    size += calculateSize(p);
                }
            }
            return size;
        } else {
            return Files.size(path);
        }
    }

    public static boolean hasEnoughSpaceForBackup(Path backupRoot, List<String> dataPaths) throws IOException {
        long totalSize = 0;
        for (String src : dataPaths) {
            totalSize += calculateSize(Paths.get(src));
        }
        FileStore store = Files.getFileStore(backupRoot);
        return store.getUsableSpace() >= totalSize;
    }

    public static boolean hasEnoughSpaceForRollback(Path dataPath, Path backupEntryPath, int numPaths) throws IOException {
        long totalSize = 0;
        for (int i = 0; i < numPaths; i++) {
            Path backupPath = backupEntryPath.resolve("path_" + i);
            totalSize += calculateSize(backupPath);
        }
        FileStore store = Files.getFileStore(dataPath);
        return store.getUsableSpace() >= totalSize;
    }
}