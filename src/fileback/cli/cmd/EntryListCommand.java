package fileback.cli.cmd;

import fileback.core.Profile;
import fileback.storage.FileLayoutResolver;
import fileback.storage.ProfileStore;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public final class EntryListCommand {

    private final String[] args;

    public EntryListCommand(String[] args) {
        this.args = args;
    }

    public void execute() throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("entry_list command requires a profile name");
        }

        String profileName = args[1];

        ProfileStore store = ProfileStore.load();
        Profile profile = store.getProfile(profileName);
        if (profile == null) {
            throw new IllegalArgumentException("Profile not found: " + profileName);
        }

        FileLayoutResolver layout = new FileLayoutResolver(profile);
        Path entriesPath = layout.getProfileRoot();

        if (!Files.exists(entriesPath)) {
            System.out.println("No backup entries found for profile: " + profileName);
            return;
        }

        List<Path> entries = Files.list(entriesPath)
                .filter(Files::isDirectory)
                .sorted((a, b) -> b.getFileName().toString().compareTo(a.getFileName().toString()))
                .collect(Collectors.toList());

        System.out.println("Backup entries for profile '" + profileName + "':");
        for (Path entry : entries) {
            System.out.println("- " + entry.getFileName().toString());
        }
    }
}