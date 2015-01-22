Instructions to run jar in command line.
---------------------------------------
Go to the CrawlerApp folder 
Prerequisites
1. Make sure D:\CrawlerApp\LogFiles folder exists. If not create it.
2.set Classpath in command prompt  and run below command
set CLASSPATH=D:\CrawlerApp\Lib
3.go to  D:\CrawlerApp\Lib  folder in command prompt and run below command
java -jar crawler.java D:\CrawlerMails


obsolete
-------------
D:\proj>java -classpath .;crawler.jar;jsoup-1.8.1.jar;commons-io-24.jar;log4j-1.2.17.jar com.pramati.training.DownloadMailsMain


