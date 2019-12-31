package com.jonathanrobertson.spotless.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Git {

	public static String AUTO_STASH_NAME = "SPOTLESS AUTO-STASH";

	private static Pattern	nameMatches	= Pattern.compile(String.format("^.+: %s$", AUTO_STASH_NAME));
	private static Pattern	stashId		= Pattern.compile("^stash@\\{(\\d+)}");

	/**
	 * {@code git stash list} to confirm the stash id of our auto-stash name
	 *
	 * @return {@code Optional<Integer>} stash id (if it exists)
	 */
	public static Optional<Integer> stashListAutoId() {
		return run("git", "stash", "list")
				.stream()
				.filter(nameMatches.asPredicate())
				.findFirst()
				.map(stashId::matcher)
				.filter(Matcher::find)
				.map(m -> m.group(1))
				.map(Integer::valueOf);
	}

	/**
	 * {@code git stash push -m [auto-stash-name]}
	 * 
	 * @return whether any content was stashed
	 */
	public static boolean stashPush() {
		return run("git", "stash", "push", "-m", AUTO_STASH_NAME)
				.stream()
				.findFirst()
				.map(out -> !"No local changes to save".equalsIgnoreCase(out))
				.orElse(false);
	}

	/**
	 * {@code git stash apply [id]}
	 * 
	 * @param id stash id to apply
	 */
	public static void stashApply(Integer id) {
		run("git", "stash", "apply", Objects.requireNonNull(id).toString());
	}

	/**
	 * {@code git stash drop {id}}
	 *
	 * @param id stash id to drop
	 */
	public static void stashDrop(Integer id) {
		run("git", "stash", "drop", Objects.requireNonNull(id).toString());
	}

	/**
	 * {@code git diff}
	 * 
	 * @return output of git diff
	 */
	public static List<String> diffStat() {
		return run("git", "diff", "--stat");
	}

	/**
	 * {@code git diff stash@{[id]}}
	 * 
	 * @param  id stash id to diff against
	 * @return    output returned from git
	 */
	public static List<String> diffStat(Integer id) {
		return run("git", "diff", "--stat", "stash@{" + Objects.requireNonNull(id).toString() + "}");
	}

	public static boolean isPresent() {
		try {
			return new ProcessBuilder("git", "status").start().waitFor() == 0;
		} catch (IOException | InterruptedException e) {
			return false;
		}
	}

	private static List<String> run(String... command) {
		List<String> out = new ArrayList<>();
		try {
			Process p = new ProcessBuilder(command).start();
			try (InputStream is = p.getInputStream();
					InputStreamReader r = new InputStreamReader(is);
					BufferedReader b = new BufferedReader(r)) {
				b.lines().forEachOrdered(out::add); // must read all lines
				p.waitFor();
			}
		} catch (IOException | InterruptedException e) {
			String message = "failed to process: " + Arrays.toString(command);
			System.out.println(message);
			throw new RuntimeException(message, e);
		}
		return out;
	}
}
