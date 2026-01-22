package fileback.cli.cmd;

import fileback.core.BackupManager;
import fileback.core.Profile;
import fileback.core.RollbackManager;
import fileback.storage.FileLayoutResolver;
import fileback.storage.ProfileStore;
import fileback.util.FileUtils;

public final class RollbackCommand {

    private final String[] args;

    public RollbackCommand(String[] args) {
        this.args = args;
    }

    public void execute() throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("rollback command requires subcommand and profile name");
        }

        String subCommand = args[1];
        String profileName = args[2];
   

        ProfileStore store = ProfileStore.load();
        Profile profile = store.getProfile(profileName);
        if (profile == null) {
            throw new IllegalArgumentException("Profile not found: " + profileName);
        }

        RollbackManager rollbackManager = new RollbackManager();
        BackupManager backupManager = new BackupManager();

        switch (subCommand) {
            case "do":
                if (args.length < 5) {
                    throw new IllegalArgumentException("rollback command do requires profilename, time entry, t/f verbose or dry ");
                }
                boolean verbose = Boolean.parseBoolean(args[4]);
                backupManager.backup(profile, verbose, true);
                rollbackManager.rollback(profile,args[3], verbose, false);
                System.out.println("Rollback completed successfully.");
                break;
            case "undo":
                if (args.length < 4) {
                    throw new IllegalArgumentException("rollback command undo requires profilename, time entry, t/f verbose or dry ");
                }
                rollbackManager.rollback(profile,"", Boolean.parseBoolean(args[3]), true);
                System.out.println("Rollback undo completed successfully.");
                break;
            case "commit":
                FileLayoutResolver layout = new FileLayoutResolver(profile);
                FileUtils.deleteRecursive(layout.getSnapshotRoot());
                break;
            default:
                throw new IllegalArgumentException("Unknown rollback subcommand: " + subCommand);
        }
    }
}