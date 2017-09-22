/**
 * 
 */
package com.citi.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author prajendra
 *
 */
public class ExcelUtil {

	
	
	
	/*
	 * 
	 * 
	 * to get the mega-menu map from XL sheet. 
	 * Returned map will have data like ([MegaMenu1,Merchandise], [MegaMenu2,Travel]...)
	 * 
	 * 
	 */
	public Map<String, String> getxLMegamenuMap(Map<String, Object> rowMap) {
		
		Map<String, String> xLmegaMenuMap = new LinkedHashMap<String, String>();
		
		// iterate all the maps in a row to retrieve only the non-empty megamenu
		// in xl sheet
		for (Entry<String, Object> entry : rowMap.entrySet()) {
			//key heading will be Megamenu1,Megamenu2, Submenu1...
			String keyHeading = entry.getKey();
			if (keyHeading.contains("Megamenu")) {
				String megamenuName = entry.getValue().toString();
				//if megamenu in the xl cell is not blank
				if (!megamenuName.isEmpty())				
					xLmegaMenuMap.put(keyHeading, megamenuName);
			}
		}
		System.out.println("xLmegaMenuMap------->" + xLmegaMenuMap);
		return xLmegaMenuMap;
	}

	/*
	 * 
	 * 
	 * to get the sub-menu map from XL sheet
	 * 
	 * 
	 */
	public Map<String, List<String>> getxLsubmenuMap(Map<String, Object> rowMap,Map<String, String>  xLmegaMenuMap) {
		
		Map<String, List<String>> xLmegaSubMenuMap = new LinkedHashMap<String, List<String>>();	
		
		// iterate megaMenuMap - for each xl megamenu
		for (Entry<String, String> entry : xLmegaMenuMap.entrySet()) {
			String megaHeading = entry.getKey();
			String megamenuName = entry.getValue();
			List<String> xLsubMenuList = new ArrayList<String>();
			// iterate all the maps in a excel row
			for (Entry<String, Object> innerEntry : rowMap.entrySet()) {
				String keyHeading = innerEntry.getKey();
				String substring = "Submenu" + megaHeading.substring(8);
				if (keyHeading.contains(substring)) {
					String subMenuName = innerEntry.getValue().toString();
					if (!subMenuName.isEmpty())
						xLsubMenuList.add(subMenuName);
				}
			}			
			xLmegaSubMenuMap.put(megamenuName, xLsubMenuList);
		}
		System.out.println("xLmegaSubMenuMap-->"+xLmegaSubMenuMap);
		return xLmegaSubMenuMap;

	}
	
	
	/*
	 * 
	 * 
	 * to get the MyAccount all non-empty menuNames from XL sheet
	 * 
	 * 
	 */
	public List<String> getxLMyAccountDropDownMenuList(Map<String, Object> rowMap) {
		
		List<String> xLMyAccountDropDownMenuList = new ArrayList<String>();
		
		// iterate all the maps in a row to retrieve only the non-empty menuName
		// in xl sheet
		for (Entry<String, Object> entry : rowMap.entrySet()) {
			
			//for key heading such as MenuName1,MenuName2, MenuName3...
			String keyHeading = entry.getKey();
			if (keyHeading.contains("MenuName")) {
				String menuName = entry.getValue().toString();
				//if MenuName in the xl cell is not blank
				if (!menuName.isEmpty())				
					xLMyAccountDropDownMenuList.add(menuName);
			}
		}		
	
		//System.out.println("xLMyAccountDropDownMenuList------>" + xLMyAccountDropDownMenuList);
		return xLMyAccountDropDownMenuList;
	}
	
	
	/*
	 * to get all the PageTitle1,PageTitle2, PageTitle3 from xl sheet
	 * 
	 */
	public List<String> getXLAllPageTitleList(Map<String, Object> rowMap)
	{
		
		List<String> xLPageTitleList = new ArrayList<String>();
		Map<String, String> xLMenuNameMap = new LinkedHashMap<String, String>();
		
		// iterate all the maps in a row to retrieve only the non-empty MenuName		
		for (Entry<String, Object> entry : rowMap.entrySet()) {
			// key heading will be MenuName1,MenuName2, MenuName3...
			String keyHeading = entry.getKey();
			if (keyHeading.contains("MenuName")) {
				String menuName = entry.getValue().toString();
				// if MenuName in the xl cell is not blank
				if (!menuName.isEmpty())
					xLMenuNameMap.put(keyHeading, menuName);
			}
		}
		
		//for each non-empty MenuName, get corresponding pageTitle
		for (Entry<String, String> entry : xLMenuNameMap.entrySet()) {
			String menuNameKeyHeading=entry.getKey();
			String pageTitleSubstring = "PageTitle" + menuNameKeyHeading.substring(8);
			String pageTitle =(String) rowMap.get(pageTitleSubstring);
			xLPageTitleList.add(pageTitle);			
		}	
		
		
		//System.out.println("xLPageTitleList------>" + xLPageTitleList);
		return xLPageTitleList;

	}

	
}
