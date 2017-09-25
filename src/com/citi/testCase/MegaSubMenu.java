/*
 * @author prajendra
 * 
 * 
 */

package com.citi.testCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.citi.fileActivity.OutputOperations;
import com.citi.util.Constants;
import com.citi.util.ExcelUtil;
import com.citi.util.WebUtil;

public class MegaSubMenu {
	
	Map<String, String> xpathIDMap = new HashMap<String, String>();
	WebUtil webUtil = new WebUtil();
	ExcelUtil excelUtil = new ExcelUtil();
	OutputOperations outputOp = new OutputOperations();
	

	/*
	 * 
	 * Check the megamenu in index page
	 * 
	 */
	public String megaMenuCheck(WebDriver webDriver, Map<String, Object> eachRowMap) {

		String reportStatus = "";
		String errorDetails = "";

		// get the mega-menu list from web page
		List<String> webMegamenuList = new ArrayList<>();
		webMegamenuList = webUtil.getWebMegamenu(webDriver);

		Map<String, String> xLmegaMenuMap = new LinkedHashMap<String, String>();
		xLmegaMenuMap = excelUtil.getxLMegamenuMap(eachRowMap);
		
		List<String> xLmissingMegaMenuList = new ArrayList<String>();
		List<String> webExtraMegaMenuList = new ArrayList<String>();
		
		// check if each item in xl-megamenu list is
		// present in web-megamenu list, if not present
		// then it is missing		
		for (Entry<String, String> entry : xLmegaMenuMap.entrySet()) {
			String xLmegamenuName = entry.getValue();
			if (webMegamenuList.contains(xLmegamenuName))
				continue;
			else				
				xLmissingMegaMenuList.add(xLmegamenuName);
		}
		if(!xLmissingMegaMenuList.isEmpty())
			errorDetails = xLmissingMegaMenuList.toString()+" is missing. ";
		
		// check if each item in web-megamenu
		// list is present in xl-megamenu,
		// if not present then  it is extra
		for (int i = 0; i < webMegamenuList.size(); i++) {
			String webMegaMenuName = webMegamenuList.get(i);
			if (xLmegaMenuMap.containsValue(webMegaMenuName))
				continue;
			else
				webExtraMegaMenuList.add(webMegaMenuName);
		}
		if(!webExtraMegaMenuList.isEmpty())
			errorDetails += webExtraMegaMenuList.toString()+" is extra. ";
		
		System.out.println("dissimilarMegaMenu=" + errorDetails);
		
		if (!errorDetails.isEmpty())
			errorDetails = "MegaMenu Dissimilarity: "+errorDetails+Constants.HASH;
		
		reportStatus=outputOp.statusReporter(errorDetails, eachRowMap, webDriver);
		return reportStatus;	
		
	}

	/*
	 * 
	 * Check the menu and submenu in index page
	 * 
	 * 
	 */
	public String subMenuCheck(WebDriver webDriver, Map<String, Object> eachRowMap, String country) {

		String reportStatus = "";
		String errorDetails = "";
		

		Map<String, String> xLmegaMenuMap = new LinkedHashMap<String, String>();
		xLmegaMenuMap = excelUtil.getxLMegamenuMap(eachRowMap);

		Map<String, List<String>> xLmegaSubMenuMap = new LinkedHashMap<String, List<String>>();
		xLmegaSubMenuMap = excelUtil.getxLsubmenuMap(eachRowMap, xLmegaMenuMap);

		Map<String, String> megaMissingSubMenuMap = new LinkedHashMap<String, String>();

		// iterate XL megaMenuMap - for each non empty xl megamenu
		for (Entry<String, String> entry : xLmegaMenuMap.entrySet()) {

			String megamenuName = entry.getValue();

			List<String> xLsubMenuList = new ArrayList<String>();
			xLsubMenuList = xLmegaSubMenuMap.get(megamenuName);
			System.out.println(megamenuName+ " - XL Submenu list : " + xLsubMenuList);

			/* to get the corresponding sub menu list from web page */

			// first - get the mega-menu(main-menu) list from web page
			List<String> webSubMenuList = new ArrayList<>();
			List<WebElement> webElementsList = webDriver.findElements(By.tagName("li"));
			webElementsList.remove(0);
			for (int x = 0; x < webElementsList.size(); x++) {
				String idWebElement = webElementsList.get(x).getAttribute("id");
				if ((!idWebElement.equals("")) && idWebElement.startsWith("mainmenu")) {
					String textWebElement = webElementsList.get(x).getText();

					if (textWebElement.equals(megamenuName)) {
						try {
							Actions builder = new Actions(webDriver);
							WebElement megamenuWebElement = webDriver.findElement(By.id((idWebElement)));
							if (megamenuWebElement != null) {

								// second - to get the sub-menu list from
								// web page
								List<WebElement> subwebElementsList = megamenuWebElement.findElements(By.tagName("a"));
								subwebElementsList.remove(0);
								for (int y = 0; y < subwebElementsList.size(); y++) {
									String ActualElement = subwebElementsList.get(y).getAttribute("textContent");
									webSubMenuList.add(ActualElement);
								}
								System.out.println(megamenuName+" - Web Submenu list : " + webSubMenuList);

								// hovers over the megamenu
								builder.moveToElement(megamenuWebElement).build().perform();

								/////////////////////////////////////////////////
								// start - comparison of xl-submenu and web-submenu
								///////////////////////////////////////////////// 

								List<String> dissimilarSubMenuList = new ArrayList<String>();

								// check if each item in xl-submenu list is
								// present in web-submenu, if not present
								// then it is missing
								for (int i = 0; i < xLsubMenuList.size(); i++) {

									String xLsubMenuName = xLsubMenuList.get(i);
									if (webSubMenuList.contains(xLsubMenuName))
										continue;
									else
										dissimilarSubMenuList.add(xLsubMenuName + " is missing. ");
								}
								/*
								 * in excel sheet ,the merchandise megamenu will
								 * have only one submenu'SEE ALL >>' ; but in
								 * web there will be more submenus for it. so No
								 * need to check if web-submenu items present in
								 * xl-submenu list for only Merchandise
								 * megamenu.
								 */								
								if(!Arrays.asList(Constants.MERCHANDISE_LANGUAGE_LIST).contains(megamenuName)){
									// check if each item in web-submenu
									// list is present in xl-submenu,
									//  if not present then  it is extra									
									for (int i = 0; i < webSubMenuList.size(); i++) {

										String webSubMenuName = webSubMenuList.get(i);
										if (xLsubMenuList.contains(webSubMenuName))
											continue;
										else
											dissimilarSubMenuList.add(webSubMenuName + " is extra. ");
									}
								}

								// if there are submenu dissimilarity
								// issues,then put to a map n take screenshot
								if (!dissimilarSubMenuList.isEmpty()) {
									megaMissingSubMenuMap.put(megamenuName, dissimilarSubMenuList.toString());
									outputOp.saveScreenshotToFile(webDriver, eachRowMap);
								}
								
								///////////////////////////////////////////////////
								// end -comparison of xl-submenu &  web-submenu								
								/////////////////////////////////////////////////////

							}
						} catch (Exception e) {
							System.out.println("In subMenuCheck-->" + e);
							//reportStatus = Constants.FAIL + Constants.UNEXPECTED_ERROR+". ";
							e.printStackTrace();
						}
						break;
					}
				}
			}

		} // end for
		System.out.println("megaMissingSubMenuMap=" + megaMissingSubMenuMap);
		
		if (!megaMissingSubMenuMap.isEmpty())
			errorDetails= "SubMenu Dissimilarity: " + megaMissingSubMenuMap.toString()+Constants.HASH;
		
		reportStatus=outputOp.statusReporter(errorDetails);
		return reportStatus;
	}

}
