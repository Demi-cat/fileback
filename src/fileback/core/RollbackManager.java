package fileback.core;

import fileback.storage.FileLayoutResolver;
import fileback.util.*;
import fileback.util.expt.DiskSpaceInsufficientException;

import java.nio.file.*;

public final class RollbackManager {

    public void rollback(Profile profile,String timestamp, boolean verbose ,boolean undo) throws Exception {
        FileLayoutResolver layout = new FileLayoutResolver(profile);

        if (!DiskUtils.hasEnoughSpaceForRollback(Paths.get(profile.getDataPaths().get(0)), layout.getEntryPath(timestamp), profile.getDataPaths().size())) {
            throw new DiskSpaceInsufficientException();
        }

        Path Backup;
        if(!undo){
            Backup = layout.getEntryPath(timestamp);
        }else{
            Backup = layout.getSnapshotRoot();
        }
        int index = 0;
        for(String Live: profile.getDataPaths()){
            Path src = Backup.resolve("path_" + index++);
            FileUtils.deleteRecursive(Paths.get(Live+"\\"));
            try{
                System.out.println(Live);
                Files.createDirectories(Paths.get(Live));
                FileUtils.copyRecursive(src, Paths.get(Live),verbose);
            }
            catch(Exception e){

                throw e;
            }
        }
        if(undo){
            FileUtils.deleteRecursive(Backup);
        }
    }
}