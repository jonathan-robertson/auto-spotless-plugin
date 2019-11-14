package com.jonathanrobertson.spotless;

import static org.junit.Assert.*;

import org.junit.Test;

public class AutoSpotlessPluginTest {

	@Test
	public void getAbsolutePathFromEmbeddedFile() {
		AutoSpotlessPlugin.getAbsolutePathFromEmbeddedFile("spotless.importorder");
		AutoSpotlessPlugin.getAbsolutePathFromEmbeddedFile("spotless.eclipseformat.xml");
	}
}
