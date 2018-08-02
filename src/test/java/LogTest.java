import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import services.ILog;
import services.impl.LogImpl;

public class LogTest {
	
	public static void main(String[] args) throws ParseException {
	
	ILog log = LogImpl.getInstance();

	log.writeWarningMessage("Testing warning message", "test 1");
	
	try {
		throw new Exception();
	} catch (Exception e) {
		log.writeErrorMessage(e, "test 2");
	}
	
	log.writeWarningMessage("Testing warning message", "test 3");
	
	System.out.println("\nToday log data:\n" + log.getLog(new Date()));

    Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2018-07-18");
	System.out.println("\nWrong date log output:\n" + log.getLog(date));
	
	}
	
}
