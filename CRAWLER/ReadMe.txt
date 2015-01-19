Instructions to run jar in command line.
---------------------------------------
Go to base folder where jar is kept.
And run below command , for eg. if D:\proj is the folder where jar is kept then use command below.
Make sure to include jsoup-1.8.1.jar, commons-io-24.jar files in the folder.


D:\proj>java -classpath .;crawler.jar;jsoup-1.8.1.jar;commons-io-24.jar com.pramati.training.DownloadMailsMain
