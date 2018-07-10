package services;

import java.util.Date;

public interface ILog {

	public void writeErrorMessage(Exception e, boolean alsoWriteToConsole, String... additionalMessages);

	public void writeWarningMessage(String message, boolean alsoWriteToConsole, String... additionalMessages);

	public String getLog(Date date);

}
