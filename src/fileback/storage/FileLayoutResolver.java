package fileback.storage;

import fileback.core.*;
import fileback.util.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public final class FileLayoutResolver {

    private final Profile profile;

    public FileLayoutResolver(Profile profile) {
        this.profile = profile;
    }

    public void ensureBaseStructure() throws IOException {
        Files.createDirectories(getProfileRoot());
    }

    public Path getProfileRoot() {
        return Paths.get(profile.getBackupRoot(), profile.getName());
    }

    public Path getEntryPath(String timestamp) {
        return getProfileRoot().resolve(timestamp);
    }

    public Path getTempEntryPath(String timestamp) {
        return getProfileRoot().resolve("._tmp_" + timestamp);
    }

    public Path getSnapshotRoot(){
        return getProfileRoot().resolve("Snapshot");
    }

    public void pruneOldEntries(int limit) throws IOException {
        List<Path> entries = new ArrayList<>();
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(getProfileRoot())) {
            for (Path p : ds) if (Files.isDirectory(p)) entries.add(p);
        }
        entries.sort(Comparator.naturalOrder());
        while (entries.size() > limit) {
            FileUtils.deleteRecursive(entries.remove(0));
        }
    }
}