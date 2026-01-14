package dev.muriplz.listener;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerSetupConnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import dev.muriplz.config.QueueConfig;
import dev.muriplz.queue.QueueManager;

import java.util.UUID;

public class PlayerListener {

    public static void onPlayerConnect(PlayerConnectEvent event) {
        String playerName = event.getPlayerRef().getUsername();

        if (QueueConfig.get().isWhitelisted(playerName)) {
            return;
        }

        UUID playerId = event.getPlayerRef().getUuid();

        int availableSlots = QueueConfig.get().maxPlayers - QueueManager.getConnectedCount();

        if (availableSlots <= 0) {
            QueueManager.addToQueue(playerId, playerName);
            int position = QueueManager.getPosition(playerId);
            int minutes = QueueConfig.get().queue.timeoutSeconds / 60;
            event.getPlayerRef().getPacketHandler().disconnect(QueueConfig.get().messages.serverFull
                    .replace("{position}", String.valueOf(position))
                    .replace("{total}", String.valueOf(QueueManager.getQueueSize()))
                    .replace("{minutes}", String.valueOf(minutes)));
            return;
        }

        int position = QueueManager.getPosition(playerId);
        if (position > 0 && position <= availableSlots) {
            QueueManager.removeFromQueue(playerId);
            return;
        }

        if (position > availableSlots) {
            int minutes = QueueConfig.get().queue.timeoutSeconds / 60;
            event.getPlayerRef().getPacketHandler().disconnect(QueueConfig.get().messages.serverFull
                    .replace("{position}", String.valueOf(position))
                    .replace("{total}", String.valueOf(QueueManager.getQueueSize()))
                    .replace("{minutes}", String.valueOf(minutes)));
            return;
        }

    }
}