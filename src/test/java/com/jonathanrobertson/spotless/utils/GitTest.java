package com.jonathanrobertson.spotless.utils;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

public class GitTest {

	@Test
	public void stashListAutoId() {
		Git.stashListAutoId(); // just confirm an unexpected exception doesn't appear
	}

	@Ignore // skipping; destructive
	@Test
	public void stashPush() {
	}

	@Ignore // skipping; destructive
	@Test
	public void stashApply() {
	}

	@Ignore // skipping; destructive
	@Test
	public void stashDrop() {
	}

	@Test
	public void diffStat() {
		Git.diffStat(); // just confirm an unexpected exception doesn't appear
	}

	@Ignore // skipping; not guaranteed to be present
	@Test
	public void testDiffStat() {
	}

	@Test
	public void isPresent() {
		Git.isPresent(); // just confirm an unexpected exception doesn't appear
	}
}
