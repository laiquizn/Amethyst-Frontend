package com.tests;

import org.testng.annotations.Test;

import com.KeywordEngine.KeywordEngine;
import com.utility.BaseClass;

public class Sample extends Common{

	public KeywordEngine keywordEngine;
	BaseClass base = new BaseClass();
		
	@Test(priority=1,enabled=true)
	public void sendmail() throws Exception {
		

		logger = rpt.startTest("Sample").assignCategory("Functional");
		
		base.sendEmail("Test Mail from Playwright", "Test", "Testing Emai Function");
		
	}	
	
	
		
}
