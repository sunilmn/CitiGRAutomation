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
		String xlFAQCategoryTitle = (String) eachRowMap.get("FAQCategoryTitle");
		String xlContacUsMenuName = (String) eachRowMap.get("ContacUsMenuName");
		String webBreadcrumb ="";
		String errorDetails ="";
		WebDriverWait wait = new WebDriverWait(webDriver, 10);
		boolean faqDefaultPage = true;

		if (xlFAQBreadCrumb.isEmpty()) {
			errorDetails += pageName + " FAQBreadCrumb xl input is blank." + Constants.HASH;
		} else if (xlFAQDefault.isEmpty()) {
			errorDetails += pageName + " FAQDefault xl input is blank." + Constants.HASH;
		} else if (xlFAQCategoryTitle.isEmpty()) {
			errorDetails += pageName + " FAQCategoryTitle xl input is blank." + Constants.HASH;
		} else if (xlContacUsMenuName.isEmpty()) {
			errorDetails += pageName + " ContacUsMenuName xl input is blank." + Constants.HASH;
		} else {
			// this function is called to close the ForeSee survey popup
			webUtil.closeForeSeeSurveyPopup(webDriver);

			// check for FAQ Default
			try {

				String webFAQDefault = wait
						.until(ExpectedConditions.elementToBeClickable((By.cssSelector(xpathIDMap.get("FAQDefault")))))
						.getText();

				if (!webFAQDefault.equals(xlFAQDefault))
					errorDetails += pageName + xlFAQDefault + " Excel 'FAQDefault' not matching with webpage"
							+ Constants.HASH;

			} catch (NoSuchElementException | TimeoutException e) {
				// TODO: handle exception
				errorDetails += pageName+xlFAQDefault +"Default page not loaded."+Constants.HASH;
				e.printStackTrace();
				faqDefaultPage = false;
			}

			if (faqDefaultPage) {
				// check for breadCrumb -> Home / FAQs and Contact Us / PROGRAM INFORMATION
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
									.elementToBeClickable((By.cssSelector(xpathIDMap.get("FAQCategoryTitle")))))
							.getText();

					if (!webFAQCategoryTitle.equals(xlFAQCategoryTitle))
						errorDetails += pageName + xlFAQCategoryTitle
								+ " Excel 'FAQCategoryTitle' not matching Webpage FAQCategoryTitle." + Constants.HASH;

				} catch (NoSuchElementException | TimeoutException e) {
					// TODO: handle exception
					errorDetails += pageName + xlFAQCategoryTitle + " not found in webpage." + Constants.HASH;
					e.printStackTrace();
				}

				// Check for FAQ CategoryTitle
				try {
					String webContacUsMenuName = wait
							.until(ExpectedConditions
									.elementToBeClickable((By.cssSelector(xpathIDMap.get("ContacUsMenuName")))))
							.getText();

					if (webContacUsMenuName.isEmpty())
						errorDetails += pageName + xlContacUsMenuName + " not found." + Constants.HASH;

				} catch (NoSuchElementException | TimeoutException e) {
					// TODO: handle exception
					errorDetails += pageName + xlContacUsMenuName + " not found in webpage." + Constants.HASH;
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
	public String validateHelpAndFaq(WebDriver webDriver, Map<String, Object> eachRowMap) {
		String errorDetails = "";
		String reportStatus = "";
		String pageName = "FAQ->";
		try {
			String xlHelpMenuNameValue = (String) eachRowMap.get("HelpMenuName");
			String xlFAQMenuNameValue = (String) eachRowMap.get("FAQMenuName");

			WebDriverWait wait = new WebDriverWait(webDriver, 10);
			if (xlHelpMenuNameValue.isEmpty()) {
				errorDetails += Constants.FAIL + " HelpMenuName xl input is blank." + Constants.HASH;
			} else if (xlFAQMenuNameValue.isEmpty()) {
				errorDetails += Constants.FAIL + " FAQMenuName xl input is blank." + Constants.HASH;
			} else {

				wait.until(ExpectedConditions.elementToBeClickable((By.partialLinkText(xlHelpMenuNameValue)))).click();

				wait.until(ExpectedConditions.elementToBeClickable((By.partialLinkText(xlFAQMenuNameValue)))).click();

				errorDetails = intialCommonHelpFaqContactUS(webDriver, eachRowMap, pageName);

			}

		} catch (NoSuchElementException | TimeoutException e) {
			errorDetails = pageName + " page not loaded." + Constants.HASH;
			e.printStackTrace();
		} catch (Exception e) {
			errorDetails = Constants.FAIL + Constants.UNEXPECTED_ERROR + "." + Constants.HASH;
			e.printStackTrace();
		}

		if (!errorDetails.isEmpty()) {
			outputOp.saveScreenshotToFile(webDriver, eachRowMap);
			reportStatus = Constants.FAIL + errorDetails + Constants.TAB;
		} else {
			reportStatus = Constants.PASS;
		}

		return reportStatus;
	}

	/**
	 * Method to validate ContactUS
	 */
	public String validateContactUS(WebDriver webDriver, Map<String, Object> eachRowMap) {
		String errorDetails ="";
		String reportStatus ="";
		String pageName = "ContactUS->";
		try {
			String xlContactUsValue = (String) eachRowMap.get("ContacUsMenuName");
			if (xlContactUsValue.isEmpty()) {
				errorDetails +=pageName + "ContactUs Excel input is blank."+Constants.HASH;
			} else {
				WebDriverWait wait = new WebDriverWait(webDriver, 10);
				wait.until(ExpectedConditions.elementToBeClickable((By.partialLinkText(xlContactUsValue)))).click();

				errorDetails = intialCommonHelpFaqContactUS(webDriver, eachRowMap, pageName);
			}

		} catch (NoSuchElementException | TimeoutException e) {
			errorDetails =pageName+ "page not loaded."+Constants.HASH;
			e.printStackTrace();
		} catch (Exception e) {
			errorDetails = Constants.FAIL + Constants.UNEXPECTED_ERROR + ". " + Constants.HASH;
			e.printStackTrace();
		}

		if (!errorDetails.isEmpty()) {
			outputOp.saveScreenshotToFile(webDriver, eachRowMap);
			reportStatus = Constants.FAIL + errorDetails;
		} else {
			reportStatus = Constants.PASS;
		}
		return reportStatus;
	}

	/**
	 * 
	 * WebPage and Excel Category compare
	 *
	 */
	public String comapareCategaory(WebDriver webDriver, Map<String, Object> eachRowMap) {
		String reportStatus = "";
		String errorDetails = "";
		try {
			// Reading the data from the XL Category
			Map<String, String> xLCategoryMap = new LinkedHashMap<String, String>();
			xLCategoryMap = getXLAllCategoryList(eachRowMap);

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
			errorDetails = Constants.FAIL + Constants.UNEXPECTED_ERROR + "." + Constants.HASH;
			e.printStackTrace();
		}

		if (errorDetails.isEmpty())
			reportStatus = Constants.PASS + "-";

		else {
			reportStatus = Constants.FAIL + errorDetails;
			outputOp.saveScreenshotToFile(webDriver, eachRowMap);
		}

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
	public String compareEachCategoryDetail(WebDriver webDriver, Map<String, Object> eachRowMap) {

		String errorDetails = "";
		String reportStatus = "";
		try {

			String xlFAQBreadCrumbOld = (String) eachRowMap.get("FAQBreadCrumb");
			String xlFAQDefault = (String) eachRowMap.get("FAQDefault");
			boolean faqCategory = true;

			// Reading the data from the XL Category
			Map<String, String> xLCategoryMap = new LinkedHashMap<String, String>();
			xLCategoryMap = getXLAllCategoryList(eachRowMap);

			WebDriverWait wait = new WebDriverWait(webDriver, 10);

			// iterate XL categoryMap - for each non empty xl categoryMap
			for (Entry<String, String> entry : xLCategoryMap.entrySet()) {
				String webBreadcrumb = "";
				String XlcategoryKey = entry.getKey();
				String XlcategoryValue = entry.getValue();

				try {
					// Click one category
					wait.until(ExpectedConditions.elementToBeClickable((By.partialLinkText(XlcategoryKey)))).click();
				} catch (NoSuchElementException | TimeoutException e) {
					// TODO: handle exception
					errorDetails += XlcategoryKey + " FAQCategory not found in webpage." + Constants.HASH;
					faqCategory = false;
					e.printStackTrace();
				}

				// this function is called to close the ForeSee survey popup
				webUtil.closeForeSeeSurveyPopup(webDriver);

				if (faqCategory) {
					try {
						// Check the title
						String webFAQCategoryTitle = wait
								.until(ExpectedConditions.elementToBeClickable((By.cssSelector(xpathIDMap.get("FAQDefault")))))
								.getText();

						if (!webFAQCategoryTitle.equals(XlcategoryKey))
							errorDetails += XlcategoryKey
									+ " Excel FAQCategoryTitle not matching with Webpage_FAQCategoryTitle."
									+ Constants.HASH;
					} catch (NoSuchElementException | TimeoutException e) {
						// TODO: handle exception
						errorDetails += XlcategoryKey + " FAQCategoryTitle not found in webpage." + Constants.HASH;
						e.printStackTrace();
					}

					try {
						// Check the breadcrumb
						List<WebElement> webBreadcrumbsList = webDriver
								.findElements(By.cssSelector(xpathIDMap.get("Bread_CrumbcheckFAQsand_ContactUs")));

						// iterate breadCrumbList
						for (int i = 0; i < webBreadcrumbsList.size(); i++) {
							if (i == 0)
								webBreadcrumb += webBreadcrumbsList.get(i).getText();
							else
								webBreadcrumb += " / " + webBreadcrumbsList.get(i).getText();
						}

						String returnConstantBreadCrumb = returnConstantBreadCrumb(xlFAQBreadCrumbOld);
						returnConstantBreadCrumb += " / " + XlcategoryKey;

						if (!webBreadcrumb.equals(returnConstantBreadCrumb))
							errorDetails += XlcategoryKey + " Excel Category breadCrumb not matching with webpage_breadCrumb."
									+ Constants.HASH;
					} catch (NoSuchElementException | TimeoutException e) {
						// TODO: handle exception
						errorDetails += XlcategoryKey + "FAQBreadCrumb not found in webpage."+ Constants.HASH;
						e.printStackTrace();
					}

					try {
						// Check the question count
						String webFAQCategoryQuestionCount = wait
								.until(ExpectedConditions
										.elementToBeClickable(By.cssSelector(xpathIDMap.get("Question_Count"))))
								.getText();

						if (!webFAQCategoryQuestionCount.equals(XlcategoryValue.replace(".0", "")))
							errorDetails += XlcategoryKey
									+ " Excel FAQCategoryQuestionCount not matching with Web_FAQCategoryQuestionCount."
									+ Constants.HASH;
					} catch (NoSuchElementException | TimeoutException e) {
						// TODO: handle exception
						errorDetails += XlcategoryKey + " FAQCategoryQuestionCount not found in webpage."
								+ Constants.HASH;
						e.printStackTrace();
					}
				}

			} // End For
		} catch (Exception e) {
			errorDetails = Constants.FAIL + Constants.UNEXPECTED_ERROR + ". " + Constants.HASH;
			e.printStackTrace();
		}

		if (errorDetails.isEmpty())
			reportStatus = Constants.PASS + "-";

		else {
			reportStatus = Constants.FAIL + errorDetails;
			outputOp.saveScreenshotToFile(webDriver, eachRowMap);
		}
		return reportStatus;
	}

	/*
	 * to get all the Category1,Category2, Category3 from xl sheet
	 * 
	 */
	public Map<String, String> getXLAllCategoryList(Map<String, Object> rowMap) {

		Map<String, String> xLCategorysAndQuestionMap = new LinkedHashMap<String, String>();

		Map<String, String> xLCategory = new LinkedHashMap<String, String>();
		int i = 1;
		// iterate all the maps in a row to retrieve only the non-empty MenuName
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
			String cateoryNameValue = entry.getValue();
			String pageTitleSubstring = categoryNameKeyHeading + "Qs";
			String pageTitle = (String) rowMap.get(pageTitleSubstring);
			xLCategorysAndQuestionMap.put(cateoryNameValue, pageTitle);
		}

		return xLCategorysAndQuestionMap;

	}

	public String returnConstantBreadCrumb(String breadCrumb) {

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

}
