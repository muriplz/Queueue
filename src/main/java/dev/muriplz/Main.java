package dev.muriplz;

import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerSetupConnectEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import dev.muriplz.config.QueueConfig;
import dev.muriplz.listener.PlayerListener;
import dev.muriplz.queue.QueueManager;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main extends JavaPlugin {

    public static final String MOD_ID = "queueue";
    private ScheduledExecutorService scheduler;

    public Main(JavaPluginInit init) {
        super(init);
        try {
            QueueConfig.load(Path.of("mods/" + MOD_ID));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }

    @Override
    protected void setup() {
        this.getEventRegistry().registerGlobal(PlayerConnectEvent.class, PlayerListener::onPlayerConnect);

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(QueueManager::cleanExpired, 10, 10, TimeUnit.SECONDS);
    }

    @Override
    protected void shutdown() {
        if (scheduler != null) scheduler.shutdown();
    }
}