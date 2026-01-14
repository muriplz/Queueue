package dev.muriplz.config;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class QueueConfig {

    private static QueueConfig instance;

    public int maxPlayers;
    public QueueSettings queue;
    public Messages messages;

    public List<String> usernameWhitelist;

    public static class QueueSettings {
        public int maxSize;
        public int timeoutSeconds;
    }

    public static class Messages {
        public String serverFull;
    }

    public static QueueConfig get() {
        return instance;
    }

    public static void load(Path modFolder) throws IOException {
        Path configPath = modFolder.resolve("config.json");

        if (!Files.exists(configPath)) {
            Files.createDirectories(modFolder);
            try (InputStream stream = QueueConfig.class.getResourceAsStream("/config.json")) {
                if (stream == null) throw new IOException("Default config not found in jar");
                Files.copy(stream, configPath);
            }
        }

        String json = Files.readString(configPath);
        instance = new Gson().fromJson(json, QueueConfig.class);
    }

    public boolean isWhitelisted(String username) {
        return usernameWhitelist != null && usernameWhitelist.contains(username);
    }
}