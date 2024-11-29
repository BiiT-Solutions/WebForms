package com.biit.webforms.gui.common.components;

import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.IActivity;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.UserSession;
import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.gui.common.language.CommonComponentsLanguageCodes;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.biit.webforms.gui.webpages.WebMap;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.security.IWebformsSecurityService;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinServlet;

import java.io.IOException;
import java.util.List;

/**
 * Before entering to the web page, the system checks user permissions to allow
 * the access or redirect to another page.
 */
public abstract class SecuredWebPage extends WebPage {
	private static final long serialVersionUID = -8522793320122921164L;

	private IWebformsSecurityService webformsSecurityService;

	public SecuredWebPage() {
		super();
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		webformsSecurityService = (IWebformsSecurityService) helper.getBean("webformsSecurityService");
	}

	public void securedEnter(ViewChangeEvent event) {
		initContent();
	}

	/**
	 * For increasing the performance, any class inheriting this, must defined
	 * the creation of the components inside the securedEnter method. Is
	 * recommended then, that the rootlayout is inserted the last one to avoid
	 * multiple communications with the client, i.e. add any component, button
	 * to the rootlayout and the last operation is
	 * getWorkingAreaLayout().addComponent(rootLayout);
	 */
	protected abstract void initContent();

	@Override
	public void enter(ViewChangeEvent event) {
		// Check if the user is logged in. If not, redirect to main page.
		try {
			IUser<Long> user = UserSession.getUser();
			if (user == null) {
				WebformsUiLogger.debug(this.getClass().getName(),
						"Unknown user is trying to access a secure webpage without login. Redirected to Login page.");
				ApplicationUi.navigateTo(WebMap.getLoginPage());
			} else {
				try {
					if (accessAuthorizationsRequired() != null && !accessAuthorizationsRequired().isEmpty()) {
						for (IActivity activity : accessAuthorizationsRequired()) {
							if (!getWebformsSecurityService().isUserAuthorizedInAnyOrganization(user, activity)) {
								WebformsUiLogger
										.debug(this.getClass().getName(), "Attempt to access application without appropiate roles.");
								MessageManager.showError(CommonComponentsLanguageCodes.ERROR_NOT_AUTHORIZED,
										CommonComponentsLanguageCodes.ERROR_CONTACT);
								//Clear session
								UserSession.setUser(null);
								ApplicationUi.navigateTo(WebMap.getLoginPage());
								return;
							}
						}
					}
					securedEnter(event);
					WebformsUiLogger.debug(this.getClass().getName(), "Initialized correctly");
				} catch (AuthenticationRequired | IOException e) {
					WebformsUiLogger.errorMessage(this.getClass().getName(), e);
					MessageManager.showError(LanguageCodes.ERROR_USER_SERVICE);
				}
			}
		} catch (Exception e) {
			// This is not a good practice, but Vaadin will mask any exception
			// that happens at this point of code as something completely
			// unrelated and unintelligible, so we catch the exception, log it
			// and go to the login page.
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
			// MessageManager.showError(CommonComponentsLanguageCodes.ERROR_UNEXPECTED_ERROR);
			ApplicationUi.navigateTo(WebMap.getErrorPage());
		}
	}

	/**
	 * Authorization required to access to this page. If user is not allowed, it
	 * will be redirect to login screen.
	 * 
	 * @return
	 */
	public abstract List<IActivity> accessAuthorizationsRequired();

	public IWebformsSecurityService getWebformsSecurityService() {
		return webformsSecurityService;
	}

}