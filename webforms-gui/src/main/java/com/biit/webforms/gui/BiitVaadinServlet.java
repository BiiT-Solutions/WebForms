package com.biit.webforms.gui;

import javax.servlet.ServletException;

import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinServlet;

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
