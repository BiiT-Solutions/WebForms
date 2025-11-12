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

import com.biit.usermanager.entity.IUser;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

import java.util.HashMap;

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
