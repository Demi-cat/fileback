package fileback.core;

import fileback.storage.*;
import fileback.util.*;
import fileback.util.expt.BackupFailedException;
import fileback.util.expt.DiskSpaceInsufficientException;

import java.nio.file.*;
import java.time.*;
import java.time.format.*;

public final class BackupManager {

    public void backup(Profile profile, boolean verbose, boolean snapshot) throws Exception {
        FileLayoutResolver layout = new FileLayoutResolver(profile);
        layout.ensureBaseStructure();

        if (!DiskUtils.hasEnoughSpaceForBackup(Paths.get(profile.getBackupRoot()), profile.getDataPaths())) {
            throw new DiskSpaceInsufficientException();
        }

        String timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
                .format(LocalDateTime.now());

        Path tempDir = layout.getTempEntryPath(timestamp);
        Files.createDirectories(tempDir);

        try {
            int index = 0;
            for (String src : profile.getDataPaths()) {
                Path source = Paths.get(src);
                if (!Files.exists(source)) {
                    throw new BackupFailedException("Source path missing: " + src);
                }
                Path target = tempDir.resolve("path_" + index++);
                FileUtils.copyRecursive(source, target, verbose);
            }

            Path finalDir = layout.getEntryPath(timestamp);
            if(snapshot){
                finalDir = layout.getSnapshotRoot();
                FileUtils.deleteRecursive(finalDir.resolve(""));
            }

            Files.move(tempDir, finalDir);
            if(!snapshot){
                layout.pruneOldEntries(profile.getHistoryLimit());
            }
            
        } catch (BackupFailedException e) {
            FileUtils.deleteRecursive(tempDir);
            if(!snapshot){
                throw e;
            }
            else{
                System.out.println("Recreating directories...");
            }
        } catch (Exception e){
            throw e;
        }
    }
}