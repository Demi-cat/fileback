package fileback.cli;

import fileback.cli.cmd.BackupCommand;
import fileback.cli.cmd.EntryListCommand;
import fileback.cli.cmd.ListProfilesCommand;
import fileback.cli.cmd.RollbackCommand;
import fileback.util.expt.InvalidCommandException;

public final class CommandDispatcher {

    public void dispatch(String[] args) throws Exception {
        if (args.length == 0) {
            throw new InvalidCommandException("No command specified");
        }

        String primary = args[0];

        switch (primary) {
            case "list":
                new ListProfilesCommand().execute();
                break;
            case "profile":
                ProfileCommandRouter.route(args);
                break;
            case "backup":
                new BackupCommand(args).execute();
                break;
            case "rollback":
                new RollbackCommand(args).execute();
                break;
            case "entry_list":
                new EntryListCommand(args).execute();
                break;
            default:
                throw new InvalidCommandException("Unknown command: " + primary);
        }
    }
}