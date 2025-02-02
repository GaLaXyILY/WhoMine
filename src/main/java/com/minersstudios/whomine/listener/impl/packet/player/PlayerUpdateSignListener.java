package com.minersstudios.whomine.listener.impl.packet.player;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.inventory.SignMenu;
import com.minersstudios.whomine.listener.api.PacketListener;
import com.minersstudios.whomine.packet.PacketEvent;
import com.minersstudios.whomine.packet.PacketType;
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PlayerUpdateSignListener extends PacketListener {

    public PlayerUpdateSignListener(final @NotNull WhoMine plugin) {
        super(plugin, PacketType.PLAY_SERVER_UPDATE_SIGN);
    }

    @Override
    public void onPacketReceive(final @NotNull PacketEvent event) {
        final Player player = event.getConnection().getPlayer().getBukkitEntity();
        final SignMenu menu = SignMenu.getSignMenu(player);

        if (
                menu != null
                && event.getPacketContainer().getPacket() instanceof final ServerboundSignUpdatePacket packet
        ) {
            if (!menu.getResponse().test(player, packet.getLines())) {
                this.getPlugin().runTaskLater(() -> menu.open(player), 2L);
            } else {
                menu.close(player);
            }
        }
    }
}
