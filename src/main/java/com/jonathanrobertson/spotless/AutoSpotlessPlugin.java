package com.jonathanrobertson.spotless;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.PluginContainer;

import com.diffplug.gradle.spotless.SpotlessExtension;
import com.diffplug.gradle.spotless.SpotlessPlugin;

public class AutoSpotlessPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		SpotlessExtension spotlessExtension = getOrAttachSpotlessPlugin(project).getExtension();

		spotlessExtension.java(format -> {
			format.removeUnusedImports();
			format.importOrderFile(getAbsolutePathFromEmbeddedFile("spotless.importorder"));
			format.eclipse().configFile(getAbsolutePathFromEmbeddedFile("spotless.eclipseformat.xml"));
		});

		spotlessExtension.format("misc", format -> {
			format.target("**/*.md", "**/*.gradle", "**/.gitignore");
			format.trimTrailingWhitespace();
			format.endWithNewline();

			format.replaceRegex("too many blank lines", "^\\n\\n+", "\n");
		});

		spotlessExtension.format("gradle", format -> {
			format.target("**/*.gradle");
			format.replaceRegex("blank lines following {", "\\{\\n\\s*\\n+", "{\n");
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
