package com.biit.webforms.gui.common.utils;

import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.UserSessionHandler;
import com.biit.webforms.gui.common.language.ILanguageCode;
import com.biit.webforms.gui.common.language.ServerTranslate;
import com.biit.webforms.logger.WebformsLogger;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

public class MessageManager {
	private final static Integer MESSAGE_DURATION_MILISECONDS = 4000;

	private static void showWarning(String caption, String description) {
		showMessage(caption, description, Notification.Type.WARNING_MESSAGE);
	}

	public static void showWarning(ILanguageCode caption, ILanguageCode description) {
		showWarning(ServerTranslate.translate(caption), ServerTranslate.translate(description));
	}

	public static void showWarning(ILanguageCode caption) {
		showWarning(ServerTranslate.translate(caption), "");
	}

	public static void showError(String caption, String description) {
		showMessage(caption, description, Notification.Type.ERROR_MESSAGE, Position.TOP_CENTER);
	}

	public static void showError(ILanguageCode caption, ILanguageCode description) {
		showError(ServerTranslate.translate(caption), ServerTranslate.translate(description));
	}

	public static void showError(String caption) {
		showMessage(caption, "", Notification.Type.ERROR_MESSAGE, Position.TOP_CENTER);
	}

	public static void showError(ILanguageCode caption) {
		showError(ServerTranslate.translate(caption));
	}

	private static void showInfo(String caption, String description) {
		showMessage(caption, description, Notification.Type.HUMANIZED_MESSAGE);
	}

	public static void showInfo(ILanguageCode caption, ILanguageCode description) {
		showInfo(ServerTranslate.translate(caption), ServerTranslate.translate(description));
	}

	private static void showInfo(String caption) {
		showMessage(caption, "", Notification.Type.HUMANIZED_MESSAGE);
	}

	public static void showInfo(ILanguageCode caption) {
		showInfo(ServerTranslate.translate(caption));
	}

	private static void showMessage(String caption, String description, Notification.Type type) {
		showMessage(caption, description, type, Position.TOP_CENTER);
	}

	private static void showMessage(String caption, String description, Notification.Type type, Position position) {
		// Log it.
		try {
			String user;
			if (UserSessionHandler.getUser() != null) {
				user = UserSessionHandler.getUser().getEmailAddress();
			} else {
				user = "none";
			}
			WebformsLogger.info(MessageManager.class.getName(), "Message '" + caption
					+ (description != null ? " | " : "") + description + "' (" + type + ") displayed to user '" + user
					+ "'.");
		} catch (Exception e) {
			WebformsLogger.errorMessage(MessageManager.class.getName(), e);
		}

		if (UI.getCurrent() != null) {
			Notification notif = new Notification(caption, description, type);

			// Set the position.
			notif.setPosition(position);

			// Let it stay there until the user clicks it if is error message
			if (type.equals(Notification.Type.ERROR_MESSAGE)) {
				notif.setDelayMsec(-1);
			} else {
				notif.setDelayMsec(MESSAGE_DURATION_MILISECONDS);
			}

			// Show it in the main window.
			notif.show(ApplicationUi.getCurrent().getPage());
		}
	}
}
