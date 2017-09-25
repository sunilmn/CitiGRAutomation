/*
 * @author prajendra
 * 
 * 
 */

package com.citi.fileActivity;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.citi.gr.CitiMain;
import com.citi.util.Constants;

public class OutputOperations {

	public void writeToResultFile(File resultFile, String testCaseName, String country, String tierName,String language,String memberID,
			String accountNumber, String reportStatus) {
		
		Writer out = null;
		try {
			String lineToWrite = testCaseName + ";" + country + ";" + tierName + ";" + language + ";" +memberID +";"+accountNumber + ";"
					+ reportStatus;		

			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resultFile, true), "UTF8"));
			out.append(lineToWrite).append("\r\n");
			out.flush();

		} catch (IOException e) {
			System.out.println("writeToResultFile ->"+e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void saveScreenshotToFile(WebDriver webDriver, Map<String, Object> eachRowMap ) {

		
		String tierName = (String) eachRowMap.get("TierName");
		if(tierName==null)
			tierName="Default";
		String accountNumber = (String) eachRowMap.get("AccountNumber");
		if(accountNumber==null)
			accountNumber="-";
		String testCase = (String) eachRowMap.get("TestCase");
		// Take screenshot and store as a file format
		File src = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		try {
			DateFormat dateFormat = new SimpleDateFormat("ddMMMyy-HH-mm-ss");
			Date currentdate=new Date();
			System.out.println("saveScreenshotToFile"+dateFormat.format(currentdate)); 
			String currentDate=dateFormat.format(currentdate);
			// now copy the screenshot to desired location using copyFile method
			FileUtils.copyFile(src, new File(CitiMain.screenShotFolderName+"/"+testCase+"_"+tierName +"_"+accountNumber+"_"+currentDate+".gif"));
			
			
			/*
			Robot robot = new Robot();
			BufferedImage screenShot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			ImageIO.write(screenShot, "JPG", new File("screenshots/abc/"+testCase+"_"+tierName +"_"+accountNumber+"_"+currentDate+".gif"));
			*/
		}
		catch (Exception e)
		{
			System.out.println("saveScreenshotToFile ->"+e.getMessage());
			e.printStackTrace();
		}

	}
	
	
	
	/*
	 * Takes one screenshot and returns reportStatus
	 * 
	 */
	public String statusReporter(String errorDetails, Map<String, Object> eachRowMap, WebDriver webDriver) {

		String reportStatus = "";
		if (errorDetails.isEmpty())
			reportStatus = Constants.PASS_REPORT;
		else {
			reportStatus = Constants.FAIL_SEMICOLON + errorDetails;
			saveScreenshotToFile(webDriver, eachRowMap);
		}
		return reportStatus;
	}

	/*
	 * Returns reportStatus
	 * 
	 */
	public String statusReporter(String errorDetails) {

		String reportStatus = "";
		if (errorDetails.isEmpty())
			reportStatus = Constants.PASS_REPORT;
		else
			reportStatus = Constants.FAIL_SEMICOLON + errorDetails;

		return reportStatus;
	}

}
