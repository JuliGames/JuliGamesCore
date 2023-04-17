package net.juligames.core.paper.misc.configmapping;

import net.juligames.core.api.API;
import net.juligames.core.api.config.representations.Representation;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * This class provides a convenient way to map configuration files to Java classes in your plugins.
 * When you create a subclass of {@link ObjectifiedConfiguration}, you can add your configuration variables as fields to the class,
 * and they will be automatically populated with the execution of the constructor {@link #ObjectifiedConfiguration(ConfigurationSection)}.
 * You need to annotate the fields you would like to be automatically filled with @{@link Autofill}.
 * <p>
 * The @{@link Autofill} annotation is used to mark fields that should be automatically populated with values from the configuration file.
 * The {@link ObjectifiedConfiguration} class uses reflection to look up the fields in the subclass and then populates
 * them with the appropriate values from the associated {@link ConfigurationSection}.
 * <p>
 * The ObjectifiedConfiguration class provides methods to save and reload the associated configuration file.
 * The {@link #save()} method saves the current values of the fields to the associated {@link ConfigurationSection}.
 * The {@link #reload()} method reads the current values of the fields from the associated {@link ConfigurationSection}.
 * <p>
 * Example implementation:
 *
 * <pre>{@code
 * package com.example.plugin;
 *
 * import org.bukkit.configuration.ConfigurationSection;
 * import net.juligames.core.paper.misc.configmapping.Autofill;
 * import net.juligames.core.paper.misc.configmapping.ObjectifiedConfiguration;
 *
 * public class MyConfig extends ObjectifiedConfiguration {
 *
 *     @Autofill
 *     public String databaseUsername = "defaultUsername";
 *
 *     @Autofill
 *     public String databasePassword = "defaultPassword";
 *
 *     public MyConfig(ConfigurationSection section) {
 *         super(section);
 *     }
 *
 * }
 * }</pre>
 * <p>
 * In this example, the {@code MyConfig} class extends {@link ObjectifiedConfiguration} and has two fields marked with the @{@link Autofill}
 * annotation. These fields will be automatically populated with values from the associated configuration file.
 * You can also manually {@link #save()} or {@link #reload()} the config.
 *
 * @author Ture Bentzin
 * 14.04.2023
 */
@ApiStatus.Experimental
@ApiStatus.AvailableSince("1.6")
public class ObjectifiedConfiguration {

    private final @NotNull ConfigurationSection associatedSection;

    public ObjectifiedConfiguration(@NotNull ConfigurationSection associatedSection) {
        this.associatedSection = associatedSection;
        reload();
    }

    public ObjectifiedConfiguration(@NotNull File ymlFile) {
        this(YamlConfiguration.loadConfiguration(ymlFile));
    }

    public ObjectifiedConfiguration(@NotNull Reader reader) {
        this(YamlConfiguration.loadConfiguration(reader));
    }

    public ObjectifiedConfiguration(@NotNull String data, @Nullable String section) throws InvalidConfigurationException {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.loadFromString(data);
        this.associatedSection = section == null ? configuration : configuration.createSection(section);
    }

    public ObjectifiedConfiguration(@NotNull String path) {
        this(new File(path));
    }

    public ObjectifiedConfiguration(@NotNull URI path) {
        this(new File(path));
    }

    public ObjectifiedConfiguration(@NotNull Representation<ConfigurationSection> sectionRepresentation) {
        this(sectionRepresentation.represent());
    }


    public final @NotNull ConfigurationSection getAssociatedSection() {
        return associatedSection;
    }

    public void reload() {
        Arrays.stream(this.getClass().getFields()).forEachOrdered(this::reload);
    }

    public void save() {
        Arrays.stream(this.getClass().getFields()).forEachOrdered(this::save);
    }

    /**
     * @param file file to save to
     * @throws IOException if saving fails fatally
     * @apiNote This method is not available with standalone!
     */
    @ApiStatus.Experimental
    public void save(File file) throws IOException {
        save();
        Configuration root = associatedSection.getRoot();
        if (root == null) {
            API.get().getAPILogger().warning("cant save " + this.getClass().getSimpleName() + " because associatedSection does not" +
                    " belong to any Configuration");
            return;
        }
        if (root instanceof FileConfiguration fileConfiguration) {
            fileConfiguration.save(file);
        } else {
            API.get().getAPILogger().warning("cant save " + this.getClass().getSimpleName() + " because associatedSection does not" +
                    " belong to a FileConfiguration! :: " + root.getClass().getSimpleName());
        }
    }

    @ApiStatus.Experimental
    public <C extends Configuration> void save(Consumer<C> saver) {
        save();
        Configuration root = associatedSection.getRoot();
        if (root == null) {
            API.get().getAPILogger().warning("cant save " + this.getClass().getSimpleName() + " because associatedSection does not" +
                    " belong to any Configuration");
            return;
        }
        saver.accept((C) root);
    }

    public void save(@NotNull Field field) {
        if (field.getDeclaringClass().equals(this.getClass())) {
            String key = getKeyForField(field);
            //time to set the value
            try {
                getAssociatedSection().set(key, field.get(this));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private String getKeyForField(@NotNull Field field) {
        return
                Arrays.stream(
                                field.getAnnotations())
                        .filter(annotation -> annotation.annotationType().equals(Autofill.class))
                        .map(annotation -> (Autofill) annotation)
                        .findFirst()
                        .flatMap(annotation -> {
                            if (!annotation.customName().equals(Autofill.defaultName))
                                return annotation.customName().describeConstable();
                            return Optional.empty();
                        }).orElse(field.getName());
    }

    protected final void reload(@NotNull Field field) {
        if (field.getDeclaringClass().equals(this.getClass())) {
            String key = getKeyForField(field);
            //check for default
            try {
                //time to get the value from section
                field.set(this, extractDefault(field)
                        .map(o1 -> getAssociatedSection()
                                .get(key, o1))
                        .orElseGet(() -> getAssociatedSection()
                                .get(key)));

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected final Optional<?> extractDefault(@NotNull Field field) throws IllegalAccessException {
        final Object o = field.get(this);
        return Optional.ofNullable(o);
    }
}
