package com.biit.webforms.gui.webpages.webservice.call;

import com.biit.webforms.gui.webpages.floweditor.SearchFormElementField;
import com.biit.webforms.language.LanguageCodes;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class WebserviceCallComponent extends CustomComponent{
	private static final long serialVersionUID = -6887534952088846830L;

	private static final String FULL = "100%";

	private static final String WEBSERVICE_CALL_DATA_HEIGHT = "100px";
	
	private final VerticalLayout rootLayout;
	private final Table inputTable;
	private final Table outputTable;
	private final Table validationTable;
	private final TextField webserviceCallName;
	private final TextField webserviceName;
	private final SearchFormElementField webserviceCallTrigger;

	public WebserviceCallComponent() {
		rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		
		setCompositionRoot(rootLayout);
		
		webserviceCallName = new TextField();
		webserviceName = new TextField();
		webserviceCallTrigger = new SearchFormElementField();
		
		inputTable = new Table();
		outputTable = new Table();
		validationTable = new Table();
		
		initLayout();
	}

	private void initLayout() {
		
		webserviceCallName.setCaption(LanguageCodes.CAPTION_WEBSERVICE_CALL_NAME.translation());
		webserviceCallName.setWidth(FULL);
		webserviceName.setCaption(LanguageCodes.CAPTION_WEBSERVICE_NAME.translation());
		webserviceName.setWidth(FULL);
		webserviceCallTrigger.setCaption(LanguageCodes.CAPTION_WEBSERVICE_CALL_TRIGGER.translation());
		webserviceCallTrigger.setWidth(FULL);
		
		FormLayout names = new FormLayout();
		names.setWidth(FULL);
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
		validationTable.setSizeFull();
		
		HorizontalLayout tableLayout = new HorizontalLayout();
		tableLayout.setSizeFull();
		
		tableLayout.addComponent(inputTable);
		tableLayout.addComponent(outputTable);
		tableLayout.addComponent(validationTable);
		
		rootLayout.addComponent(tableLayout);
		rootLayout.setExpandRatio(tableLayout, 1.0f);
		
	}
}
