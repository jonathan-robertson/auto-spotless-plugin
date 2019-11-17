package com.jonathanrobertson.spotless;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class AutoFormatWarningTask extends DefaultTask {

	@TaskAction
	public void printWarning() {
		System.out.println("WARNING: spotlessApply was run automatically; check for changes before pushing to remote");
	}
}
