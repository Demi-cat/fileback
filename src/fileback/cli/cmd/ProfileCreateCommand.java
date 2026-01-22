package fileback.cli.cmd;

import fileback.core.Profile;
import fileback.storage.ProfileStore;
import fileback.util.expt.InvalidCommandException;

import javax.swing.JFileChooser;
import java.io.File;
import java.util.Scanner;
import java.util.UUID;

public final class ProfileCreateCommand {

    private final String[] args;

    public ProfileCreateCommand(String[] args) {
        this.args = args;
    }

    public void execute() throws Exception {
        if (args.length < 4) {
            throw new InvalidCommandException(
                    "Usage: fileback profile create <name> <historyLimit>"
            );
        }

        String name = args[2];
        int historyLimit;

        try {
            historyLimit = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            throw new InvalidCommandException(
                    "History limit must be a valid integer"
            );
        }

        if (name.equalsIgnoreCase("all") || name.startsWith("fileback")) {
            throw new InvalidCommandException(
                    "Reserved profile name: " + name
            );
        }

        ProfileStore store = ProfileStore.load();
        if (store.getProfile(name) != null) {
            throw new InvalidCommandException(
                    "Profile already exists: " + name
            );
        }

        String id = UUID.randomUUID()
                .toString()
                .substring(0, 8);

        String backupRoot = "fileback_data_" + id;
        Profile profile = new Profile(id, name, historyLimit, backupRoot);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select data directory to back up");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selected = chooser.getSelectedFile();
                profile.addDataPath(selected.getAbsolutePath());
                System.out.println("Added path: " + selected.getAbsolutePath());
            }

            System.out.print(
                    "Type 'next' to add another path or 'exit' to finish: "
            );
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("exit")) {
                break;
            }
        }

        scanner.close();

        if (profile.getDataPaths().isEmpty()) {
            throw new InvalidCommandException(
                    "Profile must contain at least one data path"
            );
        }

        store.addProfile(profile);
        store.save();

        System.out.println("Profile created successfully: " + name);
    }
}
