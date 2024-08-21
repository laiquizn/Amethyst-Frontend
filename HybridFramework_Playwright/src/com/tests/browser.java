package com.tests;

import org.testng.annotations.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class browser extends Common {

	Browser browser;

	@Test(priority = 1, enabled = true)
	public void register() throws Exception {

		logger = rpt.startTest("Test01").assignCategory("SVG Module").assignCategory(testClass.groups());
		browser = Playwright.create().chromium()
				.launch(new BrowserType.LaunchOptions().setChannel("msedge").setHeadless(false));

		Page page = browser.newPage();
		page.setViewportSize(1920, 1080);
		page.navigate("http://playwright.dev");
		page.click("//a[text()='Get started']");
		Thread.sleep(2000);
		System.out.println(page.title());

	}
}
