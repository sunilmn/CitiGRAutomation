/**
 * Aug 29, 2017
 * @author prajendra
 *
 */
package com.citi.testCase;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.citi.fileActivity.OutputOperations;
import com.citi.gr.CitiMain;
import com.citi.util.Constants;
import com.citi.util.ExcelUtil;
import com.citi.util.WebUtil;

public class MyProfile {

	Map<String, String> xpathIDMap = new LinkedHashMap<String, String>();
	WebUtil webUtil = new WebUtil();
	ExcelUtil excelUtil = new ExcelUtil();
	OutputOperations outputOp = new OutputOperations();

	public MyProfile() {
		this.xpathIDMap = CitiMain.xpathIDMap;
	}

	
	public String editEmail(WebDriver webDriver, Map<String, Object> eachRowMap) {

		String reportStatus = "";
		String errorDetails = "";
		String xLEmailEditable = (String) eachRowMap.get("EmailEditable");
		String xLEmailId = (String) eachRowMap.get("EmailId");
		String xLErrorMessage = (String) eachRowMap.get("ErrorMessage");
		WebDriverWait wait = new WebDriverWait(webDriver,60);

		if (xLEmailEditable.isEmpty()) {
			errorDetails = Constants.XL_EMPTY_ERROR + " 'EmailEditable' is blank. ";
		} else if (xLEmailId.isEmpty()) {
			errorDetails = Constants.XL_EMPTY_ERROR + " 'EmailId' is blank. ";
		} else if (xLErrorMessage.isEmpty()) {
			errorDetails = Constants.XL_EMPTY_ERROR + " 'ErrorMessage' is blank. ";
		} else {

			String wrongConfirmEmailId = "Wrong" + xLEmailId;
			Boolean editableFlag = false;

			// check in web MyProfile page , if EDIT element is present
			try {
				
				WebElement contactInfoWebElement =wait.until(ExpectedConditions
						.elementToBeClickable(By.className(xpathIDMap.get("MyProfile_ContactInfo_Class"))));

				WebElement editEmailWebElement =wait.until(ExpectedConditions
						.elementToBeClickable(contactInfoWebElement.findElement(By.className(xpathIDMap.get("MyProfile_EditAdd_Class")))));
				
				if (editEmailWebElement != null)
					editableFlag = true;
			} catch (NoSuchElementException e) {
				editableFlag = false;
				// e.printStackTrace();
			}

			/*******************************************************/
			// if XL EmailEditable is No
			if (xLEmailEditable.equalsIgnoreCase(Constants.NO)) {
				if (editableFlag  == true) {
					errorDetails += " Email is editable in webpage. ";
				}
			}
			// if XL EmailEditable is Yes
			else {

				if (editableFlag == false) {
					errorDetails += " Email is non-editable in webpage. ";
				} else {

					try {
						wait.until(ExpectedConditions
								.elementToBeClickable(By.className(xpathIDMap.get("MyProfile_EditAdd_Class")))).click();

						/*-----------FIRST CASE - input wrong ConfirmatMailId and check for error message*/
						wait.until(ExpectedConditions
								.elementToBeClickable(By.id(xpathIDMap.get("MyProfile_Email_Id"))))
								.sendKeys(Keys.chord(Keys.CONTROL, "a"), xLEmailId);

						wait.until(ExpectedConditions
								.elementToBeClickable(By.id(xpathIDMap.get("MyProfile_ConfirmEmail_Id"))))
								.sendKeys(Keys.chord(Keys.CONTROL, "a"), wrongConfirmEmailId);

						// save
						wait.until(ExpectedConditions
								.elementToBeClickable(By.className(xpathIDMap.get("MyProfile_ContactInfoSave_Class"))))
								.click();

						// Get message
						String webErrorMsg = wait
								.until(ExpectedConditions.elementToBeClickable(By.className("my-profile-messages")))
								.getText();

						webErrorMsg = webErrorMsg.replace("\\n", "");
						if (!webErrorMsg.contains(xLErrorMessage))
							errorDetails += " 'EmailErrorMessage'"+ " on webpage does not match with XL input, when providing wrong 'ConfirmEmailId'";

						/*------------END- FIRST CASE*/

						
						
						/*------ SECOND CASE - Send proper confirmation mail(same as xl) and save 
						 * and reload MyProfile page and check if email-id is 
						 * updated as per excel*/

						// reload MyProfile page
						webUtil.refreshWebPage(webDriver);							
						
						wait.until(ExpectedConditions
								.elementToBeClickable(By.className(xpathIDMap.get("MyProfile_EditAdd_Class")))).click();

						wait.until(ExpectedConditions
								.elementToBeClickable(By.id(xpathIDMap.get("MyProfile_Email_Id"))))
								.sendKeys(Keys.chord(Keys.CONTROL, "a"), xLEmailId);
						// input proper ConfirMailId(same as xl)
						wait.until(ExpectedConditions
								.elementToBeClickable(By.id(xpathIDMap.get("MyProfile_ConfirmEmail_Id"))))
								.sendKeys(Keys.chord(Keys.CONTROL, "a"), xLEmailId);
						// save
						wait.until(ExpectedConditions.elementToBeClickable(By.className("my-profile-save-link")))
								.click();

						// reload MyProfile page						
						webUtil.refreshWebPage(webDriver);	
						
						// check if email-id is updated as per excel
						wait.until(ExpectedConditions
								.elementToBeClickable(By.className(xpathIDMap.get("MyProfile_EditAdd_Class"))))
								.click();

						String webEmailId = wait
								.until(ExpectedConditions
										.elementToBeClickable(By.id(xpathIDMap.get("MyProfile_Email_Id"))))
								.getAttribute("data-val");
						//one more time validating webEamilId with xLEmailID 
						if (!webEmailId.equals(xLEmailId)) {
							webDriver.navigate().refresh();
							Thread.sleep(5000);
							webEmailId = wait.until(ExpectedConditions.elementToBeClickable(By.id(xpathIDMap.get("MyProfile_Email_Id"))))
									.getAttribute("data-val");
						}

						if (!webEmailId.equals(xLEmailId))
							errorDetails += " EmailId not saved:WebEmailId-" + webEmailId + ",xLEmailId-" + xLEmailId;

						/*------------END- SECOND CASE*/

					} catch (NoSuchElementException e) {
						errorDetails += " MyProfile ContactInfo section has missing webElements. ";
						e.printStackTrace();
					} catch (Exception e) {
						errorDetails += Constants.UNEXPECTED_ERROR;
						e.printStackTrace();
					}

				}
			}
			/*******************************************************/
		} // end else

		if (errorDetails.isEmpty())
			reportStatus = Constants.PASS + "-";
		else {
			reportStatus = Constants.FAIL + errorDetails;
			outputOp.saveScreenshotToFile(webDriver, eachRowMap);
		}

		return reportStatus;
	}

	public String alterLoyalty(WebDriver webDriver, Map<String, Object> eachRowMap) {

		String reportStatus = "";
		String errorDetails = "";

		String xLLoyaltyProgramName = (String) eachRowMap.get("LoyaltyProgramName");
		String xLLoyaltyProgramNumber = (String) eachRowMap.get("LoyaltyProgramNumber");
		String xLEditLoyaltyProgramNumber = (String) eachRowMap.get("EditLoyaltyProgramNumber");

		if (xLLoyaltyProgramName.isEmpty()) {
			errorDetails = Constants.XL_EMPTY_ERROR + " 'LoyaltyProgramName' is blank. ";
		} else if (xLLoyaltyProgramNumber.isEmpty()) {
			errorDetails = Constants.XL_EMPTY_ERROR + " 'LoyaltyProgramNumber' is blank. ";
		} else if (xLEditLoyaltyProgramNumber.isEmpty()) {
			errorDetails = Constants.XL_EMPTY_ERROR + " 'EditLoyaltyProgramNumber' is blank. ";
		} else {

			// check in web MyProfile page , if EDIT element is present
			try {

				// FIRST CASE - ADD LOYALTY PROGRAM
				Boolean addSuccessFlag = addEditLoyaltyProgram(webDriver, xLLoyaltyProgramName, xLLoyaltyProgramNumber);
				if (addSuccessFlag == false) {
					errorDetails += " Add Loyalty failed. ";
					outputOp.saveScreenshotToFile(webDriver, eachRowMap);
				}

				// SECOND CASE - EDIT LOYALTY PROGRAM
				Boolean editSuccessFlag = addEditLoyaltyProgram(webDriver, xLLoyaltyProgramName,
						xLEditLoyaltyProgramNumber);
				if (editSuccessFlag == false) {
					errorDetails += " Edit Loyalty failed. ";
					outputOp.saveScreenshotToFile(webDriver, eachRowMap);
				}

				// THIRD CASE - REMOVE LOYALTY PROGRAM
				Boolean removeSuccessFlag = removeLoyaltyProgram(webDriver, xLLoyaltyProgramName);
				if (removeSuccessFlag == false) {
					errorDetails += " Remove Loyalty failed. ";
					outputOp.saveScreenshotToFile(webDriver, eachRowMap);
				}

			} catch (Exception e) {
				// e.printStackTrace();
			}
		}

		if (errorDetails.isEmpty())
			reportStatus = Constants.PASS + "-";
		else {
			reportStatus = Constants.FAIL + errorDetails;
			outputOp.saveScreenshotToFile(webDriver, eachRowMap);
		}

		return reportStatus;
	}

	public Boolean addEditLoyaltyProgram(WebDriver webDriver, String xLLoyaltyProgramName,
			String xlLoyaltyProgramNumber) {
		Boolean addEditSuccessFlag = false;
		try {
			// reload MyProfile page
			webUtil.refreshWebPage(webDriver);	

			WebElement loyalProgramWebElement = webDriver
					.findElement(By.className(xpathIDMap.get("Myprofile_LoyaltyPrograms_Class")));
			// click on ADD/EDIT
			loyalProgramWebElement.findElement(By.className(xpathIDMap.get("MyProfile_EditAdd_Class"))).click();

			// Clicking on down arrow button in loyalty program
			// WebElement
			// spanWebElement=webDriver.findElement(By.id("loyalty-programs-name-button"));
			// spanWebElement.findElement(By.className("icon_caret-down")).click();
			webDriver.findElement(By.xpath(xpathIDMap.get("Myprofile_LoyaltyProgramName_Xpath"))).click();
			// select loyalty program name from the list which appears as dropdown
			WebElement uL = webDriver.findElement(By.id(xpathIDMap.get("Myprofile_LoyaltyProgramNameList_Id")));
			List<WebElement> webDropdownList = uL.findElements(By.tagName("li"));
			for (int i = 0; i < webDropdownList.size(); i++) {
				String webloyaltyProgramName = webDropdownList.get(i).getAttribute("textContent");
				if (webloyaltyProgramName.equals(xLLoyaltyProgramName)) {
					webDropdownList.get(i).click();
					break;
				}
			}
			// input the loyalty program number to textfield
			webDriver.findElement(By.id(xpathIDMap.get("Myprofile_LoyaltyProgramNumber_Id")))
					.sendKeys(xlLoyaltyProgramNumber);
			// click on 'ADD LOYALTY PROGRAM'
			webDriver.findElement(By.className(xpathIDMap.get("Myprofile_LoyaltyProgramAdd_Class"))).click();
			// reload MyProfile page
			webUtil.refreshWebPage(webDriver);	
			
			// confirm if the new/edited loyalty program name and number is present
			WebElement tbodyStaticWeb = webDriver
					.findElement(By.className(xpathIDMap.get("Myprofile_LoyaltyProgramStaticTbody_Class")));
			List<WebElement> trList = tbodyStaticWeb.findElements(By.tagName("tr"));
			for (int i = 0; i < trList.size(); i++) {  
				List<WebElement> tdList = trList.get(i).findElements(By.tagName("td"));
				String staticloyaltyProgramName = tdList.get(0).getAttribute("textContent").trim();
				if (staticloyaltyProgramName.equals(xLLoyaltyProgramName)) {
					String staticloyaltyProgramNumber = tdList.get(1).getAttribute("textContent").trim();
					int webLength = staticloyaltyProgramNumber.length();
					// get last 4 digits
					String webNumberSubStr = staticloyaltyProgramNumber.substring(webLength - 4, webLength);

					int xlLength = xlLoyaltyProgramNumber.length();
					// get last 4 digits
					String xLNumberSubStr = xlLoyaltyProgramNumber.substring(xlLength - 4, xlLength);
					if (webNumberSubStr.equals(xLNumberSubStr)) {
						addEditSuccessFlag = true;
						break;
					}
				}
			}
		} catch (Exception e) {
			addEditSuccessFlag = false;
			e.printStackTrace();
		}

		return addEditSuccessFlag;
	}

	public Boolean removeLoyaltyProgram(WebDriver webDriver, String xLLoyaltyProgramName) {
		Boolean removeSuccessFlag = false;
		try {
			WebElement loyalProgramWebElement = webDriver
					.findElement(By.className(xpathIDMap.get("Myprofile_LoyaltyPrograms_Class")));
			// click on ADD/EDIT
			loyalProgramWebElement.findElement(By.className(xpathIDMap.get("MyProfile_EditAdd_Class"))).click();

			// check for the loyalty program name and number is present
			WebElement tbodyEditWeb = webDriver
					.findElement(By.className(xpathIDMap.get("Myprofile_LoyaltyProgramEditableTbody_Class")));
			List<WebElement> trList = tbodyEditWeb.findElements(By.tagName("tr"));
			for (int i = 0; i < trList.size(); i++) {
				List<WebElement> tdList = trList.get(i).findElements(By.tagName("td"));
				String webloyaltyProgramName = tdList.get(0).getAttribute("textContent").trim();
				if (webloyaltyProgramName.equals(xLLoyaltyProgramName)) {
					trList.get(i).findElement(By.className(xpathIDMap.get("Myprofile_LoyaltyProgramsRemove_Class")))
							.click();
					removeSuccessFlag = true;
					break;
				}
			}

		} catch (Exception e) {
			removeSuccessFlag = false;
			e.printStackTrace();
		}

		return removeSuccessFlag;
	}


}
