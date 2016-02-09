package com.biit.webforms.gui;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import com.biit.webforms.logger.WebformsLogger;
import com.vaadin.ui.UI;

public class WebformsUiLogger {

	public static void info(String className, String message) {
		WebformsLogger.info(className, formatMessage(message));
	}

	/**
	 * Shows not critical errors. I.e. Email address not found, permissions not
	 * allowed for this user, ...
	 * 
	 * @param message
	 */
	public static void warning(String className, String message) {
		WebformsLogger.warning(className, formatMessage(message));
	}

	/**
	 * For following the trace of the execution. I.e. Knowing if the application
	 * access to a method, opening database connection, etc.
	 */
	public static void debug(String className, String message) {
		WebformsLogger.debug(className, formatMessage(message));
	}

	/**
	 * To log any not expected error that can cause application malfuncionality.
	 * 
	 * @param message
	 */
	public static void severe(String className, String message) {
		WebformsLogger.severe(className, formatMessage(message));
	}

	/**
	 * To log java exceptions and log also the stack trace. If enabled, also can
	 * send an email to the administrator to alert of the error.
	 * 
	 * @param className
	 * @param throwable
	 */
	public static void errorMessage(String className, Throwable throwable) {
		String message = getStackTrace(throwable);
		WebformsLogger.errorMessage(className, formatMessage(message));
	}

	/**
	 * Log a error message. If enabled, also can send an email to the
	 * administrator to alert of the error.
	 * 
	 * @param className
	 * @param throwable
	 */
	public static void errorMessage(String className, String message) {
		WebformsLogger.errorMessage(className, formatMessage(message));
	}

	private static String formatMessage(String message) {
		String uiid = null;
		String userMail = null;

		try {
			uiid = UI.getCurrent().getUIId()+"";
		} catch (Exception e) {
			uiid = "[NO UI]";
		}

		try {
			userMail = UserSessionHandler.getUser().getEmailAddress();
		} catch (Exception e) {
			userMail = "[NO USER]";
		}
		
		return "UI '"+uiid+"', user '"+userMail+"': "+message;
	}

	public static String getStackTrace(Throwable throwable) {
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		throwable.printStackTrace(printWriter);
		return writer.toString();
	}
}
