package com.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ooxml.POIXMLProperties.CoreProperties;
import org.apache.poi.sl.usermodel.Placeholder;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTable;
import org.apache.poi.xslf.usermodel.XSLFTableCell;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;


public class PPT {

	public static void main(String[] args) throws IOException
	{
	  
	   File fileName = new File("./src/com/utility/SamplePPT.pptx");
	   FileInputStream inputStream;
	   try {
	      inputStream = new FileInputStream(fileName);
	   } 
	   catch (FileNotFoundException e)
	   {
	      e.printStackTrace();
	      return;
	   }
	   XMLSlideShow ppt;
	   try 
	   {
	      ppt = new XMLSlideShow(inputStream);
	   }
	   catch (IOException e) 
	   {
	      e.printStackTrace();
	      return;
	   }
	   readPPT(ppt);	
	}
	
	public static void readPPT(XMLSlideShow ppt)
	{
	   CoreProperties props = ppt.getProperties().getCoreProperties();
	   String title = props.getTitle();
	   System.out.println("Title: " + title); 
	   for (XSLFSlide slide: ppt.getSlides())
	   {
	      System.out.println("Starting slide...");
	      System.out.println("slide count: "+slide.getSlideNumber());     
	      System.out.println("*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&New Slide*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&");
	
	      List<XSLFShape> shapes = slide.getShapes();
	      for (XSLFShape shape: shapes) {
	         if (shape instanceof XSLFTextShape)
	         {	        	 
        	 XSLFTextShape textShape = (XSLFTextShape)shape;	
        	 for (XSLFTextParagraph p : textShape) {	        		
        		  for (XSLFTextRun r : p) {
    			  if(textShape.getTextType()==Placeholder.TITLE)//All PPT title are in Upper case
        			   {     		       
        				  if(!r.getRawText().trim().isEmpty())// should ignore the empty text
        				  {  
        					System.out.println("Text : "+r.getRawText().toUpperCase());
  		        		    System.out.println("Font Size : "+r.getFontSize());
  		        		    System.out.println("Font Family : "+r.getFontFamily());	
  		        		    System.out.println("Font Color : "+r.getFontColor());// in Hex code
        				  }
	        		    
        			    }	        			  
        			  else if(textShape.getTextType()==Placeholder.CENTERED_TITLE)//All PPT title are in Upper case
  			        	{	       				  
        				  if(!r.getRawText().trim().isEmpty())
        				  {
			      		    System.out.println("Text : "+r.getRawText().toUpperCase());
			      		    System.out.println("Font Size : "+r.getFontSize());
			      		    System.out.println("Font Family : "+r.getFontFamily());	
			      		    System.out.println("Font Color : "+r.getFontColor());
      			        	}
  			        	}	 
        			  else if(textShape.getTextType()==Placeholder.FOOTER)
		   			   {     		       
		   				  if(!r.getRawText().trim().isEmpty())// should ignore the empty text
		   				  {
		   					  	System.out.println("Text : "+r.getRawText());
				        		    System.out.println("Font Size : "+r.getFontSize());
				        		    System.out.println("Font Family : "+r.getFontFamily());	
				        		    System.out.println("Font Color : "+r.getFontColor());// in Hex code
		   				  }
		       		    
		   			    }	
	        		  else
      			        {	        			
	        			  if(!r.getRawText().trim().isEmpty())// contents of the text in the PPT Body
        				  {
			      		    System.out.println("Text : "+r.getRawText());
			      		    System.out.println("Font Size : "+r.getFontSize());
			      		    System.out.println("Font Family : "+r.getFontFamily());	
			      		    System.out.println("Font Color : "+r.getFontColor());
      			          }
	      			    }	        			  
	        		  }
	        		  System.out.println("***********************************************************************");
	        	 }      		         
	         
	         } 
	         else if (shape instanceof XSLFTable)// if the PPT contains table
	         {
	        	 XSLFTable table = (XSLFTable)shape;
		            {			            
	            	 int cols =table.getNumberOfColumns();
	            	 int rows =table.getNumberOfRows();
	            	 System.out.println("--No of Rows---" + rows);
	            	 System.out.println("--No of Columns---" + cols);
	            	 for(int i=0;i<rows;i++)
		            	 {
		            		 for(int j=0;j<cols;j++)
		            		 {
		            			 XSLFTableCell tc =table.getCell(i, j);	
		            			 for (XSLFTextParagraph p : tc) {	        		
		       	        		  for (XSLFTextRun r : p) {	
		       	        			if(!r.getRawText().trim().isEmpty())
			        				  {
		       		        		    System.out.println("TABLE TEXT : "+r.getRawText());
		       		        		    System.out.println("TABLE Font Size : "+r.getFontSize());
		       		        		    System.out.println("TABLE Font Family : "+r.getFontFamily());	
		       		        		    System.out.println("TABLE Font Color : "+r.getFontColor());
		       		        		    System.out.println("TABLE Background color "+tc.getFillColor());
			        				  }
		       	        		  }
		            		 }
		            		 System.out.println("*************************************************************************************************");
		             }		            	 
		           }
	            	 
	            }                 
	          }	      
	        }    
	      } 	

	}
}
