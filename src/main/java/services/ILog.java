package services;

import java.util.Date;

public interface ILog {

	void writeErrorMessage(Exception e, boolean alsoWriteToConsole, String... additionalMessages);
	void writeWarningMessage(String message, boolean alsoWriteToConsole, String... additionalMessages);
	String getLog(Date date);

}
