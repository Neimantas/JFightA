package services.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import models.constant.Settings;
import services.ILog;

public class LogImpl implements ILog {
	private static ILog _log = new LogImpl();

	private SimpleDateFormat _simpleDateFormat;
	private SimpleDateFormat _simpleDateAndTimeFormat;
	private static Boolean _allowWriteToConsoleGlobal = Settings.WRITE_LOGS_TO_CONSOLE;
	private static String _logDirectory = Settings.LOG_DIRECTORY;

	private LogImpl() {
		_simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		_simpleDateAndTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public void writeErrorMessage(Exception e, boolean allowWriteToConsole, String... additionalMessages) {
		if (_allowWriteToConsoleGlobal && allowWriteToConsole) {
			System.out.println(e.toString());
		}
		try (FileWriter fw = new FileWriter(createLogLocation(), true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			out.println(System.getProperty("line.separator")
					+ "------------------------------------------------------------------------------------------------------------------------------------------------------");
			out.println("[ERROR] " + _simpleDateAndTimeFormat.format(new Date()) + " " + e.toString()
					+ System.getProperty("line.separator"));
			out.print("Details:");
			e.printStackTrace(out);
			if (additionalMessages.length != 0) {
				out.println(System.getProperty("line.separator") + createAdditionalMessage(additionalMessages));
			}
			out.println(
					"------------------------------------------------------------------------------------------------------------------------------------------------------");
		} catch (IOException io) {
			System.out.println(io.toString());
		}
	}

	@Override
	public void writeWarningMessage(String message, boolean allowWriteToConsole,
			String... additionalMessages) {
		if (_allowWriteToConsoleGlobal && allowWriteToConsole) {
			System.out.println("[WARNING] " + message);
		}
		try (FileWriter fw = new FileWriter(createLogLocation(), true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			out.println(System.getProperty("line.separator")
					+ "------------------------------------------------------------------------------------------------------------------------------------------------------");
			out.println("[WARNING] " + _simpleDateAndTimeFormat.format(new Date()) + " " + message);
			if (additionalMessages.length != 0) {
				out.println(createAdditionalMessage(additionalMessages));
			}
			out.println(
					"------------------------------------------------------------------------------------------------------------------------------------------------------");
		} catch (IOException io) {
			System.out.println(io.toString());
		}
	}

	@Override
	public String getLog(Date date) {
		File file = new File(getLogLocationByDate(date));
		if (file.exists()) {

			try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
				String returnString = "";
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					returnString += line + "\n";
				}
				if (returnString.equals("")) {
					return "Log file is empty.";
				}
				return returnString;
			} catch (IOException e) {
				return e.toString();
			}

		} else {
			return "There are no logs at a current date.";
		}
	}

	public static ILog getInstance() {
		return _log;
	}

	private String createLogLocation() {
		new File(_logDirectory).mkdirs();
		return _logDirectory + _simpleDateFormat.format(new Date()) + ".log";
	}

	private String getLogLocationByDate(Date date) {
		return _logDirectory + _simpleDateFormat.format(date) + ".log";
	}

	private String createAdditionalMessage(String... additionalMessages) {
		String additionalMessage = "Additional message: ";
		for (int i = 0; i < additionalMessages.length; i++) {
			additionalMessage += additionalMessages[i] + ", ";
		}
		return additionalMessage.substring(0, additionalMessage.length() - 2);
	}

}
