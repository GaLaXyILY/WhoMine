package com.github.minersstudios.msessentials;

import com.github.minersstudios.msessentials.anomalies.Anomaly;
import com.github.minersstudios.msessentials.anomalies.AnomalyAction;
import com.github.minersstudios.msessentials.player.IDMap;
import com.github.minersstudios.msessentials.player.MuteMap;
import com.github.minersstudios.msessentials.player.PlayerInfo;
import com.github.minersstudios.msessentials.player.PlayerInfoMap;
import com.github.minersstudios.msessentials.utils.SignMenu;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache for all custom data
 */
public final class Cache {
    public final PlayerInfoMap playerInfoMap = new PlayerInfoMap();
    public final MuteMap muteMap = new MuteMap();
    public final IDMap idMap = new IDMap();
    public final Map<Player, ArmorStand> seats = new HashMap<>();
    public final Map<NamespacedKey, Anomaly> anomalies = new HashMap<>();
    public final Map<Player, Map<AnomalyAction, Long>> playerAnomalyActionMap = new ConcurrentHashMap<>();
    public final Map<UUID, Queue<String>> chatQueue = new HashMap<>();
    public final Map<Player, SignMenu> signs = new HashMap<>();
    public final List<BukkitTask> bukkitTasks = new ArrayList<>();
    public PlayerInfo consolePlayerInfo;
}
