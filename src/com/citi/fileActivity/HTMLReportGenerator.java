/**
 * 
 */
package com.citi.fileActivity;

/**
 * @author prajendra
 *
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.citi.util.Constants;

public class HTMLReportGenerator {
	
	public static void main(String a[]) {
		String environmentUrl ="https://epsilon.com/";
		
		Map<String,String> testCaseSheetMasterMap =new LinkedHashMap<String,String>();
		//testCaseSheetMasterMap.put("Anonymous Mode ", "Anonymous Mode Log In");
		//testCaseSheetMasterMap.put("Anonymous  Megamenu", "Anonymous Mode Megamenu");
		//testCaseSheetMasterMap.put("Anonymous  MegaSubmenu", "Anonymous Mode Megamenu Submenu");
		//testCaseSheetMasterMap.put("Login ", "Login Mode");
		//testCaseSheetMasterMap.put("Login ", "Login Mode Megamenu");
		testCaseSheetMasterMap.put("Login ", "Login Mode Megamenu Submenu");
		//testCaseSheetMasterMap.put(" Order History", "My Account Order History");
		//testCaseSheetMasterMap.put("Points", "My Account My Points Summary");
		//testCaseSheetMasterMap.put("Menu", "My Account Menu");
		//testCaseSheetMasterMap.put("Menu", "CompleteLogin");
		testCaseSheetMasterMap.put("Menu", "Edit Email id in Profile Page");
		//testCaseSheetMasterMap.put("Menu", "Add Edit Loyalty Program in Profile Page");
		
		HTMLReportGenerator report = new HTMLReportGenerator();
		File reportHTMLFile = new File("abcReport.html");
		report.reportGenerator("result.txt",reportHTMLFile ,testCaseSheetMasterMap,environmentUrl, "01/01/01","02/02/02");
	}

	public void reportGenerator(String sourceFileStr, File reportHTMLFile,Map<String,String> testCaseSheetMasterMap, String environmentUrl,String startRunTime,String endRunTime ) {
		
		String pageTitle ="CitiGR Automation Test Report";
		System.out.println("*************----------"+pageTitle+"-----------**********");
		try {		
			
						
			int totalTestCaseCount = 0, totalPassCount=0, totalFailCount=0,totalPassPercent=0,totalFailPercent=0; 			
			//used in first table
			List<Object> eachTestCaseMenuList = new ArrayList<Object>();	
			//used in second table
			List<String> allLinesList = new ArrayList<>();	
			//iterate each executable testcase Master	
			for (Entry<String, String> entry : testCaseSheetMasterMap.entrySet()) {
				
				String testCaseName=entry.getValue().trim();
				int subPassCount = 0, subFailCount = 0, subTotalTestCaseCount = 0,subPassPercent=0,subFailPercent=0;
				FileReader fileReader = new FileReader(sourceFileStr);				
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				String eachLine, eachLineArray[];
				// read each line from result.txt file
				while ((eachLine = bufferedReader.readLine()) != null) {					
					eachLineArray = eachLine.split(";");
					if (testCaseName.equals(eachLineArray[0])) {						
						totalTestCaseCount++;
						subTotalTestCaseCount++;
						allLinesList.add(eachLine);					

						if (eachLineArray[6].equals("Pass")){
							subPassCount++;
							totalPassCount++;
						}
						else{
							subFailCount++;
							totalFailCount++;
						}
					}
				}//end while read line	
				subPassPercent= Math.round((float)(subPassCount*100)/subTotalTestCaseCount);
				subFailPercent=Math.round((float)(subFailCount*100)/subTotalTestCaseCount);	
				
				
				eachTestCaseMenuList.add(testCaseName);
				eachTestCaseMenuList.add(subTotalTestCaseCount);
				eachTestCaseMenuList.add(subPassCount);	
				eachTestCaseMenuList.add(subFailCount);	
				eachTestCaseMenuList.add(subPassPercent);	
				eachTestCaseMenuList.add(subFailPercent);	
			}//end for testcase
			System.out.println("eachTestCase MenuList" + eachTestCaseMenuList);
			System.out.println("totalTestCaseCount" + totalTestCaseCount);
			totalPassPercent= Math.round((float)(totalPassCount*100)/totalTestCaseCount);
			totalFailPercent=Math.round((float)(totalFailCount*100)/totalTestCaseCount);	
			
			
			//create the html content			
			StringBuilder htmlStringBuilder = new StringBuilder();
			
			// append html header and title
			//<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
			htmlStringBuilder.append(
					"<html><head> <meta charset=\"UTF-8\"><title>CitiGR Automation Report</title><style>p.padding { padding-top: 2cm;}</style></head>");
			// append body
			htmlStringBuilder.append("<body>");
			htmlStringBuilder.append("<p align=\"left\" style=\"color:red\"><h2><u>" + pageTitle + "</u></h2></p>");
			htmlStringBuilder.append("<p align=\"left\" class=\"padding\" style=\" text:red\"><b>Environment URL : <a href=\""+environmentUrl+"\">"+environmentUrl+"</a>");                                         
			htmlStringBuilder.append("<br><br>Start Run Time: "+startRunTime);                                         
			htmlStringBuilder.append("<br>End  Run Time &nbsp;: "+endRunTime+"</b></p>");   			
			
			/* append 1st table */
			htmlStringBuilder.append(
					"<table border=\"1\" text-align:center><col width=\"50\"><col width=\"200\"><col width=\"200\"><col width=\"100\"><col width=\"100\"><col width=\"100\"><col width=\"100\">");
			// append row - table header
			htmlStringBuilder.append(
					"<tr>"
					+ "<td align=\"center\" bgcolor=\"LightBlue\"><b>Si.No.</b></td>"
					+ "<td align=\"center\" bgcolor=\"LightBlue\"><b>TestCase Name</b></td>"
					+ "<td align=\"center\" bgcolor=\"LightBlue\"><b>Total no. of scripts executed</b></td>"
					+ "<td align=\"center\" bgcolor=\"green\"><b>Pass</b></td>"
					+ "<td align=\"center\" bgcolor=\"red\"><b>Fail</b></td>"
					+ "<td align=\"center\" bgcolor=\"green\"><b>Pass %</b></td>"
					+ "<td align=\"center\" bgcolor=\"red\"><b>Fail %</b></td>"
					+ "</tr>");
			
			int count=1;
			for (int i = 0; i < eachTestCaseMenuList.size(); ) {						
				htmlStringBuilder.append(""
						+ "<tr>"
						+ "<td align=\"center\" >"+count+"</td>"
						+ "<td align=\"center\">"+ eachTestCaseMenuList.get(i) + "</td>"
						+ "<td align=\"center\">"+ eachTestCaseMenuList.get(i+1) + "</td>"
						+ "<td align=\"center\" style=\" color:DarkGreen\">" + eachTestCaseMenuList.get(i+2)+ "</td>"
						+ "<td align=\"center\" style=\" color:red\">" + eachTestCaseMenuList.get(i+3) + "</td>"
						+ "<td align=\"center\" style=\" color:DarkGreen\">" + eachTestCaseMenuList.get(i+4)+ "</td>"
						+ "<td align=\"center\" style=\" color:red\">" + eachTestCaseMenuList.get(i+5) + "</td>"
						+ "</tr>");
				i+=6;	
				count++;
			}		
			
			htmlStringBuilder.append(
					"<tr>"
					+ "<td align=\"center\" bgcolor=\"LightBlue\"></td>"
					+ "<td align=\"center\" bgcolor=\"LightBlue\"><b>Total</b></td>"
					+ "<td align=\"center\" bgcolor=\"LightBlue\"><b>"+totalTestCaseCount+"</b></td>"
					+ "<td align=\"center\" bgcolor=\"green\"><b>"+totalPassCount+"</b></td>"
					+ "<td align=\"center\" bgcolor=\"red\"><b>"+totalFailCount+"</b></td>"
					+ "<td align=\"center\" bgcolor=\"green\"><b>"+totalPassPercent+" %</b></td>"
					+ "<td align=\"center\" bgcolor=\"red\"><b>"+totalFailPercent+" %</b></td>"
					+ "</tr>");
		
			
			
			
			
			/* append 2nd table */
			htmlStringBuilder.append("</table><p align=\"left\" class=\"padding\" style=\" color:Maroon\"><h3>Details :</h3></p>");
			
			htmlStringBuilder.append(
					"<table border=\"1\" text-align:center><col width=\"20\"><col width=\"200\"><col width=\"100\"><col width=\"100\"><col width=\"100\"><col width=\"50\"><col width=\"50\"><col width=\"50\"><col width=\"600\">");
			// append row - table header
			htmlStringBuilder.append(
					"<tr>"
					+ "<td align=\"center\" bgcolor=\"LightBlue\"><b>Si.No.</b></td>"
					+ "<td align=\"center\" bgcolor=\"LightBlue\"><b>Script Name</b></td>"
					+ "<td align=\"center\" bgcolor=\"LightBlue\"><b>Country</b></td>"
					+ "<td align=\"center\" bgcolor=\"LightBlue\"><b>TierName</b></td>"
					+ "<td align=\"center\" bgcolor=\"LightBlue\"><b>Language</b></td>"
					+ "<td align=\"center\" bgcolor=\"LightBlue\"><b>MemberID</b></td>"
					+ "<td align=\"center\" bgcolor=\"LightBlue\"><b>AccountNumber</b></td>"
					+ "<td align=\"center\" bgcolor=\"LightBlue\"><b>Status</b></td>"
					+ "<td align=\"center\" bgcolor=\"LightBlue\"><b>Description</b></td>"
					+ "</tr>");
			
				for (int i = 0; i < allLinesList.size(); i++) {
				String eachLine, eachLineArray[];
				eachLine = allLinesList.get(i);
				eachLineArray = eachLine.split(";");
							
				/*htmlStringBuilder.append(""
						+ "<tr>"
						+ "<td align=\"center\" bgcolor=\"Bisque\"><b>" + (i+1)+ "</b></td>"
						+ "<td align=\"center\" bgcolor=\"Bisque\"><b>" + eachLineArray[0]+ "</b></td>"
						+ "<td align=\"center\" bgcolor=\"Bisque\"><b>" + eachLineArray[1]+ "</b></td>"
						+ "<td align=\"center\" bgcolor=\"Bisque\"><b>" + eachLineArray[2]+ "</b></td>"
						+ "<td align=\"center\" bgcolor=\"Bisque\"><b>" + eachLineArray[3]+ "</b></td>"
						+ "<td align=\"center\" bgcolor=\"Bisque\"><b>" + eachLineArray[4]+ "</b></td>"
						+ "<td align=\"center\" bgcolor=\"Bisque\"><b>" + eachLineArray[5]+ "</b></td>"
						+ "<td align=\"center\" bgcolor=\"Bisque\"><b>" + eachLineArray[6]+ "</b></td>"
						+ "<td align=\"left\" bgcolor=\"Bisque\"><b>" + eachLineArray[7]+ "</b></td>"
						+ "</tr>");*/
				
				htmlStringBuilder.append(""
						+ "<tr>"
						+ "<td align=\"center\" bgcolor=\"Bisque\"><b>" + (i+1)+ "</b></td>"
						+ "<td align=\"center\" bgcolor=\"Bisque\"><b>" + eachLineArray[0]+ "</b></td>"
						+ "<td align=\"center\" bgcolor=\"Bisque\"><b>" + eachLineArray[1]+ "</b></td>"
						+ "<td align=\"center\" bgcolor=\"Bisque\"><b>" + eachLineArray[2]+ "</b></td>"
						+ "<td align=\"center\" bgcolor=\"Bisque\"><b>" + eachLineArray[3]+ "</b></td>"
						+ "<td align=\"center\" bgcolor=\"Bisque\"><b>" + eachLineArray[4]+ "</b></td>"
						+ "<td align=\"center\" bgcolor=\"Bisque\"><b>" + eachLineArray[5]+ "</b></td>"
						+ "<td align=\"center\" bgcolor=\"Bisque\"><b>" + eachLineArray[6]+ "</b></td>"
						+ "<td align=\"left\" bgcolor=\"Bisque\"><b>");
				String  descriptionArray[]=eachLineArray[7].toString().split(Constants.HASH);
				
				for (int j = 0; j < descriptionArray.length; j++) {
					htmlStringBuilder.append(""							
						+(j+1)+") "+ descriptionArray[j] +"<br>"); 
					
				}
			}
			htmlStringBuilder.append("</b></td></tr></body></html>");	
			
			writeToFile(htmlStringBuilder.toString(), reportHTMLFile);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}

	}

	public static void writeToFile(String fileContent, File reportHTMLFile) throws IOException {
		//File file = new File(hTMLReportFilename);		
		// write to file with OutputStreamWriter
		//OutputStream outputStream = new FileOutputStream(file.getAbsoluteFile());
		OutputStream outputStream = new FileOutputStream(reportHTMLFile.getAbsoluteFile());
		Writer writer = new OutputStreamWriter(outputStream);
		writer.write(fileContent);
		writer.close();
	}

}
