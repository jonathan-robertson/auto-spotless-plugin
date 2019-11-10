package com.jonathanrobertson.spotless;

import com.diffplug.gradle.spotless.SpotlessPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.PluginContainer;

import java.net.URL;
import java.util.Optional;

public class AutoSpotlessPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        SpotlessPlugin spotlessPlugin = getOrAttachSpotlessPlugin(project);

        spotlessPlugin.getExtension().java(format -> {
            format.removeUnusedImports();
            format.importOrder(getExternalPath("spotless.importorder"));
            format.eclipse().configFile(getExternalPath("spotless.eclipseformat.xml"));
        });
    }

    private SpotlessPlugin getOrAttachSpotlessPlugin(Project project) {
        PluginContainer pluginContainer = project.getPlugins();
        return Optional.ofNullable(pluginContainer.findPlugin(SpotlessPlugin.class))
                .orElseGet(() -> pluginContainer.apply(SpotlessPlugin.class));
    }

    private String getExternalPath(String filename) {
        return Optional.ofNullable(this.getClass().getClassLoader().getResource(filename))
                .map(URL::toExternalForm)
                .orElseThrow(() -> new RuntimeException(filename + " could not be detected in AutoSpotlessPlugin"));
    }
}
