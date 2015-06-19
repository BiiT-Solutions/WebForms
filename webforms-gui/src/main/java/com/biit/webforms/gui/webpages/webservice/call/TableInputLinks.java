package com.biit.webforms.gui.webpages.webservice.call;

import java.util.HashMap;
import java.util.Set;

import com.biit.webforms.language.AnswerFormatUi;
import com.biit.webforms.language.AnswerSubformatUi;
import com.biit.webforms.language.AnswerTypeUi;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.WebserviceCallLink;
import com.biit.webforms.persistence.entity.WebservicePort;
import com.vaadin.data.Item;
import com.vaadin.ui.Table;

public class TableInputLinks extends Table {
	private static final long serialVersionUID = 561989680943245108L;

	enum Properties {
		PORT_NAME, FORM_ELEMENT, ELEMENT_TYPE, ELEMENT_FORMAT, ELEMENT_SUBFORMAT,
	};

	public TableInputLinks() {
		super();
		addContainerProperty(Properties.PORT_NAME, String.class, "", LanguageCodes.PORT_TABLE_NAME.translation(), null, Align.LEFT);
		addContainerProperty(Properties.FORM_ELEMENT, String.class, "", LanguageCodes.PORT_TABLE_FORM_ELEMENT.translation(), null,
				Align.LEFT);
		addContainerProperty(Properties.ELEMENT_TYPE, String.class, "", LanguageCodes.PORT_TABLE_ELEMENT_TYPE.translation(), null,
				Align.LEFT);
		addContainerProperty(Properties.ELEMENT_FORMAT, String.class, "", LanguageCodes.PORT_TABLE_ELEMENT_FORMAT.translation(), null,
				Align.LEFT);
		addContainerProperty(Properties.ELEMENT_SUBFORMAT, String.class, "", LanguageCodes.PORT_TABLE_ELEMENT_SUBFORMAT.translation(),
				null, Align.LEFT);
	}

	public void addRows(Set<WebserviceCallLink> links, Set<WebservicePort> ports) {
		HashMap<String, WebservicePort> hashedPorts = new HashMap<>();

		for (WebservicePort port : ports) {
			hashedPorts.put(port.getName(), port);
		}
		for (WebserviceCallLink link : links) {
			addRow(link, hashedPorts.get(link.getWebservicePort()));
		}
	}

	public void addRow(WebserviceCallLink link, WebservicePort webservicePort) {
		Item item = addItem(link);
		updateRow(item, link, webservicePort);
	}
	
	/**
	 * Updates row information excep webservice port type,format,subformat information.
	 * @param link
	 */
	public void updateRow(WebserviceCallLink link) {
		Item item = getItem(link);
		if (item != null) {
			updateRow(item, link, null);
		}
	}

	public void updateRow(WebserviceCallLink link, WebservicePort webservicePort) {
		Item item = getItem(link);
		if (item != null) {
			updateRow(item, link, webservicePort);
		}
	}

	@SuppressWarnings("unchecked")
	protected void updateRow(Item item, WebserviceCallLink link, WebservicePort webservicePort) {
		item.getItemProperty(Properties.PORT_NAME).setValue(link.getWebservicePort());
		if (link.getFormElement() != null) {
			item.getItemProperty(Properties.FORM_ELEMENT).setValue(link.getFormElement().getPathName());
			if (webservicePort != null) {
				if (webservicePort.getType() != null) {
					item.getItemProperty(Properties.ELEMENT_TYPE).setValue(
							AnswerTypeUi.getFromAnswerType(webservicePort.getType()).getLanguageCode().translation());
				}else{
					item.getItemProperty(Properties.ELEMENT_TYPE).setValue("");
				}
				if (webservicePort.getFormat() != null) {
					item.getItemProperty(Properties.ELEMENT_FORMAT).setValue(
							AnswerFormatUi.getFromAnswerFormat(webservicePort.getFormat()).getLanguageCode().translation());
				}else{
					item.getItemProperty(Properties.ELEMENT_FORMAT).setValue("");
				}
				if (webservicePort.getSubformat() != null) {
					item.getItemProperty(Properties.ELEMENT_SUBFORMAT).setValue(
							AnswerSubformatUi.get(webservicePort.getSubformat()).getLanguageCode().translation());
				}else{
					item.getItemProperty(Properties.ELEMENT_SUBFORMAT).setValue("");
				}
			}
		}
	}

	public void sortByName() {
		sort(new Object[] { Properties.PORT_NAME }, new boolean[] { true });
	}

}
