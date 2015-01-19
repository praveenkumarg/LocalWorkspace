
package com.pramati.training;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author praveeng
 *
 */
public class DownloadMailsMain  {
	/**
	 * @param args
	 * @throws IOException 
	 * Main sequence of action for crawler project
	 */
	public static void main(String[] args) throws IOException  {
		String baseurl = "http://mail-archives.apache.org/mod_mbox/maven-users/";
		String year = "2014";
		String fullmonurl = "";
		ArrayList<String> maillist = null;
		int counter = 0;
		DownloadMailsUtils dmu = new DownloadMailsUtils();
		LinkedHashMap<String,Integer> monthlymails = dmu.getMonthUrlList(baseurl,year);
		// till logger is committed using console to show message
		System.out.println("Mails will be stored at D:\\crawler mails. Will make it configurable soon");
		System.out.println(" Month urls captured");
		for (String mon : monthlymails.keySet()) {	
			fullmonurl = baseurl + mon;
			counter = 0;
			maillist = new ArrayList<String>();
			maillist = dmu.getMailUrlList(fullmonurl, monthlymails.get(mon), maillist, counter);
			System.out.println(" Monthly mails urls captured");

			for (String contenturl : maillist) {
				dmu.storeMail(contenturl, mon);
			}
			System.out.println("Mails stored at D:\\crawler mails. Will make it configurable soon");
		}
	}
}


