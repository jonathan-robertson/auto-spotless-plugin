package com.jonathanrobertson.spotless;

import com.diffplug.gradle.spotless.SpotlessPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.PluginContainer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

public class AutoSpotlessPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        SpotlessPlugin spotlessPlugin = getOrAttachSpotlessPlugin(project);

        spotlessPlugin.getExtension().java(format -> {
            format.removeUnusedImports();
            format.importOrderFile(getAbsolutePathFromEmbeddedFile("spotless.importorder"));
            format.eclipse().configFile(getAbsolutePathFromEmbeddedFile("spotless.eclipseformat.xml"));
        });
    }

    private SpotlessPlugin getOrAttachSpotlessPlugin(Project project) {
        PluginContainer pluginContainer = project.getPlugins();
        return Optional.ofNullable(pluginContainer.findPlugin(SpotlessPlugin.class))
                .orElseGet(() -> pluginContainer.apply(SpotlessPlugin.class));
    }

    protected static File getAbsolutePathFromEmbeddedFile(String filename) {
        File target = new File(System.getProperty("java.io.tmpdir"), filename);
        try (InputStream is = AutoSpotlessPlugin.class.getClassLoader().getResourceAsStream(filename)) {
            Files.copy(is, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (NullPointerException | IOException e) {
            throw new RuntimeException("unable to find " + filename + " in AutoSpotlessPlugin", e);
        }
        return target;
    }
}
