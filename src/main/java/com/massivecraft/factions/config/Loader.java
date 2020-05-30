package com.massivecraft.factions.config;

import com.google.common.reflect.TypeToken;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.config.annotation.Comment;
import com.massivecraft.factions.config.annotation.ConfigName;
import com.massivecraft.factions.config.annotation.DefinedType;
import com.massivecraft.factions.config.annotation.WipeOnReload;
import com.typesafe.config.ConfigRenderOptions;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Loader {
    public static void loadAndSave(String file, Object config) throws IOException, IllegalAccessException {
        HoconConfigurationLoader loader = getLoader(file);
        loadAndSave(loader, config);
    }

    public static HoconConfigurationLoader getLoader(String file) {
        Path configFolder = FactionsPlugin.getInstance().getDataFolder().toPath().resolve("config");
        try {
            Files.createDirectories(configFolder);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        Path path = configFolder.resolve(file + ".conf");
        return HoconConfigurationLoader.builder()
                .setPath(path)
                .setDefaultOptions(ConfigurationOptions.defaults().setAcceptedTypes(null))
                .setRenderOptions(ConfigRenderOptions.defaults().setComments(true).setOriginComments(false).setJson(false))
                .build();
    }

    public static void loadAndSave(HoconConfigurationLoader loader, Object config) throws IOException, IllegalAccessException {
        CommentedConfigurationNode node = loader.load();
        CommentedConfigurationNode newNode = loader.createEmptyNode();

        loadNode(node, newNode, config);

        loader.save(newNode);
    }

    public static void load(HoconConfigurationLoader loader, Object config) throws IOException, IllegalAccessException {
        CommentedConfigurationNode node = loader.load();

        loadNode(node, loader.createEmptyNode(), config);
    }

    private static final ObjectSet<Class<?>> TYPES = new ObjectOpenHashSet<>();

    static {
        TYPES.add(Boolean.TYPE);
        TYPES.add(Byte.TYPE);
        TYPES.add(Character.TYPE);
        TYPES.add(Double.TYPE);
        TYPES.add(Float.TYPE);
        TYPES.add(Integer.TYPE);
        TYPES.add(Long.TYPE);
        TYPES.add(Short.TYPE);
        TYPES.add(List.class);
        TYPES.add(Map.class);
        TYPES.add(Set.class);
        TYPES.add(String.class);
    }

    private static void loadNode(CommentedConfigurationNode current, CommentedConfigurationNode newNode, Object object) throws IllegalAccessException {
        for (Field field : getFields(object.getClass())) {
            if (field.isSynthetic()) {
                continue;
            }
            if ((field.getModifiers() & Modifier.TRANSIENT) != 0) {
                if (field.getAnnotation(WipeOnReload.class) != null) {
                    field.setAccessible(true);
                    field.set(object, null);
                }
                continue;
            }
            field.setAccessible(true);
            ConfigName configName = field.getAnnotation(ConfigName.class);
            Comment comment = field.getAnnotation(Comment.class);
            DefinedType definedType = field.getAnnotation(DefinedType.class);
            String confName = configName == null || configName.value().isEmpty() ? field.getName() : configName.value();
            CommentedConfigurationNode curNode = current.getNode(confName);
            CommentedConfigurationNode newNewNode = newNode.getNode(confName);
            boolean needsValue = curNode.isVirtual() || curNode.getValue() == null;
            if (comment != null) {
                newNewNode.setComment(comment.value());
            }
            Object defaultValue = field.get(object);
            if (TYPES.contains(field.getType())) {
                if (needsValue) {
                    if (definedType == null) {
                        newNewNode.setValue(defaultValue);
                    } else {
                        try {
                            Field tokenField = field.getDeclaringClass().getDeclaredField(field.getName() + "Token");
                            tokenField.setAccessible(true);
                            newNewNode.setValue((TypeToken<Object>) tokenField.get(object), defaultValue);
                        } catch (ObjectMappingException | NoSuchFieldException e) {
                            System.out.println("Failed horrifically to handle " + confName);
                        }
                    }
                } else {
                    try {
                        if (String.class.isAssignableFrom(curNode.getValue().getClass()) && int.class.isAssignableFrom(field.getType())) {
                            int parsed = Integer.parseInt((String) curNode.getValue());
                            field.set(object, parsed);
                            curNode.setValue(parsed);
                        }
                        if (Set.class.isAssignableFrom(field.getType()) && List.class.isAssignableFrom(curNode.getValue().getClass())) {
                            field.set(object, new HashSet<>((List<?>) curNode.getValue()));
                        } else {
                            field.set(object, curNode.getValue());
                        }
                        newNewNode.setValue(curNode.getValue());
                    } catch (IllegalArgumentException ex) {
                        System.out.println("Found incorrect type for " + getNodeName(curNode.getPath()) + ": Expected " + field.getType() + ", found " + curNode.getValue().getClass());
                        field.set(object, defaultValue);
                    }
                }
            } else {
                if (defaultValue == null) {
                    curNode.setValue(null);
                    newNewNode.setValue(null);
                } else {
                    loadNode(curNode, newNewNode, defaultValue);
                }
            }
        }
    }

    private static List<Field> getFields(Class<?> clazz) {
        return getFields(new ArrayList<>(), clazz);
    }

    private static List<Field> getFields(List<Field> fields, Class<?> clazz) {
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));

        if (clazz.getSuperclass() != null) {
            getFields(fields, clazz.getSuperclass());
        }

        return fields;
    }

    private static String getNodeName(Object[] path) {
        StringBuilder builder = new StringBuilder();
        for (Object o : path) {
            if (o != null) {
                builder.append(o.toString()).append('.');
            }
        }
        return builder.substring(0, builder.length() - 1);
    }
}
