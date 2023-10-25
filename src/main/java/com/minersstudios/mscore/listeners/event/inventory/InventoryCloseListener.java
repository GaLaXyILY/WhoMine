package com.minersstudios.mscore.listeners.event.inventory;

import com.minersstudios.mscore.MSCore;
import com.minersstudios.mscore.inventory.CustomInventory;
import com.minersstudios.mscore.listener.event.AbstractMSListener;
import com.minersstudios.mscore.listener.event.MSListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

@MSListener
public final class InventoryCloseListener extends AbstractMSListener<MSCore> {

    @EventHandler
    public void onInventoryClose(final @NotNull InventoryCloseEvent event) {
        if (event.getInventory() instanceof final CustomInventory customInventory) {
            customInventory.doCloseAction(event);
        }
    }
}
