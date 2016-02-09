package com.biit.webforms.gui.webpages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.usermanager.security.IActivity;
import com.biit.webforms.gui.UserSessionHandler;
import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.gui.common.components.SecuredWebPage;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.NotAcceptedActionListener;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.components.FormEditBottomMenu;
import com.biit.webforms.gui.webpages.webservice.call.IWebserviceCallLinkValueChange;
import com.biit.webforms.gui.webpages.webservice.call.UpperMenu;
import com.biit.webforms.gui.webpages.webservice.call.WebserviceCallComponent;
import com.biit.webforms.gui.webpages.webservice.call.WebserviceCallTable;
import com.biit.webforms.gui.webpages.webservice.call.WindowWebservices;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.webservices.WebserviceCall;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLink;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallLink;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallOutputLink;
import com.biit.webforms.security.WebformsActivity;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;

public class WebserviceCallEditor extends SecuredWebPage {
	private static final long serialVersionUID = 2762557506974832944L;
	private static final List<IActivity> activityPermissions = new ArrayList<IActivity>(Arrays.asList(WebformsActivity.READ));

	private UpperMenu upperMenu;
	private final WebserviceCallTable webserviceCallTable;
	private final WebserviceCallComponent webserviceCallComponent;
	private WebserviceCallLink selectedWebserviceCallLink;

	public WebserviceCallEditor() {
		super();
		webserviceCallTable = new WebserviceCallTable();
		webserviceCallComponent = new WebserviceCallComponent();
	}

	@Override
	protected void initContent() {
		if (UserSessionHandler.getController().getFormInUse() != null
				&& !getWebformsSecurityService().isFormEditable(UserSessionHandler.getController().getFormInUse(),
						UserSessionHandler.getUser())) {
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
		webserviceCallTable.setSelectable(true);
		webserviceCallTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 8481401135465039411L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateUpperMenu();
				updateWebserviceCallConfigComponent();
			}
		});

		webserviceCallComponent.setSizeFull();
		webserviceCallComponent.addListener(new IWebserviceCallLinkValueChange() {

			@Override
			public void valueChange(WebserviceCallLink link) {
				selectedWebserviceCallLink = link;
				updateUpperMenu();
			}
		});

		rootLayout.addComponent(webserviceCallTable);
		rootLayout.addComponent(webserviceCallComponent);
		rootLayout.setExpandRatio(webserviceCallTable, 0.2f);
		rootLayout.setExpandRatio(webserviceCallComponent, 0.8f);

		getWorkingArea().addComponent(rootLayout);

		updateUpperMenu();
		updateWebserviceCallTable();
		updateWebserviceCallConfigComponent();
		selectFirstTableItem();
	}

	/**
	 * Select first table item if any.
	 */
	private void selectFirstTableItem() {
		if (!webserviceCallTable.getItemIds().isEmpty()) {
			webserviceCallTable.setValue(webserviceCallTable.getItemIds().iterator().next());
		}
	}

	private void updateWebserviceCallConfigComponent() {
		webserviceCallComponent.setValue((WebserviceCall) webserviceCallTable.getValue());
		webserviceCallComponent.setEnabled((WebserviceCall) webserviceCallTable.getValue() != null
				&& !((WebserviceCall) webserviceCallTable.getValue()).isReadOnly());
		if (UserSessionHandler.getController().getFormInUse() != null
				&& !getWebformsSecurityService().isFormEditable(UserSessionHandler.getController().getFormInUse(),
						UserSessionHandler.getUser())) {
			webserviceCallComponent.setSelectable(false);
		} else {
			webserviceCallComponent.setSelectable(true);
		}
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
		upperMenu.getAddWebserviceCall().addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5403005874224040697L;

			@Override
			public void buttonClick(ClickEvent event) {
				addNewWebserviceCall();
			}
		});
		upperMenu.getRemoveWebserviceCall().addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7856452655739065643L;

			@Override
			public void buttonClick(ClickEvent event) {
				removeWebserviceCall();
			}
		});
		upperMenu.getEditWebserviceLink().addClickListener(new ClickListener() {
			private static final long serialVersionUID = -3649609785468773475L;

			@Override
			public void buttonClick(ClickEvent event) {
				editSelectedLink();
			}
		});
		upperMenu.getRemoveWebserviceLink().addClickListener(new ClickListener() {
			private static final long serialVersionUID = 2869493023360292398L;

			@Override
			public void buttonClick(ClickEvent event) {
				cleanSelectedLink();
			}
		});

		return upperMenu;
	}

	protected void cleanSelectedLink() {
		webserviceCallComponent.clearSelectedLink();
	}

	protected void editSelectedLink() {
		webserviceCallComponent.editSelectedLink();
	}

	protected void removeWebserviceCall() {
		UserSessionHandler.getController().removeWebserviceCall((WebserviceCall) webserviceCallTable.getValue());
		webserviceCallTable.removeItem(webserviceCallTable.getValue());
		webserviceCallTable.setValue(null);
	}

	protected void addNewWebserviceCall() {
		WindowWebservices window = new WindowWebservices();
		window.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				WindowWebservices windowWebservices = (WindowWebservices) window;
				WebserviceCall call = UserSessionHandler.getController().generateNewWebserviceCall(windowWebservices.getName(),
						windowWebservices.getWebservice());
				updateWebserviceCallTable();
				window.close();
				webserviceCallTable.setValue(call);
			}
		});
		window.addNotAcceptedActionListener(new NotAcceptedActionListener() {
			@Override
			public void notAcceptedAction(WindowAcceptCancel window) {
				MessageManager.showError(LanguageCodes.WEBSERVICE_ERROR_MESSAGE_NAME_NOT_FILLED);
			}
		});
		window.showCentered();
	}

	protected void save() {
		try {
			UserSessionHandler.getController().saveForm();
			updateWebserviceCallTable();
			MessageManager.showInfo(LanguageCodes.INFO_MESSAGE_FORM_CAPTION_SAVE, LanguageCodes.INFO_MESSAGE_FORM_DESCRIPTION_SAVE);
		} catch (UnexpectedDatabaseException e) {
			MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE, LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
		} catch (ElementCannotBePersistedException e) {
			MessageManager.showError(LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED, LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED_DESCRIPTION);
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	@Override
	public List<IActivity> accessAuthorizationsRequired() {
		return activityPermissions;
	}

	private void updateWebserviceCallTable() {
		WebserviceCall selectedCall = (WebserviceCall) webserviceCallTable.getValue();
		WebserviceCallLink selectedLink = selectedWebserviceCallLink;

		webserviceCallTable.setValue(null);
		webserviceCallTable.removeAllItems();
		Set<WebserviceCall> calls = UserSessionHandler.getController().getCompleteFormView().getWebserviceCalls();
		webserviceCallTable.addRows(calls);
		if (selectedCall != null) {
			for (WebserviceCall call : calls) {
				if (selectedCall.getComparationId().equals(call.getComparationId())) {
					webserviceCallTable.setValue(call);
					if (selectedLink != null) {
						if (selectedLink instanceof WebserviceCallInputLink) {
							for (WebserviceCallInputLink link : call.getInputLinks()) {
								if (link.getComparationId().equals(selectedLink.getComparationId())) {
									webserviceCallComponent.select(link);
								}
							}
						} else if (selectedLink instanceof WebserviceCallOutputLink) {
							for (WebserviceCallOutputLink link : call.getOutputLinks()) {
								if (link.getComparationId().equals(selectedLink.getComparationId())) {
									webserviceCallComponent.select(link);
								}
							}
						}
					}
				}
			}
		}
		webserviceCallTable.sortByName();
	}

	private void updateUpperMenu() {
		if (UserSessionHandler.getController().getFormInUse() != null
				&& !getWebformsSecurityService().isFormEditable(UserSessionHandler.getController().getFormInUse(),
						UserSessionHandler.getUser())) {
			upperMenu.getSaveButton().setEnabled(false);
			upperMenu.getAddWebserviceCall().setEnabled(false);
			upperMenu.getRemoveWebserviceCall().setEnabled(false);
			upperMenu.getEditWebserviceLink().setEnabled(false);
			upperMenu.getRemoveWebserviceLink().setEnabled(false);
			return;
		}

		boolean webserviceCallSelected = webserviceCallTable.getValue() != null;
		upperMenu.getRemoveWebserviceCall().setEnabled(webserviceCallSelected);
		upperMenu.getEditWebserviceLink().setEnabled(webserviceCallSelected && selectedWebserviceCallLink != null);
		upperMenu.getRemoveWebserviceLink().setEnabled(webserviceCallSelected && selectedWebserviceCallLink != null);
	}
}
