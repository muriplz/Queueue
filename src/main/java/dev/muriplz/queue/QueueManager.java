package dev.muriplz.queue;

import com.hypixel.hytale.server.core.universe.Universe;
import dev.muriplz.config.QueueConfig;

import java.util.*;

public class QueueManager {

    private static final LinkedHashMap<UUID, QueueEntry> queue = new LinkedHashMap<>();

    public static class QueueEntry {
        public final UUID playerId;
        public final String playerName;
        public final long joinedAt;

        public QueueEntry(UUID playerId, String playerName) {
            this.playerId = playerId;
            this.playerName = playerName;
            this.joinedAt = System.currentTimeMillis();
        }
    }

    public static synchronized void addToQueue(UUID playerId, String playerName) {
        if (queue.containsKey(playerId)) return;
        if (queue.size() >= QueueConfig.get().queue.maxSize) return;
        queue.put(playerId, new QueueEntry(playerId, playerName));
    }

    public static synchronized void removeFromQueue(UUID playerId) {
        queue.remove(playerId);
    }

    public static synchronized int getPosition(UUID playerId) {
        int pos = 1;
        for (UUID id : queue.keySet()) {
            if (id.equals(playerId)) return pos;
            pos++;
        }
        return -1;
    }

    public static synchronized int getQueueSize() {
        return queue.size();
    }

    public static synchronized void cleanExpired() {
        long now = System.currentTimeMillis();
        long timeoutMs = QueueConfig.get().queue.timeoutSeconds * 1000L;
        queue.entrySet().removeIf(e -> now - e.getValue().joinedAt > timeoutMs);
    }

    public static int getConnectedCount() {
        return Universe.get().getPlayerCount();
    }
}