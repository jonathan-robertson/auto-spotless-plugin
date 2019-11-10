package com.jonathanrobertson.spotless;

import com.diffplug.gradle.spotless.SpotlessPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.PluginContainer;

import java.util.Optional;

public class AutoSpotlessPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        SpotlessPlugin spotlessPlugin = getOrAttachSpotlessPlugin(project);

    }

    /**
     * Find and return SpotlessPlugin instance; attaching a new instance if necessary
     * @param project current project
     * @return {@link SpotlessPlugin}
     */
    private SpotlessPlugin getOrAttachSpotlessPlugin(Project project) {
        PluginContainer pluginContainer = project.getPlugins();
        return Optional.ofNullable(pluginContainer.findPlugin(SpotlessPlugin.class))
                .orElseGet(() -> pluginContainer.apply(SpotlessPlugin.class));
    }
}
