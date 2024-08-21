package com.tests;

import org.testng.annotations.Test;

import com.KeywordEngine.KeywordEngine;

public class LandingPage extends Common {

	public KeywordEngine keywordEngine;

	@Test(priority = 1, enabled = false)
	public void landingPage() {

		try {
			logger = rpt.startTest("Amethyst 2.0 - Landing Page").assignCategory("Functional");
			keywordEngine = new KeywordEngine();
			try {
				keywordEngine.startExecution("Landing Page", logger);
				//System.out.println("Demo-registered user and uploaded file");
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		} catch (Exception e) {
		}
	}
	
	@Test(priority = 2, enabled = false)
	public void AskForSupport() {

		try {
			logger = rpt.startTest("Amethyst 2.0 - AskForSupport").assignCategory("Functional");
			keywordEngine = new KeywordEngine();
			try {
				keywordEngine.startExecution("Ask for support", logger);
				//System.out.println("Demo-registered user and uploaded file");
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		} catch (Exception e) {
		}
	}
	
	@Test(priority = 3, enabled = false)
	public void GetPeople() {

			try {
				logger = rpt.startTest("Amethyst 2.0 - GetPeople").assignCategory("Functional");
				keywordEngine = new KeywordEngine();
				try {
					keywordEngine.startExecution("GetPeople", logger);
				} catch (Exception e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			} catch (Exception e) {
			}
		}
	
	@Test(priority = 4, enabled = true)
	public void GetURLContent() {

		try {
			logger = rpt.startTest("Amethyst 2.0 - GetURLContent").assignCategory("Functional");
			keywordEngine = new KeywordEngine();
			try {
				keywordEngine.startExecution("GetURLContent", logger);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		} catch (Exception e) {
		}
	}
}
