package com.biit.webforms.gui.webpages.floweditor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.biit.webforms.gui.common.components.StringToTimestampConverter;
import com.biit.webforms.gui.webpages.floweditor.listeners.EditItemAction;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.language.RuleTypeUi;
import com.biit.webforms.persistence.entity.Flow;
import com.vaadin.data.Item;
import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Table;

public class TableFlows extends Table {
	private static final long serialVersionUID = -296543725516584972L;
	private static final String NEW_RULE_ID = "NEW_RULE";
	private static final String EMPTY_TEXT = "--------";

	public interface NewItemAction {
		public void newItemAction();
	};

	public enum TableRuleProperties {
		ORIGIN, TYPE, DESTINY, CONDITION, CREATION_DATE, UPDATE_DATE
	};

	private List<NewItemAction> newItemListeners;
	private List<EditItemAction> editItemListeners;

	public Object getNewFlowId() {
		return NEW_RULE_ID;
	}

	public TableFlows() {
		super();
		newItemListeners = new ArrayList<>();
		editItemListeners = new ArrayList<>();
		initContainerProperties();
		addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = -5668566958376219563L;

			@Override
			public void itemClick(ItemClickEvent event) {
				Object itemId = event.getItemId();
				if (itemId.equals(NEW_RULE_ID) && event.isDoubleClick()) {
					fireNewItemActionListener();
					return;
				}
				if (!itemId.equals(NEW_RULE_ID) && event.isDoubleClick()) {
					fireEditItemActionListener((Flow) event.getItemId());
					return;
				}
			}
		});
		addCleanRow();
		setSelectable(true);
	}

	public void addNewItemActionListener(NewItemAction listener) {
		newItemListeners.add(listener);
	}

	protected void fireNewItemActionListener() {
		for (NewItemAction listener : newItemListeners) {
			listener.newItemAction();
		}
	}

	protected void fireEditItemActionListener(Flow ruleToEdit) {
		for (EditItemAction listener : editItemListeners) {
			listener.editItemAction(ruleToEdit);
		}
	}

	@SuppressWarnings("unchecked")
	private void addCleanRow() {

		Item cleanRow = getItem(NEW_RULE_ID);
		if (cleanRow != null) {
			removeItem(NEW_RULE_ID);
		}
		cleanRow = addItem(NEW_RULE_ID);
		cleanRow.getItemProperty(TableRuleProperties.ORIGIN).setValue(EMPTY_TEXT);
		cleanRow.getItemProperty(TableRuleProperties.TYPE).setValue(EMPTY_TEXT);
		cleanRow.getItemProperty(TableRuleProperties.DESTINY).setValue(EMPTY_TEXT);
		cleanRow.getItemProperty(TableRuleProperties.CONDITION).setValue(EMPTY_TEXT);
	}

	private void initContainerProperties() {
		addContainerProperty(TableRuleProperties.ORIGIN, String.class, null,
				LanguageCodes.TABLE_RULE_TITLE_ORIGIN.translation(), null, Align.LEFT);
		addContainerProperty(TableRuleProperties.TYPE, String.class, null,
				LanguageCodes.TABLE_RULE_TITLE_TYPE.translation(), null, Align.LEFT);
		addContainerProperty(TableRuleProperties.DESTINY, String.class, null,
				LanguageCodes.TABLE_RULE_TITLE_DESTINY.translation(), null, Align.LEFT);
		addContainerProperty(TableRuleProperties.CONDITION, String.class, null,
				LanguageCodes.TABLE_RULE_TITLE_CONDITION.translation(), null, Align.LEFT);
		addContainerProperty(TableRuleProperties.CREATION_DATE, Timestamp.class, null,
				LanguageCodes.TABLE_RULE_TITLE_CREATION_DATE.translation(), null, Align.LEFT);
		addContainerProperty(TableRuleProperties.UPDATE_DATE, Timestamp.class, null,
				LanguageCodes.TABLE_RULE_TITLE_UPDATE_DATE.translation(), null, Align.LEFT);

		setConverter(TableRuleProperties.CREATION_DATE, new StringToTimestampConverter());
		setConverter(TableRuleProperties.UPDATE_DATE, new StringToTimestampConverter());

		setColumnExpandRatio(TableRuleProperties.ORIGIN, 1.0f);
		setColumnExpandRatio(TableRuleProperties.DESTINY, 1.0f);
		setColumnExpandRatio(TableRuleProperties.CONDITION, 2.0f);

		setColumnCollapsingAllowed(true);

		setColumnCollapsible(TableRuleProperties.ORIGIN, false);
		setColumnCollapsible(TableRuleProperties.TYPE, false);
		setColumnCollapsible(TableRuleProperties.DESTINY, false);
		setColumnCollapsible(TableRuleProperties.CONDITION, false);

		setColumnCollapsible(TableRuleProperties.CREATION_DATE, true);
		setColumnCollapsible(TableRuleProperties.UPDATE_DATE, true);

		setColumnCollapsed(TableRuleProperties.CREATION_DATE, true);
		setColumnCollapsed(TableRuleProperties.UPDATE_DATE, true);

		((IndexedContainer) getContainerDataSource()).setItemSorter(new TableRulesSorter());
		
		setCellStyleGenerator(new FlowTableCellStyleGenerator());
	}

	public void addRows(Set<Flow> rules) {
		for (Flow rule : rules) {
			addRow(rule, false, false);
		}
		addCleanRow();
	}

	public void setRows(Set<Flow> rules) {
		removeAllItems();
		addRows(rules);
	}

	/**
	 * Adds a new row, moves the 'insert new rule' to the end and selects as current value the new row.
	 * 
	 * @param newRule
	 */
	public void addRow(Flow newRule) {
		addRow(newRule, true, true);
	}

	public void addRow(Flow newRule, boolean addCleanRow, boolean select) {
		Item item = addItem(newRule);
		if (item != null) {
			setItemProperties(newRule, item);
			if (addCleanRow) {
				addCleanRow();
			}
			if (select) {
				setValue(newRule);
			}
		}
	}

	public void addOrUpdateFlows(Flow... newRules) {
		if (newRules == null || newRules.length == 0) {
			return;
		}
		for (Flow newRule : newRules) {
			if (containsId(newRule)) {
				updateRow(newRule);
			} else {
				addRow(newRule, false, false);
			}
		}
		addCleanRow();
		setValue(newRules);
	}

	/**
	 * Sets item properties values from the given rule.
	 * 
	 * @param rule
	 * @param item
	 */
	@SuppressWarnings("unchecked")
	public void setItemProperties(Flow rule, Item item) {
		item.getItemProperty(TableRuleProperties.ORIGIN).setValue(rule.getOrigin().getPathName());

		item.getItemProperty(TableRuleProperties.TYPE).setValue(RuleTypeUi.getTranslation(rule.getFlowType()));

		String destiny = "";
		if (rule.getDestiny() != null) {
			destiny = rule.getDestiny().getPathName();
		}
		item.getItemProperty(TableRuleProperties.DESTINY).setValue(destiny);

		String condition = "";
		if (rule.isOthers()) {
			condition = "OTHERS";
		} else {
			condition = rule.getConditionString();
		}
		item.getItemProperty(TableRuleProperties.CONDITION).setValue(condition);

		item.getItemProperty(TableRuleProperties.CREATION_DATE).setValue(rule.getCreationTime());
		item.getItemProperty(TableRuleProperties.UPDATE_DATE).setValue(rule.getUpdateTime());
	}

	public void addEditItemActionListener(EditItemAction listener) {
		editItemListeners.add(listener);
	}

	/**
	 * Updates values of the row from the given rule.
	 * 
	 * @param rule
	 */
	public void updateRow(Flow rule) {
		Item item = getItem(rule);
		if (item != null) {
			setItemProperties(rule, item);
		}
	}

	public void sortByUpdateDate(boolean ascending) {
		sort(new Object[] { TableRuleProperties.UPDATE_DATE }, new boolean[] { ascending });
	}

	/**
	 * Custom Item sorter.
	 * 
	 * @author joriz_000
	 * 
	 */
	public class TableRulesSorter extends DefaultItemSorter {
		private static final long serialVersionUID = 132410424035740848L;

		@Override
		public int compare(Object itemId1, Object itemId2) {
			if (itemId1 instanceof String) {
				return 1;
			}
			if (itemId2 instanceof String) {
				return -1;
			}
			return super.compare(itemId1, itemId2);
		}
	}
}
