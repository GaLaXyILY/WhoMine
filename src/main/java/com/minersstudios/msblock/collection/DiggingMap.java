package com.minersstudios.msblock.collection;

import com.google.common.collect.ImmutableSet;
import com.minersstudios.msblock.MSBlock;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The DiggingMap class represents a data structure
 * that associates digging entries with blocks. It
 * allows storing and managing multiple digging
 * entries for different blocks and players.
 */
public class DiggingMap {
    private final Map<Block, Set<Entry>> diggingBlockMap = new ConcurrentHashMap<>();

    /**
     * @return An unmodifiable set of all digging entries present
     *         in the DiggingMap
     * @see Entry
     */
    public @NotNull @Unmodifiable Set<Entry> diggingEntrySet() {
        var setBuilder = new ImmutableSet.Builder<Entry>();

        for (var diggingEntrySet : this.diggingBlockMap.values()) {
            setBuilder.addAll(diggingEntrySet);
        }

        return setBuilder.build();
    }

    /**
     * @return An unmodifiable set of all blocks present in the DiggingMap
     */
    public @NotNull @UnmodifiableView Set<Block> blockSet() {
        return Collections.unmodifiableSet(this.diggingBlockMap.keySet());
    }

    /**
     * @return An unmodifiable set of map entries containing blocks
     *         and their corresponding digging entries
     */
    public @NotNull @Unmodifiable Set<Map.Entry<Block, Entry>> entrySet() {
        var entries = new ImmutableSet.Builder<Map.Entry<Block, Entry>>();

        this.diggingBlockMap.forEach((block, entrySet) ->
                entrySet.forEach(diggingEntry ->
                        entries.add(Map.entry(block, diggingEntry))
                )
        );

        return entries.build();
    }

    /**
     * @param block The block for which to retrieve the associated
     *              digging entries
     * @return An unmodifiable set of digging entries associated
     *         with the given block
     * @see Entry
     */
    public @NotNull @Unmodifiable Set<Entry> getDiggingEntrySet(@NotNull Block block) {
        return Collections.unmodifiableSet(this.entries(block));
    }

    /**
     * @param diggingEntry The digging entry for which to retrieve the
     *                     associated block
     * @return The block associated with the digging entry,
     *         or null if the entry is not found in the map
     * @see Entry
     */
    public @Nullable Block getBlock(@NotNull Entry diggingEntry) {
        for (var entry : this.diggingBlockMap.entrySet()) {
            if (!entry.getValue().contains(diggingEntry)) continue;

            for (var digging : entry.getValue()) {
                if (digging.equals(diggingEntry)) return entry.getKey();
            }
        }

        return null;
    }

    /**
     * @param player The player for which to retrieve the associated block
     * @return The block associated with the player,
     *         or null if the player is not found in the map
     * @see Entry
     * @see Entry#player()
     */
    public @Nullable Block getBlock(@NotNull Player player) {
        for (var entry : this.diggingBlockMap.entrySet()) {
            for (var diggingEntry : entry.getValue()) {
                if (diggingEntry.player.equals(player)) return entry.getKey();
            }
        }

        return null;
    }

    /**
     * @param block  The block for which to retrieve the digging entry
     * @param player The player for which to retrieve the digging entry
     * @return The digging entry associated with the block and player,
     *         or null if no entry is found for the block and player
     */
    public @Nullable Entry getEntry(
            @NotNull Block block,
            @NotNull Player player
    ) {
        if (!this.diggingBlockMap.containsKey(block)) return null;

        for (var diggingEntry : this.diggingBlockMap.get(block)) {
            if (diggingEntry.player.equals(player)) return diggingEntry;
        }

        return null;
    }

    /**
     * @param block The block for which to retrieve the digging entry
     *              with the highest stage
     * @return The digging entry with the highest stage for the block,
     *         or null if no entries are found for the block
     * @see Entry
     * @see Entry#stage()
     */
    public @Nullable Entry getBiggestStageEntry(@NotNull Block block) {
        return this.entries(block).stream()
                .max(Comparator.comparingInt(Entry::stage))
                .orElse(null);
    }

    /**
     * Associates the given digging entry with the specified block in
     * the DiggingMap. If the block already exists in the map, the
     * digging entry is added to the existing set of entries. Otherwise,
     * a new set of entries is created and the digging entry is added to it.
     *
     * @param block        The block to associate with the digging entry
     * @param diggingEntry The digging entry to associate with the block
     * @see Entry
     */
    public synchronized void put(
            @NotNull Block block,
            @NotNull Entry diggingEntry
    ) {
        var diggingEntrySet = this.diggingBlockMap.computeIfAbsent(block, b -> ConcurrentHashMap.newKeySet());

        diggingEntrySet.add(diggingEntry);
        this.diggingBlockMap.put(block, diggingEntrySet);
    }

    /**
     * Removes the given digging entry associated with the specified
     * block from the DiggingMap. If the block has no other associated
     * digging entries, the block is removed from the map. Otherwise,
     * the digging entry is removed from the set of entries associated
     * with the block. Additionally, the digging entry's task is cancelled.
     *
     * @param block        The block from which to remove the digging entry
     * @param diggingEntry The digging entry to remove
     * @see Entry
     * @see Entry#cancelTask()
     */
    public synchronized void remove(
            @NotNull Block block,
            @NotNull Entry diggingEntry
    ) {
        var diggingEntrySet = this.entries(block);

        diggingEntrySet.remove(diggingEntry);
        diggingEntry.cancelTask();

        if (diggingEntrySet.isEmpty()) {
            this.diggingBlockMap.remove(block);
        } else {
            this.diggingBlockMap.put(block, diggingEntrySet);
        }
    }

    /**
     * Removes the digging entry associated with the specified block
     * and player from the DiggingMap. If the block has no other associated
     * digging entries, the block is removed from the map. Otherwise,
     * the digging entry is removed from the set of entries associated
     * with the block. Additionally, the digging entry's task is cancelled.
     *
     * @param block  The block from which to remove the digging entry
     * @param player The player whose digging entry to remove
     * @see Entry
     * @see Entry#cancelTask()
     */
    public synchronized void remove(
            @NotNull Block block,
            @NotNull Player player
    ) {
        if (!this.diggingBlockMap.containsKey(block)) return;

        this.diggingBlockMap.forEach((diggingBlock, diggingEntrySet) ->
                diggingEntrySet.forEach(diggingEntry -> {
                    if (
                            diggingBlock.equals(block)
                            && diggingEntry.player.equals(player)
                    ) {
                        this.remove(block, diggingEntry);
                    }
                })
        );
    }

    /**
     * Removes all digging entries associated with the specified block
     * from the DiggingMap. Additionally, all digging entries' tasks are
     * cancelled.
     *
     * @param block The block from which to remove all digging entries
     * @see Entry
     * @see Entry#cancelTask()
     */
    public synchronized void removeAll(@NotNull Block block) {
        var diggingEntrySet = this.diggingBlockMap.remove(block);

        if (diggingEntrySet != null) {
            diggingEntrySet.forEach(Entry::cancelTask);
        }
    }

    /**
     * Removes all digging entries that match the specified digging entry
     * from the DiggingMap. Additionally, all digging entries' tasks are
     * cancelled.
     *
     * @param diggingEntry The digging entry to remove from the DiggingMap
     * @see Entry
     * @see Entry#cancelTask()
     * @see #remove(Block, Entry)
     */
    public synchronized void removeAll(@NotNull Entry diggingEntry) {
        this.diggingBlockMap.forEach((block, entrySet) ->
                entrySet.forEach(digging -> {
                    if (digging.equals(diggingEntry)) {
                        this.remove(block, digging);
                    }
                })
        );
    }

    /**
     * Removes all digging entries associated with the specified player
     * from the DiggingMap. Additionally, all digging entries' tasks are
     * cancelled.
     *
     * @param player The player whose digging entries to remove
     * @see Entry
     * @see Entry#cancelTask()
     * @see #remove(Block, Entry)
     */
    public synchronized void removeAll(@NotNull Player player) {
        this.diggingBlockMap.forEach((block, entrySet) ->
                entrySet.forEach(diggingEntry -> {
                    if (diggingEntry.player.equals(player)) {
                        this.remove(block, diggingEntry);
                    }
                })
        );
    }

    /**
     * @param block The block to check for existence in the DiggingMap
     * @return True if the block is present in the DiggingMap
     */
    public boolean containsBlock(@NotNull Block block) {
        return this.diggingBlockMap.containsKey(block);
    }

    /**
     * Checks if the DiggingMap contains the specified digging entry
     *
     * @param diggingEntry The digging entry to check for existence
     *                     in the DiggingMap
     * @return True if the digging entry is present in the DiggingMap
     */
    public boolean containsEntry(@NotNull Entry diggingEntry) {
        for (var diggingEntrySet : this.diggingBlockMap.values()) {
            if (diggingEntrySet.contains(diggingEntry)) return true;
        }

        return false;
    }

    /**
     * Checks if the DiggingMap contains any digging entry associated
     * with the specified player
     *
     * @param player The player to check for existence in the DiggingMap
     * @return True if the DiggingMap contains any digging entry
     *         associated with the player
     */
    public boolean containsPlayer(@NotNull Player player) {
        for (var diggingEntrySet : this.diggingBlockMap.values()) {
            for (var diggingEntry : diggingEntrySet) {
                if (diggingEntry.player.equals(player)) return true;
            }
        }

        return false;
    }

    /**
     * @return True if the DiggingMap contains no blocks
     */
    public boolean isEmpty() {
        return this.diggingBlockMap.isEmpty();
    }

    /**
     * Clears all blocks and their associated digging entries from
     * the DiggingMap
     */
    public synchronized void clear() {
        this.diggingBlockMap.clear();
    }

    /**
     * @return The total number of blocks present in the DiggingMap
     */
    public int blocksSize() {
        return this.diggingBlockMap.size();
    }

    /**
     * @return The total number of digging entries present in the DiggingMap
     */
    public int entriesSize() {
        return this.diggingBlockMap.values().stream()
                .mapToInt(Set::size)
                .sum();
    }

    /**
     * @param block The block for which to retrieve the associated
     *              digging entries
     * @return The set of digging entries associated with the block
     */
    private @NotNull Set<Entry> entries(@NotNull Block block) {
        return this.diggingBlockMap.getOrDefault(block, ConcurrentHashMap.newKeySet());
    }

    /**
     * The Entry class represents a single digging entry
     * associated with a player and a block in the DiggingMap.
     * It contains information about the player, the stage,
     * and other properties related to the digging process.
     */
    @SuppressWarnings("UnusedReturnValue")
    public static class Entry {
        private final Player player;
        private int taskId;
        private int stage;
        private boolean farAway;

        private Entry(
                @NotNull Player player,
                int taskId,
                int stage,
                boolean farAway
        ) {
            this.player = player;
            this.stage = stage;
            this.taskId = taskId;
            this.farAway = farAway;
        }

        /**
         * Creates a new Entry instance for the specified player with
         * default values for task ID, stage, and farAway. The task ID
         * is required to be set later.
         *
         * @param player The player associated with the digging entry
         * @return A new Entry instance with the specified player
         *         and default values
         */
        @Contract(value = "_ -> new")
        public static @NotNull Entry create(@NotNull Player player) {
            return new Entry(player, -1, 0, false);
        }

        /**
         * Creates a new Entry instance with the specified player
         * and task ID. The stage and farAway properties are set
         * to default values.
         *
         * @param player The player associated with the digging entry
         * @param taskId The task ID associated with the digging entry
         * @return A new Entry instance with the specified player
         *         and task ID
         */
        @Contract(value = "_, _ -> new")
        public static @NotNull Entry create(
                @NotNull Player player,
                int taskId
        ) {
            return new Entry(player, taskId, 0, false);
        }

        /**
         * Creates a new Entry instance with the specified parameters
         *
         * @param player   The player associated with the digging entry
         * @param taskId   The task ID associated with the digging entry
         * @param stage    The current stage of the digging process
         * @param farAway  A boolean indicating whether the player is
         *                 far away from the block being dug
         * @return A new Entry instance with the specified parameters
         */
        @Contract(value = "_, _, _, _ -> new")
        public static @NotNull Entry create(
                @NotNull Player player,
                int taskId,
                int stage,
                boolean farAway
        ) {
            return new Entry(player, taskId, stage, farAway);
        }

        /**
         * @return The player associated with the digging entry
         */
        public @NotNull Player player() {
            return this.player;
        }

        /**
         * @return The task ID associated with the digging entry
         */
        public int taskId() {
            return this.taskId;
        }

        /**
         * Sets the task ID for the digging entry
         *
         * @param taskId The task ID to set for the digging entry
         * @return The current Entry instance with the updated task ID
         */
        public @NotNull Entry taskId(int taskId) {
            this.taskId = taskId;
            return this;
        }

        /**
         * @return The current stage of the digging process
         */
        public int stage() {
            return this.stage;
        }

        /**
         * Sets the current stage for the digging entry
         *
         * @param stage The stage to set for the digging entry
         * @return The current Entry instance with the updated stage
         */
        public @NotNull Entry stage(int stage) {
            this.stage = stage;
            return this;
        }

        /**
         * @return True if the player is considered far away from the block
         */
        public boolean farAway() {
            return this.farAway;
        }

        /**
         * Sets whether the player is considered far away from the block
         * being dug
         *
         * @param farAway A boolean indicating whether the player is far away
         * @return The current Entry instance with the updated farAway value
         */
        public @NotNull Entry farAway(boolean farAway) {
            this.farAway = farAway;
            return this;
        }

        /**
         * Checks if the player's current stage is the biggest stage
         * for the specified block in the DiggingMap
         *
         * @param block The block for which to check if the player's
         *              stage is the biggest
         * @return True if the player's stage is the biggest for the block
         * @throws NullPointerException If the plugin's cache is null,
         *                              that means the plugin is not enabled
         */
        public boolean isStageTheBiggest(@NotNull Block block) throws NullPointerException {
            Entry biggestStageEntry = MSBlock.getCache().diggingMap.getBiggestStageEntry(block);
            return biggestStageEntry != null
                    && (this.equals(biggestStageEntry)
                    || this.stage > biggestStageEntry.stage());
        }

        /**
         * @param obj The object to compare with this Entry
         * @return True if the object is equal to this Entry
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            return obj instanceof Entry entry && entry.taskId == this.taskId;
        }

        /**
         * @return A string representation of this Entry
         */
        @Override
        public @NotNull String toString() {
            return "Entry{" +
                    "player=" + this.player.getName() +
                    ", taskId=" + this.taskId +
                    ", stage=" + this.stage +
                    '}';
        }

        /**
         * Cancels the task associated with this Entry. If the task ID
         * is valid, it cancels the scheduled task using Bukkit's scheduler.
         * After cancellation, the task ID is set to -1.
         *
         * @throws NullPointerException If the server is not running
         * @see BukkitScheduler#cancelTask(int)
         */
        public void cancelTask() throws NullPointerException {
            if (this.taskId != -1) {
                Bukkit.getScheduler().cancelTask(this.taskId);
                this.taskId = -1;
            }
        }
    }
}
