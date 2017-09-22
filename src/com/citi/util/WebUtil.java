/**
 * 
 */
package com.citi.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.citi.gr.CitiMain;

/**
 * @author prajendra
 *
 */
public class WebUtil {
	
	Map<String, String> xpathIDMap = new LinkedHashMap<String, String>();
	public WebUtil() {
		this.xpathIDMap = CitiMain.xpathIDMap;
	}

	/*
	 * 
	 * 
	 * to get the mega-menu list from web page
	 * 
	 * 
	 */
	public List<String> getWebMegamenu(WebDriver webDriver) {

		List<String> webMegamenuList = new ArrayList<>();
		try {

			/* to get the mega-menu list from web page */
			List<WebElement> webElementsList = webDriver.findElements(By.tagName("li"));
			webElementsList.remove(0);
			
			for (int i = 0; i < webElementsList.size(); i++) {
				String idWebElement = webElementsList.get(i).getAttribute("id");
				// String nameWebElement =
				// webElementsList.get(i).getAttribute("textContent");//gets all
				// the
				// mega and sub menu text names
				if ((!idWebElement.equals("")) && idWebElement.startsWith("mainmenu")) {
					String textWebElement = webElementsList.get(i).getText();
					webMegamenuList.add(textWebElement);
				}
			}
			System.out.println("webMegamenuList" + webMegamenuList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return webMegamenuList;
	}
	
	
	

	/*
	 * 
	 * to get the sub-menu list for a given megamenu from web page
	 * 
	 * 
	 */
	/*public List<String> getWebSubmenu(WebDriver webDriver,String megamenuName) {		
		
		
		// to get the mega-menu list from web page 
		List<String> webSubMenuList = new ArrayList<>();
		List<WebElement> webElementsList = webDriver.findElements(By.tagName("li"));
		webElementsList.remove(0);
		
		for (int i = 0; i < webElementsList.size(); i++) {
			String idWebMegamenu = webElementsList.get(i).getAttribute("id");
			if ((!idWebMegamenu.equals("")) && idWebMegamenu.startsWith("mainmenu")) {
				String webMainMenuName = webElementsList.get(i).getText();

				if (webMainMenuName.equals(megamenuName)) {

					try {
						WebElement megamenuWebElement = webDriver.findElement(By.id((idWebMegamenu)));
						if (megamenuWebElement != null) {
							//to get the sub-menu list from web page 
							List<WebElement> subwebElementsList = megamenuWebElement.findElements(By.tagName("a"));
							subwebElementsList.remove(0);
							for (int i1 = 0; i1 < subwebElementsList.size(); i1++) {
								String subMenuName = subwebElementsList.get(i1).getAttribute("textContent");
								webSubMenuList.add(subMenuName);
							}
						}
					} catch (Exception e) {
						System.out.println("In getWebSubmenu-->" + e);
					}
					break;
				}
			}
		}			

		System.out.println("WebSubmenu Size -----: " + webSubMenuList.size());
		System.out.println("Web Submenu list : " + webSubMenuList);
		return null;

	}
*/
	
	
	
	/*
	 * this function is called to close the ForeSee survey popup			
	 * 
	 */
	public void closeForeSeeSurveyPopup(WebDriver webDriver)
	{
				
		try {
			WebElement cancelPopup =webDriver.findElement(By.className(xpathIDMap.get("ForeSeeSurveyPopUpDecline_Class")));
			if(cancelPopup!=null)
			{	
				cancelPopup.click();
				System.out.println("--------ForeSee survey popup closed--------");
			}
		} catch (NoSuchElementException e) {
			//System.out.println("Expected-ForeSee survey popup--------------->Decline Element not found");
			
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
	
	
	/*
	 * this function is called to refresh the web page and
	 * close the ForeSee survey popup			
	 * 
	 */
	public void refreshWebPage(WebDriver webDriver)
	{
				
		try {
			webDriver.navigate().refresh();
			closeForeSeeSurveyPopup(webDriver);		 
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
	
	
	
	/*
	 * 
	 * 
	 * to get the MyAccount dropdown list from web page
	 * 
	 * 
	 */
	public List<String> getWebMyAccountDropDownMenu(WebDriver webDriver, List<String> xLMyAccountDropDownMenuList) {

		List<String> webMyAccountDropDownMenuList = new ArrayList<String>();
		try {

			WebElement ul=webDriver.findElement(By.id(xpathIDMap.get("MyAccount_Dropdown_Id")));
			List<WebElement> webMyAccountDropdownList=ul.findElements(By.tagName("a"));		
			int mYAccountDropdownSize=webMyAccountDropdownList.size();			
			
			for (int i = 0; i < mYAccountDropdownSize; i++) {		
				
				String webDropdownName = webMyAccountDropdownList.get(i).getAttribute("textContent");					
				
				/** MenuName1 is 'My Points Summary',(based on XL convention that MenuName1 is not
				 * blank and can only be PointsSummary)  but some times it contains exclamation mark with
				 * the content 'Points/Miles will expire soon'. So, get the substring of length same
				 * as excel input- My Points Summary which can be in different languages
				 */
				if(i==0)
				{
					String pointsSummaryStr=xLMyAccountDropDownMenuList.get(i);
					int xLPointsSummaryStrLength =pointsSummaryStr.length();
					if(xLPointsSummaryStrLength>0)
						if(xLPointsSummaryStrLength!=webDropdownName.length())
							webDropdownName = webDropdownName.substring(0,xLPointsSummaryStrLength);
				}
				
				
				webMyAccountDropDownMenuList.add(webDropdownName);
			}
			//System.out.println("webMyAccountDropDownMenuList" + webMyAccountDropDownMenuList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return webMyAccountDropDownMenuList;
	}
	
		
	

}
