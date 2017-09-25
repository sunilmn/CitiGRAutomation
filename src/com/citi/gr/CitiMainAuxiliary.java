/**
 * Sep 8, 2017
 * @author prajendra
 *
 */
package com.citi.gr;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.citi.fileActivity.OutputOperations;
import com.citi.testCase.EntryMode;
import com.citi.testCase.FaqContactUs;
import com.citi.testCase.MegaSubMenu;
import com.citi.testCase.MyAccount;
import com.citi.testCase.MyProfile;
import com.citi.util.Constants;

public class CitiMainAuxiliary {

	static WebDriver webDriver;

	String environmentUrl = "";
	String browserName = "";
	File resultTextFile = null;

	public CitiMainAuxiliary() {
		this.resultTextFile = CitiMain.resultTextFile;
		this.environmentUrl = CitiMain.environmentUrl;
		this.browserName = CitiMain.browserName;
	}

	DriverOperations driverOperations = new DriverOperations();
	EntryMode entryMode = new EntryMode();
	OutputOperations outputOp = new OutputOperations();
	MegaSubMenu megaSubMenu = new MegaSubMenu();
	MyAccount myAccount = new MyAccount();
	MyProfile myProfile = new MyProfile();
	FaqContactUs faqContactUs = new FaqContactUs();


	// reportStatus contains status=pass/fail and the description
	String reportStatus = "";
	// default values of input parameters for testcases
	static private String tierName = "Default", accountNumber = "-", country = "-", language = "-", memberID = "-";

	
	
	/*
	 * this method is not applicable to Anonymous Mode testcases
	 */
	private static void setInputValues(Map<String, Object> eachRowMap)
	{
		country = (String) eachRowMap.get("Country");
		tierName = (String) eachRowMap.get("TierName");
		accountNumber = (String) eachRowMap.get("AccountNumber");
		memberID = (String) eachRowMap.get("MemberId");
		language = (String) eachRowMap.get("LanguageCode");
	}
	
	/*
	 * 
	 * 
	 */
	public void runAnonMode(List<Map<String, Object>> executableRowsList, String testCaseName) {
		try {
			for (Map<String, Object> eachRowMap : executableRowsList) {
				webDriver = driverOperations.getWebDriver();
				country = (String) eachRowMap.get("Country");
				reportStatus = entryMode.openWebPage(webDriver);
				if (reportStatus.contains("Pass"))
					reportStatus = entryMode.selectCountry(webDriver, country);

				outputOp.writeToResultFile(resultTextFile, testCaseName, country, tierName, language, memberID,
						accountNumber, reportStatus);
				// single screenshot per failed testcase row
				if (reportStatus.contains("Fail"))
					outputOp.saveScreenshotToFile(webDriver, eachRowMap);
				webDriver.close();
			} // end for
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/*
	 * 
	 * 
	 */
	public void runAnonMegaMenu(List<Map<String, Object>> executableRowsList, String testCaseName) {
		try {
			for (Map<String, Object> eachRowMap : executableRowsList) {
				webDriver = driverOperations.getWebDriver();
				// 1st step - open url webpage
				reportStatus = entryMode.openWebPage(webDriver);
				if (reportStatus.contains("Pass")) {
					country = (String) eachRowMap.get("Country");					
					// 2nd step - select country and go to index page
					reportStatus = entryMode.selectCountry(webDriver, country);
					if (reportStatus.contains("Pass"))
						// 3rd step - check the megamenu
						reportStatus = megaSubMenu.megaMenuCheck(webDriver, eachRowMap);
				}

				outputOp.writeToResultFile(resultTextFile, testCaseName, country, tierName, language, memberID,
						accountNumber, reportStatus);
				// single screenshot per testcase row( failed)
				if (reportStatus.contains("Fail"))
					outputOp.saveScreenshotToFile(webDriver, eachRowMap);
				webDriver.close();
			} // end-for
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * 
	 * 
	 */
	public void runAnonMegaMenuSubmenu(List<Map<String, Object>> executableRowsList, String testCaseName) {
		try {
			// taking screenshots if login fails or megamenu check fails - coding needs to be done properly(pending)
			
			for (Map<String, Object> eachRowMap : executableRowsList) {
				webDriver = driverOperations.getWebDriver();						
				reportStatus = entryMode.openWebPage(webDriver);
				if (reportStatus.contains("Pass")) {
					country = (String) eachRowMap.get("Country");					
					reportStatus = entryMode.selectCountry(webDriver, country);
					if (reportStatus.contains("Pass")) {
						reportStatus = megaSubMenu.megaMenuCheck(webDriver, eachRowMap);
						reportStatus = megaSubMenu.subMenuCheck(webDriver, eachRowMap, country);	
					}
					else
					{
						//single screenshot if  index page loading fails
						outputOp.saveScreenshotToFile(webDriver, eachRowMap);
					}
				}
				else
				{
					//single screenshot if  country selection page loading fails
					outputOp.saveScreenshotToFile(webDriver, eachRowMap);
				}
				
				outputOp.writeToResultFile(resultTextFile, testCaseName, country, tierName, language,memberID,
						accountNumber, reportStatus);
				
				//single screenshot if anonymous-entry fails or megamenu check fails
				if (reportStatus.contains("Fail"))
					outputOp.saveScreenshotToFile(webDriver, eachRowMap);
				
				webDriver.close();
			} // end-for

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 
	 * 
	 */
	public void runLogin(List<Map<String, Object>> executableRowsList, String testCaseName) {
		try {
			for (Map<String, Object> eachRowMap : executableRowsList) {
				webDriver = driverOperations.getWebDriver();						
				setInputValues(eachRowMap);
				reportStatus = entryMode.commonLogin(webDriver, eachRowMap);
				
				outputOp.writeToResultFile(resultTextFile, testCaseName, country, tierName, language,memberID,
						accountNumber, reportStatus);
				// single screenshot per testcase row( failed)
				if (reportStatus.contains("Fail"))
					outputOp.saveScreenshotToFile(webDriver, eachRowMap);
				
				webDriver.close();
			} // end for

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 
	 * 
	 */
	public void runLogonMegaMenu(List<Map<String, Object>> executableRowsList, String testCaseName) {
		try {
			for (Map<String, Object> eachRowMap : executableRowsList) {
				webDriver = driverOperations.getWebDriver();						
				setInputValues(eachRowMap);
				reportStatus = entryMode.commonLogin(webDriver, eachRowMap);
				if (reportStatus.contains("Pass"))
					reportStatus = megaSubMenu.megaMenuCheck(webDriver, eachRowMap);
				
				outputOp.writeToResultFile(resultTextFile, testCaseName, country, tierName, language,memberID,
						accountNumber, reportStatus);
				if (reportStatus.contains("Fail"))
					outputOp.saveScreenshotToFile(webDriver, eachRowMap);
				webDriver.close();
			} // end for

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 
	 * 
	 */
	public void runLogonMegaMenuSubmenu(List<Map<String, Object>> executableRowsList, String testCaseName) {
		try {
			for (Map<String, Object> eachRowMap : executableRowsList) {
				webDriver = driverOperations.getWebDriver();						
				setInputValues(eachRowMap);
				Boolean reportCompositeFailFlag=false;
				String compositeDetails="";
				
					reportStatus = entryMode.commonLogin(webDriver, eachRowMap);
				if (reportStatus.contains("Pass")) {
					
					reportStatus = megaSubMenu.megaMenuCheck(webDriver, eachRowMap);
					if (reportStatus.contains("Fail")) {							
						reportCompositeFailFlag=true;
						String reportStatusArray[] = reportStatus.split(";");
						compositeDetails+=reportStatusArray[1];
					}
					
					reportStatus = megaSubMenu.subMenuCheck(webDriver, eachRowMap, country);
					if (reportStatus.contains("Fail")) {							
						reportCompositeFailFlag=true;
						String reportStatusArray[] = reportStatus.split(";");
						compositeDetails+=reportStatusArray[1];
					}
				}
				
				if(reportCompositeFailFlag==true)
				{
					reportStatus=Constants.FAIL_SEMICOLON+compositeDetails;
				}
				
				outputOp.writeToResultFile(resultTextFile, testCaseName, country, tierName, language,memberID,
						accountNumber, reportStatus);
				
				webDriver.close();
			} // end for

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 
	 * 
	 */
	public void runSingleMyAccount(List<Map<String, Object>> executableRowsList, String testCaseName) {
		try {
			for (Map<String, Object> eachRowMap : executableRowsList) {
				webDriver = driverOperations.getWebDriver();						
				setInputValues(eachRowMap);
				reportStatus = entryMode.commonLogin(webDriver, eachRowMap);
				if (reportStatus.contains("Pass"))
					reportStatus = myAccount.clickSingleMyAccountMenu(webDriver, eachRowMap);

				outputOp.writeToResultFile(resultTextFile, testCaseName, country, tierName, language, memberID,
						accountNumber, reportStatus);
				// single screenshot per testcase row( failed)
				if (reportStatus.contains("Fail"))
					outputOp.saveScreenshotToFile(webDriver, eachRowMap);

				webDriver.close();
			} // end for

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 
	 * 
	 */
	public void runMyAccountMenus(List<Map<String, Object>> executableRowsList, String testCaseName) {
		try {
			for (Map<String, Object> eachRowMap : executableRowsList) {

				webDriver = driverOperations.getWebDriver();
				setInputValues(eachRowMap);						
				reportStatus = entryMode.commonLogin(webDriver, eachRowMap);
				if (reportStatus.contains("Pass"))
				{
					//multiple screenshots taken inside the below function for many dropdown menus
					reportStatus = myAccount.clickAllMyAccountMenus(webDriver, eachRowMap);
				}						

				outputOp.writeToResultFile(resultTextFile, testCaseName, country, tierName, language,memberID,
						accountNumber, reportStatus);
				
				webDriver.close();
			} // end for

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 
	 * 
	 */
	public void runCompleteLogin(List<Map<String, Object>> executableRowsList, String testCaseName) {
		try {
			for (Map<String, Object> eachRowMap : executableRowsList) {

				webDriver = driverOperations.getWebDriver();
				setInputValues(eachRowMap);		
				Boolean reportCompositeFailFlag=false;
				String compositeDetails="";
				
				reportStatus = entryMode.commonLogin(webDriver, eachRowMap);
				if (reportStatus.contains("Pass")) {
					
					reportStatus = megaSubMenu.megaMenuCheck(webDriver, eachRowMap);
					if (reportStatus.contains("Fail")) {							
						reportCompositeFailFlag=true;
						String reportStatusArray[] = reportStatus.split(";");
						compositeDetails+=reportStatusArray[1];
					}
					
					reportStatus = megaSubMenu.subMenuCheck(webDriver, eachRowMap, country);
					if (reportStatus.contains("Fail")) {							
						reportCompositeFailFlag=true;
						String reportStatusArray[] = reportStatus.split(";");
						compositeDetails+=reportStatusArray[1];
					}
					
					reportStatus = myAccount.clickAllMyAccountMenus(webDriver, eachRowMap);
					if (reportStatus.contains("Fail")) {							
						reportCompositeFailFlag=true;
						String reportStatusArray[] = reportStatus.split(";");
						compositeDetails+=reportStatusArray[1];
					}
					
				}
				
				if(reportCompositeFailFlag==true)
				{
					reportStatus=Constants.FAIL_SEMICOLON+compositeDetails;
				}
				
				outputOp.writeToResultFile(resultTextFile, testCaseName, country, tierName, language,memberID,
						accountNumber, reportStatus);
				
				webDriver.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 
	 * 
	 */
	public void runEditEmail(List<Map<String, Object>> executableRowsList, String testCaseName) {
		try {
			for (Map<String, Object> eachRowMap : executableRowsList) {

				webDriver = driverOperations.getWebDriver();
				setInputValues(eachRowMap);

				reportStatus = entryMode.commonLogin(webDriver, eachRowMap);
				if (reportStatus.contains("Pass")) {							
					reportStatus = myAccount.clickSingleMyAccountMenu(webDriver, eachRowMap);
					if (reportStatus.contains("Pass")) 
						reportStatus= myProfile.editEmail(webDriver, eachRowMap);	
				}
				outputOp.writeToResultFile(resultTextFile, testCaseName, country, tierName, language,
						memberID, accountNumber, reportStatus);

				webDriver.close();	
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 
	 * 
	 */
	public void runAlterLoyalty(List<Map<String, Object>> executableRowsList, String testCaseName) {
		try {
			for (Map<String, Object> eachRowMap : executableRowsList) {

				webDriver = driverOperations.getWebDriver();
				setInputValues(eachRowMap);

				reportStatus = entryMode.commonLogin(webDriver, eachRowMap);
				if (reportStatus.contains("Pass")) {							
					reportStatus = myAccount.clickSingleMyAccountMenu(webDriver, eachRowMap);
					if (reportStatus.contains("Pass")) 
						reportStatus= myProfile.alterLoyalty(webDriver, eachRowMap);
				}
				outputOp.writeToResultFile(resultTextFile, testCaseName, country, tierName, language,
						memberID, accountNumber, reportStatus);

				webDriver.close();	
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/*
	 * Method will check the FaqContactUs Page
	 * 
	 */
	public void runFaqContactUs(List<Map<String, Object>> executableRowsList, String testCaseName) {
		try {
			for (Map<String, Object> eachRowMap : executableRowsList) {
				webDriver = driverOperations.getWebDriver();
				setInputValues(eachRowMap);
				reportStatus = entryMode.commonLogin(webDriver, eachRowMap);
				Boolean reportCompositeFailFlag = false;
				String compositeDetails = "";
				if (reportStatus.contains("Pass")) {

					
					/***First Check for FAQ page***/	
				    //click on Help->FAQ and check if page loads
					reportStatus = faqContactUs.checkHelpAndFaqPage(webDriver, eachRowMap);
					if (reportStatus.contains("Pass")) {
						
						reportStatus = faqContactUs.intialCommonHelpFaqContactUS(webDriver, eachRowMap, Constants.FAQ);
						if (reportStatus.contains("Fail")) {
							reportCompositeFailFlag = true;
							String reportStatusArray[] = reportStatus.split(";");
							compositeDetails += reportStatusArray[1];
						}
						
						reportStatus = faqContactUs.compareCategoryList(webDriver, eachRowMap);
						if (reportStatus.contains("Fail")) {
							reportCompositeFailFlag = true;
							String reportStatusArray[] = reportStatus.split(";");
							compositeDetails += reportStatusArray[1];
						}

						reportStatus = faqContactUs.checkEachCategoryDetail(webDriver, eachRowMap);
						if (reportStatus.contains("Fail")) {
							reportCompositeFailFlag = true;
							String reportStatusArray[] = reportStatus.split(";");
							compositeDetails += reportStatusArray[1];
						}
						
						
					}

					
					
					/***Second Check for ContactUs page***/
					reportStatus = faqContactUs.validateContactUS(webDriver, eachRowMap);
					if (reportStatus.contains("Fail")) {
						reportCompositeFailFlag = true;
						String reportStatusArray[] = reportStatus.split(";");
						compositeDetails += reportStatusArray[1];
					}

				}

				if (reportCompositeFailFlag == true) {
					reportStatus = Constants.FAIL_SEMICOLON + compositeDetails;
				}

				outputOp.writeToResultFile(resultTextFile, testCaseName, country, tierName, language, memberID,
						accountNumber, reportStatus);

				webDriver.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/*
	 * 
	 * 
	 */
	public void runOBOLogin(List<Map<String, Object>> executableRowsList, String testCaseName) {
		try {
			for (Map<String, Object> eachRowMap : executableRowsList) {
				webDriver = driverOperations.getWebDriver();						
				setInputValues(eachRowMap);
				reportStatus = entryMode.commonOBOLogin(webDriver, eachRowMap);
				
				outputOp.writeToResultFile(resultTextFile, testCaseName, country, tierName, language,memberID,
						accountNumber, reportStatus);
				// single screenshot per testcase row( failed)
				if (reportStatus.contains("Fail"))
					outputOp.saveScreenshotToFile(webDriver, eachRowMap);
				
				webDriver.close();
			} // end for

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	 * 
	 * 
	 */
	public void runCookiedLogin(List<Map<String, Object>> executableRowsList, String testCaseName) {
		try {
			for (Map<String, Object> eachRowMap : executableRowsList) {
				webDriver = driverOperations.getWebDriver();						
				setInputValues(eachRowMap);
				reportStatus = entryMode.commonCookiedLogin(webDriver, eachRowMap);
				
				outputOp.writeToResultFile(resultTextFile, testCaseName, country, tierName, language,memberID,
						accountNumber, reportStatus);
				// single screenshot per testcase row( failed)
				if (reportStatus.contains("Fail"))
					outputOp.saveScreenshotToFile(webDriver, eachRowMap);
				
				webDriver.close();
			} // end for

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

