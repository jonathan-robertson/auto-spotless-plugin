package com.jonathanrobertson.spotless;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.tasks.TaskContainer;

import com.diffplug.gradle.spotless.SpotlessExtension;
import com.diffplug.gradle.spotless.SpotlessPlugin;
import com.jonathanrobertson.spotless.utils.Git;

public class AutoSpotlessPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		PluginContainer pluginContainer = project.getPlugins();
		SpotlessExtension spotlessExtension = Optional
				.ofNullable(pluginContainer.findPlugin(SpotlessPlugin.class))
				.orElseGet(() -> pluginContainer.apply(SpotlessPlugin.class))
				.getExtension();
		configureFormatting(spotlessExtension);

		if ("dev".equals(System.getenv("AUTO_SPOTLESS_ENV"))) {
			configureDevDependencies(project.getTasks());
		}
	}

	protected static File getAbsolutePathFromEmbeddedFile(String filename) {
		File target = new File(System.getProperty("java.io.tmpdir"), filename);
		try (InputStream is = AutoSpotlessPlugin.class.getClassLoader().getResourceAsStream(filename)) {
			Files.copy(Objects.requireNonNull(is), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (NullPointerException | IOException e) {
			throw new RuntimeException("unable to find " + filename + " in AutoSpotlessPlugin", e);
		}
		return target;
	}

	private void configureFormatting(SpotlessExtension spotlessExtension) {
		spotlessExtension.java(format -> {
			format.removeUnusedImports();
			format.importOrderFile(getAbsolutePathFromEmbeddedFile("spotless.importorder"));
			format.eclipse().configFile(getAbsolutePathFromEmbeddedFile("spotless.eclipseformat.xml"));
		});

		spotlessExtension.format("misc", format -> {
			format.target("**/*.md", "**/*.gradle", "**/.gitignore", "**/*.yml", "**/*.yaml");
			format.trimTrailingWhitespace();
			format.endWithNewline();

			format.replaceRegex("too many blank lines", "^\\n\\n+", "\n");
		});

		spotlessExtension.format("gradle", format -> {
			format.target("**/*.gradle");
			format.replaceRegex("blank lines following {", "\\{\\n\\s*\\n+", "{\n");
		});
	}

	private void configureDevDependencies(TaskContainer taskContainer) {
		if (Git.isPresent()) {
			taskContainer.register("beforeFormatting", t -> {
				t.finalizedBy("spotlessApply");
				t.doFirst(task -> {
					// confirm we do not already have a dangling auto-stash entry
					Git.stashListAutoId().ifPresent(i -> {
						String message = String.format("ERROR: must pop or remove auto-stash entry before retrying: `git stash drop %d`, `git stash pop %d`", i, i);
						System.out.println(message);
						System.exit(1);
					});

					// copy current state to stash for comparison after spotlessApply
					if (Git.stashPush()) {
						Git.stashListAutoId().ifPresent(Git::stashApply);
					}
				});
			});

			taskContainer.getByName("spotlessApply", t -> {
				t.finalizedBy("afterFormatting");
			});

			taskContainer.register("afterFormatting", t -> {
				t.finalizedBy("build");
				t.doFirst(task -> {
					// get id for existing auto-stash (if one exists)
					Optional<Integer> id = Git.stashListAutoId();

					// check for and display changes to user
					List<String> results = id.map(Git::diffStat).orElseGet(Git::diffStat);
					if (results.size() == 0) {
						System.out.println("INFO: no changes were made to tracked files by spotlessApply");
					} else {
						String newline = System.getProperty("line.separator");
						System.out.println("WARNING: the following files were changed by spotlessApply; be sure to include them in your commit(s)"
								+ newline + String.join(newline, results));
					}

					// drop stash entry from before (if exists) since it's no longer needed
					id.ifPresent(Git::stashDrop);
				});
			});

			taskContainer.getByName("build", t -> {
				t.dependsOn("beforeFormatting");
			});
		} else {
			taskContainer.getByName("build", t -> {
				t.dependsOn("spotlessApply");
				t.doLast(task -> System.out.println("WARNING: spotlessApply was automatically run; files may've changed"));
			});
		}
	}
}
