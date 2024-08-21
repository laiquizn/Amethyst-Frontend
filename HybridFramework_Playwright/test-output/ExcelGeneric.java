package com.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//--Cell Level Import



public class ExcelGeneric {

	public XSSFWorkbook wb;
	public XSSFSheet sh;
	FileInputStream fs;
	public XSSFCell cell;
	public ExcelGeneric(String excelPath) {

		try {
			
			FileInputStream fs = new FileInputStream(new File(excelPath));
			wb = new XSSFWorkbook(fs);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getData(int row, int col, String sheetName) {
		sh = wb.getSheet(sheetName);
		DataFormatter form = new DataFormatter();
		//Cell celval = sh.getRow(row).getCell(col);
		cell=sh.getRow(row).getCell(col);
		String val = form.formatCellValue(cell);
		return val;
	}

	public int getRow(String sheetName) {
		int row = wb.getSheet(sheetName).getLastRowNum();
	

		return row + 1;
	
	}

	public void closeWorkBook()
	{
		try {
			wb.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
}
