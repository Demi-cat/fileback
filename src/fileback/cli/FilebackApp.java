package fileback.cli;

import fileback.util.expt.FilebackException;

public final class FilebackApp {
    public static void main(String[] args) {
        try {
            CommandDispatcher dispatcher = new CommandDispatcher();
            dispatcher.dispatch(args);
        } catch (FilebackException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected failure:");
            e.printStackTrace();
        }
    }
}