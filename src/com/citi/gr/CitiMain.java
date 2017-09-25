/*
 * @author prajendra
 * 
 * 
 */

package com.citi.gr;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.citi.fileActivity.ImageFilesOperations;
import com.citi.fileActivity.ExcelRead;
import com.citi.fileActivity.HTMLReportGenerator;
import com.citi.fileActivity.OutputOperations;
import com.citi.testCase.EntryMode;
import com.citi.testCase.MegaSubMenu;
import com.citi.testCase.MyAccount;
import com.citi.testCase.MyProfile;
import com.citi.util.Constants;

public class CitiMain {

	// static String inputTestCaseFile = "//pirwcorpfs01.corp.epsilon.com/shared/CitiGR_Automation/TestDataCitiGR.xlsx";
	static String inputTestCaseFile = "TestDataCitiGR.xlsx";
	static String xpathIdDataFile = "XPathIdData.xlsx";
	static File resultTextFile = new File("result.txt");
	static File reportHTMLFile = new File("Report.html");
	public static String screenShotFolderName="screenshots";
	public static String screenShotBackupFolderName="screenshotBackup";

	static Map<String, Map<String, Object>> configurationMap = new LinkedHashMap<String, Map<String, Object>>();
	static Map<String, String> testCaseSheetMasterMap = new LinkedHashMap<String, String>();
	static List<String> executableTestCaseMasterList = new ArrayList<String>();
	static Map<String, List<Map<String, Object>>> testCaseSheetsDataMap = new LinkedHashMap<String, List<Map<String, Object>>>();
	public static Map<String, String> xpathIDMap = new LinkedHashMap<String, String>();
	
	public static String environmentUrl = "";
	static String browserName = "";

	@Test(priority = 1)
	public void main() {
		// public static void main(String a[]) {

		prepareTestData();
		runTestCases();
	}

	public static void prepareTestData() {

		ExcelRead excelRead = new ExcelRead();
		try {

			configurationMap = excelRead.getConfigurationSheet(inputTestCaseFile);
			System.out.println("configurationMap..." + configurationMap);

			// (Sheet name, testCaseName)
			testCaseSheetMasterMap = excelRead.getMasterMap(inputTestCaseFile);
			System.out.println("testCaseMasterMap..." + testCaseSheetMasterMap);

			executableTestCaseMasterList = excelRead.getTestCaseMasterSheet(inputTestCaseFile);
			System.out.println("testCaseMasterList..." + executableTestCaseMasterList);
			// sheetMap<sheetName, executableRowList[{key=value,key=value,key=value}]>
			testCaseSheetsDataMap = excelRead.readInputTestCaseFile(inputTestCaseFile, executableTestCaseMasterList);
			System.out.println("testCaseSheetsDataMap...." + testCaseSheetsDataMap);

			// read xpathIdDataFile excel file
			xpathIDMap = excelRead.readXPathData(xpathIdDataFile);
			System.out.println("xpathIdMap....." + xpathIDMap);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void runTestCases() {

		
		ImageFilesOperations imageOps = new ImageFilesOperations();

		if (resultTextFile.exists()) {
			resultTextFile.delete();
		}
		if (reportHTMLFile.exists()) {
			reportHTMLFile.delete();
		}
		imageOps.clearScreenshots(screenShotFolderName);

		try {

			DateFormat dateFormat = new SimpleDateFormat("ddMMMyyyy HH:mm:ss");
			Date startRun = new Date();
			String startRunTime = dateFormat.format(startRun);

			// get url from the configuration sheet
			for (Entry<String, Map<String, Object>> entry : configurationMap.entrySet()) {
				String key = entry.getKey().toString().trim();
				Map<String, Object> configValuesMap = entry.getValue();
				if (key.equalsIgnoreCase("Environment"))
					environmentUrl = (String) configValuesMap.get("Value");

				if (key.equalsIgnoreCase("Browser"))
					browserName = (String) configValuesMap.get("Value");

			}

			
			//////////////////////////////////////////////////////////////
			// main iteration - for every sheet
			for (Entry<String, List<Map<String, Object>>> entry : testCaseSheetsDataMap.entrySet()) {

				
				String sheetNameKey = entry.getKey().trim();
				String testCaseName = testCaseSheetMasterMap.get(sheetNameKey).trim();
				List<Map<String, Object>> executableRowsList = new ArrayList<Map<String, Object>>();
				executableRowsList = entry.getValue();
				
				CitiMainAuxiliary auxiliary = new CitiMainAuxiliary();

				switch (testCaseName) {

				case "Anonymous Mode Log In":
					auxiliary.runAnonMode(executableRowsList, testCaseName);
					break;

				case "Anonymous Mode Megamenu":
					auxiliary.runAnonMegaMenu(executableRowsList, testCaseName);
					break;

				case "Anonymous Mode Megamenu Submenu":
					auxiliary.runAnonMegaMenuSubmenu(executableRowsList, testCaseName);
					break;

				case "Login Mode":
					auxiliary.runLogin(executableRowsList, testCaseName);
					break;

				case "Login Mode Megamenu":
					auxiliary.runLogonMegaMenu(executableRowsList, testCaseName);
					break;

				case "Login Mode Megamenu Submenu":
					auxiliary.runLogonMegaMenuSubmenu(executableRowsList, testCaseName);
					break;

				case "My Account Order History":
				case "My Account My Points Summary":
				case "My Account My Profile":
				case "My Account Shop with Points":
					auxiliary.runSingleMyAccount(executableRowsList, testCaseName);
					break;

				case "My Account Menu":
					auxiliary.runMyAccountMenus(executableRowsList, testCaseName);
					break;

				case "CompleteLogin":
					auxiliary.runCompleteLogin(executableRowsList, testCaseName);
					break;
				
				case "Edit Email id in Profile Page":
					auxiliary.runEditEmail(executableRowsList, testCaseName);
					break;
				
				case "Add Edit Loyalty Program in Profile Page":
					auxiliary.runAlterLoyalty(executableRowsList, testCaseName);
					break;
					
				case "Logged-in Mode - FAQ and Contact Us":
					auxiliary.runFaqContactUs(executableRowsList, testCaseName);
					break;
				
				case "OBO Mode Login":
					auxiliary.runOBOLogin(executableRowsList, testCaseName);
					break;
					
				case "Cookied Mode Login":
					auxiliary.runCookiedLogin(executableRowsList, testCaseName);
					break;
				

				default:
					break;

				}

			} // end-for testCaseSheetsDataMap
			////////////////////////////////////////////////////////////////////

			Date endRun = new Date();
			String endRunTime = dateFormat.format(endRun);

			if (resultTextFile.exists()) {
				HTMLReportGenerator htmlReport = new HTMLReportGenerator();
				htmlReport.reportGenerator("result.txt", reportHTMLFile, testCaseSheetMasterMap, environmentUrl,
						startRunTime, endRunTime);
			}		
			
			
			
			imageOps.imageCopy(screenShotFolderName);
			
		

			System.out.println("End Main");
			
		} catch (Exception e) {
			// webDriver.close();
			e.printStackTrace();
		}

	}

}
