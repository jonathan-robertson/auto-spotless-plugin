package com.jonathanrobertson.spotless;

import static org.junit.Assert.*;

import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class AutoSpotlessPluginTest {

	@Test
	public void getAbsolutePathFromEmbeddedFile() {
		final String tempDir = System.getProperty("java.io.tmpdir");
		Arrays.asList("spotless.importorder", "spotless.eclipseformat.xml").forEach(filename -> {
			String expected = Paths.get(tempDir, filename).toString();
			String actual = AutoSpotlessPlugin.getAbsolutePathFromEmbeddedFile(filename).getAbsolutePath();
			Assert.assertEquals(expected, actual);
		});
	}

	@Test
	public void gitIsPresent() {
		AutoSpotlessPlugin.gitIsPresent(); // just confirm an unexpected exception doesn't appear
	}
}
