package com.jonathanrobertson.spotless;

import java.io.*;
import java.util.StringJoiner;

import org.gradle.api.DefaultTask;
import org.gradle.api.UncheckedIOException;
import org.gradle.api.tasks.TaskAction;

public class GitStatusTask extends DefaultTask {

	@TaskAction
	public void gitStatus() {
		try {
			Process p = new ProcessBuilder("git", "status", "-s", "-uno").start();
			try (InputStream is = p.getInputStream(); InputStreamReader r = new InputStreamReader(is); BufferedReader b = new BufferedReader(r)) {
				StringJoiner joiner = new StringJoiner(System.getProperty("line.separator"));
				b.lines().iterator().forEachRemaining(joiner::add);
				p.waitFor();
				System.out.println(joiner.toString());
			}
		} catch (InterruptedException | IOException e) {
			throw new UncheckedIOException("unable to write git status", e);
		}
	}
}
