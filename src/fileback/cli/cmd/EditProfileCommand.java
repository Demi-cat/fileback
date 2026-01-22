package fileback.cli.cmd;

import fileback.core.Profile;
import fileback.storage.ProfileStore;

import javax.swing.JFileChooser;
import java.io.File;
import java.util.Scanner;

public final class EditProfileCommand {

    private final String[] args;

    public EditProfileCommand(String[] args) {
        this.args = args;
    }

    public void execute() throws Exception {
        if (args.length < 4) {
            throw new IllegalArgumentException("profile edit requires profile name and options");
        }

        String profileName = args[2];
        String option = args[3];

        ProfileStore store = ProfileStore.load();
        Profile profile = store.getProfile(profileName);
        if (profile == null) {
            throw new IllegalArgumentException("Profile not found: " + profileName);
        }

        switch (option) {
            case "add_path":
                Scanner scannerAdd = new Scanner(System.in);
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

                    System.out.print("Type 'next' to add another path or 'exit' to finish: ");
                    String input = scannerAdd.nextLine().trim().toLowerCase();
                    if (input.equals("exit")) {
                        break;
                    }
                }
                scannerAdd.close();
                break;
            case "remove_path":
                Scanner scannerRemove = new Scanner(System.in);
                while (true) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setDialogTitle("Select data directory to remove");
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                    int result = chooser.showOpenDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selected = chooser.getSelectedFile();
                        profile.removeDataPath(selected.getAbsolutePath());
                        System.out.println("Removed path: " + selected.getAbsolutePath());
                    }

                    System.out.print("Type 'next' to remove another path or 'exit' to finish: ");
                    String input = scannerRemove.nextLine().trim().toLowerCase();
                    if (input.equals("exit")) {
                        break;
                    }
                }
                scannerRemove.close();
                break;
            case "rename":
                if (args.length < 5) {
                    throw new IllegalArgumentException("rename requires a new name");
                }
                store.renameProfile(profileName, args[4]);
                profileName = args[4];
                break;
            case "set_history_limit":
                if (args.length < 5) {
                    throw new IllegalArgumentException("set_history_limit requires a number");
                }
                profile.setHistoryLimit(Integer.parseInt(args[4]));
                break;
            default:
                throw new IllegalArgumentException("Unknown edit option: " + option);
        }

        store.save();
        System.out.println("Profile '" + profileName + "' updated successfully.");
    }
}