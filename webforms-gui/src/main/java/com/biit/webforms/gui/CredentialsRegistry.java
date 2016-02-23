package com.biit.webforms.gui;

import java.util.HashMap;

import com.biit.usermanager.entity.IUser;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

public class CredentialsRegistry {

	private static final String UNKNOWN = "[UNK]";
	private static HashMap<IUser<Long>, VaadinSession> sessionByCredential = new HashMap<>();
	private static HashMap<VaadinSession, IUser<Long>> credentialBySession = new HashMap<>();

	public static synchronized void registerCredential(IUser<Long> credential, VaadinSession session) {
		WebformsUiLogger.debug(CredentialsRegistry.class.getName(), "User '" + credential.getEmailAddress()
				+ "' tries to register in session '" + getSessionId(session) + "'");

		if (sessionByCredential.containsKey(credential)) {
			VaadinSession sessionToClose = sessionByCredential.get(credential);
			WebformsUiLogger.debug(CredentialsRegistry.class.getName(), "User '" + credential.getEmailAddress()
					+ "' was already registered in session '" + getSessionId(session) + "' removing from registry. ");
			removeCredential(credential);
			closeSession(sessionToClose);
		}
		addCredential(credential, session);
	}

	private static String getSessionId(VaadinSession session) {
		if (session.getSession() != null) {
			return session.getSession().getId();
		}
		return UNKNOWN;
	}

	private static void closeSession(VaadinSession sessionToClose) {
		// Check that session has not been destroyed already.
		if (sessionToClose.getSession() == null) {
			return;
		}

		for (UI ui : sessionToClose.getUIs()) {
			ui.access(new Runnable() {
				
				@Override
				public void run() {
					UI.getCurrent().getPage().setLocation("./VAADIN/logout.html");
				}
			});
		}
		sessionToClose.close();
	}

	private static void addCredential(IUser<Long> credential, VaadinSession session) {
		sessionByCredential.put(credential, session);
		credentialBySession.put(session, credential);
	}

	private static void removeCredential(IUser<Long> credential) {
		credentialBySession.remove(sessionByCredential.get(credential));
		sessionByCredential.remove(credential);
	}

	public static void removeSession(VaadinSession session) {
		sessionByCredential.remove(credentialBySession.get(session));
		credentialBySession.remove(session);
	}

}
