package com.massivecraft.factions.cmd;

import com.google.common.base.Charsets;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.kitteh.pastegg.PasteBuilder;
import org.kitteh.pastegg.PasteContent;
import org.kitteh.pastegg.PasteFile;
import org.kitteh.pastegg.Visibility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.regex.Pattern;

public class CmdDebug extends FCommand {

    private static final String BASE = "https://paste.gg/anonymous/";

    private static final Pattern SERVER_PROPERTIES_EXEMPTION_PATTERN = Pattern.compile("(?:(?:server-ip=)|(?:server-port=)|(?:rcon\\.port=)|(?:rcon\\.password=)|(?:query.port=))[^\n]*[\r\n]*");
    private static final Pattern NEW_LINE_PATTERN = Pattern.compile(System.lineSeparator());

    private boolean running;

    public CmdDebug() {
        super();
        this.aliases.add("debug");
        this.aliases.add("helpme");
        this.optionalArgs.put("mini/full", "full");

        this.requirements = new CommandRequirements.Builder(Permission.DEBUG).build();
    }

    @Override
    public void perform(CommandContext context) {
        String newLine = System.lineSeparator();
        StringBuilder mainInfo = new StringBuilder();
        mainInfo.append(Bukkit.getName()).append(" version: ").append(Bukkit.getVersion()).append(newLine);
        mainInfo.append("Plugin version: ").append(this.plugin.getDescription().getVersion()).append(newLine);
        mainInfo.append("Java version: ").append(System.getProperty("java.version")).append(newLine);
        if (!context.args.isEmpty() && context.argAsString(0).equalsIgnoreCase("mini")) {
            for (String string : NEW_LINE_PATTERN.split(mainInfo.toString())) {
                context.msg(string);
            }
            return;
        }
        if (this.running) {
            context.msg(TL.COMMAND_DEBUG_ALREADY_RUNNING);
            return;
        }
        this.running = true;
        mainInfo.append(newLine);
        mainInfo.append("Plugins:").append(newLine);
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            mainInfo.append(' ').append(plugin.getName()).append(" - ").append(plugin.getDescription().getVersion()).append(newLine);
            mainInfo.append("  ").append(plugin.getDescription().getAuthors()).append(newLine);
        }
        StringBuilder permInfo = new StringBuilder(FPlayers.getInstance().onlineSize() * Permission.VALUES.length * 2);
        for (Player player : Bukkit.getOnlinePlayers()) {
            permInfo.append(player.getName()).append(newLine);
            for (Permission permission : Permission.VALUES) {
                permInfo.append(' ');
                if (player.hasPermission(permission.toString())) {
                    permInfo.append('\u2713');
                } else {
                    permInfo.append('\u2715');
                }
                permInfo.append(permission.toString()).append(newLine);
            }
            permInfo.append(newLine);
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            private final PasteBuilder builder = new PasteBuilder()
                    .name("TitaniumFactions (" + plugin.getDescription().getVersion() + ") - Debug Report")
                    .visibility(Visibility.UNLISTED)
                    .expires(ZonedDateTime.now(ZoneOffset.UTC).plusDays(3));

            private void add(String name, String content) {
                this.builder.addFile(new PasteFile(name, new PasteContent(PasteContent.ContentType.TEXT, content)));
            }

            private String getFile(Path file) {
                try {
                    return new String(Files.readAllBytes(file), Charsets.UTF_8);
                } catch (IOException exception) {
                    return ExceptionUtils.getFullStackTrace(exception);
                }
            }

            @Override
            public void run() {
                try {
                    Path dataPath = plugin.getDataFolder().toPath();
                    add("info.txt", mainInfo.toString());
                    Path latestLog = plugin.getStartupLog();
                    if (latestLog != null && Files.exists(latestLog)) {
                        add("latest.log", getFile(latestLog));
                    }
                    add("server.properties", SERVER_PROPERTIES_EXEMPTION_PATTERN.matcher(getFile(Paths.get("server.properties"))).replaceAll(""));
                    add("main.conf", getFile(dataPath.resolve("config/main.conf")));
                    add("spigot.yml", getFile(Paths.get("spigot.yml")));
                    if (permInfo.length() > 0) {
                        add("perms.txt", permInfo.toString());
                    }
                    PasteBuilder.PasteResult result = builder.build();
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        if (result.getPaste().isPresent()) {
                            String complete = TL.COMMAND_DEBUG_COMPLETE.format(BASE + result.getPaste().get().getId());
                            String deletionKey = TL.COMMAND_DEBUG_DELETIONKEY.format(result.getPaste().get().getDeletionKey().orElse("No deletion key"));

                            context.msg(complete);
                            context.msg(deletionKey);
                            if (context.player != null) {
                                plugin.getLogger().info(complete);
                                plugin.getLogger().info(deletionKey);
                            }
                        } else {
                            context.msg(TL.COMMAND_DEBUG_FAIL);
                            plugin.getLogger().warning("Received: " + result.getMessage());
                        }
                        running = false;
                    });
                } catch (Exception e) {
                    running = false;

                    plugin.getPluginLogger().severe("Failed to execute debug command\n" + ExceptionUtils.getFullStackTrace(e));
                    context.msg(TL.COMMAND_DEBUG_FAIL);
                }
            }
        });
        context.msg(TL.COMMAND_DEBUG_RUNNING);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_DEBUG_DESCRIPTION;
    }
}