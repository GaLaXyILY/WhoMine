package com.minersstudios.msessentials.listeners.event.player;

import com.minersstudios.mscore.listener.event.MSListener;
import com.minersstudios.msessentials.MSEssentials;
import com.minersstudios.msessentials.player.PlayerInfo;
import com.minersstudios.msessentials.util.MessageUtils;
import com.minersstudios.mscore.listener.event.AbstractMSListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

@MSListener
public class PlayerDeathListener extends AbstractMSListener<MSEssentials> {

    @EventHandler
    public void onPlayerDeath(final @NotNull PlayerDeathEvent event) {
        final Player killedPlayer = event.getEntity();
        final PlayerInfo killedInfo = PlayerInfo.fromOnlinePlayer(killedPlayer);

        event.deathMessage(null);
        killedInfo.unsetSitting();
        MessageUtils.sendDeathMessage(killedPlayer, killedPlayer.getKiller());
    }
}
