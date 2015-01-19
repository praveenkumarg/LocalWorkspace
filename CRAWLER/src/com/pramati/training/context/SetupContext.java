/**
 * 
 */
package com.pramati.training.context;

/**
 * @author praveeng
 *
 */
public class SetupContext {

	private static SetupContext scinst = null;
	private SetupContext() {
	}
	
	public static SetupContext getInstance(){
		if (scinst == null){
			scinst = new SetupContext();
		}
		return scinst;
		
	}
	/**
	 * Initializing the application Context
	 *  read from configuration property file.
	 *  load all the config data like base url of the mail archive where
	 *  to store mails,deleting previous mail store file based on config,etc..	
	 */
	public void init(){
		// read the appconfig.properties file and load them into config object
		// make config a signle ton as this is same across the application
			
	}

}
