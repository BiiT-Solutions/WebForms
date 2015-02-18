package com.biit.webforms.gui.common.components;

import java.io.IOException;
import java.util.List;

import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.security.IActivity;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.UserSessionHandler;
import com.biit.webforms.gui.common.language.CommonComponentsLanguageCodes;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.webpages.WebMap;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.liferay.portal.model.User;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * Before entering to the web page, the system checks user permissions to allow the access or redirect to another page.
 */
public abstract class SecuredWebPage extends WebPage {
	private static final long serialVersionUID = -8522793320122921164L;

	public void securedEnter(ViewChangeEvent event) {
		initContent();
	}

	/**
	 * For increasing the performance, any class inheriting this, must defined the creation of the components inside the
	 * securedEnter method. Is recommended then, that the rootlayout is inserted the last one to avoid multiple
	 * communications with the client, i.e. add any component, button to the rootlayout and the last operation is
	 * getWorkingAreaLayout().addComponent(rootLayout);
	 */
	protected abstract void initContent();

	@Override
	public final void enter(ViewChangeEvent event) {
		// Check if the user is logged in. If not, redirect to main page.
		try {
			User user = UserSessionHandler.getUser();
			if (user == null) {
				WebformsLogger.debug(this.getClass().getName(), "User is null. Redirecting to login page.");
				ApplicationUi.navigateTo(WebMap.getLoginPage());
			} else {
				try {
					if (accessAuthorizationsRequired() != null && !accessAuthorizationsRequired().isEmpty()) {
						for (IActivity activity : accessAuthorizationsRequired()) {
							if (!WebformsAuthorizationService.getInstance().isUserAuthorizedInAnyOrganization(user,
									activity)) {
								WebformsLogger.debug(this.getClass().getName(), "User: "
										+ UserSessionHandler.getUser().getEmailAddress()
										+ " tried to access application without appropiate roles.");
								MessageManager.showError(CommonComponentsLanguageCodes.ERROR_NOT_AUTHORIZED,
										CommonComponentsLanguageCodes.ERROR_CONTACT);
								ApplicationUi.navigateTo(WebMap.getLoginPage());
								return;
							}
						}
					}
					securedEnter(event);
					WebformsLogger.debug(this.getClass().getName(), "Initialized correctly for user: "
							+ UserSessionHandler.getUser().getEmailAddress());
				} catch (AuthenticationRequired | IOException e) {
					WebformsLogger.errorMessage(this.getClass().getName(), e);
					MessageManager.showError(LanguageCodes.ERROR_USER_SERVICE);
				}
			}
		} catch (Exception e) {
			// This is not a good practice, but Vaadin will mask any exception
			// that happens at this point of code as something completely
			// unrelated and unintelligible, so we catch the exception, log it
			// and go to the login page.
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			MessageManager.showError(CommonComponentsLanguageCodes.ERROR_UNEXPECTED_ERROR);
			ApplicationUi.navigateTo(WebMap.getLoginPage());
		}
	}

	/**
	 * Authorization required to access to this page. If user is not allowed, it will be redirect to login screen.
	 * 
	 * @return
	 */
	public abstract List<IActivity> accessAuthorizationsRequired();

}