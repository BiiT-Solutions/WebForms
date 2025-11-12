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

import com.vaadin.server.*;

import javax.servlet.ServletException;

/**
 * Standard servlet that extends base vaadin servlet and registers session
 * creation/destruction listeners to help the process of controlling user
 * sessions and credentials.
 *
 */
public class BiitVaadinServlet extends VaadinServlet implements SessionDestroyListener, SessionInitListener {

	private static final long serialVersionUID = 843355335806848536L;

	@Override
	protected void servletInitialized() throws ServletException {
		super.servletInitialized();
		this.getService().addSessionInitListener(this);
		this.getService().addSessionDestroyListener(this);
	}

	@Override
	public void sessionDestroy(SessionDestroyEvent event) {
		WebformsUiLogger.info(BiitVaadinServlet.class.getName(), "Vaadin Session destroyed");
		CredentialsRegistry.removeSession(event.getSession());
	}

	@Override
	public void sessionInit(SessionInitEvent event) throws ServiceException {
		WebformsUiLogger.info(BiitVaadinServlet.class.getName(), "Vaadin Session initialized '" + event.getSession().getSession().getId()
				+ "'");
	}

}
