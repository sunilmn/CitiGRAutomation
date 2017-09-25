/**
 * Jul 4, 2017
 * @author prajendra
 *
 */
package com.citi.testCase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.citi.fileActivity.OutputOperations;
import com.citi.gr.CitiMain;
import com.citi.util.Constants;
import com.citi.util.ExcelUtil;
import com.citi.util.WebUtil;

public class MyAccount {
	
	Map<String, String> xpathIDMap = new LinkedHashMap<String, String>();
	WebUtil webUtil = new WebUtil();
	ExcelUtil excelUtil = new ExcelUtil();
	OutputOperations outputOp = new OutputOperations();

	public MyAccount() {
		this.xpathIDMap = CitiMain.xpathIDMap;
	}
	
	
	
	
	
	/*
	 * This method checks if single MyAccount dropdown menu is present and after
	 * clicking that menu,checks if the page loads or not
	 */
	public String clickSingleMyAccountMenu(WebDriver webDriver, Map<String, Object> eachRowMap) {
		String reportStatus = "";
		String errorDetails = "";
		String xLMyAccountMenuName = (String) eachRowMap.get("MenuName");
		String xLMyAccountPageTitle = (String) eachRowMap.get("PageTitle");
		
		// Used for boolean check the condition
		boolean myAccountLinkFound = true, dropdownMenuNameFound = true;
		//used for explicit wait
		WebDriverWait wait = new WebDriverWait(webDriver, 60);

		if (xLMyAccountMenuName.isEmpty()) {
			errorDetails += Constants.XL_EMPTY_ERROR+" MenuName xl input is blank."+Constants.HASH;
		} else if (xLMyAccountPageTitle.isEmpty()) {
			errorDetails += Constants.XL_EMPTY_ERROR+" PageTitle xl input is blank."+Constants.HASH;
		} else {

			try {
				

				try {
					webDriver.findElement(By.id(xpathIDMap.get("MyAccountMenu_Id"))).click();
				} catch (NoSuchElementException e) {
					errorDetails += " Main MyAccount Menu  not found in index page. "+Constants.HASH;
					myAccountLinkFound = false;					
					e.printStackTrace();
				}

				if (myAccountLinkFound == true) {
					try {
						System.out.println("clicking MyAccount dropdown menu --->  " + xLMyAccountMenuName);
						wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(xLMyAccountMenuName)))
								.click();
						// this function is called to close the ForeSee survey popup
						webUtil.closeForeSeeSurveyPopup(webDriver);
						
					} catch (NoSuchElementException | TimeoutException e) {
						
						errorDetails += xLMyAccountMenuName + "  menu not found. "+Constants.HASH;
						dropdownMenuNameFound = false;						
						e.printStackTrace();
					}

					if (dropdownMenuNameFound == true) {
						try {
							String pageTitle = wait.until(ExpectedConditions
									.elementToBeClickable(By.className(xpathIDMap.get("MyAccountPageTitle_Class"))))
									.getText();
							if (!pageTitle.equals(xLMyAccountPageTitle))
								errorDetails += xLMyAccountPageTitle + "  page not loaded. "+Constants.HASH;
						} catch (NoSuchElementException | TimeoutException e) {							
							errorDetails += xLMyAccountPageTitle + "  page not loaded. "+Constants.HASH;							
							e.printStackTrace();
						}
					}
				} // end  if

			} catch (Exception e) {
				errorDetails += xLMyAccountMenuName +"-"+ Constants.UNEXPECTED_ERROR +". "+Constants.HASH;
				e.printStackTrace();
			}			
			
			reportStatus=outputOp.statusReporter(errorDetails, eachRowMap, webDriver);

		} // end else

		return reportStatus;
	}

/*	
	 * This method checks if single MyAccount dropdown menu is present and after
	 * clicking that menu, if the page loads or not
	 
	public String clickSingleMyAccountMenu(WebDriver webDriver, Map<String, Object> eachRowMap) {
		String reportStatus = "";
		String xLMyAccountMenuName = (String) eachRowMap.get("MenuName");
		String xLMyAccountPageTitle = (String) eachRowMap.get("PageTitle");
		
		if (xLMyAccountMenuName.isEmpty() ) {
			reportStatus = Constants.FAIL + " MenuName xl input is blank.";
		}
		else if (xLMyAccountPageTitle.isEmpty()) {
			reportStatus = Constants.FAIL + " PageTitle xl input is blank.";
		}
		else
		{
			
			try {
								
				webDriver.findElement(By.id(xpathIDMap.get("MyAccountMenu_Id"))).click();
				
				System.out.println("clicking MyAccount dropdown menu --->  " + xLMyAccountMenuName);
				webDriver.findElement(By.partialLinkText(xLMyAccountMenuName)).click();				
				
				//this function is called to close the ForeSee survey popup					
				webUtil.closeForeSeeSurveyPopup(webDriver);

				String pageTitle = webDriver.findElement(By.className(xpathIDMap.get("MyAccountPageTitle_Class"))).getText();
				if (pageTitle.equals(xLMyAccountPageTitle))
					reportStatus = Constants.PASS + "-";
				else
					reportStatus = Constants.FAIL + xLMyAccountPageTitle + "  page not loaded. ";

			} catch (NoSuchElementException e) {
				reportStatus = Constants.FAIL + xLMyAccountMenuName +  "  page not loaded. ";
				e.printStackTrace();
			} catch (Exception e) {
				reportStatus = Constants.FAIL + Constants.UNEXPECTED_ERROR+". ";
				e.printStackTrace();
			}
			
			
			if (reportStatus.contains("Fail"))
				outputOp.saveScreenshotToFile(webDriver, eachRowMap);
		}
		
		return reportStatus;
	}*/

		
	/*
	 * This method checks if all menus in the MyAccount dropdown menu is present
	 * and after clicking that menu,checks if the page loads or not
	 */
	public String clickAllMyAccountMenus(WebDriver webDriver, Map<String, Object> eachRowMap) {

		String reportStatus = "";
		String errorDetails = "";
		Boolean reportFail = false;		
		
		
		List<String> xLMyAccountDropDownMenuList = new ArrayList<String>();
		xLMyAccountDropDownMenuList=excelUtil.getxLMyAccountDropDownMenuList(eachRowMap);
		System.out.println("xLMyAccountDropDownMenuList------>" + xLMyAccountDropDownMenuList);
		List<String> xLPageTitleList = new ArrayList<String>();
		xLPageTitleList =excelUtil.getXLAllPageTitleList(eachRowMap);
		System.out.println("xLPageTitleList------>" + xLPageTitleList);

		///////////Compare web n xl MyAccount menu////////////////////////////			
		List<String> dissimilarMyAccountMenuList = new ArrayList<String>();
		dissimilarMyAccountMenuList=compareXLnWebMyAccountMenuLists(webDriver,xLMyAccountDropDownMenuList,eachRowMap);
		
		// if there are MyAccount menu dissimilarity
		// issues,then report it n take screenshot
		if (!dissimilarMyAccountMenuList.isEmpty()) {
			reportFail = true;
			errorDetails+="Dissimilar MyAccountDropdown: "+dissimilarMyAccountMenuList.toString()+Constants.HASH;
			// clicking main menu MyAccount on webpage, to display menus to take screenshot
			try {
				webDriver.findElement(By.id(xpathIDMap.get("MyAccountMenu_Id"))).click();
			} catch (NoSuchElementException e) {				
				errorDetails += "  Main MyAccount Menu  not found in index page. "+Constants.HASH;
				e.printStackTrace();
			}
			outputOp.saveScreenshotToFile(webDriver, eachRowMap);
		}
		////////////////////////////////////////////////////////////////////

		for (int x = 0; x < xLMyAccountDropDownMenuList.size(); x++) {
			
			//used for setting report status
			Boolean currentMenuFail = false;
			//used for flagging NoSuchElementException
			Boolean mainMyAccountMenuFound = true, dropdownMenuNameFound = true;

			String xLMyAccountMenuName = xLMyAccountDropDownMenuList.get(x);
			String xLMyAccountPageTitle = xLPageTitleList.get(x);

			if (xLMyAccountPageTitle.isEmpty()) {
				reportFail = true;
				errorDetails+= Constants.XL_EMPTY_ERROR+xLMyAccountMenuName +" 'PageTitle' is blank. "+Constants.HASH;
			} else {
				try {

					
					// clicking main-menu MyAccount on webpage, to display menus
					try {
						webDriver.findElement(By.id(xpathIDMap.get("MyAccountMenu_Id"))).click();
					} catch (NoSuchElementException e) {
						mainMyAccountMenuFound = false;
						currentMenuFail = true;
						errorDetails += " Main MyAccount Menu  not found in index page. "+Constants.HASH;
						e.printStackTrace();
					}

					// clicking MyAccount dropdown-menu-name
					if (mainMyAccountMenuFound == true) {
						System.out.println("clicking account dropdown menu --->  " + xLMyAccountMenuName);
						try {
							webDriver.findElement(By.partialLinkText(xLMyAccountMenuName)).click();
						} catch (NoSuchElementException e) {
							currentMenuFail = true;
							errorDetails += xLMyAccountMenuName + "  menu not found. "+Constants.HASH;
							dropdownMenuNameFound = false;
							e.printStackTrace();
						}
						

						// check if page loads
						if (dropdownMenuNameFound == true) {
							
							// this function is called to close the ForeSee survey popup
							webUtil.closeForeSeeSurveyPopup(webDriver);
							
							try {
								String pageTitle = webDriver
										.findElement(By.className(xpathIDMap.get("MyAccountPageTitle_Class")))
										.getText();
								if (!pageTitle.equals(xLMyAccountPageTitle)) {
									errorDetails += xLMyAccountPageTitle + "  page not loaded. "+Constants.HASH;
									currentMenuFail = true;
								}

							} catch (NoSuchElementException e) {
								errorDetails += xLMyAccountPageTitle + "  pa"
										+ "ge not loaded. "+Constants.HASH;
								currentMenuFail = true;
							}

						}

					}

				} catch (Exception e) {
					errorDetails += xLMyAccountMenuName + Constants.UNEXPECTED_ERROR +". "+Constants.HASH;
					currentMenuFail = true;
					e.printStackTrace();
				}

				if (currentMenuFail == true) {
					reportFail = true;
					outputOp.saveScreenshotToFile(webDriver, eachRowMap);
				}

			} // end if
		} // end for

		reportStatus=outputOp.statusReporter(errorDetails);

		return reportStatus;
	}
	
	
	/*
	 * compare xl-MyAccountMenu and web-MyAccountMenu and return
	 * dissimilar list if not same 
	 */
	private List<String> compareXLnWebMyAccountMenuLists(WebDriver webDriver,List<String> xLMyAccountDropDownMenuList,Map<String, Object> eachRowMap)
	{
		List<String> webMyAccountDropDownMenuList = new ArrayList<String>();
		webMyAccountDropDownMenuList = webUtil.getWebMyAccountDropDownMenu(webDriver, xLMyAccountDropDownMenuList);	

		List<String> dissimilarMyAccountMenuList = new ArrayList<String>();
		// check if each item in xl-menu list is
		// present in web-menu, if not present
		// then it is missing
		for (int i = 0; i < xLMyAccountDropDownMenuList.size(); i++) {

			String xLMyAccountMenuName = xLMyAccountDropDownMenuList.get(i);
			if (webMyAccountDropDownMenuList.contains(xLMyAccountMenuName))
				continue;
			else
				dissimilarMyAccountMenuList.add(xLMyAccountMenuName + " - is missing ");
		}

		// check if each item in web-menu
		// list is present in xl-menu,
		// if not present then it is extra
		for (int i = 0; i < webMyAccountDropDownMenuList.size(); i++) {

			String webMyAccountMenuName = webMyAccountDropDownMenuList.get(i);
			if (xLMyAccountDropDownMenuList.contains(webMyAccountMenuName))
				continue;
			else
				dissimilarMyAccountMenuList.add(webMyAccountMenuName + " - is extra. ");
		}

		return dissimilarMyAccountMenuList;

	}

}
