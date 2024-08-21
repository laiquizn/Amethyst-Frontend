package com.tests;

import org.testng.TestNG;

public class TestRunnerJar {

	static TestNG testNg;
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		testNg = new TestNG();
	
		testNg.setTestClasses(new Class[] {browser.class});
	//	testNg.setTestClasses(new Class[] {Register.class});
		//testNg.addListener(ext);
		testNg.run();
		
	}

}
