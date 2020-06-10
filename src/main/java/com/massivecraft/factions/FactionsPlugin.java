package com.massivecraft.factions;

import com.earth2me.essentials.IEssentials;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.massivecraft.factions.cmd.FCmdRoot;
import com.massivecraft.factions.config.ConfigManager;
import com.massivecraft.factions.config.file.MainConfig;
import com.massivecraft.factions.cooldown.StuckCooldown;
import com.massivecraft.factions.data.SaveTask;
import com.massivecraft.factions.data.json.adapters.EnumTypeTypeAdapter;
import com.massivecraft.factions.data.json.adapters.FactionMaterialTypeAdapter;
import com.massivecraft.factions.data.json.adapters.MapFLocToStringSetTypeAdapter;
import com.massivecraft.factions.data.json.adapters.MaterialTypeAdapter;
import com.massivecraft.factions.data.json.adapters.MyLocationTypeAdapter;
import com.massivecraft.factions.data.json.adapters.PermissionsMapTypeAdapter;
import com.massivecraft.factions.data.json.adapters.UUIDTypeAdapter;
import com.massivecraft.factions.integration.ClipPlaceholderAPIManager;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.integration.Essentials;
import com.massivecraft.factions.integration.IWorldguard;
import com.massivecraft.factions.integration.LWC;
import com.massivecraft.factions.integration.Worldguard6;
import com.massivecraft.factions.integration.Worldguard7;
import com.massivecraft.factions.integration.dynmap.EngineDynmap;
import com.massivecraft.factions.io.IOController;
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
import com.massivecraft.factions.logging.FactionsLogger;
import com.massivecraft.factions.meta.BlockVisualizer;
import com.massivecraft.factions.meta.scoreboards.SidebarProvider;
import com.massivecraft.factions.meta.tablist.TablistProvider;
import com.massivecraft.factions.metrics.Metrics;
import com.massivecraft.factions.perms.Permissible;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.struct.ChatMode;
import com.massivecraft.factions.struct.wild.WildManager;
import com.massivecraft.factions.tasks.FlightTask;
import com.massivecraft.factions.tasks.SeeChunkTask;
import com.massivecraft.factions.util.AutoLeaveTask;
import com.massivecraft.factions.util.FormatTransitioner;
import com.massivecraft.factions.util.LazyLocation;
import com.massivecraft.factions.util.PermUtil;
import com.massivecraft.factions.util.Persist;
import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.TextUtil;
import com.massivecraft.factions.util.material.FactionMaterial;
import com.massivecraft.factions.util.material.MaterialDb;
import com.massivecraft.factions.util.particle.BukkitParticleProvider;
import com.massivecraft.factions.util.particle.PacketParticleProvider;
import com.massivecraft.factions.util.particle.ParticleProvider;
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
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.logging.Logger;

public final class FactionsPlugin extends JavaPlugin implements FactionsAPI {

    private static FactionsPlugin instance;

    private Logger logger;
    private Path path;

    private static final MinecraftVersion MINECRAFT_VERSION = MinecraftVersion.getRuntimeVersion();

    private Permission perms = null;

    private final IOController ioController = new IOController();
    private final ConfigManager configManager = new ConfigManager();

    private boolean autoSave = true;

    private boolean loadDataSuccessful = false;
    private boolean loadSettingsSuccessful = false;

    // Some utils
    private Persist persist;

    // Persist related
    private Gson gson;

    private final Map<UUID, StuckCooldown> stuckSessions = new HashMap<>();

    // Persistence related
    private boolean locked = false;

    private AutoLeaveTask autoLeaveTask;

    private boolean hookedPlayervaults;
    private ClipPlaceholderAPIManager clipPlaceholderAPIManager;
    private boolean mvdwPlaceholderAPIManager = false;
    private IWorldguard worldguard;

    private final Set<String> pluginsHandlingChat = new HashSet<>();

    private ParticleProvider<?> particleProvider;
    private BlockVisualizer blockVisualizer;
    private WildManager wildManager;

    private final Set<EntityType> safeZoneNerfedCreatureTypes = EnumSet.noneOf(EntityType.class);
    private LandRaidControl landRaidControl;

    public static final List<String> DEFAULT_COMMAND_BASE = ImmutableList.of("f");

    private Metrics metrics;

    private static final Function<FactionsPlugin, String> ENABLE_BANNER = plugin -> TextUtil.parseAnsi(
            "&f\n" +
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
        this.path = this.getDataFolder().toPath();
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

        this.gson = this.getGsonBuilder().create();

        long settingsStart = System.nanoTime();

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

            if (ess != null && conf().factions().other().isDeleteEssentialsHomes()) {
                Bukkit.getPluginManager().registerEvents(new EssentialsListener(ess), this);
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

                        this.wildManager = new WildManager();
                        this.wildManager.deserialize(this.path.resolve("config").resolve("wild.conf"), loadedWorlds -> {
                            this.logger.info("== Wild Worlds: &7" + loadedWorlds + "&b loaded");
                            this.logger.info("Faction data has been loaded successfully. (&7" + TextUtil.formatDecimal((System.nanoTime() - dataStart) / 1_000_000.0D) + "ms&b)");
                            this.loadDataSuccessful = true;
                        });
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
                    this.metrics = new Metrics();
                    this.metrics.setup(this);
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

    public Path getPath() {
        return path;
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

    public Map<UUID, StuckCooldown> getStuckSessions() {
        return stuckSessions;
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

    public IOController getIOController() {
        return ioController;
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
            this.wildManager.serialize(this.path.resolve("config").resolve("wild.conf"), result -> this.logger.info(" == Wild: Successfully saved all data."));
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

    public WildManager getWildManager() {
        return wildManager;
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
            return Collections.emptySet();
        }
        Set<FPlayer> fPlayers = faction.getFPlayers();
        Set<String> players = new HashSet<>(fPlayers.size());
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
            return Collections.emptySet();
        }
        Set<FPlayer> fPlayers = faction.getFPlayersWhereOnline(true);
        Set<String> players = new HashSet<>(fPlayers.size());
        for (FPlayer fplayer : fPlayers) {
            players.add(fplayer.getName());
        }
        return players;
    }

    @Override
    public Set<Player> getRawOnlinePlayersInFaction(String factionTag) {
        Faction faction = Factions.getInstance().getByTag(factionTag);
        if (faction == null) {
            return Collections.emptySet();
        }
        return new HashSet<>(faction.getOnlinePlayers());
    }

    public boolean hasPlayerVaults() {
        return hookedPlayervaults;
    }

    public Permission getPerms() {
        return perms;
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