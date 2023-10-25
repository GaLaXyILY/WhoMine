package com.minersstudios.msessentials.anomalies;

import com.minersstudios.msessentials.Cache;
import com.minersstudios.msessentials.MSEssentials;
import com.minersstudios.msessentials.anomalies.actions.AddPotionAction;
import com.minersstudios.msessentials.anomalies.actions.SpawnParticlesAction;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Anomaly action class.
 * Used to do something when a player is in anomaly zone
 * and the time is up and the percentage is reached.
 *
 * @see AddPotionAction
 * @see SpawnParticlesAction
 */
public abstract class AnomalyAction {
    private final long time;
    private final int percentage;

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * @param time       Time in ticks to perform action (1 second = 20 ticks)
     * @param percentage Percentage chance of completing action
     */
    protected AnomalyAction(
            final long time,
            final int percentage
    ) {
        this.time = time;
        this.percentage = percentage;
    }

    /**
     * @return Time in ticks to perform action (1 second = 20 ticks)
     */
    public final long getTime() {
        return this.time;
    }

    /**
     * @return Percentage chance of completing the action
     */
    public final int getPercentage() {
        return this.percentage;
    }

    /**
     * @return True if the percentage is reached
     */
    public final boolean isPercentageReached() {
        return RANDOM.nextInt(100) < this.percentage;
    }

    /**
     * Puts this action to player's action map with current time
     *
     * @param player The player to be influenced
     * @return The previous action map associated with player,
     *         or null if there was no mapping for player
     */
    public final @Nullable Map<AnomalyAction, Long> putAction(final @NotNull Player player) {
        final Cache cache = MSEssentials.getCache();
        final var actionMap = cache.playerAnomalyActionMap.getOrDefault(player, new ConcurrentHashMap<>());

        actionMap.put(this, System.currentTimeMillis());
        return cache.playerAnomalyActionMap.put(player, actionMap);
    }

    /**
     * Removes this action from player's action map
     *
     * @param player A player who has been influenced
     *               and from which the action will be removed
     */
    public final void removeAction(final @NotNull Player player) {
        final Cache cache = MSEssentials.getCache();
        final var actionMap = cache.playerAnomalyActionMap.get(player);

        if (actionMap != null) {
            actionMap.remove(this);
            cache.playerAnomalyActionMap.put(player, actionMap);
        }
    }

    /**
     * Do action if the time is up and the percentage is reached
     *
     * @param player         The player to be influenced
     * @param ignorableItems Ignorable items that will be damaged
     *                       if player has them and the action will be performed
     */
    public abstract void doAction(
            final @NotNull Player player,
            final @Nullable AnomalyIgnorableItems ignorableItems
    );
}
