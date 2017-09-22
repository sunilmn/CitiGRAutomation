/**
 * 
 */
package com.citi.testCase;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.citi.fileActivity.OutputOperations;
import com.citi.gr.CitiMain;
import com.citi.util.Constants;
import com.citi.util.ExcelUtil;
import com.citi.util.WebUtil;

/**
 * @author prajendra
 *
 */
public class EntryMode {

	String url = "";
	Map<String, String> xpathIDMap = new LinkedHashMap<String, String>();
	WebUtil webUtil = new WebUtil();
	ExcelUtil excelUtil = new ExcelUtil();
	OutputOperations outputOp = new OutputOperations();
	
	
	public EntryMode() {
		this.url = CitiMain.environmentUrl;
		this.xpathIDMap = CitiMain.xpathIDMap;
	}

	/*
	 * 
	 * Get the url and open it in browser, check if the country selection page
	 * loads by comparing the title
	 * 
	 */
	public String openWebPage(WebDriver webDriver) {
		String reportStatus = "";
		System.out.println("URL : " + url);
		try {
			webDriver.get(url);
			webDriver.manage().window().maximize();
			String expectedTitle = "Welcome to Citi Rewards | Please Select Your Country";

		
			// Assert.assertEquals(url, driver.getCurrentUrl());
			Assert.assertEquals(expectedTitle, webDriver.getTitle());
			reportStatus = Constants.PASS + "-";
		} catch (Exception e) {
			reportStatus = Constants.FAIL + url + "  ->CountrySelectionPage not loaded. ";
			e.printStackTrace();
		}
		return reportStatus;
	}

	/*
	 * 
	 * Select the country in first page and see if index page opens by checking
	 * if signon id is present in index page
	 * 
	 */

	public String selectCountry(WebDriver webDriver, String country) {
		String reportStatus = "";
		Boolean countryFound=false;
		System.out.println("Country selected:" + country);
		
		try {
			
			try {
				webDriver.findElement(By.partialLinkText(country)).click();
				countryFound=true;
			} catch (NoSuchElementException e) {
				reportStatus = Constants.FAIL + country+" not found. ";
				countryFound=false;
			}
			if(countryFound==true)	
			{
				WebElement element = webDriver.findElement(By.id("header-signon"));
				if (element != null) {
					reportStatus = Constants.PASS + "-";
				}
			}
		} catch (Exception e) {
			reportStatus = Constants.FAIL + "Index Page not loaded. ";
			e.printStackTrace();
		}
		return reportStatus;
	}


	/*
	 * 
	 * Click on SignOn in index page to open the mockLogin page
	 * 
	 */

	public void clickSignOn(WebDriver webDriver) {
		
		try {
			
			webDriver.findElement(By.id(xpathIDMap.get("SignOn_Id"))).click();
			//Thread.sleep(2000);
			System.out.println("Signon button Clicked");
		} catch (Exception e) {
			System.out.println("In clickSignOn->" + e);
			e.printStackTrace();
		}
	}

	/*
	 * 
	 * In the mockLogin page , enter all the details.
	 * If logged in ,it has to navigate to Index page, 
	 * confirm it by checking if
	 * 'sign off' id is present in Index page
	 * 
	 */
	public String login(WebDriver webDriver, Map<String, Object> eachRowMap) {

		String reportStatus = "";

		System.out.println("Entering Logon Credentials");

		// driver.findElement(By.id(xpathMap.get("GRID_Id"))).clear();
		// driver.findElement(By.id(xpathMap.get("GRID_Id"))).sendKeys((String)
		// rowMap.get("GRID"));

		// Enter Country code
		// driver.findElement(By.id(xpathMap.get("CountryCode_Id"))).clear();
		// driver.findElement(By.id(xpathMap.get("CountryCode_Id"))).sendKeys((String)
		// rowMap.get("CountryCode"));
		try {

			// Enter Source Code
			webDriver.findElement(By.id(xpathIDMap.get("SourceCode_Id"))).clear();
			webDriver.findElement(By.id(xpathIDMap.get("SourceCode_Id")))
					.sendKeys((String) eachRowMap.get("SourceCode"));

			// Enter Language Code
			webDriver.findElement(By.id(xpathIDMap.get("LanguageCode_Id"))).clear();
			webDriver.findElement(By.id(xpathIDMap.get("LanguageCode_Id")))
					.sendKeys((String) eachRowMap.get("LanguageCode"));

			// Enter Account number
			webDriver.findElement(By.id(xpathIDMap.get("AccountNumber_Id"))).clear();
			webDriver.findElement(By.id(xpathIDMap.get("AccountNumber_Id")))
					.sendKeys((String) eachRowMap.get("AccountNumber"));

			// click on Login button
			System.out.println("Clicking on Login Button");
			String xpath = xpathIDMap.get("Logon_Id");

			webDriver.findElement(By.xpath(xpath)).click();
			//Thread.sleep(9000);

			WebElement element = webDriver.findElement(By.id(xpathIDMap.get("SignOff_Id")));
			if (element != null) {
				reportStatus = Constants.PASS + "-";
			}
		} catch (Exception e) {
			reportStatus = Constants.FAIL + "Could not login. " + Constants.TAB;
			e.printStackTrace();
		}
		return reportStatus;

	}

	/*
	 * 
	 * click on sign-off. It is successful if redirected to country selection
	 * page
	 * 
	 */
	public String clickSignOff(WebDriver webDriver) {
		String reportStatus = "";
		String expectedTitle = "Redirect";
		try {
			webDriver.findElement(By.id(xpathIDMap.get("SignOff_Id"))).click();
			System.out.println("Signoff button Clicked");
			//Thread.sleep(2000);
			Assert.assertEquals(expectedTitle, webDriver.getTitle());
			reportStatus = Constants.PASS + "-";

		} catch (Exception e) {
			reportStatus = Constants.FAIL + url + "  ->Did not signoff -CountrySelectionPage not loaded. ";
			System.out.println("In clickSignOff->" + e);
			e.printStackTrace();
		}
		return reportStatus;
	}

	

	/**
	 * 
	 * This method is used in login mode - defines initial 4 steps for logging in (until coming to welcome
	 * page)
	 * 
	 */
	public String commonLogin(WebDriver webDriver, Map<String, Object> eachRowMap) {

		// 1st step - open url webpage
		String reportStatus = openWebPage(webDriver);
		if (reportStatus.contains("Pass")) {
			// 2nd step - select country and go to index page
			reportStatus = selectCountry(webDriver, (String) eachRowMap.get("Country"));
			if (reportStatus.contains("Pass")) {
				clickSignOn(webDriver);
				reportStatus = login(webDriver, eachRowMap);
			}
		}
		
		// this function is called to close the ForeSee survey popup
		webUtil.closeForeSeeSurveyPopup(webDriver);
		
		if (reportStatus.contains("Fail"))
			outputOp.saveScreenshotToFile(webDriver, eachRowMap);	
		
		return reportStatus;
	}

}
