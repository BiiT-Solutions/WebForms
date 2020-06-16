package com.biit.webforms.gui.webpages.webservice.call;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.form.entity.BaseQuestion;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.UserSession;
import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.biit.webforms.gui.webpages.floweditor.SearchFormElementField;
import com.biit.webforms.gui.webpages.floweditor.SearchFormElementField.SearchFormElementChanged;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.dao.exceptions.WebserviceNotFoundException;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.persistence.entity.webservices.WebserviceCall;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLink;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallLink;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallOutputLink;
import com.biit.webforms.security.IWebformsSecurityService;
import com.biit.webforms.webservices.Webservice;
import com.biit.webforms.webservices.WebservicePort;
import com.biit.webforms.webservices.WebserviceValidatedPort;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.VaadinServlet;
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
	private final TextField webserviceCallName;
	private final TextField webserviceName;
	private final SearchFormElementField webserviceCallTrigger;
	private final ValueChangeListener inputValueChangeListener;
	private final ValueChangeListener outputValueChangeListener;

	private WebserviceCall webserviceCall;
	private Webservice webservice;

	private final List<IWebserviceCallLinkValueChange> listeners;

	private IWebformsSecurityService webformsSecurityService;

	public WebserviceCallComponent() {
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		webformsSecurityService = (IWebformsSecurityService) helper.getBean("webformsSecurityService");
		listeners = new ArrayList<>();
		setStyleName(CLASSNAME);
		rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);

		setCompositionRoot(rootLayout);

		webserviceCallName = new TextField();
		webserviceName = new TextField();
		webserviceCallTrigger = new SearchFormElementField(Form.class, Category.class, Group.class, SystemField.class, Question.class);
		webserviceCallTrigger.setSelectableFilter(SystemField.class, Question.class);

		inputValueChangeListener = new ValueChangeListener() {
			private static final long serialVersionUID = -6889903544916406512L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				fireListenerValueChange((WebserviceCallLink) event.getProperty().getValue());
				setTableToNullSilently(outputTable, outputValueChangeListener);
			}
		};
		outputValueChangeListener = new ValueChangeListener() {
			private static final long serialVersionUID = -5773928794741079464L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				fireListenerValueChange((WebserviceCallLink) event.getProperty().getValue());
				setTableToNullSilently(inputTable, inputValueChangeListener);
			}
		};

		inputTable = new TableInputLinks();
		outputTable = new TableOutputLinks();

		inputTable.setSelectable(true);
		outputTable.setSelectable(true);

		inputTable.addValueChangeListener(inputValueChangeListener);
		outputTable.addValueChangeListener(outputValueChangeListener);

		initLayout();
	}

	private void setTableToNullSilently(Table table, ValueChangeListener listener) {
		table.removeValueChangeListener(listener);
		table.setValue(null);

		table.addValueChangeListener(listener);
	}

	private void initLayout() {
		webserviceCallName.setCaption(LanguageCodes.CAPTION_WEBSERVICE_CALL_NAME.translation());
		webserviceCallName.setWidth(FULL);
		webserviceName.setCaption(LanguageCodes.CAPTION_WEBSERVICE_NAME.translation());
		webserviceName.setWidth(FULL);
		webserviceCallTrigger.setCaption(LanguageCodes.CAPTION_WEBSERVICE_CALL_TRIGGER.translation());
		webserviceCallTrigger.setWidth(FULL);
		webserviceCallTrigger.addValueChangeListener(new SearchFormElementChanged() {

			@Override
			public void currentElement(Object object) {
				if (webserviceCall != null) {
					webserviceCall.setFormElementTrigger((BaseQuestion) object);
				}
			}
		});

		FormLayout names = new FormLayout();
		names.setWidth("50%");
		names.setHeightUndefined();
		names.setSpacing(true);
		names.addComponent(webserviceCallName);
		names.addComponent(webserviceName);

		FormLayout trigger = new FormLayout();
		trigger.setWidth(FULL);
		trigger.setHeightUndefined();
		trigger.setSpacing(true);
		trigger.addComponent(webserviceCallTrigger);

		HorizontalLayout webserviceCallData = new HorizontalLayout();
		webserviceCallData.setWidth(FULL);
		webserviceCallData.setHeight(WEBSERVICE_CALL_DATA_HEIGHT);
		webserviceCallData.setSpacing(true);
		webserviceCallData.addComponent(names);
		webserviceCallData.addComponent(trigger);

		rootLayout.addComponent(webserviceCallData);

		inputTable.setSizeFull();
		outputTable.setSizeFull();

		inputTable.setCaption(LanguageCodes.CAPTION_WEBSERVICE_CALL_INPUT_LINK.translation());
		outputTable.setCaption(LanguageCodes.CAPTION_WEBSERVICE_CALL_OUTPUT_LINK.translation());

		inputTable.addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = -5668566958376219563L;

			@Override
			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()) {
					Object itemId = event.getItemId();
					inputTable.setValue(itemId);
					editSelectedLink();
				}
			}
		});
		outputTable.addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = -5668566958376219563L;

			@Override
			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()) {
					Object itemId = event.getItemId();
					outputTable.setValue(itemId);
					editSelectedLink();
				}
			}
		});

		HorizontalLayout tableLayout = new HorizontalLayout();
		tableLayout.setSizeFull();
		tableLayout.setSpacing(true);

		tableLayout.addComponent(inputTable);
		tableLayout.addComponent(outputTable);

		rootLayout.addComponent(tableLayout);
		rootLayout.setExpandRatio(tableLayout, 1.0f);

	}

	public void setValue(WebserviceCall value) {
		this.webserviceCall = value;
		if (value != null) {
			try {
				this.webservice = ApplicationUi.getController().findWebservice(webserviceCall.getWebserviceName());
			} catch (WebserviceNotFoundException e) {
				this.webservice = null;
				WebformsUiLogger.errorMessage(this.getClass().getName(), e);
				MessageManager.showError(LanguageCodes.COMMON_ERROR_UNEXPECTED_ERROR);
			}
		}
		refreshUi();
	}

	private void refreshUi() {
		inputTable.setValue(null);
		outputTable.setValue(null);
		inputTable.removeAllItems();
		outputTable.removeAllItems();

		webserviceCallName.setEnabled(true);
		webserviceName.setEnabled(true);
		if (webserviceCall == null || webservice == null) {
			webserviceCallName.setValue("");
			webserviceName.setValue("");
			webserviceCallTrigger.setTreeObject(null);
		} else {
			webserviceCallName.setValue(webserviceCall.getName());
			webserviceName.setValue(webservice.getName());
			inputTable.addRows(getAllInputLinks(), new HashSet<WebservicePort>(webservice.getInputPorts()));
			outputTable.addRows(getAllOutputLinks(), webservice.getOutputPorts());
			webserviceCallTrigger.setTreeObject(webserviceCall.getFormElementTrigger());

			inputTable.sortByName();
			outputTable.sortByName();
		}
		webserviceCallName.setEnabled(false);
		webserviceName.setEnabled(false);
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
		Set<WebserviceValidatedPort> outputPorts = webservice.getInputPorts();
		Set<WebserviceCallInputLink> existentLinks = webserviceCall.getInputLinks();
		Set<WebserviceCallLink> allLinks = new HashSet<>();
		for (WebserviceCallInputLink link : existentLinks) {
			allLinks.add(link);
		}

		for (WebserviceValidatedPort port : outputPorts) {
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
			selected.remove();

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
	}

	public WebserviceCallLink getSelectedLink() {
		if (inputTable.getValue() != null) {
			return (WebserviceCallLink) inputTable.getValue();
		}
		if (outputTable.getValue() != null) {
			return (WebserviceCallLink) outputTable.getValue();
		}
		return null;
	}

	public void editSelectedLink() {
		// Check read only.
		if (ApplicationUi.getController().getFormInUse() != null
				&& !webformsSecurityService.isFormEditable(ApplicationUi.getController().getFormInUse(), UserSession.getUser())) {
			return;
		}

		WebserviceCallLink selected = getSelectedLink();
		if (selected instanceof WebserviceCallInputLink) {
			editInputLink((WebserviceCallInputLink) selected, webservice.getInputPort(selected.getWebservicePort()));
			return;
		}
		if (selected instanceof WebserviceCallOutputLink) {
			editOutputLink((WebserviceCallOutputLink) selected, webservice.getOutputPort(selected.getWebservicePort()));
			return;
		}
	}

	private void editOutputLink(final WebserviceCallOutputLink link, WebservicePort port) {
		WindowEditOutputLink window = new WindowEditOutputLink();
		window.setValue(link, port);
		window.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				WindowEditOutputLink windowLink = (WindowEditOutputLink) window;
				windowLink.updateValue();

				webserviceCall.addOutputLink(link);
				updateUiLinkInformation(link);
				window.close();
			}
		});
		window.showCentered();
	}

	private void editInputLink(final WebserviceCallInputLink link, WebserviceValidatedPort port) {
		WindowEditInputLink window = new WindowEditInputLink();
		window.setValue(link, port);
		window.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				WindowEditLink windowLink = (WindowEditLink) window;
				windowLink.updateValue();

				webserviceCall.addInputLink(link);
				if (isLastInputLink(link) && (link.getFormElement() instanceof Question)) {
					if (webserviceCallTrigger.getTreeObject() == null) {
						webserviceCallTrigger.setTreeObject(link.getFormElement());
					}
				}
				updateUiLinkInformation(link);
				window.close();
			}
		});
		window.showCentered();
	}

	/**
	 * Checks if the selected link is the last one.
	 * 
	 * @param link
	 * @return
	 */
	private boolean isLastInputLink(WebserviceCallInputLink link) {
		Set<WebserviceCallInputLink> inputLinks = webserviceCall.getInputLinks();
		List<BaseQuestion> questions = new ArrayList<>();
		for (WebserviceCallInputLink inputLink : inputLinks) {
			questions.add(inputLink.getFormElement());
		}
		// Order question.
		Collections.sort(questions);
		return questions.get(questions.size() - 1).equals(link.getFormElement());
	}

	public void addListener(IWebserviceCallLinkValueChange listener) {
		listeners.add(listener);
	}

	public void fireListenerValueChange(WebserviceCallLink link) {
		for (IWebserviceCallLinkValueChange listener : listeners) {
			listener.valueChange(link);
		}
	}

	public void select(WebserviceCallLink link) {
		if (link != null) {
			if (link instanceof WebserviceCallInputLink) {
				inputTable.setValue(link);
			} else if (link instanceof WebserviceCallOutputLink) {
				outputTable.setValue(link);
			}
		}
	}

	public void setSelectable(boolean value) {
		inputTable.setSelectable(value);
		outputTable.setSelectable(value);
	}
}
