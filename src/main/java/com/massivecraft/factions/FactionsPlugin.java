package com.massivecraft.factions;

import com.earth2me.essentials.IEssentials;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.massivecraft.factions.cmd.FCmdRoot;
import com.massivecraft.factions.config.ConfigManager;
import com.massivecraft.factions.config.file.MainConfig;
import com.massivecraft.factions.data.SaveTask;
import com.massivecraft.factions.data.json.adapters.EnumTypeAdapter;
import com.massivecraft.factions.data.json.adapters.FactionMaterialAdapter;
import com.massivecraft.factions.data.json.adapters.MapFLocToStringSetTypeAdapter;
import com.massivecraft.factions.data.json.adapters.MaterialAdapter;
import com.massivecraft.factions.data.json.adapters.MyLocationTypeAdapter;
import com.massivecraft.factions.data.json.adapters.UUIDTypeAdapter;
import com.massivecraft.factions.event.FactionCreateEvent;
import com.massivecraft.factions.event.FactionEvent;
import com.massivecraft.factions.event.FactionRelationEvent;
import com.massivecraft.factions.integration.ClipPlaceholderAPIManager;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.integration.Essentials;
import com.massivecraft.factions.integration.IWorldguard;
import com.massivecraft.factions.integration.LWC;
import com.massivecraft.factions.integration.Worldguard6;
import com.massivecraft.factions.integration.Worldguard7;
import com.massivecraft.factions.integration.dynmap.EngineDynmap;
import com.massivecraft.factions.landraidcontrol.LandRaidControl;
import com.massivecraft.factions.listeners.EssentialsListener;
import com.massivecraft.factions.listeners.FactionsBlockListener;
import com.massivecraft.factions.listeners.FactionsChatListener;
import com.massivecraft.factions.listeners.FactionsDataListener;
import com.massivecraft.factions.listeners.FactionsEntityListener;
import com.massivecraft.factions.listeners.FactionsExploitListener;
import com.massivecraft.factions.listeners.FactionsPlayerListener;
import com.massivecraft.factions.listeners.versionspecific.PortalHandler;
import com.massivecraft.factions.listeners.versionspecific.PortalListenerLegacy;
import com.massivecraft.factions.listeners.versionspecific.PortalListener_114;
import com.massivecraft.factions.metrics.Metrics;
import com.massivecraft.factions.perms.Permissible;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.perms.PermissionsMapTypeAdapter;
import com.massivecraft.factions.scoreboards.Sidebar;
import com.massivecraft.factions.struct.ChatMode;
import com.massivecraft.factions.util.AutoLeaveTask;
import com.massivecraft.factions.util.FlightUtil;
import com.massivecraft.factions.util.LazyLocation;
import com.massivecraft.factions.util.PermUtil;
import com.massivecraft.factions.util.Persist;
import com.massivecraft.factions.util.SeeChunkTask;
import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.TextUtil;
import com.massivecraft.factions.util.material.FactionMaterial;
import com.massivecraft.factions.util.material.MaterialDb;
import com.massivecraft.factions.util.particle.PacketParticleProvider;
import com.massivecraft.factions.util.particle.ParticleProvider;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import me.lucko.helper.reflect.MinecraftVersion;
import me.lucko.helper.reflect.MinecraftVersions;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.function.IntConsumer;
import java.util.function.Supplier;
import java.util.logging.Level;

public class FactionsPlugin extends JavaPlugin implements FactionsAPI {

    private static FactionsPlugin instance;

    private static final MinecraftVersion MINECRAFT_VERSION = MinecraftVersion.getRuntimeVersion();

    private Permission perms = null;

    private ConfigManager configManager = new ConfigManager();

    private boolean autoSave = true;

    private boolean loadDataSuccessful = false;
    private boolean loadSettingsSuccessful = false;

    // Some utils
    private Persist persist;

    // Persist related
    private Gson gson;

    // holds f stuck start times
    private Object2LongMap<UUID> timers = new Object2LongOpenHashMap<>();

    //holds f stuck taskids
    private Object2IntMap<UUID> stuckMap = new Object2IntOpenHashMap<>();

    // Persistence related
    private boolean locked = false;

    private AutoLeaveTask autoLeaveTask;

    private boolean hookedPlayervaults;
    private ClipPlaceholderAPIManager clipPlaceholderAPIManager;
    private boolean mvdwPlaceholderAPIManager = false;
    private ObjectSet<String> pluginsHandlingChat = new ObjectOpenHashSet<>();

    private ParticleProvider<?> particleProvider;
    private IWorldguard worldguard;
    private Set<EntityType> safeZoneNerfedCreatureTypes = EnumSet.noneOf(EntityType.class);
    private LandRaidControl landRaidControl;

    private Metrics metrics;

    public FactionsPlugin() {
        if (instance != null) {
            return;
        }
        synchronized (FactionsPlugin.class) {
            if (instance == null) {
                instance = this;
            }
        }
    }

    public static FactionsPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        if (this.isFinishedLoading()) {
            this.loadSettingsSuccessful = false;
            this.loadDataSuccessful = false;
            FPlayers.getInstance().wipeOnline();
            FlightUtil.getInstance().wipe();
        }
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.kickPlayer("Server data reloading...");
        }
        getLogger().info("=== Starting up! ===");

        // Ensure basefolder exists!

        getLogger().info("Running Minecraft version: " + getMCVersion().getVersion());

        if (MinecraftVersion.getRuntimeVersion().isBefore(MinecraftVersions.v1_8)) {
            Bukkit.getPluginManager().disablePlugin(this);
            getLogger().info("1.8 is the minimum required version for Factions to run.");
            return;
        }

        Bukkit.getPluginManager().registerEvents(new FactionsDataListener(), this);

        try {
            Files.createDirectories(getDataFolder().toPath().resolve("data"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        long settingsStart = System.nanoTime();

        getLogger().info("");

        this.gson = this.getGsonBuilder().create();

        this.loadMaterials(amount -> {
            getLogger().info("Loaded " + amount + " material mappings.");
            loadLang(result -> {
                if (!result) {
                    Bukkit.getPluginManager().disablePlugin(this);
                    return;
                }

                Econ.setup();
                LWC.setup();
                setupPermissions();
                loadWorldGuard();

                // Load Conf from disk
                this.setNerfedEntities();
                this.configManager.startup();

                this.landRaidControl = LandRaidControl.getByName(this.conf().factions().landRaidControl().getSystem());
                this.persist = new Persist();

                TextUtil.init();
                PermUtil.init();

                // attempt to get first command defined in plugin.yml as reference command, if any commands are defined in there
                // reference command will be used to prevent "unknown command" console messages
                String refCommand = "";
                try {
                    Map<String, Map<String, Object>> refCmd = this.getDescription().getCommands();
                    if (!refCmd.isEmpty()) {
                        refCommand = (String) (refCmd.keySet().toArray()[0]);
                    }
                } catch (ClassCastException ignored) {}

                // Register recurring tasks
                if (this.conf().factions().other().getSaveToFileEveryXMinutes() > 0.0) {
                    SaveTask.get().start();
                }
                // Check for Essentials
                IEssentials ess = Essentials.setup();

                if (ess != null) {
                    if (conf().factions().other().isDeleteEssentialsHomes()) {
                        Bukkit.getPluginManager().registerEvents(new EssentialsListener(ess), this);
                    }
                }

                hookedPlayervaults = setupPlayervaults();

                long dataStart = System.nanoTime();
                getLogger().info("Loading player data...");
                FPlayers.getInstance().load(loadedPlayers -> {
                    getLogger().info("Loaded data for " + loadedPlayers + " players.");

                    getLogger().info("Loading faction data...");
                    Factions.getInstance().load(loadedFactions -> {
                        getLogger().info("Loaded data for " + loadedFactions + " factions.");
                        for (FPlayer fPlayer : FPlayers.getInstance().getAllFPlayers()) {
                            Faction faction = Factions.getInstance().getFactionById(fPlayer.getFactionIdRaw());
                            if (faction == null) {
                                log("Invalid faction id on " + fPlayer.getName() + ":" + fPlayer.getFactionIdRaw());
                                fPlayer.resetFactionData(false);
                                continue;
                            }
                            faction.addFPlayer(fPlayer);
                        }
                        //board needs to be loaded after factions in order for cleaning to work correctly.
                        getLogger().info("Loading claim data...");
                        Board.getInstance().load(loadedClaims -> {
                            Board.getInstance().clean();
                            getLogger().info("Loaded data for " + loadedClaims + " claims.");

                            //dynmap data needs to be loaded after board in order for display to work correctly.
                            EngineDynmap.getInstance().init();

                            // dataStart up task which runs the autoLeaveAfterDaysOfInactivity routine
                            //task needs to be instantiated after players in order for purging to work correctly
                            startAutoLeaveTask(false);

                            // Grand metrics adventure!
                            //metrics needs to be instantiated after all the data in order for proper data collection
                            this.setupMetrics();

                            getLogger().info("Loaded all faction related data in " + ((System.nanoTime() - dataStart) / 1000000.0D) + "ms.");
                            loadDataSuccessful = true; //1st checkpoint for proper saving when plugin disables
                        });
                    });
                });
                // Add Base Commands

                Bukkit.getPluginManager().registerEvents(new FactionsPlayerListener(), this);
                Bukkit.getPluginManager().registerEvents(new FactionsChatListener(), this);
                Bukkit.getPluginManager().registerEvents(new FactionsEntityListener(), this);
                Bukkit.getPluginManager().registerEvents(new FactionsExploitListener(), this);
                Bukkit.getPluginManager().registerEvents(new FactionsBlockListener(), this);

                particleProvider = new PacketParticleProvider();
                Sidebar.get().start();

/*            // Run before initializing listeners to handle reloads properly.
            if (mcVersion < 1300) { // Before 1.13
                particleProvider = new PacketParticleProvider();
            } else {
                particleProvider = new BukkitParticleProvider();
            }*/

                if (conf().commands().seeChunk().isParticles()) {
                    SeeChunkTask.get().start();
                }
                // End run before registering event handlers.

                // Register Event Handlers

                // Version specific portal listener check.
                if (getMCVersion().isAfterOrEq(MinecraftVersions.v1_14)) { // Starting with 1.14
                    Bukkit.getPluginManager().registerEvents(new PortalListener_114(), this);
                } else {
                    Bukkit.getPluginManager().registerEvents(new PortalListenerLegacy(new PortalHandler()), this);
                }

                // since some other plugins execute commands directly through this command interface, provide it
                this.getCommand(refCommand).setExecutor(new FCmdRoot());

                if (conf().commands().fly().isEnable()) {
                    FlightUtil.getInstance().start();
                }

                setupPlaceholderAPI();

                if (ChatColor.stripColor(TL.NOFACTION_PREFIX.toString()).equals("[4-]")) {
                    getLogger().warning("Looks like you have an old, mistaken 'nofactions-prefix' in your lang.yml. It currently displays [4-] which is... strange.");
                }

                getLogger().info("=== Loaded factions settings in " + ((System.nanoTime() - settingsStart) / 1000000.0D) + "ms! ===");
                this.loadSettingsSuccessful = true; //2nd checkpoint for proper saving when plugin disables
            });
        });
    }

    public MinecraftVersion getMCVersion() {
        return MINECRAFT_VERSION;
    }

    private void loadMaterials(IntConsumer result) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            int loaded = MaterialDb.load();
            Bukkit.getScheduler().runTask(this, () -> result.accept(loaded));
        });
    }

    private void setupMetrics() {
        this.metrics = new Metrics();

        this.metricsDrillPie("fuuid_version", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            Map<String, Integer> entry = new HashMap<>();
            entry.put(this.getDescription().getVersion(), 1);
            map.put(this.getDescription().getVersion(), entry);
            return map;
        });

        // Essentials
        Plugin ess = Essentials.getEssentials();
        this.metricsDrillPie("essentials", () -> this.metricsPluginInfo(ess));
        if (ess != null) {
            this.metricsSimplePie("essentials_delete_homes", () -> "" + conf().factions().other().isDeleteEssentialsHomes());
            this.metricsSimplePie("essentials_home_teleport", () -> "" + this.conf().factions().homes().isTeleportCommandEssentialsIntegration());
        }

        // LWC
        Plugin lwc = LWC.getLWC();
        this.metricsDrillPie("lwc", () -> this.metricsPluginInfo(lwc));
        if (lwc != null) {
            boolean enabled = conf().lwc().isEnabled();
            this.metricsSimplePie("lwc_integration", () -> "" + enabled);
            if (enabled) {
                this.metricsSimplePie("lwc_reset_locks_unclaim", () -> "" + conf().lwc().isResetLocksOnUnclaim());
                this.metricsSimplePie("lwc_reset_locks_capture", () -> "" + conf().lwc().isResetLocksOnCapture());
            }
        }

        // Vault
        Plugin vault = Bukkit.getPluginManager().getPlugin("Vault");
        this.metricsDrillPie("vault", () -> this.metricsPluginInfo(vault));
        if (vault != null) {
            this.metricsDrillPie("vault_perms", () -> this.metricsInfo(perms, perms::getName));
            this.metricsDrillPie("vault_econ", () -> {
                Map<String, Map<String, Integer>> map = new HashMap<>();
                Map<String, Integer> entry = new HashMap<>();
                entry.put(Econ.getEcon() == null ? "none" : Econ.getEcon().getName(), 1);
                map.put((this.conf().economy().isEnabled() && Econ.getEcon() != null) ? "enabled" : "disabled", entry);
                return map;
            });
        }

        // WorldGuard
        IWorldguard wg = this.getWorldguard();
        String wgVersion = wg == null ? "nope" : wg.getVersion();
        this.metricsDrillPie("worldguard", () -> this.metricsInfo(wg, () -> wgVersion));

        // Dynmap
        String dynmapVersion = EngineDynmap.getInstance().getVersion();
        boolean dynmapEnabled = EngineDynmap.getInstance().isRunning();
        this.metricsDrillPie("dynmap", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            Map<String, Integer> entry = new HashMap<>();
            entry.put(dynmapVersion == null ? "none" : dynmapVersion, 1);
            map.put(dynmapEnabled ? "enabled" : "disabled", entry);
            return map;
        });

        // Clip Placeholder
        Plugin clipPlugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        this.metricsDrillPie("clipplaceholder", () -> this.metricsPluginInfo(clipPlugin));

        // MVdW Placeholder
        Plugin mvdw = Bukkit.getPluginManager().getPlugin("MVdWPlaceholderAPI");
        this.metricsDrillPie("mvdwplaceholder", () -> this.metricsPluginInfo(mvdw));

        // Overall stats
        this.metricsLine("factions", () -> Factions.getInstance().getAllFactions().size() - 3);
        this.metricsSimplePie("scoreboard", () -> "" + conf().scoreboard().constant().isEnabled());

        // Event listeners
        this.metricsDrillPie("event_listeners", () -> {
            Set<Plugin> pluginsListening = this.getPlugins(FactionEvent.getHandlerList(), FactionCreateEvent.getHandlerList(), FactionRelationEvent.getHandlerList());
            Map<String, Map<String, Integer>> map = new HashMap<>();
            for (Plugin plugin : pluginsListening) {
                if (plugin.getName().equalsIgnoreCase("factions")) {
                    continue;
                }
                Map<String, Integer> entry = new HashMap<>();
                entry.put(plugin.getDescription().getVersion(), 1);
                map.put(plugin.getName(), entry);
            }
            return map;
        });
    }

    private Set<Plugin> getPlugins(HandlerList... handlerLists) {
        Set<Plugin> plugins = new HashSet<>(handlerLists.length);
        for (HandlerList handlerList : handlerLists) {
            plugins.addAll(this.getPlugins(handlerList));
        }
        return plugins;
    }

    private Set<Plugin> getPlugins(HandlerList handlerList) {
        RegisteredListener[] listeners = handlerList.getRegisteredListeners();
        Set<Plugin> plugins = new HashSet<>(listeners.length);
        for (RegisteredListener registeredListener : listeners) {
            plugins.add(registeredListener.getPlugin());
        }
        return plugins;
    }

    private void metricsLine(String name, Callable<Integer> callable) {
        this.metrics.addCustomChart(new Metrics.SingleLineChart(name, callable));
    }

    private void metricsDrillPie(String name, Callable<Map<String, Map<String, Integer>>> callable) {
        this.metrics.addCustomChart(new Metrics.DrilldownPie(name, callable));
    }

    private void metricsSimplePie(String name, Callable<String> callable) {
        this.metrics.addCustomChart(new Metrics.SimplePie(name, callable));
    }

    private Map<String, Map<String, Integer>> metricsPluginInfo(Plugin plugin) {
        return this.metricsInfo(plugin, () -> plugin.getDescription().getVersion());
    }

    private Map<String, Map<String, Integer>> metricsInfo(Object plugin, Supplier<String> versionGetter) {
        Map<String, Map<String, Integer>> map = new HashMap<>();
        Map<String, Integer> entry = new HashMap<>();
        entry.put(plugin == null ? "nope" : versionGetter.get(), 1);
        map.put(plugin == null ? "absent" : "present", entry);
        return map;
    }

    private void setNerfedEntities() {
        safeZoneNerfedCreatureTypes.add(EntityType.BLAZE);
        safeZoneNerfedCreatureTypes.add(EntityType.CAVE_SPIDER);
        safeZoneNerfedCreatureTypes.add(EntityType.CREEPER);
        safeZoneNerfedCreatureTypes.add(EntityType.ENDER_DRAGON);
        safeZoneNerfedCreatureTypes.add(EntityType.ENDERMITE);
        safeZoneNerfedCreatureTypes.add(EntityType.ENDERMAN);
        safeZoneNerfedCreatureTypes.add(EntityType.GHAST);
        safeZoneNerfedCreatureTypes.add(EntityType.GUARDIAN);
        safeZoneNerfedCreatureTypes.add(EntityType.MAGMA_CUBE);
        safeZoneNerfedCreatureTypes.add(EntityType.PIG_ZOMBIE);
        safeZoneNerfedCreatureTypes.add(EntityType.SILVERFISH);
        safeZoneNerfedCreatureTypes.add(EntityType.SKELETON);
        safeZoneNerfedCreatureTypes.add(EntityType.SPIDER);
        safeZoneNerfedCreatureTypes.add(EntityType.SLIME);
        safeZoneNerfedCreatureTypes.add(EntityType.WITCH);
        safeZoneNerfedCreatureTypes.add(EntityType.WITHER);
        safeZoneNerfedCreatureTypes.add(EntityType.ZOMBIE);
        if (getMCVersion().isAfterOrEq(MinecraftVersions.v1_9)) {
            safeZoneNerfedCreatureTypes.add(EntityType.SHULKER);
        }
        if (getMCVersion().isAfterOrEq(MinecraftVersions.v1_10)) {
            safeZoneNerfedCreatureTypes.add(EntityType.HUSK);
            safeZoneNerfedCreatureTypes.add(EntityType.STRAY);
        }
        if (getMCVersion().isAfterOrEq(MinecraftVersions.v1_11)) {
            safeZoneNerfedCreatureTypes.add(EntityType.ELDER_GUARDIAN);
            safeZoneNerfedCreatureTypes.add(EntityType.EVOKER);
            safeZoneNerfedCreatureTypes.add(EntityType.VEX);
            safeZoneNerfedCreatureTypes.add(EntityType.VINDICATOR);
            safeZoneNerfedCreatureTypes.add(EntityType.ZOMBIE_VILLAGER);
        }
        if (getMCVersion().isAfterOrEq(MinecraftVersions.v1_13)) {
            safeZoneNerfedCreatureTypes.add(EntityType.DROWNED);
            safeZoneNerfedCreatureTypes.add(EntityType.PHANTOM);
        }
        if (getMCVersion().isAfterOrEq(MinecraftVersions.v1_14)) {
            safeZoneNerfedCreatureTypes.add(EntityType.PILLAGER);
            safeZoneNerfedCreatureTypes.add(EntityType.RAVAGER);
        }
        if (getMCVersion().isAfterOrEq(MinecraftVersions.v1_15)) {
            safeZoneNerfedCreatureTypes.add(EntityType.BEE);
        }
    }

    private void loadWorldGuard() {
        if (!this.conf().worldGuard().isChecking() && !this.conf().worldGuard().isBuildPriority()) {
            return;
        }
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (plugin != null) {
            String version = plugin.getDescription().getVersion();
            if (version.startsWith("6")) {
                this.worldguard = new Worldguard6(plugin);
            } else if (version.startsWith("7")) {
                this.worldguard = new Worldguard7();
            }
            if (this.worldguard != null) {
                this.getLogger().info("WorldGuard support enabled.");
            }
        }
    }

    public void loadLang(BooleanConsumer result) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            File lang = new File(this.getDataFolder(), "lang.yml");
            if (!lang.exists()) {
                this.saveResource("lang.yml", false);
            }
            YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);
            for (TL item : TL.VALUES) {
                if (conf.getString(item.getPath()) == null) {
                    conf.set(item.getPath(), item.getDefault());
                }
            }

            // Remove this here because I'm sick of dealing with bug reports due to bad decisions on my part.
            if (conf.getString(TL.COMMAND_SHOW_POWER.getPath(), "").contains("%5$s")) {
                conf.set(TL.COMMAND_SHOW_POWER.getPath(), TL.COMMAND_SHOW_POWER.getDefault());
                log(Level.INFO, "Removed errant format specifier from f show power.");
            }

            TL.setFile(conf);
            try {
                conf.save(lang);
                Bukkit.getScheduler().runTask(this, () -> result.accept(true));
            } catch (IOException e) {
                FactionsPlugin.getInstance().getLogger().log(Level.SEVERE, "Failed to save lang.yml", e);
                Bukkit.getScheduler().runTask(this, () -> result.accept(false));
            }
        });
    }

    public Persist getPersist() {
        return persist;
    }

    public Gson getGson() {
        return gson;
    }

    public ParticleProvider getParticleProvider() {
        return particleProvider;
    }

    public Map<UUID, Integer> getStuckMap() {
        return this.stuckMap;
    }

    public Map<UUID, Long> getTimers() {
        return this.timers;
    }

    // -------------------------------------------- //
    // LOGGING
    // -------------------------------------------- //
    public void log(String msg) {
        log(Level.INFO, msg);
    }

    public void log(String str, Object... args) {
        log(Level.INFO, TextUtil.parse(str, args));
    }

    public void log(Level level, String str, Object... args) {
        log(level, TextUtil.parse(str, args));
    }

    public void log(Level level, String msg) {
        this.getLogger().log(level, msg);
    }

    public boolean getLocked() {
        return this.locked;
    }

    public void setLocked(boolean val) {
        this.locked = val;
        this.setAutoSave(val);
    }

    public boolean getAutoSave() {
        return this.autoSave;
    }

    public void setAutoSave(boolean val) {
        this.autoSave = val;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    public MainConfig conf() {
        return this.configManager.getMainConfig();
    }

    public LandRaidControl getLandRaidControl() {
        return this.landRaidControl;
    }

    public Set<EntityType> getSafeZoneNerfedCreatureTypes() {
        return safeZoneNerfedCreatureTypes;
    }

    public IWorldguard getWorldguard() {
        return this.worldguard;
    }

    private void setupPlaceholderAPI() {
        Plugin clip = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if (clip != null && clip.isEnabled()) {
            this.clipPlaceholderAPIManager = new ClipPlaceholderAPIManager();
            this.clipPlaceholderAPIManager.register();
        }

        Plugin mvdw = Bukkit.getPluginManager().getPlugin("MVdWPlaceholderAPI");
        if (mvdw != null && mvdw.isEnabled()) {
            this.mvdwPlaceholderAPIManager = true;
        }
    }

    public boolean isClipPlaceholderAPIHooked() {
        return this.clipPlaceholderAPIManager != null;
    }

    public boolean isMVdWPlaceholderAPIHooked() {
        return this.mvdwPlaceholderAPIManager;
    }

    private boolean setupPermissions() {
        try {
            RegisteredServiceProvider<Permission> rsp = Bukkit.getServicesManager().getRegistration(Permission.class);
            if (rsp != null) {
                perms = rsp.getProvider();
            }
        } catch (NoClassDefFoundError ex) {
            return false;
        }
        return perms != null;
    }

    private boolean setupPlayervaults() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("PlayerVaults");
        return plugin != null && plugin.isEnabled();
    }

    private GsonBuilder getGsonBuilder() {
        GsonBuilder builder = new GsonBuilder();

        if (!this.conf().data().json().useEfficientStorage()) {
            builder.setPrettyPrinting();
        }

        return builder
                .disableHtmlEscaping()
                .enableComplexMapKeySerialization()
                .excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.VOLATILE)
                .registerTypeAdapter(FactionMaterial.class, new FactionMaterialAdapter())
                .registerTypeAdapter(Material.class, new MaterialAdapter())
                .registerTypeAdapter(new TypeToken<Map<Permissible, Map<PermissibleAction, Boolean>>>(){}.getType(), new PermissionsMapTypeAdapter())
                .registerTypeAdapter(LazyLocation.class, new MyLocationTypeAdapter())
                .registerTypeAdapter(new TypeToken<Map<FLocation, Set<String>>>(){}.getType(), new MapFLocToStringSetTypeAdapter())
                .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
                .registerTypeAdapterFactory(EnumTypeAdapter.ENUM_FACTORY);
    }

    @Override
    public void onDisable() {
        if (this.autoLeaveTask != null) {
            this.autoLeaveTask.close();
            this.autoLeaveTask = null;
        }
        SeeChunkTask.get().close();
        Sidebar.get().close();
        SaveTask.get().close();
        // only save data if plugin actually loaded successfully
        log("Saving data...");
        if (this.isFinishedLoading()) {
            Factions.getInstance().forceSave(result -> log("Faction data saved."));
            FPlayers.getInstance().forceSave(result -> log("Player data saved."));
            Board.getInstance().forceSave(result -> log("Claim data saved."));
        }
    }

    public void startAutoLeaveTask(boolean restartIfRunning) {
        if (this.autoLeaveTask != null) {
            if (!restartIfRunning) {
                return;
            }
            this.autoLeaveTask.close();
        }
        if (this.conf().factions().other().getAutoLeaveRoutineRunsEveryXMinutes() > 0.0) {
            this.autoLeaveTask = new AutoLeaveTask();
            this.autoLeaveTask.start();
        }
    }

    public boolean logPlayerCommands() {
        return this.conf().logging().isPlayerCommands();
    }

    // -------------------------------------------- //
    // Functions for other plugins to hook into
    // -------------------------------------------- //

    // This value will be updated whenever new hooks are added
    @Override
    public int getAPIVersion() {
        // Updated from 4 to 5 for version 0.5.0
        return 4;
    }

    // If another plugin is handling insertion of chat tags, this should be used to notify Factions
    @Override
    public void setHandlingChat(Plugin plugin, boolean handling) {
        if (plugin == null) {
            throw new IllegalArgumentException("Null plugin!");
        }
        if (plugin == this) {
            throw new IllegalArgumentException("Nice try, but this plugin isn't going to register itself!");
        }
        if (handling) {
            this.pluginsHandlingChat.add(plugin.getName());
        } else {
            this.pluginsHandlingChat.remove(plugin.getName());
        }
    }

    @Override
    public boolean isAnotherPluginHandlingChat() {
        return this.conf().factions().chat().isTagHandledByAnotherPlugin() || !this.pluginsHandlingChat.isEmpty();
    }

    // Simply put, should this chat event be left for Factions to handle? For now, that means players with Faction Chat
    // enabled or use of the Factions f command without a slash; combination of isPlayerFactionChatting() and isFactionsCommand()

    @Override
    public boolean shouldLetFactionsHandleThisChat(AsyncPlayerChatEvent event) {
        return event != null && (isPlayerFactionChatting(event.getPlayer()) || isFactionsCommand(event.getMessage()));
    }

    // Does player have Faction Chat enabled? If so, chat plugins should preferably not do channels,
    // local chat, or anything else which targets individual recipients, so Faction Chat can be done
    @Override
    public boolean isPlayerFactionChatting(Player player) {
        if (player == null) {
            return false;
        }
        FPlayer me = FPlayers.getInstance().getByPlayer(player);

        return me != null && me.getChatMode().isAtLeast(ChatMode.ALLIANCE);
    }

    // Is this chat message actually a Factions command, and thus should be left alone by other plugins?

    // TODO: GET THIS BACK AND WORKING

    public boolean isFactionsCommand(String check) {
        return !(check == null || check.isEmpty()); //&& this.handleCommand(null, check, true);
    }

    // Get a player's faction tag (faction name), mainly for usage by chat plugins for local/channel chat
    @Override
    public String getPlayerFactionTag(Player player) {
        return getPlayerFactionTagRelation(player, null);
    }

    // Same as above, but with relation (enemy/neutral/ally) coloring potentially added to the tag
    @Override
    public String getPlayerFactionTagRelation(Player speaker, Player listener) {
        String tag = "~";

        if (speaker == null) {
            return tag;
        }

        FPlayer me = FPlayers.getInstance().getByPlayer(speaker);
        if (me == null) {
            return tag;
        }

        // if listener isn't set, or config option is disabled, give back uncolored tag
        if (listener == null || !this.conf().factions().chat().isTagRelationColored()) {
            tag = me.getChatTag().trim();
        } else {
            FPlayer you = FPlayers.getInstance().getByPlayer(listener);
            if (you == null) {
                tag = me.getChatTag().trim();
            }
            else { // everything checks out, give the colored tag
                tag = me.getChatTag(you).trim();
            }
        }
        if (tag.isEmpty()) {
            tag = "~";
        }

        return tag;
    }

    // Get a player's title within their faction, mainly for usage by chat plugins for local/channel chat
    @Override
    public String getPlayerTitle(Player player) {
        if (player == null) {
            return "";
        }

        FPlayer me = FPlayers.getInstance().getByPlayer(player);
        if (me == null) {
            return "";
        }

        return me.getTitle().trim();
    }

    // Get a list of all faction tags (names)
    @Override
    public Set<String> getFactionTags() {
        return Factions.getInstance().getFactionTags();
    }

    // Get a list of all players in the specified faction
    @Override
    public Set<String> getPlayersInFaction(String factionTag) {
        Faction faction = Factions.getInstance().getByTag(factionTag);
        if (faction == null) {
            return ObjectSets.emptySet();
        }
        Set<FPlayer> fPlayers = faction.getFPlayers();
        ObjectSet<String> players = new ObjectOpenHashSet<>(fPlayers.size());
        for (FPlayer fplayer : fPlayers) {
            players.add(fplayer.getName());
        }
        return players;
    }

    // Get a list of all online players in the specified faction
    @Deprecated
    @Override
    public Set<String> getOnlinePlayersInFaction(String factionTag) {
        Faction faction = Factions.getInstance().getByTag(factionTag);
        if (faction == null) {
            return ObjectSets.emptySet();
        }
        Set<FPlayer> fPlayers = faction.getFPlayersWhereOnline(true);
        ObjectSet<String> players = new ObjectOpenHashSet<>(fPlayers.size());
        for (FPlayer fplayer : fPlayers) {
            players.add(fplayer.getName());
        }
        return players;
    }

    @Override
    public Set<Player> getRawOnlinePlayersInFaction(String factionTag) {
        Faction faction = Factions.getInstance().getByTag(factionTag);
        if (faction == null) {
            return ObjectSets.emptySet();
        }
        return new ObjectOpenHashSet<>(faction.getOnlinePlayers());
    }

    public boolean isHookedPlayervaults() {
        return hookedPlayervaults;
    }

    public String getPrimaryGroup(OfflinePlayer player) {
        return perms == null || !perms.hasGroupSupport() ? " " : perms.getPrimaryGroup(Bukkit.getWorlds().get(0).toString(), player);
    }

    public boolean isFinishedLoading() {
        return this.loadDataSuccessful && this.loadSettingsSuccessful;
    }

    public void debug(Level level, String s) {
        if (conf().getaVeryFriendlyFactionsConfig().isDebug()) {
            getLogger().log(level, s);
        }
    }

    public void debug(String s) {
        debug(Level.INFO, s);
    }
}
