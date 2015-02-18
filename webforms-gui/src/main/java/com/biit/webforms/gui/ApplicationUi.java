package com.biit.webforms.gui;

import com.biit.webforms.gui.webpages.WebMap;
import com.biit.webforms.logger.WebformsLogger;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

/**
 * Main Ui class of application. Ui has been configured to preserve the Ui when a Refresh action has occurred. This way,
 * a UI is only discarded when the user is no longer active in a long time, instead of every time there is a refresh
 * event.
 * 
 */
@Push
@Theme("webforms")
@PreserveOnRefresh
public class ApplicationUi extends UI {
	public final static String USER_PARAMETER_TAG = "user";
	public final static String PASSWORD_PARAMETER_TAG = "password";
	private static final long serialVersionUID = -704009283476930001L;
	private Navigator navigator;
	private View currentView;
	private String user;
	private String password;

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("");
		defineWebPages();
		UiAccesser.register(UserSessionHandler.getController());

		// Liferay send this data and automatically are used in the login screen.
		this.user = request.getParameter(USER_PARAMETER_TAG);
		this.password = request.getParameter(PASSWORD_PARAMETER_TAG);
	}

	@Override
	public void detach() {
		if (UserSessionHandler.getUser() != null) {
			// Log user ui expired.
			WebformsLogger.info(this.getClass().getName(), UserSessionHandler.getUser().getEmailAddress()
					+ " UI has expired.");
		}
		UiAccesser.unregister(UserSessionHandler.getController());
		super.detach();
	}

	private void setChangeViewEvents() {
		navigator.addViewChangeListener(new ViewChangeListener() {
			private static final long serialVersionUID = -668206181478591694L;

			@Override
			public boolean beforeViewChange(ViewChangeEvent event) {
				setCurrentView(event.getNewView());
				return true;
			}

			@Override
			public void afterViewChange(ViewChangeEvent event) {
				if (UserSessionHandler.getUser() != null) {
					WebformsLogger.info(this.getClass().getName(), "User '"
							+ UserSessionHandler.getUser().getEmailAddress() + "' has change view to '"
							+ event.getNewView().getClass().getName() + "'.");
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void defineWebPages() {
		// Create a navigator to control the views
		navigator = new Navigator(this, this);
		// Define login page as first one.
		navigator.addView("", WebMap.getLoginPage().getWebPageJavaClass());
		navigator.setErrorView(WebMap.getErrorPage().getWebPageJavaClass());
		// Create and register the other web pages.
		for (WebMap page : WebMap.values()) {
			addView(page);
		}
		setChangeViewEvents();
	}

	@SuppressWarnings("unchecked")
	public void addView(WebMap newPage) {
		navigator.addView(newPage.toString(), newPage.getWebPageJavaClass());
	}

	public static void navigateTo(WebMap newPage) {
		UI.getCurrent().getNavigator().navigateTo(newPage.toString());
		UserSessionHandler.setUserLastPage(newPage);
	}

	public View getCurrentView() {
		return currentView;
	}

	private void setCurrentView(View currentView) {
		this.currentView = currentView;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

}
