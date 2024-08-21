package testNgRunner;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.ITestNGListener;
import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import com.utility.BaseClass;

public class TestNgCodeRunner {

	BaseClass base = new BaseClass();

	/*
	 * Method to trigger execution of test classes mentioned in
	 * ./src/RunnerExcel/Runner.xlsx file
	 */
	@Test
	public void testNgXMLSuite() throws IOException {
		FileInputStream fs = new FileInputStream("./src/RunnerExcel/Runner.xlsx");
		XSSFWorkbook wb = new XSSFWorkbook(fs);
		XSSFSheet sheet1 = wb.getSheet("runner");
		int rows = sheet1.getPhysicalNumberOfRows();
		int columns = sheet1.getRow(0).getPhysicalNumberOfCells();
		List<XmlSuite> suites = new ArrayList<XmlSuite>();
		List<XmlClass> classes = new ArrayList<XmlClass>();
		List<Class<? extends ITestNGListener>> listenerClasses = new ArrayList<Class<? extends ITestNGListener>>();

		XmlSuite suite = new XmlSuite();
		suite.setName("suite");
		XmlTest test = new XmlTest(suite);
		test.setName("test");
		TestNG tng = new TestNG();
		tng.setXmlSuites(suites);
		suites.add(suite);
		test.setXmlClasses(classes);
		for (int i = 1; i < rows; i++) {
			try {
				if (sheet1.getRow(i).getCell(0).toString().contains("Y")) {
					String className = sheet1.getRow(i).getCell(1).toString();
					XmlClass class1 = new XmlClass(className);
					classes.add(class1);
					tng.setListenerClasses(listenerClasses);
					listenerClasses.add(ListenerTest.class);
				} else {
					System.out.println("Test Not marked as Yes");
				}
			} catch (NullPointerException e) {
				System.out.println(e);
			}

		}
		tng.run();
		wb.close()	;
		try {
			base.sendEmail("Automation Testing", "Functional", "Test");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
