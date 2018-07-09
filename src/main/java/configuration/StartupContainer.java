package configuration;

import javax.servlet.ServletContextEvent;

import eu.lestard.easydi.EasyDI;

public class StartupContainer implements javax.servlet.ServletContextListener {

	public static EasyDI easyDI;
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		easyDI = new EasyDI();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
