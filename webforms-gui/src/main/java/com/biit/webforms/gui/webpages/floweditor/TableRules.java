package com.biit.webforms.gui.webpages.floweditor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.language.RuleTypeUi;
import com.biit.webforms.persistence.entity.Rule;
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

	public enum TableRuleProperties {
		ORIGIN, TYPE, DESTINY, CONDITION
	};

	private List<NewItemAction> newItemListeners;
	private List<EditItemAction> editItemListeners;

	public Object getNewRuleId() {
		return NEW_RULE;
	}

	public TableRules() {
		super();
		newItemListeners = new ArrayList<>();
		editItemListeners = new ArrayList<>();
		initContainerProperties();
		addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = -5668566958376219563L;

			@Override
			public void itemClick(ItemClickEvent event) {
				Object itemId = event.getItemId();
				if (itemId.equals(NEW_RULE) && event.isDoubleClick()) {
					fireNewItemActionListener();
					return;
				}
				if (!itemId.equals(NEW_RULE) && event.isDoubleClick()) {
					fireEditItemActionListener((Rule) event.getItemId());
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

	protected void fireEditItemActionListener(Rule ruleToEdit) {
		for (EditItemAction listener : editItemListeners) {
			listener.editItemAction(ruleToEdit);
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

		setColumnExpandRatio(TableRuleProperties.ORIGIN, 1.0f);
		setColumnExpandRatio(TableRuleProperties.DESTINY, 1.0f);
		setColumnExpandRatio(TableRuleProperties.CONDITION, 2.0f);
	}

	public void addRows(Set<Rule> rules) {
		for (Rule rule : rules) {
			addRow(rule, false, false);
		}
		addCleanRow();
	}

	/**
	 * Adds a new row, moves the 'insert new rule' to the end and selects as
	 * current value the new row.
	 * 
	 * @param newRule
	 */
	public void addRow(Rule newRule) {
		addRow(newRule, true, true);
	}

	public void addRow(Rule newRule, boolean addCleanRow, boolean select) {
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

	public void addOrUpdateRules(Rule... newRules) {
		if (newRules == null || newRules.length == 0) {
			return;
		}
		for (Rule newRule : newRules) {
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
	public void setItemProperties(Rule rule, Item item) {
		item.getItemProperty(TableRuleProperties.ORIGIN).setValue(rule.getOrigin().getName());

		item.getItemProperty(TableRuleProperties.TYPE).setValue(RuleTypeUi.getTranslation(rule.getRuleType()));

		String destiny = "";
		if (rule.getDestiny() != null) {
			destiny = rule.getDestiny().getName();
		}
		item.getItemProperty(TableRuleProperties.DESTINY).setValue(destiny);

		String condition = "";
		if(rule.isOthers()){
			condition = "OTHERS";
		}else{
			condition = rule.getConditionString();
		}
		item.getItemProperty(TableRuleProperties.CONDITION).setValue(condition);
	}

	public void addEditItemActionListener(EditItemAction listener) {
		editItemListeners.add(listener);
	}

	/**
	 * Updates values of the row from the given rule.
	 * 
	 * @param rule
	 */
	public void updateRow(Rule rule) {
		Item item = getItem(rule);
		if (item != null) {
			setItemProperties(rule, item);
		}
	}
}
