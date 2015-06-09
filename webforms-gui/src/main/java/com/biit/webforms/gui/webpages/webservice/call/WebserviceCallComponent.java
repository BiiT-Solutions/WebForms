package com.biit.webforms.gui.webpages.webservice.call;

import com.biit.webforms.gui.webpages.floweditor.SearchFormElementField;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.WebserviceCall;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
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
	private final TableInputLinks inputTable;
	private final TableOutputLinks outputTable;
	private final Table validationTable;
	private final TextField webserviceCallName;
	private final TextField webserviceName;
	private final SearchFormElementField webserviceCallTrigger;
	private final ValueChangeListener inputValueChangeListener;
	private final ValueChangeListener outputValueChangeListener;
	private final ValueChangeListener validationValueChangeListener;
	
	private WebserviceCall webserviceCall;

	public WebserviceCallComponent() {
		rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);
		
		setCompositionRoot(rootLayout);
		
		webserviceCallName = new TextField();
		webserviceName = new TextField();
		webserviceCallTrigger = new SearchFormElementField();
		
		inputValueChangeListener = new ValueChangeListener() {
			private static final long serialVersionUID = -6889903544916406512L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				setTableToNullSilently(outputTable,outputValueChangeListener);
				setTableToNullSilently(validationTable,validationValueChangeListener);
			}
		};
		outputValueChangeListener = new ValueChangeListener() {
			private static final long serialVersionUID = -5773928794741079464L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				setTableToNullSilently(inputTable,inputValueChangeListener);
				setTableToNullSilently(validationTable,validationValueChangeListener);
			}
		};
		validationValueChangeListener = new ValueChangeListener() {
			private static final long serialVersionUID = -3907254412145157798L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				setTableToNullSilently(outputTable,outputValueChangeListener);
				setTableToNullSilently(inputTable,validationValueChangeListener);
			}
		};
		
		inputTable = new TableInputLinks();
		outputTable = new TableOutputLinks();
		validationTable = new Table();
		inputTable.setSelectable(true);
		outputTable.setSelectable(true);
		validationTable.setSelectable(true);
		inputTable.addValueChangeListener(inputValueChangeListener);
		outputTable.addValueChangeListener(outputValueChangeListener);
		validationTable.addValueChangeListener(validationValueChangeListener);
		
		initLayout();
	}
	
	private void setTableToNullSilently(Table table, ValueChangeListener listener){
		table.removeValueChangeListener(listener);
		table.setValue(null);;
		table.addValueChangeListener(listener);
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
		tableLayout.setSpacing(true);
		
		tableLayout.addComponent(inputTable);
		tableLayout.addComponent(outputTable);
		tableLayout.addComponent(validationTable);
		
		rootLayout.addComponent(tableLayout);
		rootLayout.setExpandRatio(tableLayout, 1.0f);
		
	}

	public void setValue(WebserviceCall value) {
		this.webserviceCall = value;
		refreshUi();
	}

	private void refreshUi() {
		webserviceCallName.setEnabled(true);
		webserviceName.setEnabled(true);
		if(webserviceCall==null){
			webserviceCallName.setValue("");
			webserviceName.setValue("");
		}else{
			webserviceCallName.setValue(webserviceCall.getName());
			webserviceName.setValue(webserviceCall.getName());
		}
		webserviceCallName.setEnabled(false);
		webserviceName.setEnabled(false);
		
		
		
		inputTable.addRows(webserviceCall.getInputLinks());
		outputTable.addRows(webserviceCall.getOutputLinks());
		
		
	}
}
