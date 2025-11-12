package com.biit.webforms.gui.common.language;

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

import com.biit.webforms.gui.UserSession;
import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinServlet;
import org.springframework.util.StringUtils;

import java.util.Locale;

public class ServerTranslate {

	private static SpringContextHelper helper = null;

	private static void initialize() {
		if (helper == null) {
			helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		}
	}

	private static String translationException(String code, Object[] args) {
		initialize();
		String translation = helper.getContext().getMessage(code, args, getLocale());
		return translation;
	}

	private static Locale getLocale() {
		if (UserSession.getUser() != null) {
			return StringUtils.parseLocaleString(UserSession.getUser().getLanguageId());
		} else {
			if (Page.getCurrent() != null) {
				return Page.getCurrent().getWebBrowser().getLocale();
			} else {
				return null;
			}
		}
	}

	public static String translate(ILanguageCode code) {
		if (code == null) {
			return null;
		}
		return translate(code.getCode(), null);
	}

	public static String translate(String code) {
		if (code == null) {
			return null;
		}
		return translate(code.trim(), null);
	}

	public static String translate(ILanguageCode code, Object[] args) {
		return translate(code.getCode(), args);
	}

	protected static String translate(String code, Object[] args) {
		try {
			return translationException(code, args);
		} catch (RuntimeException e) {
			WebformsUiLogger.errorMessage(ServerTranslate.class.getName(), e);
			try {
				MessageManager.showError(ServerTranslate.translationException("error.fatal", null));
			} catch (RuntimeException e2) {
				MessageManager.showError("Fatal error in the translations.");
			}
			return "No translation.";
		}
	}

}
