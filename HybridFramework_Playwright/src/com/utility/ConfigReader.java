package com.utility;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {

	Properties prop;

	/* method to read properties file */
	public ConfigReader() {

		try {

			File src = new File("./src/com/utility/resources/Config.property");
			FileInputStream fs = new FileInputStream(src);

			prop = new Properties();
			prop.load(fs);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * Below methods read data from .property file
	 */
	public String getUrl() {
		return prop.getProperty("url");
	}

	public String getbrowser() {
		return prop.getProperty("browser");
	}

}
