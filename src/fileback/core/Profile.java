package fileback.core;

import java.util.*;

public final class Profile {

    private final String id;
    private String name;
    private int historyLimit;
    private List<String> dataPaths;
    private String backupRoot;

    public Profile(String id, String name, int historyLimit, String backupRoot) {
        this.id = id;
        this.name = name;
        this.historyLimit = historyLimit;
        this.backupRoot = backupRoot;
        this.dataPaths = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getHistoryLimit() { return historyLimit; }
    public void setHistoryLimit(int historyLimit) { this.historyLimit = historyLimit; }

    public List<String> getDataPaths() { return Collections.unmodifiableList(dataPaths); }
    public void addDataPath(String path) { dataPaths.add(path); }
    public void removeDataPath(String path) { dataPaths.remove(path); }

    public String getBackupRoot() { return backupRoot; }
}