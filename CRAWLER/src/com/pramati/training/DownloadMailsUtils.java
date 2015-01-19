package com.pramati.training;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class DownloadMailsUtils {

	private int filecntpermonth = 0;
	/**
	 * 
	 * @param contenturl
	 * @param mon
	 * @throws IOException
	 */
	public void storeMail(String contenturl, String mon) throws IOException {
		Document doc = getHTMLDocForUrl(contenturl); 
		filecntpermonth++;
		File path = new File ("D:\\CrawlerMails\\"+mon.substring(0, 6)+"_"+filecntpermonth+".html");
		FileUtils.writeStringToFile(path, doc.toString()) ;
	}

	public LinkedHashMap<String,Integer> getMonthUrlList(String url, String year) throws IOException{
		String hreflink ="";
		String mailcount = "";
		Elements siblings ;
		Elements maillinks  = getLinkElements(url);
		LinkedHashMap<String,Integer> monthlymails = new LinkedHashMap<>();
		for(Element link: maillinks){
			hreflink = link.attr("href");
			if(hreflink.startsWith(year) && hreflink.endsWith("thread")){
				siblings = link.parent().parent().siblingElements();
				for(Element sibling: siblings){
					if (sibling.className().equals("msgcount")){
						List<Node> children = sibling.childNodes();
						for (Node node : children) {
							mailcount = node.toString();
						}
					}
				}
				monthlymails.put(hreflink, new Integer(mailcount));
			}
		}
		return monthlymails;
	}
	
	public  ArrayList<String> getMailUrlList(String url, Integer mailcount, ArrayList<String> maillist, int counter) throws IOException{
		String hreflink ="";
		Elements maillinks = getLinkElements(url); 
		for(Element link: maillinks){
			hreflink = link.attr("href");
			if (hreflink.startsWith("%3")){
				maillist.add(url.replaceFirst("thread","")+hreflink);
			}
		}
		if(maillist.size() != mailcount){
			if (url.indexOf(63) >0){
				url = url.replaceFirst("\\"+url.substring(url.indexOf(63)),"");
			}
			counter++;
			url = url +"?" + counter;
			getMailUrlList(url, mailcount, maillist, counter);
		}
		return maillist;
	}
	
	public Elements getLinkElements(String url) throws IOException{
		Document doc = getHTMLDocForUrl(url);
		Elements maillinks = doc.select("a[href]");
		return maillinks; 
	}
	
	public Document getHTMLDocForUrl(String url) throws IOException{
		Document doc = null;
		try {
			doc = Jsoup.connect(url).timeout(0).get();
		} catch (IOException e) {	
			System.out.println("Internet connection failed!! Trying one more time after 3 secs..Its an infinite loop. Will change this pattern in next commits..");
			try {
				Thread.sleep(3000);
				getHTMLDocForUrl(url);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		return doc; 
	}
	
	
	
		
}
