package com.citi.testCase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.citi.fileActivity.OutputOperations;
import com.citi.gr.CitiMain;
import com.citi.util.Constants;
import com.citi.util.ExcelUtil;
import com.citi.util.WebUtil;

/**
 * @author suchandra
 */
public class FaqContactUs {

	Map<String, String> xpathIDMap = new LinkedHashMap<String, String>();
	WebUtil webUtil = new WebUtil();
	ExcelUtil excelUtil = new ExcelUtil();
	OutputOperations outputOp = new OutputOperations();

	public FaqContactUs() {
		// TODO Auto-generated constructor stub
		this.xpathIDMap = CitiMain.xpathIDMap;
	}

	/**
	 * 
	 * // Method will check for the FAQContactUs in main menu
	 *
	 */
	public String intialCommonHelpFaqContactUS(WebDriver webDriver, Map<String, Object> eachRowMap, String pageName) {

		String xlFAQBreadCrumb = (String) eachRowMap.get("FAQBreadCrumb");
		String xlFAQDefault = (String) eachRowMap.get("FAQDefault");
		String xlFAQCategoryTitle = (String) eachRowMap.get("FAQDefaultCategoryTitle");
		//to check ContactUs section
		String xlContacUsMenuName = (String) eachRowMap.get("ContacUsMenuName");
		String webBreadcrumb = "";
		String errorDetails = "";
		WebDriverWait wait = new WebDriverWait(webDriver, 10);
		boolean faqDefaultPage = true;

		if (xlFAQBreadCrumb.isEmpty()) {
			errorDetails += pageName + " FAQBreadCrumb xl input is blank." + Constants.HASH;
		} else if (xlFAQDefault.isEmpty()) {
			errorDetails += pageName + " FAQDefault xl input is blank." + Constants.HASH;
		} else if (xlFAQCategoryTitle.isEmpty()) {
			errorDetails += pageName + " FAQDefaultCategoryTitle xl input is blank." + Constants.HASH;
		} else if (xlContacUsMenuName.isEmpty()) {
			errorDetails += pageName + " ContacUsMenuName xl input is blank." + Constants.HASH;
		} else {
			// this function is called to close the ForeSee survey popup
			webUtil.closeForeSeeSurveyPopup(webDriver);

			/*// check for FAQ Default
			try {

				String webFAQDefault = wait
						.until(ExpectedConditions
								.elementToBeClickable((By.cssSelector(xpathIDMap.get("FAQPageCategoryTitle")))))
						.getText();

				if (!webFAQDefault.equals(xlFAQDefault))
					errorDetails += pageName + xlFAQDefault + " Excel 'FAQDefault' not matching with webpage"
							+ Constants.HASH;

			} catch (NoSuchElementException | TimeoutException e) {
				// TODO: handle exception
				errorDetails += pageName + "-" + xlFAQDefault + " Default page not loaded." + Constants.HASH;
				e.printStackTrace();
				faqDefaultPage = false;
			}*/

			if (faqDefaultPage) {
				// check for breadCrumb -> Home / FAQs and Contact Us / PROGRAM
				// INFORMATION
				try {

					List<WebElement> webBreadcrumbsList = webDriver
							.findElements(By.cssSelector(xpathIDMap.get("Bread_CrumbcheckFAQsand_ContactUs")));
					// iterate breadCrumbList
					for (int i = 0; i < webBreadcrumbsList.size(); i++) {
						if (i == 0)
							webBreadcrumb += webBreadcrumbsList.get(i).getText();
						else
							webBreadcrumb += " / " + webBreadcrumbsList.get(i).getText();

					}

					if (!webBreadcrumb.equals(xlFAQBreadCrumb))
						errorDetails += pageName + xlFAQDefault
								+ " Excel Default breadCrumb not matching with webpage breadCrumb." + Constants.HASH;
				} catch (NoSuchElementException | TimeoutException e) {
					// TODO: handle exception
					errorDetails += pageName + xlFAQDefault + " BreadCrumb not found in webpage." + Constants.HASH;
					e.printStackTrace();
				}

				// Check for FAQ CategoryTitle
				try {
					String webFAQCategoryTitle = wait
							.until(ExpectedConditions
									.elementToBeClickable((By.cssSelector(xpathIDMap.get("FAQDefaultCategoryTitle")))))
							.getText();

					if (!webFAQCategoryTitle.equals(xlFAQCategoryTitle))
						errorDetails += pageName + xlFAQCategoryTitle
								+ " Excel 'FAQDefaultCategoryTitle' not matching Webpage FAQDefaultCategoryTitle."
								+ Constants.HASH;

				} catch (NoSuchElementException | TimeoutException e) {
					// TODO: handle exception
					errorDetails += pageName + xlFAQCategoryTitle + " not found in webpage." + Constants.HASH;
					e.printStackTrace();
				}

				
				
				// Check for ContactUs Section (using the same name as XL ContacUsMenuName)			
				try {
					String webContacUsMenuName = wait
							.until(ExpectedConditions
									.elementToBeClickable((By.cssSelector(xpathIDMap.get("ContacUsMenuName")))))
							.getText();

					if (webContacUsMenuName.isEmpty())
						errorDetails += pageName + xlContacUsMenuName + " section not found." + Constants.HASH;

				} catch (NoSuchElementException | TimeoutException e) {
					// TODO: handle exception
					errorDetails += pageName + xlContacUsMenuName + " section not found in webpage." + Constants.HASH;
					e.printStackTrace();
				}
			}
		}
		return errorDetails;
	}

	/**
	 * 
	 * Method to validate Help and FAQ
	 *
	 */
	public String checkHelpAndFaqPage(WebDriver webDriver, Map<String, Object> eachRowMap) {
		String errorDetails = "";
		String reportStatus = "";
		String pageName = Constants.FAQ;
		try {
			String xlHelpMenuNameValue = (String) eachRowMap.get("HelpMenuName");
			String xlFAQMenuNameValue = (String) eachRowMap.get("FAQMenuName");

			WebDriverWait wait = new WebDriverWait(webDriver, 10);
			if (xlHelpMenuNameValue.isEmpty()) {
				errorDetails += Constants.XL_EMPTY_ERROR + " HelpMenuName xl input is blank." + Constants.HASH;
			} else if (xlFAQMenuNameValue.isEmpty()) {
				errorDetails += Constants.XL_EMPTY_ERROR + " FAQMenuName xl input is blank." + Constants.HASH;
			} else {

				wait.until(ExpectedConditions.elementToBeClickable((By.partialLinkText(xlHelpMenuNameValue)))).click();
				wait.until(ExpectedConditions.elementToBeClickable((By.partialLinkText(xlFAQMenuNameValue)))).click();				
				
				errorDetails += checkDefaultPageLoad(webDriver, eachRowMap, pageName);

				//errorDetails = intialCommonHelpFaqContactUS(webDriver, eachRowMap, pageName);

			}

		} catch (NoSuchElementException | TimeoutException e) {
			errorDetails += pageName + " page not loaded." + Constants.HASH;
			e.printStackTrace();
		} catch (Exception e) {
			errorDetails += Constants.UNEXPECTED_ERROR + "." + Constants.HASH;
			e.printStackTrace();
		}

		reportStatus = outputOp.statusReporter(errorDetails, eachRowMap, webDriver);
		return reportStatus;
	}

	/**
	 * Method to validate ContactUS
	 */
	public String validateContactUS(WebDriver webDriver, Map<String, Object> eachRowMap) {
		String errorDetails = "";
		String reportStatus = "";
		String pageName = Constants.CONTACTUS;
		try {
			String xlContactUsValue = (String) eachRowMap.get("ContacUsMenuName");
			if (xlContactUsValue.isEmpty()) {
				errorDetails += pageName + " - Excel input is blank." + Constants.HASH;
			} else {
				
				//click on ContactUs link
				WebDriverWait wait = new WebDriverWait(webDriver, 10);
				wait.until(ExpectedConditions.elementToBeClickable((By.partialLinkText(xlContactUsValue)))).click();

				//check if page loads
				errorDetails = checkDefaultPageLoad(webDriver, eachRowMap, pageName);
				if(errorDetails.isEmpty())				
						errorDetails = intialCommonHelpFaqContactUS(webDriver, eachRowMap, pageName);
			}

		} catch (NoSuchElementException | TimeoutException e) {
			errorDetails += pageName + " page not loaded." + Constants.HASH;
			e.printStackTrace();
		} catch (Exception e) {
			errorDetails += Constants.UNEXPECTED_ERROR + ". " + Constants.HASH;
			e.printStackTrace();
		}

		reportStatus = outputOp.statusReporter(errorDetails, eachRowMap, webDriver);

		return reportStatus;
	}

	/**
	 * 
	 * WebPage and Excel Category compare
	 *
	 */
	public String compareCategoryList(WebDriver webDriver, Map<String, Object> eachRowMap) {
		String reportStatus = "";
		String errorDetails = "";
		try {
			// Reading the data from the XL Category
			Map<String, String> xLCategoryMap = new LinkedHashMap<String, String>();
			xLCategoryMap = getXLAllCategoryMap(eachRowMap);

			// Read the Category from from the web page
			List<String> webCategoryList = new ArrayList<>();
			webCategoryList = getWebCategory(webDriver);

			List<String> xLmissingCategoryList = new ArrayList<String>();
			List<String> webExtraCategoryList = new ArrayList<String>();

			// check if each item in xl-Category list is
			// present in web-category list, if not present
			// then it is missing
			for (Entry<String, String> entry : xLCategoryMap.entrySet()) {
				String xLCategoryName = entry.getKey();
				if (webCategoryList.contains(xLCategoryName))
					continue;
				else
					xLmissingCategoryList.add(xLCategoryName);
			}
			if (!xLmissingCategoryList.isEmpty())
				errorDetails = xLmissingCategoryList.toString() + " is missing. " + Constants.HASH;

			// check if each item in web-Category
			// list is present in xl-Category,
			// if not present then it is extra
			for (int i = 0; i < webCategoryList.size(); i++) {
				String webCategoryName = webCategoryList.get(i);
				if (xLCategoryMap.containsKey(webCategoryName))
					continue;
				else
					webExtraCategoryList.add(webCategoryName);
			}
			if (!webExtraCategoryList.isEmpty())
				errorDetails += webExtraCategoryList.toString() + " is extra." + Constants.HASH;

		} catch (Exception e) {
			errorDetails += Constants.FAIL + Constants.UNEXPECTED_ERROR + "." + Constants.HASH;
			e.printStackTrace();
		}

		reportStatus = outputOp.statusReporter(errorDetails, eachRowMap, webDriver);
		return reportStatus;
	}

	/*
	 * 
	 * 
	 * Get the Category list from web page
	 * 
	 * 
	 */
	public List<String> getWebCategory(WebDriver webDriver) {

		List<String> webCategoryList = new ArrayList<>();
		try {

			List<WebElement> webElementsList = webDriver.findElements(By.tagName("li"));
			webElementsList.remove(0);

			for (int i = 0; i < webElementsList.size(); i++) {
				String idWebElement = webElementsList.get(i).getAttribute("class");

				if ((!idWebElement.equals("")) && idWebElement.startsWith("category")) {
					String textWebElement = webElementsList.get(i).getText();
					webCategoryList.add(textWebElement);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return webCategoryList;
	}

	/**
	 * 
	 * Comparing the WebElement and XLCategoryDetails.
	 *
	 */
	public String checkEachCategoryDetail(WebDriver webDriver, Map<String, Object> eachRowMap) {

		String errorDetails = "";
		String reportStatus = "";
		try {

			String xlFAQBreadCrumb = (String) eachRowMap.get("FAQBreadCrumb");
			boolean faqCategory = true;

			// Reading the data from the XL Category
			Map<String, String> xLCategoryMap = new LinkedHashMap<String, String>();
			xLCategoryMap = getXLAllCategoryMap(eachRowMap);

			WebDriverWait wait = new WebDriverWait(webDriver, 10);

			// iterate XL categoryMap - for each non empty xl categoryMap
			for (Entry<String, String> entry : xLCategoryMap.entrySet()) {
				String webBreadcrumb = "";
				String xlcategoryTitle = entry.getKey();
				String xlQuestionCount = entry.getValue();

				// Checks if the category is found in the LeftNavigation
				// CategoryList
				try {
					// Click one category
					wait.until(ExpectedConditions.elementToBeClickable((By.partialLinkText(xlcategoryTitle)))).click();
				} catch (NoSuchElementException | TimeoutException e) {
					// TODO: handle exception
					errorDetails += xlcategoryTitle + " 'FAQCategory' not found in LeftNavigation of webpage."
							+ Constants.HASH;
					faqCategory = false;
					e.printStackTrace();
				}

				// this function is called to close the ForeSee survey popup
				webUtil.closeForeSeeSurveyPopup(webDriver);

				if (faqCategory) {
					try {

						// 1st -> Check the page title
						String webFAQCategoryTitle = wait
								.until(ExpectedConditions
										.elementToBeClickable((By.cssSelector(xpathIDMap.get("FAQPageCategoryTitle")))))
								.getText();

						if (!webFAQCategoryTitle.equals(xlcategoryTitle))
							errorDetails += xlcategoryTitle
									+ " Excel FAQCategoryTitle not matching with Webpage_FAQCategoryTitle."
									+ Constants.HASH;
					} catch (NoSuchElementException | TimeoutException e) {
						// TODO: handle exception
						errorDetails += xlcategoryTitle + " FAQCategoryTitle not found in webpage." + Constants.HASH;
						e.printStackTrace();
					}

					// 2nd - Check the breadcrumb
					try {

						List<WebElement> webBreadcrumbsList = webDriver
								.findElements(By.cssSelector(xpathIDMap.get("Bread_CrumbcheckFAQsand_ContactUs")));

						// construct web breadcrumb in the appropriate format
						for (int i = 0; i < webBreadcrumbsList.size(); i++) {
							if (i == 0)
								webBreadcrumb += webBreadcrumbsList.get(i).getText();
							else
								webBreadcrumb += " / " + webBreadcrumbsList.get(i).getText();
						}

						// get XL breadCrumb (static part)
						String xlBreadCrumb = getXLStaticBreadCrumb(xlFAQBreadCrumb);

						// append dynamic xlcategoryTitle
						xlBreadCrumb += " / " + xlcategoryTitle;

						if (!webBreadcrumb.equals(xlBreadCrumb))
							errorDetails += xlcategoryTitle
									+ " Excel Category breadCrumb not matching with webpage_breadCrumb."
									+ Constants.HASH;
					} catch (NoSuchElementException | TimeoutException e) {
						// TODO: handle exception
						errorDetails += xlcategoryTitle + "FAQBreadCrumb not found in webpage." + Constants.HASH;
						e.printStackTrace();
					}

					// 3rd- Check the question count
					try {
						String webFAQCategoryQuestionCount = wait
								.until(ExpectedConditions
										.elementToBeClickable(By.cssSelector(xpathIDMap.get("Question_Count"))))
								.getText();

						if (!webFAQCategoryQuestionCount.equals(xlQuestionCount.replace(".0", "")))
							errorDetails += xlcategoryTitle
									+ " Excel FAQCategoryQuestionCount not matching with Web_FAQCategoryQuestionCount."
									+ Constants.HASH;
					} catch (NoSuchElementException | TimeoutException e) {
						// TODO: handle exception
						errorDetails += xlcategoryTitle + " FAQCategoryQuestionCount not found in webpage."
								+ Constants.HASH;
						e.printStackTrace();
					}
				}

			} // End For
		} catch (Exception e) {
			errorDetails = Constants.UNEXPECTED_ERROR + ". " + Constants.HASH;
			e.printStackTrace();
		}

		reportStatus = outputOp.statusReporter(errorDetails, eachRowMap, webDriver);
		return reportStatus;
	}

	/*
	 * to get map of all the xlcategoryTitle And Question Count
	 */
	public Map<String, String> getXLAllCategoryMap(Map<String, Object> rowMap) {

		Map<String, String> xLCategorysAndQuestionMap = new LinkedHashMap<String, String>();

		Map<String, String> xLCategory = new LinkedHashMap<String, String>();
		int i = 1;
		// iterate all the maps in a row to retrieve only the non-empty category
		for (Entry<String, Object> entry : rowMap.entrySet()) {
			// key heading will be Category1,Category2, Category3...
			String keyHeading = entry.getKey();
			if (keyHeading.contains("Category" + i)) {
				String CategoryName = entry.getValue().toString();
				i++;
				// if category in the xl cell is not blank
				if (!CategoryName.isEmpty())
					xLCategory.put(keyHeading, CategoryName);
			}
		}

		// for each non-empty category, get corresponding question count
		for (Entry<String, String> entry : xLCategory.entrySet()) {
			String categoryNameKeyHeading = entry.getKey();
			String xlcategoryTitle = entry.getValue();
			String xlFaqCategoryQuestion = categoryNameKeyHeading + "Qs";
			String questionCount = (String) rowMap.get(xlFaqCategoryQuestion);
			xLCategorysAndQuestionMap.put(xlcategoryTitle, questionCount);
		}

		return xLCategorysAndQuestionMap;

	}

	
	/*
	 * 
	 * In Excel, the breadcrumb is of the format 'Home / FAQs and Contact US / PROGRAM INFORMATION'
     * This function gets the first and second part (static) and returns the string
	 * 
	 */
	public String getXLStaticBreadCrumb(String breadCrumb) {

		String[] split = breadCrumb.split("/");
		String finalBreadCrumb = "";
		for (int i = 0; i < split.length; i++) {

			if (i == 0)
				finalBreadCrumb += split[i];

			if (i == 1)
				finalBreadCrumb += " / " + split[i];
		}

		return finalBreadCrumb;
	}

	/*
	 * Check if the default page loads , by taking the xl input which mentions 
	 * FAQDefault(i.e, the default category that is seen when page loads)
	 * 
	 */
	public String checkDefaultPageLoad(WebDriver webDriver, Map<String, Object> eachRowMap, String pageName) {
		String xlFAQDefault = (String) eachRowMap.get("FAQDefault");
		String errorDetails = "";
		WebDriverWait wait = new WebDriverWait(webDriver, 10);

		if (xlFAQDefault.isEmpty()) {
			errorDetails += Constants.XL_EMPTY_ERROR + " 'FAQDefault' xl input is blank." + Constants.HASH;
		} else {
			
			// check if the default page loads
			try {

				String webFAQDefault = wait
						.until(ExpectedConditions
								.elementToBeClickable((By.cssSelector(xpathIDMap.get("FAQPageCategoryTitle")))))
						.getText();

				if (!webFAQDefault.equals(xlFAQDefault))
					errorDetails += pageName + xlFAQDefault + " -Excel 'FAQDefault' not matching with webpage"
							+ Constants.HASH;

			} catch (NoSuchElementException | TimeoutException e) {
				// TODO: handle exception
				errorDetails += pageName + xlFAQDefault + " -Default page not loaded." + Constants.HASH;
				e.printStackTrace();
			}
		}
		return errorDetails;
	}

}
