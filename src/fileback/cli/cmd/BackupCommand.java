package fileback.cli.cmd;

import fileback.core.BackupManager;
import fileback.core.Profile;
import fileback.storage.ProfileStore;

public final class BackupCommand {

    private final String[] args;

    public BackupCommand(String[] args) {
        this.args = args;
    }

    public void execute() throws Exception {
        if (args.length < 3) {
            throw new IllegalArgumentException("backup command requires a profile name");
        }

        String profileName = args[1];
        boolean verbose = Boolean.parseBoolean(args[2]);

        ProfileStore store = ProfileStore.load();
        Profile profile = store.getProfile(profileName);
        if (profile == null) {
            throw new IllegalArgumentException("Profile not found: " + profileName);
        }

        BackupManager manager = new BackupManager();
        manager.backup(profile, verbose, false);
        System.out.println("Backup completed successfully.");
    }
}