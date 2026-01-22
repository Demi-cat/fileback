package fileback.cli;

import fileback.cli.cmd.EditProfileCommand;
import fileback.cli.cmd.ProfileCreateCommand;

public final class ProfileCommandRouter {

    public static void route(String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException(
                    "profile command requires a subcommand"
            );
        }

        String subCommand = args[1];

        switch (subCommand) {
            case "create":
                new ProfileCreateCommand(args).execute();
                break;
            case "edit":
                new EditProfileCommand(args).execute();
                break;
            default:
                throw new UnsupportedOperationException(
                        "Unknown profile operation: " + subCommand
                );
        }
    }
}
