/**
 * Jul 13, 2017
 * @author prajendra
 *
 */
package com.citi.fileActivity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.citi.gr.CitiMain;

public class ImageFilesOperations {

	
	/*
	 * This function deletes all the image files in screenshot folder
	 */
	public void clearScreenshots(String screenShotFolderName) {

		File file = new File(screenShotFolderName);
		String[] fileArray;
		if (file.isDirectory()) {
			fileArray = file.list();
			for (int i = 0; i < fileArray.length; i++) {
				File eachFile = new File(file, fileArray[i]);
				eachFile.delete();
			}
		}
	}

	/*
	 * This function  creates a folder with today's date(14SEP2017)
	 * as folder name inside the 'screenShotBackupFolder' .
	 *  Then it copies all images in screenshot folder
	 * to 'todayDate' folder
	 * 
	 */
	public void imageCopy(String screenShotFolderName) {
		
		File screenshotsFolder = new File(screenShotFolderName);

		DateFormat folderDateFormat = new SimpleDateFormat("ddMMMyyyy");
		Date todayDate = new Date();
		String todayDateDirName = folderDateFormat.format(todayDate);

		String folderPath=CitiMain.screenShotBackupFolderName+"/"+todayDateDirName;
		File todayDateDir = new File(folderPath);
		if (!todayDateDir.exists())
			todayDateDir.mkdir();

		File[] filesArray = screenshotsFolder.listFiles();
		for (int i = 0; i < filesArray.length; i++) {
			String fileName = filesArray[i].getName();
			String sourcePath = screenShotFolderName + "/" + fileName;
			String destPath = folderPath + "/" + fileName;

			Path source = Paths.get(sourcePath);
			Path destination = Paths.get(destPath);
			try {
				Files.copy(source, destination);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
