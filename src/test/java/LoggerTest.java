import services.impl.LoggerImpl;

public class LoggerTest {

	public static void main(String[] args) {
		LoggerImpl log = new LoggerImpl();
		
//		log.logFightData(2);
		log.showLogs(1, 2);

	}

}
