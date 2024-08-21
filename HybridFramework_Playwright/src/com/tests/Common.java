package com.tests;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.microsoft.playwright.Browser;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Common {
	public static ExtentReports rpt;
	public static ExtentTest logger;
	public static Browser browser;
	public XSSFWorkbook wb;
	public XSSFSheet sheet;
	public File failedResults;
	public FileOutputStream fout;
	public Test testClass;
	public static String planID, suiteID, testID;

	/*
	 * Method to initialize extent report and to delete the existing used rows in
	 * the FailedTestcases Excel sheet
	 */
	@BeforeSuite
	public void startTest() throws IOException {
		rpt = new ExtentReports("./TestReport.html", true);
		rpt.loadConfig(Common.class, "resources", "config.xml");
	}

	@BeforeMethod
	public void beforeMethod(Method method) {
		testClass = method.getAnnotation(Test.class);
//        for (int i = 0; i < testClass.groups().length; i++) {
//            System.out.println(testClass.groups()[i]);
//        }
	}

	public static boolean localExecution = false;

	public static void sysoutRequied(String message) {
		if (localExecution)
			System.out.println(message);
	}

	public static void setVSTSIDs(String testPlan, String testSuite, String point) {
		planID = testPlan;
		suiteID = testSuite;
		testID = point;
		sysoutRequied("IDs are: " + planID + " & " + suiteID + " & " + testID);
	}

	public void ExceptionCategory(ITestResult result) {
		String os = System.getProperty("os.name").toLowerCase();
		sysoutRequied(os);
		String classname = result.getTestClass().toString();
		String testmethodname = result.getName();
		if (result.getStatus() == ITestResult.FAILURE) {
			String errorname = result.getThrowable().toString();
			if (errorname.contains("AssertionError")) {
				logger.log(LogStatus.FAIL, "Test Class  Failed is " + classname + " and Test Method " + testmethodname);
				logger.log(LogStatus.FAIL, "Test case failed due to the " + errorname);
				logger.log(LogStatus.FAIL, "Category : " + "Application Change");
			} else if (errorname.contains("NoSuchElementException")) {
				logger.log(LogStatus.FAIL, "Test Class  Failed is " + classname + " and Test Method " + testmethodname);
				logger.log(LogStatus.FAIL, "Test case failed due to the " + errorname);
				logger.log(LogStatus.FAIL, "Category : " + "Locator Change");

			} else if (errorname.contains("TimeoutException")) {
				logger.log(LogStatus.FAIL, "Test Class  Failed is " + classname + " and Test Method " + testmethodname);
				logger.log(LogStatus.FAIL, "Test case failed due to the " + errorname);
				logger.log(LogStatus.FAIL, "Category : " + "Application Slowness");
			} else if (errorname.contains("Webdriver")) {
				logger.log(LogStatus.FAIL, "Test Class  Failed is " + classname + " and Test Method " + testmethodname);
				logger.log(LogStatus.FAIL, "Test case failed due to the " + errorname);
				logger.log(LogStatus.FAIL, "Category : " + "Browser Upgrade");

			} else if (errorname.contains("ElementNotInteractableException")) {
				logger.log(LogStatus.FAIL, "Test Class  Failed is " + classname + " and Test Method " + testmethodname);
				logger.log(LogStatus.FAIL, "Test case failed due to the " + errorname);
				logger.log(LogStatus.FAIL, "Category : " + "Locator not in View");

			} else if (errorname.contains("ElementNotVisibleException")) {
				logger.log(LogStatus.FAIL, "Test Class  Failed is " + classname + " and Test Method " + testmethodname);
				logger.log(LogStatus.FAIL, "Test case failed due to the " + errorname);
				logger.log(LogStatus.FAIL, "Category : " + "Locator not visible");

			} else if (errorname.contains("StaleElementReferenceException")) {
				logger.log(LogStatus.FAIL, "Test Class  Failed is " + classname + " and Test Method " + testmethodname);
				logger.log(LogStatus.FAIL, "Test case failed due to the " + errorname);
				logger.log(LogStatus.FAIL, "Category : " + "Dynamic Page Reload Changes");
			} else {
				logger.log(LogStatus.FAIL, "Test Class  Failed is " + classname + " and Test Method " + testmethodname);
				logger.log(LogStatus.FAIL, "Test case failed due to the " + errorname);
				sysoutRequied("Test Case Failed: " + result.getName());

			}
		}

		else if (result.getStatus() == ITestResult.SUCCESS) {
			logger.log(LogStatus.PASS, "Test Case Passed " + result.getName());
			sysoutRequied("Test Case Passed: " + result.getName());

		} else if (result.getStatus() == ITestResult.SKIP) {
			logger.log(LogStatus.SKIP, "Test Case Skipped " + result.getName());
		}

	}

	/*
	 * Method to flush/populate data in extent report
	 */
	@AfterMethod
	public void getResult(ITestResult result) throws IOException, InterruptedException {

		ExceptionCategory(result);

		rpt.endTest(logger);
		

	}

	/*
	 * Method to close extent report object
	 */
	@AfterSuite
	public void close() {
		rpt.flush();
		//rpt.close();

	}
}
