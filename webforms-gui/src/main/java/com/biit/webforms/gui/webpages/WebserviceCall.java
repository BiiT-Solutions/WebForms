package com.biit.webforms.gui.webpages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.biit.liferay.security.IActivity;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.gui.UserSessionHandler;
import com.biit.webforms.gui.common.components.SecuredWebPage;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.components.FormEditBottomMenu;
import com.biit.webforms.gui.webpages.webservice.call.UpperMenu;
import com.biit.webforms.gui.webpages.webservice.call.WebserviceCallComponent;
import com.biit.webforms.gui.webpages.webservice.call.WebserviceCallTable;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.security.WebformsActivity;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalLayout;

public class WebserviceCall extends SecuredWebPage {
	private static final long serialVersionUID = 2762557506974832944L;
	private static final List<IActivity> activityPermissions = new ArrayList<IActivity>(
			Arrays.asList(WebformsActivity.READ));
	
	private UpperMenu upperMenu;
	private final WebserviceCallTable webserviceCallTable;
	private final WebserviceCallComponent webserviceCallComponent;
	
	public WebserviceCall() {
		super();
		webserviceCallTable= new WebserviceCallTable();
		webserviceCallComponent = new WebserviceCallComponent();
	}

	@Override
	protected void initContent() {
		if (UserSessionHandler.getController().getFormInUse() != null
				&& !WebformsAuthorizationService.getInstance().isFormEditable(
						UserSessionHandler.getController().getFormInUse(), UserSessionHandler.getUser())) {
			MessageManager.showWarning(LanguageCodes.INFO_MESSAGE_FORM_IS_READ_ONLY);
		}
		
		setCentralPanelAsWorkingArea();
		upperMenu = createUpperMenu();
		setUpperMenu(upperMenu);
		
		setBottomMenu(new FormEditBottomMenu());
		
		HorizontalLayout rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setSpacing(true);
		rootLayout.setMargin(true);
		
		webserviceCallTable.setSizeFull();

		webserviceCallComponent.setSizeFull();

		rootLayout.addComponent(webserviceCallTable);
		rootLayout.addComponent(webserviceCallComponent);
		rootLayout.setExpandRatio(webserviceCallTable, 0.2f);
		rootLayout.setExpandRatio(webserviceCallComponent, 0.8f);
		
		getWorkingArea().addComponent(rootLayout);
	}

	private UpperMenu createUpperMenu() {
		final UpperMenu upperMenu = new UpperMenu();
		
		upperMenu.getSaveButton().addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7091738197213563730L;

			@Override
			public void buttonClick(ClickEvent event) {
				save();
			}
		});
		
		return upperMenu;
	}

	protected void save() {
		try {
			UserSessionHandler.getController().saveForm();
		} catch (UnexpectedDatabaseException e) {
			MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
					LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		} catch (ElementCannotBePersistedException e) {
			MessageManager.showError(LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED,
					LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED_DESCRIPTION);
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	@Override
	public List<IActivity> accessAuthorizationsRequired() {
		return activityPermissions;
	}

}
