package services;

import java.util.Date;

public interface ILog {

	public void writeErrorMessage(Exception e, String... additionalMessages);

	public void writeWarningMessage(String message, String... additionalMessages);

	public String getLog(Date date);

}
