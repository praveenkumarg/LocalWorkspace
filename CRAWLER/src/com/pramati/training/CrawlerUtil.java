package com.pramati.training;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class CrawlerUtil {

	private int fileCounter = 0;
	/**
	 * 
	 * @param contenturl
	 * @param mon
	 * @throws IOException
	 */
	Logger logger = Logger.getLogger(Crawler.class);
	int numAttempts = 5;

	public void storeMail(String contentUrl, String mon, String pathMailstore) throws IOException {
		String mailFileName = "";
		Document doc = null;
		doc = getHTMLDocForUrl(contentUrl); 
		fileCounter++;
		mailFileName = pathMailstore +"\\"+mon.substring(0, 6)+"_"+fileCounter+".html";
		
		try {
			File mailFile = new File (mailFileName);
			FileUtils.writeStringToFile(mailFile, doc.toString()) ;
		} catch (Exception e) {
			logger.error(mailFileName +" file storage encountered problem.");
			e.printStackTrace();
		}
		logger.info(" mail file stored for yearmon_ "+mon.substring(0, 6)+"_"+fileCounter);
	}

	public LinkedHashMap<String,Integer> getMonthUrlList(String url, String year) throws IOException{
		String hrefLink ="";
		String mailCount = "";
		Elements siblings ;
		Elements mailLinks  = getLinkElements(url);
		LinkedHashMap<String,Integer> monthlyMails = new LinkedHashMap<>();
		for(Element link: mailLinks){
			hrefLink = link.attr("href");
			if(hrefLink.startsWith(year) && hrefLink.endsWith("thread")){
				siblings = link.parent().parent().siblingElements();
				for(Element sibling: siblings){
					if (sibling.className().equals("msgcount")){
						List<Node> children = sibling.childNodes();
						mailCount = children.get(0).toString();
					}
				}
				monthlyMails.put(hrefLink, new Integer(mailCount));
			}
		}
		return monthlyMails;
	}
	
	public  ArrayList<String> getMailUrlList(String url, Integer mailCount, ArrayList<String> mailList, int pageCounter) throws IOException{
		String hrefLink ="";
		Elements maillinks = getLinkElements(url); 
		for(Element link: maillinks){
			hrefLink = link.attr("href");
			if (hrefLink.startsWith("%3")){
				mailList.add(url.replaceFirst("thread","")+hrefLink);
			}
		}
		if(mailList.size() < mailCount){
			if (url.indexOf(63) >0){
				url = url.replaceFirst("\\"+url.substring(url.indexOf(63)),"");
			}
			pageCounter++;
			url = url +"?" + pageCounter;
			getMailUrlList(url, mailCount, mailList, pageCounter);
		} else if(mailList.size() > mailCount){
			logger.error("Mails read greater than the total count.");
		}
		
		return mailList;
	}
	
	public Elements getLinkElements(String url) throws IOException{
		Document doc = getHTMLDocForUrl(url);
		Elements mailLinks = doc.select("a[href]");
		return mailLinks; 
	}
	
	public Document getHTMLDocForUrl(String url) throws IOException{
		Document doc = null;
		try {
			doc = Jsoup.connect(url).timeout(0).get();
		} catch (IOException e) {	
			logger.error("Internet connection failed!! Trying one more time after 5 secs..");
			logger.error("After 5 unsuccessful attempts programme shuts down automatically.");
			numAttempts--;
			if(numAttempts>0){
				try {
					Thread.sleep(5000);
					getHTMLDocForUrl(url);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		return doc; 
	}	
		
}
