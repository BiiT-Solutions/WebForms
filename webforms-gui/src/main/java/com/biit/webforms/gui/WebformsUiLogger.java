package com.biit.webforms.gui;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (GUI)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.webforms.logger.WebformsLogger;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Timestamp;

public class WebformsUiLogger {

	private static final String UNKNOWN = "[UNK]";

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
		WebformsLogger.errorMessage(className, throwable);
	}

	/**
	 * Log a error message. If enabled, also can send an email to the
	 * administrator to alert of the error.
	 *
	 * @param className
	 */
	public static void errorMessage(String className, String message) {
		WebformsLogger.errorMessage(className, formatMessage(message));
	}

	private static String formatMessage(String message) {
		String sessionId = UNKNOWN;
		String sessionStart = UNKNOWN;
		String numberOfUi = UNKNOWN;
		VaadinSession currentSession = VaadinSession.getCurrent();
		if (currentSession != null && currentSession.getSession() != null) {
			sessionId = currentSession.getSession().getId();
			try {
				sessionStart = new Timestamp(currentSession.getSession().getCreationTime()).toString();
			} catch (IllegalStateException e) {
				// Do nothing
			}
			numberOfUi = currentSession.getUIs().size() + "";
		}
		String uiid = null;
		String userMail = null;

		try {
			uiid = UI.getCurrent().getUIId() + "";
		} catch (Exception e) {
			uiid = UNKNOWN;
		}

		try {
			userMail = UserSession.getUser().getEmailAddress();
		} catch (Exception e) {
			userMail = UNKNOWN;
		}

		return "Session '" + sessionId + "' active since '" + sessionStart + "' TotalUi '" + numberOfUi + "' UI '" + uiid + "', user '" + userMail + "': "
				+ message;
	}

	public static String getStackTrace(Throwable throwable) {
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		throwable.printStackTrace(printWriter);
		return writer.toString();
	}
}
