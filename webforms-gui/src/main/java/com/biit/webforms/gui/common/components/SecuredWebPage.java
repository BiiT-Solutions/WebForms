package com.biit.webforms.gui.common.components;

import java.util.List;

import com.biit.liferay.security.IActivity;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.gui.ApplicationFrame;
import com.biit.webforms.gui.common.language.CommonLanguageCodes;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.common.utils.WebpageAuthorizationService;
import com.biit.webforms.gui.webpages.WebMap;
import com.biit.webforms.logger.WebformsLogger;
import com.liferay.portal.model.User;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * Before entering to the web page, the system checks user permissions to allow
 * the access or redirect to another page.
 */
public abstract class SecuredWebPage extends WebPage {
	private static final long serialVersionUID = -8522793320122921164L;

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
	public final void enter(ViewChangeEvent event) {
		// Check if the user is logged in. If not, redirect to main page.
		try {
			User user = UserSessionHandler.getUser();
			if (user == null) {
				ApplicationFrame.navigateTo(WebMap.getLoginPage());
			} else {
				if (accessAuthorizationsRequired() != null && !accessAuthorizationsRequired().isEmpty()) {
					for (IActivity activity : accessAuthorizationsRequired()) {
						if (!WebpageAuthorizationService.getInstance().isAuthorizedActivity(user, activity)) {
							ApplicationFrame.navigateTo(WebMap.getLoginPage());
						}
					}
				}
				securedEnter(event);
			}
		} catch (Exception e) {
			// This is not a good practice, but Vaadin will mask any exception
			// that happens at this point of code as something completely
			// unrelated and unintelligible, so we catch the exception, log it
			// and go to the login page.
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			MessageManager.showError(CommonLanguageCodes.ERROR_UNEXPECTED_ERROR);
			ApplicationFrame.navigateTo(WebMap.getLoginPage());
		}
	}

	/**
	 * Authorization required to access to this page. If user is not allowed, it
	 * will be redirect to login screen.
	 * 
	 * @return
	 */
	public abstract List<IActivity> accessAuthorizationsRequired();

}