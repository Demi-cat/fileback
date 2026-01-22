package fileback.storage;

import fileback.core.Profile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ProfileStore {

    private static final Path STORE_PATH =
            Paths.get("fileback_data", "profiles.json");

    private Map<String, Profile> profiles = new HashMap<>();

    public static ProfileStore load() throws Exception {
        if (!Files.exists(STORE_PATH)) {
            Files.createDirectories(STORE_PATH.getParent());
            ProfileStore store = new ProfileStore();
            store.save();
            return store;
        }

        try (Reader reader = Files.newBufferedReader(STORE_PATH)) {
            ProfileStore store = new Gson().fromJson(reader, ProfileStore.class);
            return store != null ? store : new ProfileStore();
        }
    }

    public synchronized void save() throws Exception {
        try (Writer writer = Files.newBufferedWriter(STORE_PATH)) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
            gson.toJson(this, writer);
        }
    }

    public synchronized void addProfile(Profile profile) {
        if (profiles.containsKey(profile.getName())) {
            throw new IllegalArgumentException(
                    "Profile already exists: " + profile.getName()
            );
        }
        profiles.put(profile.getName(), profile);
    }

    public synchronized Profile getProfile(String name) {
        return profiles.get(name);
    }

    public synchronized void removeProfile(String name) {
        if (!profiles.containsKey(name)) {
            throw new IllegalArgumentException(
                    "Profile not found: " + name
            );
        }
        profiles.remove(name);
    }

    public synchronized void renameProfile(String oldName, String newName) {
        if (!profiles.containsKey(oldName)) {
            throw new IllegalArgumentException(
                    "Profile not found: " + oldName
            );
        }
        if (profiles.containsKey(newName)) {
            throw new IllegalArgumentException(
                    "Profile already exists: " + newName
            );
        }

        Profile profile = profiles.remove(oldName);
        profile.setName(newName);
        profiles.put(newName, profile);
    }

    public synchronized Collection<Profile> getAllProfiles() {
        return Collections.unmodifiableCollection(profiles.values());
    }
}
