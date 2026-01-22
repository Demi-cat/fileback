package fileback.cli.cmd;

import fileback.core.Profile;
import fileback.storage.ProfileStore;

public final class ListProfilesCommand {

    public void execute() throws Exception {
        ProfileStore store = ProfileStore.load();
        System.out.println("Available profiles:");
        for (Profile profile : store.getAllProfiles()) {
            System.out.println("- " + profile.getName() + " (ID: " + profile.getId() + ")");
        }
    }
}