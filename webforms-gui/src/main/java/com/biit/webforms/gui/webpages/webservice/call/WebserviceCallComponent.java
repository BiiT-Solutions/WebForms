package com.biit.webforms.gui.webpages.webservice.call;

import java.util.HashSet;
import java.util.Set;

import com.biit.webforms.gui.UserSessionHandler;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.dao.exceptions.WebserviceNotFoundException;
import com.biit.webforms.persistence.entity.Webservice;
import com.biit.webforms.persistence.entity.WebserviceCall;
import com.biit.webforms.persistence.entity.WebserviceCallInputLink;
import com.biit.webforms.persistence.entity.WebserviceCallLink;
import com.biit.webforms.persistence.entity.WebserviceCallOutputLink;
import com.biit.webforms.persistence.entity.WebserviceCallValidationLink;
import com.biit.webforms.persistence.entity.WebservicePort;
import com.biit.webforms.persistence.entity.WebserviceValidationPort;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class WebserviceCallComponent extends CustomComponent {
	private static final long serialVersionUID = -6887534952088846830L;

	private static final String FULL = "100%";

	private static final String WEBSERVICE_CALL_DATA_HEIGHT = "100px";

	private static final String CLASSNAME = "v-webservice-call";

	private final VerticalLayout rootLayout;
	private final TableInputLinks inputTable;
	private final TableOutputLinks outputTable;
	private final TableValidationLinks validationTable;
	private final TextField webserviceCallName;
	private final TextField webserviceName;
	private final ValueChangeListener inputValueChangeListener;
	private final ValueChangeListener outputValueChangeListener;
	private final ValueChangeListener validationValueChangeListener;

	private WebserviceCall webserviceCall;
	private Webservice webservice;

	public WebserviceCallComponent() {
		setStyleName(CLASSNAME);
		rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);

		setCompositionRoot(rootLayout);

		webserviceCallName = new TextField();
		webserviceName = new TextField();

		inputValueChangeListener = new ValueChangeListener() {
			private static final long serialVersionUID = -6889903544916406512L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				setTableToNullSilently(outputTable, outputValueChangeListener);
				setTableToNullSilently(validationTable, validationValueChangeListener);
			}
		};
		outputValueChangeListener = new ValueChangeListener() {
			private static final long serialVersionUID = -5773928794741079464L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				setTableToNullSilently(inputTable, inputValueChangeListener);
				setTableToNullSilently(validationTable, validationValueChangeListener);
			}
		};
		validationValueChangeListener = new ValueChangeListener() {
			private static final long serialVersionUID = -3907254412145157798L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				setTableToNullSilently(outputTable, outputValueChangeListener);
				setTableToNullSilently(inputTable, inputValueChangeListener);
			}
		};

		inputTable = new TableInputLinks();
		outputTable = new TableOutputLinks();
		validationTable = new TableValidationLinks();
		inputTable.setSelectable(true);
		outputTable.setSelectable(true);
		validationTable.setSelectable(true);
		inputTable.addValueChangeListener(inputValueChangeListener);
		outputTable.addValueChangeListener(outputValueChangeListener);
		validationTable.addValueChangeListener(validationValueChangeListener);

		initLayout();
	}

	private void setTableToNullSilently(Table table, ValueChangeListener listener) {
		table.removeValueChangeListener(listener);
		table.setValue(null);
		;
		table.addValueChangeListener(listener);
	}

	private void initLayout() {

		webserviceCallName.setCaption(LanguageCodes.CAPTION_WEBSERVICE_CALL_NAME.translation());
		webserviceCallName.setWidth(FULL);
		webserviceName.setCaption(LanguageCodes.CAPTION_WEBSERVICE_NAME.translation());
		webserviceName.setWidth(FULL);

		FormLayout names = new FormLayout();
		names.setWidth("50%");
		names.setHeightUndefined();
		names.setSpacing(true);
		names.addComponent(webserviceCallName);
		names.addComponent(webserviceName);

		HorizontalLayout webserviceCallData = new HorizontalLayout();
		webserviceCallData.setWidth(FULL);
		webserviceCallData.setHeight(WEBSERVICE_CALL_DATA_HEIGHT);
		webserviceCallData.setSpacing(true);
		webserviceCallData.addComponent(names);

		rootLayout.addComponent(webserviceCallData);

		inputTable.setSizeFull();
		outputTable.setSizeFull();
		validationTable.setSizeFull();

		inputTable.setCaption(LanguageCodes.CAPTION_WEBSERVICE_CALL_INPUT_LINK.translation());
		outputTable.setCaption(LanguageCodes.CAPTION_WEBSERVICE_CALL_OUTPUT_LINK.translation());
		validationTable.setCaption(LanguageCodes.CAPTION_WEBSERVICE_CALL_VALIDATION_LINK.translation());

		HorizontalLayout tableLayout = new HorizontalLayout();
		tableLayout.setSizeFull();
		tableLayout.setSpacing(true);

		tableLayout.addComponent(inputTable);
		tableLayout.addComponent(outputTable);
		tableLayout.addComponent(validationTable);

		rootLayout.addComponent(tableLayout);
		rootLayout.setExpandRatio(tableLayout, 1.0f);

	}

	public void setValue(WebserviceCall value) {
		this.webserviceCall = value;
		if (value != null) {
			try {
				this.webservice = UserSessionHandler.getController().findWebservice(webserviceCall.getWebserviceName());
			} catch (WebserviceNotFoundException e) {
				this.webservice = null;
				WebformsLogger.errorMessage(this.getClass().getName(), e);
				MessageManager.showError(LanguageCodes.COMMON_ERROR_UNEXPECTED_ERROR);
			}
		}
		refreshUi();
	}

	private void refreshUi() {
		inputTable.setValue(null);
		outputTable.setValue(null);
		validationTable.setValue(null);
		inputTable.removeAllItems();
		outputTable.removeAllItems();
		validationTable.removeAllItems();

		webserviceCallName.setEnabled(true);
		webserviceName.setEnabled(true);
		if (webserviceCall == null || webservice == null) {
			webserviceCallName.setValue("");
			webserviceName.setValue("");
		} else {
			webserviceCallName.setValue(webserviceCall.getName());
			webserviceName.setValue(webservice.getName());
			inputTable.addRows(getAllInputLinks());
			outputTable.addRows(getAllOutputLinks());
			validationTable.addRows(getAllValidationLinks());

			inputTable.sortByName();
			outputTable.sortByName();
			validationTable.sortByName();
		}
		webserviceCallName.setEnabled(false);
		webserviceName.setEnabled(false);
	}

	/**
	 * Generates all possible validation links. And creates a new set with
	 * existing validation links and all new validation links that are not used.
	 * 
	 * If a port is being used all the generated links use the same form element
	 * that the existing ones are using.
	 * 
	 * @return
	 */
	private Set<WebserviceCallLink> getAllValidationLinks() {
		Set<WebserviceValidationPort> validationPorts = webservice.getValidationPorts();
		Set<WebserviceCallValidationLink> generatedLinks = WebserviceCallValidationLink.generateWebserviceValidationLinks(validationPorts);
		Set<WebserviceCallValidationLink> usedLinks = webserviceCall.getValidateLinks();
		Set<WebserviceCallLink> mixedLinks = new HashSet<>();
		mixedLinks.addAll(usedLinks);

		for (WebserviceCallValidationLink link : generatedLinks) {
			boolean exists = false;
			for (WebserviceCallValidationLink usedLink : usedLinks) {
				if (usedLink.getWebservicePort().equals(link.getWebservicePort())){
					link.setFormElement(usedLink.getFormElement());
				}
				if (usedLink.getWebservicePort().equals(link.getWebservicePort()) && usedLink.getErrorCode().equals(link.getErrorCode())) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				mixedLinks.add(link);
			}
		}

		return mixedLinks;
	}

	/**
	 * Get a set of output call links with new elements for all not used ports
	 * and the already existing ones.
	 * 
	 * @return
	 */
	private Set<WebserviceCallLink> getAllOutputLinks() {
		Set<WebservicePort> outputPorts = webservice.getOutputPorts();
		Set<WebserviceCallOutputLink> existentLinks = webserviceCall.getOutputLinks();
		Set<WebserviceCallLink> allLinks = new HashSet<>();
		for (WebserviceCallOutputLink link : existentLinks) {
			allLinks.add(link);
		}

		for (WebservicePort port : outputPorts) {
			boolean exists = false;
			for (WebserviceCallOutputLink link : existentLinks) {
				if (link.getWebservicePort().equals(port.getName())) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				allLinks.add(new WebserviceCallOutputLink(port));
			}
		}
		return allLinks;
	}

	private Set<WebserviceCallLink> getAllInputLinks() {
		Set<WebservicePort> outputPorts = webservice.getInputPorts();
		Set<WebserviceCallInputLink> existentLinks = webserviceCall.getInputLinks();
		Set<WebserviceCallLink> allLinks = new HashSet<>();
		for (WebserviceCallInputLink link : existentLinks) {
			allLinks.add(link);
		}

		for (WebservicePort port : outputPorts) {
			boolean exists = false;
			for (WebserviceCallInputLink link : existentLinks) {
				if (link.getWebservicePort().equals(port.getName())) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				allLinks.add(new WebserviceCallInputLink(port));
			}
		}
		return allLinks;
	}

	public void clearSelectedLink() {
		WebserviceCallLink selected = getSelectedLink();
		if (selected != null) {
			selected.clear();
			selected.setWebserviceCall(null);

			if (selected instanceof WebserviceCallInputLink) {
				webserviceCall.getInputLinks().remove(selected);
			}
			if (selected instanceof WebserviceCallOutputLink) {
				webserviceCall.getOutputLinks().remove(selected);
			}
			if (selected instanceof WebserviceCallValidationLink) {
				webserviceCall.getValidateLinks().remove(selected);
			}

			// refresh completely.
			refreshUi();
		}
	}

	private void updateUiLinkInformation(WebserviceCallLink selected) {
		if (selected instanceof WebserviceCallInputLink) {
			inputTable.updateRow(selected);
			return;
		}
		if (selected instanceof WebserviceCallOutputLink) {
			outputTable.updateRow(selected);
			return;
		}
		if (selected instanceof WebserviceCallValidationLink) {
			validationTable.updateRow(selected);
			return;
		}
	}

	public WebserviceCallLink getSelectedLink() {
		if (inputTable.getValue() != null) {
			return (WebserviceCallLink) inputTable.getValue();
		}
		if (outputTable.getValue() != null) {
			return (WebserviceCallLink) outputTable.getValue();
		}
		if (validationTable.getValue() != null) {
			return (WebserviceCallLink) validationTable.getValue();
		}
		return null;
	}

	public void editSelectedLink() {
		WebserviceCallLink selected = getSelectedLink();
		if (selected instanceof WebserviceCallInputLink) {
			editInputLink((WebserviceCallInputLink) selected);
			return;
		}
		if (selected instanceof WebserviceCallOutputLink) {
			editOutputLink((WebserviceCallOutputLink) selected);
			return;
		}
		if (selected instanceof WebserviceCallValidationLink) {
			editValidationLink((WebserviceCallValidationLink) selected);
			return;
		}
	}

	private void editValidationLink(final WebserviceCallValidationLink selected) {
		WindowEditValidationLink window = new WindowEditValidationLink();
		window.setValue(selected);
		window.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				WindowEditValidationLink windowLink = (WindowEditValidationLink) window;
				windowLink.updateValue();
				
				selected.setWebserviceCall(webserviceCall);
				webserviceCall.getValidateLinks().add(selected);
				
				for(Object id: validationTable.getItemIds()){
					WebserviceCallValidationLink link = (WebserviceCallValidationLink)id;
					if(selected.getWebservicePort().equals(link.getWebservicePort())){
						link.setFormElement(selected.getFormElement());
						updateUiLinkInformation(link);
					}
				}
				window.close();
			}
		});
		window.showCentered();
	}

	private void editOutputLink(final WebserviceCallOutputLink selected) {
		WindowEditOutputLink window = new WindowEditOutputLink();
		window.setValue(selected);
		window.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				WindowEditOutputLink windowLink = (WindowEditOutputLink) window;
				windowLink.updateValue();

				selected.setWebserviceCall(webserviceCall);
				webserviceCall.getOutputLinks().add(selected);
				updateUiLinkInformation(selected);
				window.close();
			}
		});
		window.showCentered();
	}

	private void editInputLink(final WebserviceCallInputLink selected) {
		WindowEditInputLink window = new WindowEditInputLink();
		window.setValue(selected);
		window.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				WindowEditInputLink windowLink = (WindowEditInputLink) window;
				windowLink.updateValue();

				selected.setWebserviceCall(webserviceCall);
				webserviceCall.getInputLinks().add(selected);
				updateUiLinkInformation(selected);
				window.close();
			}
		});
		window.showCentered();
	}
}
