package com.KeywordEngine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.*;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.FileChooser;
import com.microsoft.playwright.Frame;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.MouseButton;
import com.microsoft.playwright.options.SelectOption;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.utility.BaseClass;

import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import com.google.gson.JsonObject;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;

/*
	KeywordEngine class has actions to be performed by each Keyword mentioned in ./src/TestScenarios/TestScenarios.xlsx
 */
public class KeywordEngine {

	public BrowserContext bc;
	public Properties prop;

	public final String path = "./src/TestScenarios/TestScenarios.xlsx";
	public Page page;
	static XSSFWorkbook wb;
	public static XSSFSheet sh;
	FileInputStream fs;
	public static XSSFCell cell;
	public static XSSFRow row;
	public ExtentTest logger;
	static BaseClass base;
	public ExtentReports rpt;
	public ElementHandle element;
	public Browser browser;
	Playwright playwright;
	APIRequestContext apiRequestContext;

	public void startExecution(String sheetname, ExtentTest logger) throws Exception {
		List<Page> windows;
		String locatorName = null;
		String locatorValue = null;
		String screenShotName = null;

		screenShotName = new Exception().getStackTrace()[0].getMethodName();
		Frame frame = null;

		try {
			File file = new File(path);
			FileInputStream fis = new FileInputStream(file);
			wb = new XSSFWorkbook(fis);
			sh = wb.getSheet(sheetname);

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		int k = 0;
		System.out.println(sh.getLastRowNum());
		for (int i = 0; i < sh.getLastRowNum(); i++) {

			try {
				String locatorColValue = sh.getRow(i + 1).getCell(k + 1).toString().trim();
				String action = sh.getRow(i + 1).getCell(k + 2).toString().trim();
				String value = sh.getRow(i + 1).getCell(k + 3).toString().trim();
				String loggerMessage = sh.getRow(i + 1).getCell(k + 4).toString().trim();
				String ExpectedValue = sh.getRow(i + 1).getCell(k + 5).toString().trim();
				String ValueMatched = sh.getRow(i + 1).getCell(k + 6).toString().trim();

				if (!locatorColValue.equalsIgnoreCase("NA")) {

					// locatorName = locatorColValue.split("==")[0].trim();
					// locatorValue = (locatorColValue.split("=",1)[1]).trim();
					// locatorValue = locatorColValue.split("==",0)[1].trim();
					locatorValue = locatorColValue;
					// System.out.println(locatorValue);
					List<ElementHandle> ele = page.querySelectorAll(locatorValue);
					int elesize = ele.size();
					if (elesize > 0) {
						element = page.querySelector(locatorValue);
						if (action.equalsIgnoreCase("waitForElement")) {
							try {
								page.waitForSelector(locatorValue);
							} catch (Exception e) {
								System.out.println(e.getLocalizedMessage());
								logger.log(LogStatus.FAIL, e.toString());
							}

						}
						else if (action.equalsIgnoreCase("doubleClick")) {
							element.dblclick();
							logger.log(LogStatus.PASS, loggerMessage
									+ logger.addScreenCapture(BaseClass.captureScreenShot(page, screenShotName)));

						}
						else if (action.equalsIgnoreCase("rightClick")) {
							page.locator(locatorValue).click(new Locator.ClickOptions().setButton(MouseButton.RIGHT));
							logger.log(LogStatus.PASS, loggerMessage
									+ logger.addScreenCapture(BaseClass.captureScreenShot(page, screenShotName)));

						}
						else if (action.equalsIgnoreCase("switchFrame")) {
							page.locator(locatorValue);
							logger.log(LogStatus.PASS, loggerMessage
									+ logger.addScreenCapture(BaseClass.captureScreenShot(page, screenShotName)));
						}
						else if (action.equalsIgnoreCase("specificElementScreenshot")) {
							logger.log(LogStatus.PASS, loggerMessage
									+ logger.addScreenCapture(BaseClass.specificCaptureScreenShot(page, screenShotName,locatorValue)));
						}
						else if (action.equalsIgnoreCase("scrollToElement")) {
							try {
								element.scrollIntoViewIfNeeded();
								logger.log(LogStatus.PASS, loggerMessage
										+ logger.addScreenCapture(BaseClass.captureScreenShot(page, screenShotName)));
							} catch (Exception e) {
								System.out.println(e.getLocalizedMessage());
								logger.log(LogStatus.FAIL, e.toString());
							}

						} else if (action.equalsIgnoreCase("click")) {
							try {
								element.click();
								logger.log(LogStatus.PASS, loggerMessage
										+ logger.addScreenCapture(BaseClass.captureScreenShot(page, screenShotName)));

							} catch (Exception e) {
								System.out.println(e.getLocalizedMessage());
								logger.log(LogStatus.FAIL, e.toString());

							}

						} else if (action.equalsIgnoreCase("selectDropdownByValue")) {
							try {
								element.selectOption(value);
								logger.log(LogStatus.PASS, loggerMessage
										+ logger.addScreenCapture(BaseClass.captureScreenShot(page, screenShotName)));

							} catch (Exception e) {
								System.out.println(e.getLocalizedMessage());
								logger.log(LogStatus.FAIL, e.toString());
							}

						} else if (action.equalsIgnoreCase("selectDropdownByIndex")) {

							double d = Double.parseDouble(value);
							int index1 = (int) d;
							try {
								element.selectOption(new SelectOption().setIndex(index1));
								logger.log(LogStatus.PASS, loggerMessage
										+ logger.addScreenCapture(BaseClass.captureScreenShot(page, screenShotName)));
							} catch (Exception e) {
								System.out.println(e.getMessage());
								logger.log(LogStatus.FAIL, e.toString());
							}

						} else if (action.equalsIgnoreCase("focus")) {
							try {
								element.focus();
								logger.log(LogStatus.PASS, loggerMessage
										+ logger.addScreenCapture(BaseClass.captureScreenShot(page, screenShotName)));

							} catch (Exception e) {
								System.out.println(e.getLocalizedMessage());
								logger.log(LogStatus.FAIL, e.toString());
							}

						}
						/**
						 * @author divya.b.sellamuthu
						 * @Date : 13/10/2021 File upload in playwright
						 *
						 *       User should pass click element as locator and file path as value in
						 *       excel
						 */
						else if (action.equalsIgnoreCase("upload")) {
							try {
								FileChooser fileChooser = page.waitForFileChooser(() -> element.click());
								fileChooser.setFiles(Paths.get(value));
								logger.log(LogStatus.PASS, loggerMessage
										+ logger.addScreenCapture(BaseClass.captureScreenShot(page, screenShotName)));
								// page.setInputFiles(locatorValue, Paths.get(value));
							} catch (Exception e) {
								System.out.println(e.getLocalizedMessage());
								logger.log(LogStatus.FAIL, e.toString());
							}

						}

						else if (action.equalsIgnoreCase("UserName")) {
							try{
								element.fill("");
							element.fill(System.getProperty("UserName"));
							logger.log(LogStatus.PASS, loggerMessage
									+ logger.addScreenCapture(BaseClass.captureScreenShot(page, screenShotName)));
							}
							catch (Exception e) {
								System.out.println(e.getLocalizedMessage());
								logger.log(LogStatus.FAIL, e.toString());
							}
						} else if (action.equalsIgnoreCase("Password")) {
							try{
								element.fill("");
							element.fill(System.getProperty("Password"));
							logger.log(LogStatus.PASS, loggerMessage
									+ logger.addScreenCapture(BaseClass.captureScreenShot(page, screenShotName)));
						}
							catch (Exception e) {
								System.out.println(e.getLocalizedMessage());
								logger.log(LogStatus.FAIL, e.toString());
							}
						}
						/**
						 * @author divya.b.sellamuthu
						 * @Date : 13/10/2021 JS execution in playwright
						 *
						 *       User should add locator only in CSS
						 */
						else if (action.equalsIgnoreCase("JSClick")) {

							try {
								Object jselement = page.querySelector(locatorValue);
								if (element.isEnabled() && element.isVisible()) {
									String text = "document.querySelector(" + '"' + locatorValue + '"' + ").click();";
									System.out.println("Clicking on element  using java script click");
									System.out.println(text);
									page.evaluate(text);
									logger.log(LogStatus.PASS, loggerMessage + logger
											.addScreenCapture(BaseClass.captureScreenShot(page, screenShotName)));
								} else {
									System.out.println("Unable to click on element");
								}
							} catch (Exception e) {
								System.out.println(e.getLocalizedMessage());
								logger.log(LogStatus.FAIL, e.toString());
							}
						} else if (action.equalsIgnoreCase("sendkeys")) {
                          try {
							element.fill(value);
							logger.log(LogStatus.PASS, loggerMessage
									+ logger.addScreenCapture(BaseClass.captureScreenShot(page, screenShotName)));
						}
							catch (Exception e) {
								System.out.println(e.getLocalizedMessage());
								logger.log(LogStatus.FAIL, e.toString());
							}

						} else if (action.equalsIgnoreCase("click")) {
							try{
								element.click();
							logger.log(LogStatus.PASS, loggerMessage
									+ logger.addScreenCapture(BaseClass.captureScreenShot(page, screenShotName)));
						}
							catch (Exception e) {
								System.out.println(e.getLocalizedMessage());
								logger.log(LogStatus.FAIL, e.toString());
							}

						} else if (action.equalsIgnoreCase("getText")) {
							try{
								String ActualValue = page.innerText(locatorValue);
							System.out.println(ActualValue);
							logger.log(LogStatus.PASS, ActualValue
									+ logger.addScreenCapture(BaseClass.captureScreenShot(page, screenShotName)));
						}
							catch (Exception e) {
								System.out.println(e.getLocalizedMessage());
								logger.log(LogStatus.FAIL, e.toString());
							}
						} else if (action.equalsIgnoreCase("verifyText")) {
							String ActualValue = page.innerText(locatorValue);
							try {
								Assert.assertEquals(ActualValue, ExpectedValue);
//								logger.log(LogStatus.PASS, ValueMatched
//										+ logger.addScreenCapture(BaseClass.captureScreenShot(page, screenShotName)));
								logger.log(LogStatus.PASS, loggerMessage
										+ logger.addScreenCapture(BaseClass.captureScreenShot(page, screenShotName)));
							} catch (AssertionError | Exception e) {
								logger.log(LogStatus.FAIL, e.toString());
							}
						} else if (action.equalsIgnoreCase("mouseHover")) {
							try {

								element.hover();
								logger.log(LogStatus.PASS, loggerMessage
										+ logger.addScreenCapture(BaseClass.captureScreenShot(page, screenShotName)));

							} catch (Exception e) {
								System.out.println(e.getLocalizedMessage());
								logger.log(LogStatus.FAIL, e.toString());
							}
						} else if (action.equalsIgnoreCase("mouseHoverClick")) {
							try {

								element.hover();
								element.click();
								logger.log(LogStatus.PASS, loggerMessage
										+ logger.addScreenCapture(BaseClass.captureScreenShot(page, screenShotName)));

							} catch (Exception e) {
								System.out.println(e.getLocalizedMessage());
								logger.log(LogStatus.FAIL, e.toString());
							}
						} else if (action.equalsIgnoreCase("elementPresent")) {

							boolean isDisplayed = true;
							try {
								isDisplayed = element.isVisible();
								logger.log(LogStatus.PASS, loggerMessage
										+ logger.addScreenCapture(BaseClass.captureScreenShot(page, screenShotName)));

							} catch (Exception e) {
								System.out.println(e.getLocalizedMessage());
								logger.log(LogStatus.FAIL, e.toString());
							}
						}

					}
					
					
					/**
					 * @author divya.b.sellamuthu
					 * @Date : 13/10/2021 Handling actions in Frame
					 *
					 *       Frame detected is assigned to Frame object and actions like click and
					 *       sendkeys can be performed inside frame using frame object
					 */
					else if (action.equalsIgnoreCase("frameClick") || action.equalsIgnoreCase("frameSendkeys")) {
						if (action.equalsIgnoreCase("frameClick") && frame.querySelectorAll(locatorValue).size() > 0) {
							try {
								element = frame.querySelector(locatorValue);
								element.click();
								logger.log(LogStatus.PASS, loggerMessage
										+ logger.addScreenCapture(BaseClass.captureScreenShot(page, screenShotName)));

							} catch (Exception e) {
								System.out.println(e.getLocalizedMessage());
								logger.log(LogStatus.FAIL, e.toString());
							}
						} else if (action.equalsIgnoreCase("frameSendkeys")
								&& frame.querySelectorAll(locatorValue).size() > 0) {
							try {
								element = frame.querySelector(locatorValue);
								element.fill(value);
								logger.log(LogStatus.PASS, loggerMessage
										+ logger.addScreenCapture(BaseClass.captureScreenShot(page, screenShotName)));

							} catch (Exception e) {
								System.out.println(e.getLocalizedMessage());
								logger.log(LogStatus.FAIL, e.toString());
							}

						} else {
							logger.log(LogStatus.FAIL, locatorName + " " + element + " = Element Not Present"
									+ logger.addScreenCapture(BaseClass.captureScreenShot(page, screenShotName)));
							page.close();
							Assert.fail("element not Present");
						}

					} else {
						logger.log(LogStatus.FAIL, locatorName + " " + element + " = Element Not Present"
								+ logger.addScreenCapture(BaseClass.captureScreenShot(page, screenShotName)));
						page.close();
						Assert.fail("element not Present");
					}

				}
				
				else if (locatorColValue.equalsIgnoreCase("NA")) {
					locatorName = "NA";
					switch (action) {
					case "open browser":
						base = new BaseClass();
						prop = base.init_properties();
						if (value.isEmpty() || value.equals("NA")) {
							page = base.browser_setup(prop.getProperty("browser"));
							logger.log(LogStatus.PASS, loggerMessage
									+ logger.addScreenCapture(BaseClass.captureScreenShot(page, screenShotName)));
						} else {

							page = base.browser_setup(value);
							logger.log(LogStatus.PASS, loggerMessage
									+ logger.addScreenCapture(BaseClass.captureScreenShot(page, screenShotName)));
						}
						break;
					case "detectFrameByURL":
						frame = page.frameByUrl(value);
						break;
					case "detectFrameByName":
						frame = page.frame(value);
					case "enter url":
						if (value.isEmpty() || value.equals("NA")) {
							page.navigate(prop.getProperty("url"));
							logger.log(LogStatus.PASS, loggerMessage
									+ logger.addScreenCapture(BaseClass.captureScreenShot(page, screenShotName)));
						} else {
							page.navigate(value);
							logger.log(LogStatus.PASS, loggerMessage
									+ logger.addScreenCapture(BaseClass.captureScreenShot(page, "Url_entered")));
						}
						break;
					case "close":
						page.close();
						logger.log(LogStatus.PASS, loggerMessage);
						break;
					case "wait":
						page.waitForTimeout(Double.parseDouble(value));
						break;
					/**
					 * @author divya.b.sellamuthu
					 * @Date : 13/10/2021 Switching tabs
					 *
					 *       Identifies the latest opened tab and assign it to the page object.If a
					 *       click event opens a new tab, it should be called first in excel.
					 */
					case "switchWindow":
						page.waitForTimeout(10000);
						bc = BaseClass.context();
						windows = bc.pages();
						System.out.println("no. of windows is" + windows.size());
						page = windows.get(windows.size() - 1);// refer to the newly opened tab
						System.out.println(page.title());
						break;
					/**
					 * @author divya.b.sellamuthu
					 * @Date : 13/10/2021 Switching tabs
					 *
					 *       Identifies the parent tab and assign it to the page object
					 */
					case "switchDefaultWindow":
						bc = BaseClass.context();
						windows = bc.pages();
						page = windows.get(0);// refer to the parent tab
						System.out.println(page.title());
						break;
					case "frameSize":
						List<Frame> frame_list = page.frames();
						System.out.println("Number of Frames : " + frame_list.size());
						break;
					case "webelementlist":
						List<ElementHandle> elements = page.querySelectorAll(locatorColValue);
						for (int j = 0; j < elements.size(); j++)
							if (elements.get(j).getAttribute("Value").contains(value)) {
								elements.get(j).click();
							}
						break;

					case "Up":
						page.keyboard().press("Up");
						logger.log(LogStatus.PASS, loggerMessage);
						break;

					case "Down":
						page.keyboard().press("Down");
						logger.log(LogStatus.PASS, loggerMessage);
						break;
					case "tab":
						page.keyboard().press("Tab");
						logger.log(LogStatus.PASS, loggerMessage);
						break;
					case "enter":
						page.keyboard().press("Enter");
						logger.log(LogStatus.PASS, loggerMessage);
						break;

					case "scrollDown":
						page.evaluate("window.scrollBy(0,2000)");
						logger.log(LogStatus.PASS, loggerMessage);
						break;

					case "scrollBottomPage":
						page.evaluate("window.scrollTo(0, document.body.scrollHeight)");
						logger.log(LogStatus.PASS, loggerMessage
								+ logger.addScreenCapture(BaseClass.captureScreenShot(page, screenShotName)));
						break;

					case "scrollTopPage":
						page.evaluate("window.scrollTo(document.body.scrollHeight,0)");
						logger.log(LogStatus.PASS, loggerMessage
								+ logger.addScreenCapture(BaseClass.captureScreenShot(page, screenShotName)));
						break;

					case "refresh":
						page.reload();
						break;
					/**
					 * @author divya.b.sellamuthu
					 * @Date : 13/10/2021 Handling alerts
					 *
					 *       USer should call the event which triggers the pop-up after alertAccept
					 *       in excel. alertAccept is a listener which starts listening for the
					 *       dialog box
					 */
					case "alertAccept":
						page.onceDialog(dialog -> {
							dialog.accept();
							System.out.println(dialog.message());
						});
						logger.log(LogStatus.PASS, loggerMessage);
						break;
					case "alertDismiss":
						page.onceDialog(dialog -> {
							System.out.println(dialog.type());
							dialog.dismiss();
						});
						logger.log(LogStatus.PASS, loggerMessage);
						break;
					case "API_GET":
						playwright = Playwright.create();
						apiRequestContext = playwright.request().newContext(new APIRequest.NewContextOptions());
						byte[] responseBody = apiRequestContext.get(value).body();
						System.out.println("Response Body is =>  " + responseBody);
						int responseStatusCode = apiRequestContext.get(value).status();
						System.out.println("Status Code => "+ responseStatusCode);
						apiRequestContext.dispose();
						break;
					case "API_PUT":
						playwright = Playwright.create();
						apiRequestContext = playwright.request().newContext(new APIRequest.NewContextOptions());
						String[] arrOfvalues = value.split("::");
						String[] arrOfStr = arrOfvalues[0].split(",");
						Map<String,String> data = new HashMap<>();
						for(int j=0;j<=arrOfStr.length-1;j++)
						{
				    		String[] arrOfjson = arrOfStr[j].split(":");
				    		 data.put(arrOfjson[0], arrOfjson[1]);
				    	}
						String response = apiRequestContext.put(arrOfvalues[1], RequestOptions.create().setData(data)).text();
						System.out.println("Response => " +response);
				 		System.out.println("Response code => " +apiRequestContext.put(arrOfvalues[1]).status());
				 		apiRequestContext.dispose();
						break;	
					case "API_POST":
						playwright = Playwright.create();
						apiRequestContext = playwright.request().newContext(new APIRequest.NewContextOptions());
						String[] arrOfvalues1 = value.split("::");
						String[] arrOfStr1 = arrOfvalues1[0].split(",");
						Map<String,String> data1 = new HashMap<>();
						for(int j=0;j<=arrOfStr1.length-1;j++)
						{
				    		String[] arrOfjson = arrOfStr1[j].split(":");
				    		 data1.put(arrOfjson[0], arrOfjson[1]);
				    	}
						String response1 = apiRequestContext.post(arrOfvalues1[1], RequestOptions.create().setData(data1)).text();
						System.out.println("Response => " +response1);
				 		System.out.println("Response code => " +apiRequestContext.post(arrOfvalues1[1]).status());
				 		apiRequestContext.dispose();
						break;
					case "API_DELETE":
						playwright = Playwright.create();
						apiRequestContext = playwright.request().newContext(new APIRequest.NewContextOptions());
						String[] arrOfvalues2 = value.split("::");
						String[] arrOfStr2 = arrOfvalues2[0].split(",");
						Map<String,String> data2 = new HashMap<>();
						for(int j=0;j<=arrOfStr2.length-1;j++)
						{
				    		String[] arrOfjson = arrOfStr2[j].split(":");
				    		 data2.put(arrOfjson[0], arrOfjson[1]);
				    	}
						String response2 = apiRequestContext.delete(arrOfvalues2[1], RequestOptions.create().setData(data2)).text();
						System.out.println("Response => " +response2);
				 		System.out.println("Response code => " +apiRequestContext.delete(arrOfvalues2[1]).status());
				 		apiRequestContext.dispose();
						break;
					default:
						break;
					}
				}
			} catch (Exception e) {
				System.out.print(e.toString());
				logger.log(LogStatus.FAIL, e.toString());
			}

		}
	}

}
