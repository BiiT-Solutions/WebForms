package com.biit.webforms.gui;

import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.gui.webpages.WebMap;
import com.biit.webforms.logger.WebformsLogger;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@Theme("webforms")
public class ApplicationUi extends UI {
	private static final long serialVersionUID = -704009283476930001L;
	private Navigator navigator;
	private View currentView;

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("");
		defineWebPages();
		System.out.println("***************************************");
		System.out.println("Ui init id: "+this.getId());	
		UiAccesser.register(UserSessionHandler.getController());
	}
	
	@Override
    public void detach() {
		System.out.println("***************************************");
		System.out.println("Detach Ui");	
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
		navigator.setErrorView(WebMap.getLoginPage().getWebPageJavaClass());
		// Create and register the other web pages.
		for (WebMap page : WebMap.values()) {
			addView(page);
		}
		setChangeViewEvents();
	}

	@SuppressWarnings("unchecked")
	private void addView(WebMap newPage) {
		navigator.addView(newPage.toString(), newPage.getWebPageJavaClass());
	}

	public static void navigateTo(WebMap newPage) {
		UI.getCurrent().getNavigator().navigateTo(newPage.toString());
	}

	public View getCurrentView() {
		return currentView;
	}

	private void setCurrentView(View currentView) {
		this.currentView = currentView;
	}

}
