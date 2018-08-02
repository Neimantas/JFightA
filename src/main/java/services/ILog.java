package services;

import java.util.Date;

public interface ILog {

	void writeErrorMessage(Exception e, String... additionalMessages);
	void writeWarningMessage(String message, String... additionalMessages);
	String getLog(Date date);

}
