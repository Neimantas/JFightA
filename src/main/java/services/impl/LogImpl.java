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

import services.ILog;

public class LogImpl implements ILog {

	private SimpleDateFormat simpleDateFormat;
	private SimpleDateFormat simpleDateAndTimeFormat;
	private static ILog log;

	private LogImpl() {
		simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		simpleDateAndTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		log = this;
	}

	@Override
	public synchronized void writeErrorMessage(Exception e, boolean alsoWriteToConsole, String... additionalMessages) {
		if (alsoWriteToConsole) {
			System.out.println(e.toString());
		}
		String fileName = "log\\" + simpleDateFormat.format(new Date()) + ".log";
		try (FileWriter fw = new FileWriter(fileName, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			out.println(System.getProperty("line.separator")
					+ "------------------------------------------------------------------------------------------------------------------------------------------------------");
			out.println("[ERROR] " + simpleDateAndTimeFormat.format(new Date()) + " " + e.toString()
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
	public synchronized void writeWarningMessage(String message, boolean alsoWriteToConsole,
			String... additionalMessages) {
		if (alsoWriteToConsole) {
			System.out.println("[WARNING] " + message);
		}
		String fileName = "log\\" + simpleDateFormat.format(new Date()) + ".log";
		try (FileWriter fw = new FileWriter(fileName, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			out.println(System.getProperty("line.separator")
					+ "------------------------------------------------------------------------------------------------------------------------------------------------------");
			out.println("[WARNING] " + simpleDateAndTimeFormat.format(new Date()) + " " + message);
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
		String fileName = "log\\" + simpleDateFormat.format(date) + ".log";
		File file = new File(fileName);
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

	private String createAdditionalMessage(String... additionalMessages) {
		String additionalMessage = "Additional message: ";
		for (int i = 0; i < additionalMessages.length; i++) {
			additionalMessage += additionalMessages[i] + ", ";
		}
		return additionalMessage.substring(0, additionalMessage.length() - 2);
	}

	public static ILog getInstance() {
		if (log == null) {
			log = new LogImpl();
		}
		return log;
	}

}
