package com.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.BasePropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.search.ComparisonMode;
import microsoft.exchange.webservices.data.core.enumeration.search.ContainmentMode;
import microsoft.exchange.webservices.data.core.enumeration.search.LogicalOperator;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.core.service.schema.EmailMessageSchema;
import microsoft.exchange.webservices.data.core.service.schema.ItemSchema;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;
import microsoft.exchange.webservices.data.search.filter.SearchFilter;

public class BaseClass {

	public Page page;
	public static BrowserContext bc;
	public Properties prop;
	public Browser browser;
	ConfigReader conf = new ConfigReader();
	static XSSFWorkbook wb;
	public static XSSFSheet sh;
	FileInputStream fs;
	public static XSSFCell cell;
	public static XSSFRow row;
	public final String excel_path = "./src/com/utility/Excel_validation.xlsx";
	public final String pdf_path = "./src/com/utility/PDF_validation.pdf";

	/*
	 * Method to initiatlize browser drivers based on browsername given in test
	 * scenarios sheet/properties file
	 */
	public Page browser_setup(String browserName) {

		if (browserName.equalsIgnoreCase("Firefox")) {
			browser = Playwright.create().chromium().launch(new BrowserType.LaunchOptions().setChannel("firefox")
					.setHeadless(false).setArgs(Arrays.asList("--start-maximized")));
		} else if (browserName.equalsIgnoreCase("Chrome")) {
			browser = Playwright.create().chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome")
					.setHeadless(false).setArgs(Arrays.asList("--start-maximized", "--start-fullscreen")));

		} else if (browserName.equalsIgnoreCase("EDGE")) {
			browser = Playwright.create().chromium().launch(new BrowserType.LaunchOptions().setChannel("msedge")
					.setHeadless(false).setArgs(Arrays.asList("--start-maximized")));
		}

		bc = browser.newContext();
		Page page = bc.newPage();
		page.setViewportSize(1900, 1000); //1300,600
		return page;

	}

	/*
	 * Method to return current browser context
	 */
	public static BrowserContext context() {
		return bc;

	}

	/*
	 * Method to read key value pairs from property file
	 */
	public Properties init_properties() {

		prop = new Properties();
		try {

			File src = new File("./src/com/utility/resources/Config.property");
			FileInputStream fs = new FileInputStream(src);

			prop.load(fs);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return prop;
	}

	/*
	 * Method to capture UI snippets and save them in ScreenShots folder at project
	 * level
	 */
	public static String captureScreenShot(Page page, String fileName) throws IOException {
		Path currentPath = Paths.get(System.getProperty("user.dir"));
		Path filePath = Paths.get(currentPath.toString(), "screenshots", fileName + "_" + timestamp() + ".png");
		page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(filePath.toString())).setFullPage(false));
		return filePath.toString();
	}
	
	public static String specificCaptureScreenShot(Page page, String fileName1,String locatorValue) throws IOException {
		Path currentPath = Paths.get(System.getProperty("user.dir"));
		Path filePath = Paths.get(currentPath.toString(), "screenshots", fileName1 + "_" + timestamp() + ".png");
		//page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(filePath.toString())).setFullPage(false));
		page.locator(locatorValue).screenshot(new Locator.ScreenshotOptions().setPath(Paths.get(filePath.toString())));
		return filePath.toString();
	}

	/*
	 * This method returns latest date and timestamp
	 */
	public static String timestamp() {
		return new SimpleDateFormat("MMddYYYYHHMMSS").format(new Date());
	}

	/*
	 * Method to trigger email with test report(extent report/excel report) as an
	 * attachment through ESO mailbox
	 */

	public void sendEmail(String subject, String env, String info) {
		String username = "testing.india.2791@accenture.com";
		String password = "uEd9yBm7lKv5c1t4(";
		String path = System.getProperty("user.dir") + "\\TestReport.html";

		ExchangeService service = new ExchangeService();
		ExchangeCredentials credentials = new WebCredentials(username, password);
		service.setCredentials(credentials);

		try {
			java.net.URI uri = new java.net.URI("https://myemail.accenture.com/ews/exchange.asmx");
			service.setUrl(uri);
			EmailMessage replymessage = new EmailMessage(service);
			EmailAddress fromEmailAddress = new EmailAddress(username);
			replymessage.setFrom(fromEmailAddress);
			//add recipient email
			replymessage.getToRecipients().add("");
			replymessage.setSubject(subject);
			replymessage.setBody(new MessageBody(
					"<h2>Test Automation Execution Details :" + info + "</h2>\r\n" + "<ul>Execution Environment Name : "
							+ "<b>" + env + "</b>" + " </ul><p>Test Execution Report is Enclosed</p>"));

			replymessage.getAttachments().addFileAttachment(path);

			replymessage.send();

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	/**
	 * @author divya.b.sellamuthu
	 * @Date : 13/10/2021 PDF read/write
	 *
	 *       This method reads the PDF PDF_Validation.pdf under utility package and
	 *       also creates a newpdf.pdf
	 */
	public void PDF_validation() throws IOException {
		// Loading an existing document
		File file = new File(pdf_path);
		PDDocument document = PDDocument.load(file);
		// Instantiate PDFTextStripper class
		PDFTextStripper pdfStripper = new PDFTextStripper();
		// Retrieving text from PDF document
		String text = pdfStripper.getText(document);
		System.out.println(text);
		// Closing the document
		document.close();
		PDDocument document1 = new PDDocument();
		PDPage my_page = new PDPage();
		document1.addPage(my_page);
		PDPageContentStream contentStream = new PDPageContentStream(document1, my_page);
		contentStream.beginText();
		contentStream.newLineAtOffset(25, 700);
		contentStream.setFont(PDType1Font.COURIER_BOLD, 12);
		text = text + " write in pdf";
		text = text.replace("\n", "").replace("\r", "");
		contentStream.showText(text);
		contentStream.endText();
		contentStream.close();
		document1.save("./newpdf.pdf");
		document1.close();

	}

	/**
	 * @author divya.b.sellamuthu
	 * @Date : 13/10/2021 Excel read/write
	 *
	 *       This method reads the cell value from Excel_validation.xlsx under
	 *       utility package and also updates cell value
	 */
	public void Excel_validation(String sheetname) throws IOException {
		try {
			File file = new File(excel_path);
			FileInputStream fis = new FileInputStream(file);
			wb = new XSSFWorkbook(fis);
			sh = wb.getSheet(sheetname);
			String data = sh.getRow(0).getCell(1).toString().trim();
			System.out.println("Excel data for A1 cell is " + data);
			XSSFCell cell1 = sh.getRow(2).getCell(1);
			cell1.setCellValue(data + "updated");
			FileOutputStream outputStream = new FileOutputStream(file);
			wb.write(outputStream);
			wb.close();
			outputStream.close();

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * @author divya.b.sellamuthu
	 * @Date : 13/10/2021 Ag-grid validation This method iterates over rows(top to
	 *       bottom) and columns(Left to right for odd rows and right to left for
	 *       even rows) in a table User has to change the xpath below to iterate
	 *       over the rows and columns
	 */

	public void ag_grid_validation() {
		boolean proceed = true;
		int i = 0;
		int even = 2;
		while (proceed) {
			try {
				page.focus("//div[@row-index=" + i + "]/div[@col-id='firstColumn']");
				// System.out.println(page.innerText("//div[@row-index="+i+"]/div[@col-id='firstColumn']")+"-"+page.innerText("//div[@row-index="+i+"]/div[9]"));
				if (even % 2 == 0) {
					for (int n = 213; n < 225; n++) {
						page.focus("//div[@row-index=" + i + "]/div[@col-id='" + n + "_salesTotalAmt']");
						System.out.println(page.innerText("//div[@row-index=" + i + "]/div[@col-id='firstColumn']")
								+ "-"
								+ page.innerText("//div[@row-index=" + i + "]/div[@col-id='" + n + "_salesTotalAmt']"));
					}
				} else {
					for (int n = 224; n > 212; n--) {
						page.focus("//div[@row-index=" + i + "]/div[@col-id='" + n + "_salesTotalAmt']");
						System.out.println(page.innerText("//div[@row-index=" + i + "]/div[@col-id='firstColumn']")
								+ "-"
								+ page.innerText("//div[@row-index=" + i + "]/div[@col-id='" + n + "_salesTotalAmt']"));
					}
				}

			} catch (Exception e) {
				proceed = false;
				System.out.println(e);
			}

			i = i + 1;
			even = even + 1;

		}
		System.out.println("Total rows in ag-grid=" + i);

	}

	/**
	 * @author divya.b.sellamuthu
	 * @Date : 21/10/2021 Method to get cell value from spreadsheet
	 */
	public void spreadsheet() {

		page.evaluate("window.testSpread = new GC.Spread.Sheets.findControl(document.getElementById('ss'))");
		Object value = page.evaluate(" window.testSpread.getActiveSheet().getValue(0,0);");
		System.out.println(value.toString());
	}

	/**
	 * @author divya.b.sellamuthu
	 * @Date : 21/10/2021 Method to read email based on subject
	 */
	public void Read_Email(String recipient) throws InterruptedException, URISyntaxException {
		String username = "testing.india.2791@accenture.com";
		String password = "uEd9yBm7lKv5c1t4(";
		ExchangeService service = new ExchangeService();
		ExchangeCredentials credentials = new WebCredentials(username, password);
		service.setCredentials(credentials);
		java.net.URI uri = new java.net.URI("https://myemail.accenture.com/ews/exchange.asmx");
		service.setUrl(uri);
		try {
			EmailMessage replymessage = new EmailMessage(service);
			EmailAddress fromEmailAddress = new EmailAddress(username);
			SearchFilter subjectFilter = new SearchFilter.ContainsSubstring(ItemSchema.Subject,
					"PLAYWRIGHT POC-READ EMAIL verification", ContainmentMode.Substring, ComparisonMode.IgnoreCase);
			ItemView itemview = new ItemView(100);
			FindItemsResults findResults = service.findItems(WellKnownFolderName.Inbox,
					new SearchFilter.SearchFilterCollection(LogicalOperator.And, subjectFilter), itemview);
			System.out.println("No. of records: " + findResults.getTotalCount());
			PropertySet set = new PropertySet(BasePropertySet.FirstClassProperties, EmailMessageSchema.Attachments,
					EmailMessageSchema.MimeContent);
			set.setRequestedBodyType(BodyType.Text);
			service.loadPropertiesForItems(findResults, set);
			ArrayList<Item> Mailitems = findResults.getItems();
			for (Item mail : Mailitems) {
				System.out.println("Subject: " + mail.getSubject());
				System.out.println("Email Size: " + mail.getSize());
				System.out.println("Sender: " + ((EmailMessage) mail).getSender().getName());
				System.out.println("Date of Sent: " + mail.getDateTimeSent());
				System.out.println("Date of Received: " + mail.getDateTimeReceived());
				System.out.println("Mail Content: " + mail.getBody());
				String Mail_content = mail.getBody().toString();
				// assertTrue(Mail_content.contains("Email validation scenario for POC"));
			}
		} catch (Exception e) {
			System.out.println(e);

		}
	}
}