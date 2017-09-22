/*
 * @author prajendra
 * 
 * 
 */

package com.citi.fileActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelRead {

	private int configurationSheetIndex = 0;
	private int testCaseMenuSheetIndex = 1;

	/*
	 * public static void main(String[] args) {
	 * 
	 * // this is a list having testcases for which 'execute' is 'yes'
	 * List<String> testCaseNameList = getMasterSheet("NewTestCasesGR.xlsx");
	 * System.out.println("testCaseNameList..." + testCaseNameList);
	 * 
	 * //Map<String,List<List<Map<String, Object>>>>
	 * testCaseSheetMap=readTestCaseFile("NewTestCasesGR.xlsx",
	 * testCaseNameList); Map<String,List<Map<String, Object>>>
	 * testCaseSheetMap=readTestCaseFile("NewTestCasesGR.xlsx",
	 * testCaseNameList); System.out.println(
	 * "------------------testCaseSheetMap---------------------" +
	 * testCaseSheetMap); }
	 */

	/**
	 * 
	 * Reads Configuration(first)sheet data of testCase XLFile to a map
	 * 
	 */
	public Map<String, Map<String, Object>> getConfigurationSheet(String inputTestCaseFile) {

		// Map<String, String> configurationMap = new LinkedHashMap<String, String>();
		Map<String, Map<String, Object>> configurationMap = new LinkedHashMap<String, Map<String, Object>>();
		try {
			FileInputStream inputStream = new FileInputStream(new File(inputTestCaseFile));
			@SuppressWarnings("resource")
			Workbook workbook = new XSSFWorkbook(inputStream);
			
			XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(configurationSheetIndex);
			System.out.println("\n ********......getConfigurationSheet(FirstSheet)...*************");
			String sheetName = sheet.getSheetName();
			System.out.println("SheetName=" + sheetName);

			int firstRowIndex = 0;
			XSSFRow firstRow = sheet.getRow(firstRowIndex);
			int firstRowColumnCount = firstRow.getLastCellNum();
			System.out.println("firstRowColumnCount=" + firstRowColumnCount);

			// for each row
			int rowCount = sheet.getPhysicalNumberOfRows();
			System.out.println("Number of rows..." + rowCount);
			for (int i = 1; i < rowCount; i++) {

				String configNameKey = sheet.getRow(i).getCell(0).getStringCellValue();
				Map<String, Object> configValuesMap = new LinkedHashMap<String, Object>();
				XSSFRow row = sheet.getRow(i);

				// from second column onwards-for each column 
				for (int j = 1; j < firstRowColumnCount; j++) { 

					Object columnValue = "";
					String columnHeadingKey = sheet.getRow(0).getCell(j).getStringCellValue();

					if (row.getCell(i) != null) {
						switch (row.getCell(j).getCellTypeEnum()) {
						case STRING:
							columnValue = row.getCell(j).getStringCellValue();
							break;
						case NUMERIC:
							columnValue = row.getCell(j).getNumericCellValue();
							break;
						case BOOLEAN:
							columnValue = row.getCell(j).getBooleanCellValue();
							break;
						default:
							columnValue = "";
							break;
						}
					}
					columnValue.toString().trim();
					configValuesMap.put(columnHeadingKey, columnValue);
				} // end for each column
				configurationMap.put(configNameKey, configValuesMap);
			}
			// System.out.println("configurationMap..." + configurationMap);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("Corrupted  Excel Sheet/Missing Data");
			e.printStackTrace();
		}
		return configurationMap;
	}

	/**
	 * 
	 * Reads TestCaseMaster(second)sheet data of testCase XLFile to a list. 
	 * Returned list has sheet names which has to be executed(Yes)
	 * 
	 */
	public List<String> getTestCaseMasterSheet(String inputTestCaseFile) {

		List<String> testCaseNameList = new ArrayList<String>();
		try {
			FileInputStream inputStream = new FileInputStream(new File(inputTestCaseFile));
			@SuppressWarnings("resource")
			Workbook workbook = new XSSFWorkbook(inputStream);

			XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(testCaseMenuSheetIndex);
			System.out.println("********......getSecondSheet(Master)...*************");
			String sheetName = sheet.getSheetName();
			System.out.println("Sheet=" + sheetName);

			// for each row
			int rowCount = sheet.getPhysicalNumberOfRows();
			System.out.println("Number of rows..." + rowCount);
			for (int i = 1; i < rowCount; i++) {

				// column2 - Execute
				String execute = sheet.getRow(i).getCell(1).getStringCellValue();
				if (execute.equalsIgnoreCase("Yes")) {
					/*// column1 - TestCaseName
					String key = sheet.getRow(i).getCell(0).getStringCellValue();*/
					// column3 - SheetName
					String key = sheet.getRow(i).getCell(2).getStringCellValue();
					testCaseNameList.add(key);
				}
			}
			// System.out.println("testCaseList..." + testCaseList);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("Corrupted  Excel Sheet...");
			e.printStackTrace();
		}
		return testCaseNameList;
	}

	/**
	 * Reads all data in a sheet(3rd sheet onwards-executable) from TestCase excel sheet and returns a map
	 * 
	 */
	public Map<String, List<Map<String, Object>>> readInputTestCaseFile(String inputTestCaseFile,
			List<String> executableSheetList) {

		System.out.println("\n ********......readTestCaseFile...*************");
		String excelFilePath = inputTestCaseFile;
		Map<String, List<Map<String, Object>>> testCaseSheetsDataMap = new LinkedHashMap<String, List<Map<String, Object>>>();

		try {
			FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
			@SuppressWarnings("resource")
			Workbook workbook = new XSSFWorkbook(inputStream);
			int sheetCount = workbook.getNumberOfSheets();
			System.out.println("sheetCount..." + sheetCount);

			for (String testCaseName : executableSheetList) {// for each testCase sheet
				// to be executed
				
				Boolean sheetFound=false;

				System.out.println("---------------------------");
				System.out.println("Sheet testCaseName ...." + testCaseName);
				List<Map<String, Object>> allrowLists = new ArrayList<Map<String, Object>>();

				for (int i = 2; i < sheetCount; i++) { // from sheet3 ,for each 
														// sheet
					XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(i);

					String sheetName = sheet.getSheetName();

					if (sheetName.equals(testCaseName)) {
						sheetFound=true;

						// first row - all column count
						int firstRowIndex = 0;
						XSSFRow firstRow = sheet.getRow(firstRowIndex);
						int firstRowColumnCount = firstRow.getLastCellNum();

						int rowCount = sheet.getPhysicalNumberOfRows();
						System.out.println("rowCount..." + rowCount);
						for (int j = 1; j < rowCount; j++) { // for each row, from second row
							Map<String, Object> columnMap = null;
							XSSFRow row = sheet.getRow(j);

							String rowExecute = row.getCell(1).getStringCellValue();
							if (rowExecute.equalsIgnoreCase("yes")) {
								columnMap = new LinkedHashMap<String, Object>();
								for (int k = 0; k < firstRowColumnCount; k++) { // for
																				// each
																				// column
									Object columnValue = "";
									String columnHeadingKey = sheet.getRow(0).getCell(k).getStringCellValue();

									XSSFCell cell=sheet.getRow(j).getCell(k);
									if (cell != null) {
										switch (cell.getCellTypeEnum()) {
										case STRING:
											columnValue = cell.getStringCellValue();
											break;
										case NUMERIC:
											columnValue = cell.getNumericCellValue();
											break;
										case BOOLEAN:
											columnValue = cell.getBooleanCellValue();
											break;
										case FORMULA:
											switch (cell.getCachedFormulaResultTypeEnum()) {
											case NUMERIC:
												columnValue = cell.getNumericCellValue();
												break;
											case STRING:
												columnValue = cell.getStringCellValue();
												break;
											default:
												columnValue = "defaultFormulaSwitchCase";
												break;
											}											
											break;
										case BLANK:
											columnValue = "";
											break;
										default:
											//columnValue = "";
											columnValue = "defaultSwitchCase";
											break;
										}
									}							
								
									
									columnValue = columnValue.toString().trim();
									columnMap.put(columnHeadingKey, columnValue);
								} // for each column
								allrowLists.add(columnMap);
							} // endif
						} // for each row
						//System.out.println("allRowLists..." + allrowLists);
						break;
					} // end if (sheetName.equals(testCaseName))
				} // for each sheet
				 
				if(sheetFound==false)
					System.out.println("XL Error: "+testCaseName+" Sheet not found.");
				testCaseSheetsDataMap.put(testCaseName, allrowLists);
			} // for testcaseNameList
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("Corrupted  Excel Sheet...");
			e.printStackTrace();
		}
		return testCaseSheetsDataMap;
	}

	/**
	 * Reads xpath and id data from XpathData excel sheet
	 * 
	 */
	public Map<String, String> readXPathData(String xpathDataFile) {

		System.out.println("\n ********......readXPathData...*************");		
		Map<String, String> xPathIdMap = new LinkedHashMap<String, String>();

		try {
			FileInputStream inputStream = new FileInputStream(new File(xpathDataFile));
			@SuppressWarnings("resource")
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			//int LastRowNum = sheet.getLastRowNum();
			int rowCount = sheet.getPhysicalNumberOfRows();
			System.out.println("readXPathData rowCount..." + rowCount);
			String key, value;
			for (int i = 0; i < rowCount; i++) {
				key = sheet.getRow(i).getCell(0).getStringCellValue().trim();
				value = sheet.getRow(i).getCell(1).getStringCellValue().trim();
				xPathIdMap.put(key, value);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("Corrupted MasterData Excel Sheet...");
			e.printStackTrace();
		}
		return xPathIdMap;
	}
	
	
	/**
	 * 
	 * Reads TestCaseMaster(second)sheet data of testCase XLFile to a map. 
	 * Returned map has executable(Sheet name, testCaseName) 
	 * 
	 */
	public Map<String,String> getMasterMap(String inputTestCaseFile) {

		 Map<String,String> masterMap = new  LinkedHashMap<String,String>();
		try {
			FileInputStream inputStream = new FileInputStream(new File(inputTestCaseFile));
			@SuppressWarnings("resource")
			Workbook workbook = new XSSFWorkbook(inputStream);

			XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(testCaseMenuSheetIndex);
			System.out.println("\n ********......getSecondSheet(Master)...*************");
			String sheetName = sheet.getSheetName();
			System.out.println("Sheet=" + sheetName);

			// for each row
			int rowCount = sheet.getPhysicalNumberOfRows();
			System.out.println("Number of rows..." + rowCount);
			for (int i = 1; i < rowCount; i++) {

				// column2 - Execute
				String execute = sheet.getRow(i).getCell(1).getStringCellValue();
				if (execute.equalsIgnoreCase("Yes")) {
					// column1 - TestCaseName is the value
					String testCaseName = sheet.getRow(i).getCell(0).getStringCellValue().trim();
					// column3 - SheetName is the key
					String keySheetName = sheet.getRow(i).getCell(2).getStringCellValue().trim();
					masterMap.put( keySheetName,testCaseName);
				}
			}
			// System.out.println("testCaseList..." + testCaseList);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("Corrupted  Excel Sheet...");
			e.printStackTrace();
		}
		return masterMap;
	}
	

}
