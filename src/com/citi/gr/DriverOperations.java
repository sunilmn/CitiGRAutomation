package com.citi.gr;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;

public class DriverOperations {

	String browserName="";
	public DriverOperations() {
		this.browserName=CitiMain.browserName;
	}

	WebDriver webDriver;
	String Value; // to hold the value of TestCase map
	static List<Object> columnValues = null;
	public static java.util.List<WebElement> webElementsList;
	static Map<String, List<Object>> columnValueMap;

	//public WebDriver getWebDriver(String browserName) {
		public WebDriver getWebDriver() {
		try {

			if (browserName.equals("Chrome")) {
				System.setProperty("webdriver.chrome.driver", "lib/chromedriver.exe");
				webDriver = new ChromeDriver();
			} 
			else if (browserName.equals("InternetExplorer")) {
				System.setProperty("webdriver.ie.driver", "IEDriverServer.exe");
				DesiredCapabilities capability = DesiredCapabilities.internetExplorer();
				capability.setCapability("ignoreZoomSetting", true);
				webDriver = new InternetExplorerDriver(capability);
			}
			else if (browserName.equals("Firefox")){
				webDriver = new FirefoxDriver();
			}
			else if (browserName.equals("Safari")){
				webDriver = new SafariDriver();
			}
			
			webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			webDriver.manage().deleteAllCookies();
			// wait 5 seconds to clear cookies.
			Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * ChromeOptions options = new ChromeOptions();
		 * options.addArguments("--incognito"); DesiredCapabilities capabilities
		 * = DesiredCapabilities.chrome();
		 * capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		 * webDriver =new ChromeDriver(capabilities);
		 */
		return webDriver;
	}

	/*public void clearBrowserCookies(WebDriver webDriver) {
		webDriver.manage().deleteAllCookies();
		try {
			// wait 5 seconds to clear cookies.
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}*/

	
}
