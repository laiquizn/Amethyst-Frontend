package com.tests;

import org.testng.annotations.Test;

import com.KeywordEngine.KeywordEngine;

public class Demo extends Common {

	public KeywordEngine keywordEngine;

	@Test(priority = 1, enabled = true)
	public void login1() {

		try {
			logger = rpt.startTest("Amethyst 2.0").assignCategory("Functional");
			keywordEngine = new KeywordEngine();
			try {
				keywordEngine.startExecution("Amethyst", logger);
				//System.out.println("Demo-registered user and uploaded file");
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		} catch (Exception e) {
		}
	}
}
