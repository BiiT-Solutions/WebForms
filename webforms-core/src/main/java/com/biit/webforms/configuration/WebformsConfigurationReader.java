package com.biit.webforms.configuration;

import java.io.IOException;
import java.util.Properties;

import com.biit.utils.file.PropertiesFile;

public class WebformsConfigurationReader {
	private final String DATABASE_CONFIG_FILE = "settings.conf";

	private final String GRAPHVIZ_TAG = "graphvizBinPath";

	private final String DEFAULT_GRAPHVIZ_VALUE = "/usr/bin/dot";

	private String graphvizBinPath;

	private static WebformsConfigurationReader instance;

	private WebformsConfigurationReader() {
		readConfig();
	}

	public static WebformsConfigurationReader getInstance() {
		if (instance == null) {
			synchronized (WebformsConfigurationReader.class) {
				if (instance == null) {
					instance = new WebformsConfigurationReader();
				}
			}
		}
		return instance;
	}

	/**
	 * Read database config from resource and update default connection parameters.
	 */
	private void readConfig() {
		Properties prop = new Properties();
		try {
			prop = PropertiesFile.load(DATABASE_CONFIG_FILE);
			graphvizBinPath = prop.getProperty(GRAPHVIZ_TAG);
		} catch (IOException e) {
			// Do nothing.
		}

		if (graphvizBinPath == null) {
			graphvizBinPath = DEFAULT_GRAPHVIZ_VALUE;
		}

	}

	public String getGraphvizBinPath() {
		return graphvizBinPath;
	}

}
