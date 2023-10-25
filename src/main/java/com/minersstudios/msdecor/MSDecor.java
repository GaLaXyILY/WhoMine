package com.minersstudios.msdecor;

import com.minersstudios.mscore.plugin.MSPlugin;
import com.minersstudios.msdecor.api.CustomDecorType;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

/**
 * The main class of the MSDecor plugin
 *
 * @see MSPlugin
 */
public final class MSDecor extends MSPlugin<MSDecor> {
    private static MSDecor instance;
    private Config config;
    private Cache cache;
    private CoreProtectAPI coreProtectAPI;

    public MSDecor() {
        instance = this;
    }

    @Override
    public void load() {
        initClass(CustomDecorType.class);
    }

    @Override
    public void enable() {
        this.coreProtectAPI = CoreProtect.getInstance().getAPI();
        this.cache = new Cache();
        this.config = new Config(this, this.getConfigFile());

        this.config.reload();
    }

    /**
     * @return The instance of the plugin
     * @throws NullPointerException If the plugin is not enabled
     */
    public static MSDecor getInstance() throws NullPointerException {
        return instance;
    }

    /**
     * @return The logger of the plugin
     * @throws NullPointerException If the plugin is not enabled
     */
    public static @NotNull Logger logger() throws NullPointerException {
        return instance.getLogger();
    }

    /**
     * @return The component logger of the plugin
     * @throws NullPointerException If the plugin is not enabled
     */
    public static @NotNull ComponentLogger componentLogger() throws NullPointerException {
        return instance.getComponentLogger();
    }

    /**
     * @return The cache of the plugin
     * @throws NullPointerException If the plugin is not enabled
     */
    public static Cache getCache() throws NullPointerException {
        return instance.cache;
    }

    /**
     * @return The configuration of the plugin
     * @throws NullPointerException If the plugin is not enabled
     */
    public static Config getConfiguration() throws NullPointerException {
        return instance.config;
    }

    /**
     * @return The CoreProtectAPI instance
     * @throws NullPointerException If the {@link CoreProtect} is not enabled
     */
    public static CoreProtectAPI getCoreProtectAPI() throws NullPointerException {
        return instance.coreProtectAPI;
    }
}
