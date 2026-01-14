package dev.muriplz;

import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import dev.muriplz.listener.PlayerReadyListener;


public class Main extends JavaPlugin {

    public Main(JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {

        // ######## Events ######## //
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, PlayerReadyListener::onPlayerReady);

    }
}
