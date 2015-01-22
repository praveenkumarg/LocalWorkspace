
package com.pramati.training;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author praveeng
 *
 */
public class Crawler {
	/**
	 * @param args takes Mailstrorage folder path as argument
	 * @throws IOException 
	 * Main sequence of action for crawler project
	 */
	private static final String baseUrl = "http://mail-archives.apache.org/mod_mbox/maven-users/";
	private static final String year = "2014";
	private static final Logger logger = Logger.getLogger(Crawler.class);
	
	public static void main(String[] args) throws IOException, URISyntaxException  {
		int pageCounter = 0;
		boolean isValidPath = false;
		String fullMonUrl = "";
		String pathMailstore = "";
		ArrayList<String> mailList = null;
		
		//Initialize logger
		InputStream is = Crawler.class.getResourceAsStream("/log4j.properties");
		PropertyConfigurator.configure(is);
		is.close();
		
		//validate mail storage  folder path
		isValidPath = validateInputPath(args);
		
		if(!isValidPath){
			pathMailstore = takePathFromUser("mail storage");
		} else {
			pathMailstore = args[0];
		}
	
		logger.info("Mails will be stored at path " +pathMailstore);
		logger.info("Log will be stored at path D:\\CrawlerApp\\LogFiles");
		
		//Logic to get mails starts
		CrawlerUtil dmu = new CrawlerUtil();
		// getting the monthly url list 
		LinkedHashMap<String,Integer> monthlyMails = dmu.getMonthUrlList(baseUrl,year);
		logger.info("Urls all months are captured");
	
		//iterating over each month and storing them to to a folder
		for (String mon : monthlyMails.keySet()) {	
			fullMonUrl = baseUrl + mon;
			pageCounter = 0;
			mailList = new ArrayList<String>();
			//get the indiviual mail url list within a month
			mailList = dmu.getMailUrlList(fullMonUrl, monthlyMails.get(mon), mailList, pageCounter);
			logger.info("Urls of mails within  month ("+mon.substring(0, 6)+")are captured");
			//iterate over the individual mail url in a month and store them in file
			logger.info("Storage of mails for month "+ mon.substring(0, 6)+" started.");
			for (String contentUrl : mailList) {
				dmu.storeMail(contentUrl, mon, pathMailstore);
			}
			logger.info("Storage of mails for month "+mon.substring(0, 6)+" completed.");
		}
	}
	
	 private static String takePathFromUser(String pathFor) throws IOException {
		 String folderPath = "";
		 logger.info("Please enter folder path for "+pathFor);
		 BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		 folderPath = bufferRead.readLine();
		 if (!folderPath.isEmpty()){
			 return folderPath;
		 } else {
			 folderPath = takePathFromUser(pathFor);
		 }
		return folderPath;
			 
	}

	public static boolean validateInputPath(String[] path){
		 boolean isValid = false;
		 if(path.length!=0 && !path[0].isEmpty()){
			 isValid = true;
		 } 
		 return isValid;
	 }
	
	
	
}


