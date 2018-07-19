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
	public synchronized void writeErrorMessage(Exception e, boolean allowWriteToConsole, String... additionalMessages) {
		if (_allowWriteToConsoleGlobal && allowWriteToConsole) {
			System.out.println(e.toString());
		}
		createDirIfNotExists();
		String fileName = _simpleDateFormat.format(new Date()) + ".log";
		try (FileWriter fw = new FileWriter(_logDirectory + fileName, true);
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
	public synchronized void writeWarningMessage(String message, boolean allowWriteToConsole,
			String... additionalMessages) {
		if (_allowWriteToConsoleGlobal && allowWriteToConsole) {
			System.out.println("[WARNING] " + message);
		}
		createDirIfNotExists();
		String fileName = _simpleDateFormat.format(new Date()) + ".log";
		try (FileWriter fw = new FileWriter(_logDirectory + fileName, true);
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
		String fileName = _simpleDateFormat.format(date) + ".log";
		File file = new File(_logDirectory + fileName);
		if (file.exists()) {
			String returnString = "";
			try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					returnString += line + "\n";
				}
			} catch (IOException e) {
				return e.toString();
			}
			if (returnString.equals("")) {
				return "Log file is empty.";
			}
			return returnString;
		} else {
			return "There are no logs at a current date.";
		}
	}

	public static ILog getInstance() {
		return _log;
	}

	private void createDirIfNotExists() {
		new File(_logDirectory).mkdirs();
	}

	private String createAdditionalMessage(String... additionalMessages) {
		String additionalMessage = "Additional message: ";
		for (int i = 0; i < additionalMessages.length; i++) {
			additionalMessage += additionalMessages[i] + ", ";
		}
		return additionalMessage.substring(0, additionalMessage.length() - 2);
	}

}
