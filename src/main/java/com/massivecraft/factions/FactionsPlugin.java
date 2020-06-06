package com.massivecraft.factions;

import com.earth2me.essentials.IEssentials;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.massivecraft.factions.cmd.FCmdRoot;
import com.massivecraft.factions.config.ConfigManager;
import com.massivecraft.factions.config.file.MainConfig;
import com.massivecraft.factions.data.SaveTask;
import com.massivecraft.factions.data.json.adapters.*;
import com.massivecraft.factions.event.FactionCreateEvent;
import com.massivecraft.factions.event.FactionEvent;
import com.massivecraft.factions.event.FactionRelationEvent;
import com.massivecraft.factions.integration.*;
import com.massivecraft.factions.integration.dynmap.EngineDynmap;
import com.massivecraft.factions.landraidcontrol.LandRaidControl;
import com.massivecraft.factions.listeners.*;
import com.massivecraft.factions.listeners.versionspecific.PortalHandler;
import com.massivecraft.factions.listeners.versionspecific.PortalListenerLegacy;
import com.massivecraft.factions.listeners.versionspecific.PortalListener_114;
import com.massivecraft.factions.logging.FactionsLogger;
import com.massivecraft.factions.meta.scoreboards.SidebarProvider;
import com.massivecraft.factions.meta.tablist.TablistProvider;
import com.massivecraft.factions.metrics.Metrics;
import com.massivecraft.factions.perms.Permissible;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.struct.ChatMode;
import com.massivecraft.factions.util.*;
import com.massivecraft.factions.util.material.FactionMaterial;
import com.massivecraft.factions.util.material.MaterialDb;
import com.massivecraft.factions.util.particle.BukkitParticleProvider;
import com.massivecraft.factions.util.particle.PacketParticleProvider;
import com.massivecraft.factions.util.particle.ParticleProvider;
import it.unimi.dsi.fastutil.objects.*;
import me.lucko.helper.reflect.MinecraftVersion;
import me.lucko.helper.reflect.MinecraftVersions;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.Supplier;
import java.util.logging.Logger;

public final class FactionsPlugin extends JavaPlugin implements FactionsAPI {

    private static FactionsPlugin instance;

    private static final MinecraftVersion MINECRAFT_VERSION = MinecraftVersion.getRuntimeVersion();

    private Logger logger;

    private Permission perms = null;

    private final ConfigManager configManager = new ConfigManager();

    private boolean autoSave = true;

    private boolean loadDataSuccessful = false;
    private boolean loadSettingsSuccessful = false;

    // Some utils
    private Persist persist;

    // Persist related
    private Gson gson;

    // holds f stuck start times
    private final Object2LongMap<UUID> timers = new Object2LongOpenHashMap<>();

    //holds f stuck taskids
    private final Object2IntMap<UUID> stuckMap = new Object2IntOpenHashMap<>();

    // Persistence related
    private boolean locked = false;

    private AutoLeaveTask autoLeaveTask;

    private boolean hookedPlayervaults;
    private ClipPlaceholderAPIManager clipPlaceholderAPIManager;
    private boolean mvdwPlaceholderAPIManager = false;
    private IWorldguard worldguard;

    private final ObjectSet<String> pluginsHandlingChat = new ObjectOpenHashSet<>();

    private ParticleProvider<?> particleProvider;
    private BlockVisualizer blockVisualizer;


    private final Set<EntityType> safeZoneNerfedCreatureTypes = EnumSet.noneOf(EntityType.class);
    private LandRaidControl landRaidControl;

    public static final List<String> DEFAULT_COMMAND_BASE = ImmutableList.of("f");

    private Metrics metrics;

    private static final Function<FactionsPlugin, String> ENABLE_BANNER = plugin -> TextUtil.parseAnsi(
            "&f" +
            "                  _____ ___ _____ _   _  _ ___ _   _ __  __ \n" +
            "                 |_   _|_ _|_   _/_\\ | \\| |_ _| | | |  \\/  |\n" +
            "                   | |  | |  | |/ _ \\| .` || || |_| | |\\/| |\n" +
            "                  _|_|_|___|_|_/_/_\\_\\_|\\_|___|\\___/|_|  |_|\n" +
            "                 | __/_\\ / __|_   _|_ _/ _ \\| \\| / __|      \n" +
            "                 | _/ _ \\ (__  | |  | | (_) | .` \\__ \\      \n" +
            "                 |_/_/ \\_\\___| |_| |___\\___/|_|\\_|___/ &b(v" + plugin.getDescription().getVersion() + ")\n"
    );

    public FactionsPlugin() {
        if (instance != null) {
            throw new IllegalStateException("Unable to instantiate new plugin instance");
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
        this.logger = new FactionsLogger(this, "&f[TitaniumFactions] &b");
        if (this.isFinishedLoading()) {
            this.loadSettingsSuccessful = false;
            this.loadDataSuccessful = false;
            FPlayers.getInstance().wipeOnline();
            FlightTask.get().wipe();
        }
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.kickPlayer("Server data reloading...");
        }

        System.out.println(ENABLE_BANNER.apply(this));

        this.logger.info("Server version detected: &7" + getMCVersion().getVersion());

        if (MinecraftVersion.getRuntimeVersion().isBefore(MinecraftVersions.v1_8)) {
            Bukkit.getPluginManager().disablePlugin(this);
            this.logger.severe("Server version is incompatible with &fTitaniumFactions&b. (1.8+)");
            return;
        }
        this.configManager.startup();

        if (!this.loadLang()) {
            return;
        }

        try {
            Files.createDirectories(getDataFolder().toPath().resolve("data"));
        } catch (IOException e) {
            this.logger.severe("Unable to create data directory.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (Bukkit.getOnlineMode()) {
            Bukkit.getPluginManager().registerEvents(new FactionsDataListener(), this);
        }

        long settingsStart = System.nanoTime();

        this.gson = this.getGsonBuilder().create();

        this.loadMaterials(amount -> {
            this.logger.info("Material database updated with &7" + amount + " &bmaterials.");

            Econ.setup();
            LWC.setup();
            setupPermissions();
            loadWorldGuard();

            // Load Conf from disk
            this.setNerfedEntities();

            this.landRaidControl = LandRaidControl.getByName(this.conf().factions().landRaidControl().getSystem());
            this.persist = new Persist();

            TextUtil.init();
            PermUtil.init();

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
            FPlayers.getInstance().load(loadedPlayers -> {
                this.logger.info("== Players: &7" + loadedPlayers + "&b loaded");

                Factions.getInstance().load(loadedFactions -> {
                    this.logger.info("== Factions: &7" + loadedFactions + "&b loaded");
                    for (FPlayer fPlayer : FPlayers.getInstance().getAllFPlayers()) {
                        Faction faction = Factions.getInstance().getFactionById(fPlayer.getFactionIdRaw());
                        if (faction == null) {
                            fPlayer.resetFactionData(false);
                            continue;
                        }
                        faction.addFPlayer(fPlayer);
                    }
                    //board needs to be loaded after factions in order for cleaning to work correctly.
                    Board.getInstance().load(loadedClaims -> {
                        Board.getInstance().clean();


                        this.logger.info("== Claims: &7" + loadedClaims + "&b loaded");

                        //dynmap data needs to be loaded after board in order for display to work correctly.
                        EngineDynmap.getInstance().init();

                        // dataStart up task which runs the autoLeaveAfterDaysOfInactivity routine
                        //task needs to be instantiated after players in order for purging to work correctly
                        startAutoLeaveTask(false);

                        // Grand metrics adventure!
                        //metrics needs to be instantiated after all the data in order for proper data collection
                        //this.setupMetrics();

                        this.logger.info("Faction data has been loaded successfully. (&7" + TextUtil.formatDecimal((System.nanoTime() - dataStart) / 1_000_000.0D) + "ms&b)");
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

            if (getMCVersion().isBefore(MinecraftVersions.v1_13)) {
                this.particleProvider = new PacketParticleProvider();
            } else {
                this.particleProvider = new BukkitParticleProvider();
            }

            this.blockVisualizer = new BlockVisualizer();

            if (this.conf().scoreboard().info().isEnabled()) { //implement a toggle for reload command that enables/disables this
                SidebarProvider.get().start();
            }

            if (this.conf().tablist().isEnabled()) { //implement a toggle for reload command that enables/disables this
                TablistProvider.get().start();
            }

            if (conf().commands().seeChunk().isParticles()) {
                SeeChunkTask.get().start();
            }

            // Register Event Handlers

            // Version specific portal listener check.
            if (getMCVersion().isAfterOrEq(MinecraftVersions.v1_14)) { // Starting with 1.14
                Bukkit.getPluginManager().registerEvents(new PortalListener_114(), this);
            } else {
                Bukkit.getPluginManager().registerEvents(new PortalListenerLegacy(new PortalHandler()), this);
            }

            // since some other plugins execute commands directly through this command interface, provide it
            this.getCommand("factions").setExecutor(new FCmdRoot());

            if (conf().commands().fly().isEnable()) {
                FlightTask.get().start();
            }

            setupPlaceholderAPI();

            this.logger.info("Faction settings have been loaded successfully. (&7" + TextUtil.formatDecimal((System.nanoTime() - settingsStart) / 1_000_000.0D) + "ms&b)");
            this.loadSettingsSuccessful = true; //2nd checkpoint for proper saving when plugin disables

            Bukkit.getScheduler().runTaskLaterAsynchronously(this, () -> {
                if (Bukkit.getPluginManager().isPluginEnabled(this)) {
                    this.setupMetrics();
                }
            }, 20L);
        });
    }

    public MinecraftVersion getMCVersion() {
        return MINECRAFT_VERSION;
    }

    private void loadMaterials(IntConsumer result) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            int loaded = MaterialDb.get().load();
            Bukkit.getScheduler().runTask(this, () -> result.accept(loaded));
        });
    }

    private void setupMetrics() {
        this.metrics = new Metrics();

        this.metricsDrillPie("fuuid_version", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>(1);
            Map<String, Integer> entry = new HashMap<>(1);
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
                Map<String, Map<String, Integer>> map = new HashMap<>(1);
                Map<String, Integer> entry = new HashMap<>(1);
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
            Map<String, Map<String, Integer>> map = new HashMap<>(1);
            Map<String, Integer> entry = new HashMap<>(1);
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
                if (plugin == this) {
                    continue;
                }
                Map<String, Integer> entry = new HashMap<>(1);
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
        Map<String, Map<String, Integer>> map = new HashMap<>(1);
        Map<String, Integer> entry = new HashMap<>(1);
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
                this.logger.info("Support enabled for: &7WorldGuard (" + version + ")");
            }
        }
    }

    public boolean loadLang() {
        Path lang = this.getDataFolder().toPath().resolve("lang.yml");
        if (Files.exists(lang)) {
            try {
                if (!this.conf().lang().isLangTransitioned()) {
                    Files.copy(lang, this.getDataFolder().toPath().resolve("old_lang.yml"), StandardCopyOption.REPLACE_EXISTING);
                    if (FormatTransitioner.get().toIndexed(lang)) {
                        TL.setTransition(true);
                        this.logger.info("Translation file successfully converted.");
                    } else {
                        this.logger.severe("Unable to apply INDEX format to translation file.");
                        Bukkit.getPluginManager().disablePlugin(this);
                        return false;
                    }
                }
            } catch (IOException exception) {
                this.logger.severe("Unable to convert translation file.");
                Bukkit.getPluginManager().disablePlugin(this);
                return false;
            }
        } else {
            this.saveResource("lang.yml", false);
        }
        FileConfiguration yml = YamlConfiguration.loadConfiguration(lang.toFile());
        boolean modified = false;
        for (TL item : TL.VALUES) {
            if (!yml.isSet(item.getPath())) {
                yml.set(item.getPath(), item.getDefault());
                modified = true;
            }
        }
        String type = yml.getString("lang-type");
        if (type != null) {
            if (!type.equalsIgnoreCase(this.conf().lang().getFormatType())) {
                modified = true;
                if (this.conf().lang().getFormatType().equalsIgnoreCase("LOOSE")) {
                    FormatTransitioner.get().toLoose(lang);
                } else {
                    FormatTransitioner.get().toIndexed(lang);
                }
                yml = YamlConfiguration.loadConfiguration(lang.toFile());
            }
        } else {
            yml.set("lang-type", "INDEX");
            modified = true;
        }
        TL.inheritFrom(lang, yml);
        if (modified) {
            try {
                yml.save(lang.toFile());
            } catch (IOException exception) {
                this.logger.severe("Unable to modify translation file.");
                Bukkit.getPluginManager().disablePlugin(this);
                return false;
            }
        }
        return true;
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

    public BlockVisualizer getBlockVisualizer() {
        return this.blockVisualizer;
    }

    public Map<UUID, Integer> getStuckMap() {
        return this.stuckMap;
    }

    public Map<UUID, Long> getTimers() {
        return this.timers;
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

    public ClipPlaceholderAPIManager getClipPlaceholderAPIManager() {
        return clipPlaceholderAPIManager;
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
                .registerTypeAdapter(FactionMaterial.class, new FactionMaterialTypeAdapter())
                .registerTypeAdapter(Material.class, new MaterialTypeAdapter())
                .registerTypeAdapter(new TypeToken<Map<Permissible, Map<PermissibleAction, Boolean>>>(){}.getType(), new PermissionsMapTypeAdapter())
                .registerTypeAdapter(LazyLocation.class, new MyLocationTypeAdapter())
                .registerTypeAdapter(new TypeToken<Map<FLocation, Set<String>>>(){}.getType(), new MapFLocToStringSetTypeAdapter())
                .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
                .registerTypeAdapterFactory(EnumTypeTypeAdapter.ENUM_FACTORY);
    }

    @Override
    public void onDisable() {
        if (this.autoLeaveTask != null) {
            this.autoLeaveTask.close();
            this.autoLeaveTask = null;
        }
        SeeChunkTask.get().close();
        SidebarProvider.get().close();
        SaveTask.get().close();
        this.logger.info("Saving data...");
        if (this.isFinishedLoading()) {
            FPlayers.getInstance().forceSave(result -> this.logger.info(" == Players: Successfully saved all data."));
            Factions.getInstance().forceSave(result -> this.logger.info(" == Factions: Successfully saved all data."));
            Board.getInstance().forceSave(result -> this.logger.info(" == Claims: Successfully saved all data."));
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

    @Override
    public Logger getPluginLogger() {
        return this.logger;
    }

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
            tag = me.getChatTag();
        } else {
            FPlayer you = FPlayers.getInstance().getByPlayer(listener);
            if (you == null) {
                tag = me.getChatTag().trim();
            }
            else { // everything checks out, give the colored tag
                tag = me.getChatTag(you).trim();
            }
        }

        return tag.isEmpty() ? "~" : tag;
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

    public Path getStartupLog() {
        Path logs = Bukkit.getWorldContainer().toPath().resolve("logs");
        if (Files.notExists(logs)) {
            return null;
        }
        return logs.resolve("latest.log");

    }

    public boolean isFinishedLoading() {
        return this.loadDataSuccessful && this.loadSettingsSuccessful;
    }
}
