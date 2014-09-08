package com.biit.webforms.gui.webpages.floweditor;

import java.util.ArrayList;
import java.util.List;

import com.biit.webforms.language.LanguageCodes;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Table;

public class TableRules extends Table {
	private static final long serialVersionUID = -296543725516584972L;
	private static final String NEW_RULE = "NEW_RULE";
	private static final String EMPTY_TEXT = "--------";

	public interface NewItemAction {
		public void newItemAction();
	};

	private enum TableRuleProperties {
		ORIGIN, DESTINY, CONDITION
	};

	private List<NewItemAction> newItemListeners;

	public TableRules() {
		super();
		newItemListeners = new ArrayList<>();
		initContainerProperties();
		addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = -5668566958376219563L;

			@Override
			public void itemClick(ItemClickEvent event) {
				Object itemId = event.getItemId();
				if (itemId.equals(NEW_RULE) && event.isDoubleClick()) {
					fireNewItemActionListener();
				}
			}
		});
		addCleanRow();
	}
	
	public void addNewItemActionListener(NewItemAction listener){
		newItemListeners.add(listener);
	}

	protected void fireNewItemActionListener() {
		for (NewItemAction listener : newItemListeners) {
			listener.newItemAction();
		}
	}

	@SuppressWarnings("unchecked")
	private void addCleanRow() {

		Item cleanRow = getItem(NEW_RULE);
		if (cleanRow != null) {
			removeItem(NEW_RULE);
		}
		cleanRow = addItem(NEW_RULE);
		cleanRow.getItemProperty(TableRuleProperties.ORIGIN).setValue(EMPTY_TEXT);
		cleanRow.getItemProperty(TableRuleProperties.DESTINY).setValue(EMPTY_TEXT);
		cleanRow.getItemProperty(TableRuleProperties.CONDITION).setValue(EMPTY_TEXT);

	}

	private void initContainerProperties() {
		addContainerProperty(TableRuleProperties.ORIGIN, String.class, null,
				LanguageCodes.TABLE_RULE_TITLE_ORIGIN.translation(), null, Align.LEFT);
		addContainerProperty(TableRuleProperties.DESTINY, String.class, null,
				LanguageCodes.TABLE_RULE_TITLE_DESTINY.translation(), null, Align.LEFT);
		addContainerProperty(TableRuleProperties.CONDITION, String.class, null,
				LanguageCodes.TABLE_RULE_TITLE_CONDITION.translation(), null, Align.LEFT);

		setColumnExpandRatio(TableRuleProperties.ORIGIN, 1.0f);
		setColumnExpandRatio(TableRuleProperties.DESTINY, 1.0f);
		setColumnExpandRatio(TableRuleProperties.CONDITION, 2.0f);
	}

}
