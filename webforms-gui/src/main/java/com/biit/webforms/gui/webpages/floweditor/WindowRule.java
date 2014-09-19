package com.biit.webforms.gui.webpages.floweditor;

import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.language.RuleTypeUi;
import com.biit.webforms.persistence.entity.Rule;
import com.biit.webforms.persistence.entity.enumerations.RuleType;
import com.biit.webforms.persistence.entity.exceptions.BadRuleContentException;
import com.biit.webforms.persistence.entity.exceptions.RuleDestinyIsBeforeOrigin;
import com.biit.webforms.persistence.entity.exceptions.RuleSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.RuleWithoutSource;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * Window to generate or edit a flow rule. This gui class can call the update
 * and insert of the rule by itself using the Application Controller.
 * 
 * It contains a "control" bar that permits the selection of origin, type and
 * destiny of rule. This window also has space to write the logic condition.
 * 
 * The window will enable and disable the areas to assign destiny or logic
 * condition depending of the rule type.
 * 
 */
public class WindowRule extends WindowAcceptCancel {
	private static final long serialVersionUID = 5164868235165988674L;
	private static final String width = "800px";
	private static final String height = "600px";
	private static final String BAR_HEIGHT = "50px";

	private SearchFormElementField searchOrigin;
	private SearchFormElementField searchDestiny;
	private ComboBox ruleTypeSelector;
	private ConditionEditor conditionEditor;
	private Rule rule;

	public WindowRule() {
		super();
		setContent(generateContent());
		setResizable(false);
		setDraggable(false);
		setClosable(false);
		setModal(true);
		setWidth(width);
		setHeight(height);
		setRule(new Rule());
	}

	public Component generateContent() {
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);
		rootLayout.setSpacing(true);

		Component barLayout = generateControlBar();
		barLayout.setHeight(BAR_HEIGHT);

		initializeConditionEditor();

		rootLayout.addComponent(barLayout);
		rootLayout.addComponent(conditionEditor);

		rootLayout.setExpandRatio(conditionEditor, 1.0f);

		// Initialize component default values.
		ruleTypeSelector.setValue(RuleType.NORMAL);

		return rootLayout;
	}

	private Component initializeConditionEditor() {
		conditionEditor = new ConditionEditor();

		return conditionEditor;
	}

	private Component generateControlBar() {
		HorizontalLayout barLayout = new HorizontalLayout();
		barLayout.setSizeFull();
		// barLayout.setMargin(true);
		barLayout.setSpacing(true);

		Component selectOriginComponent = generateSearchOriginContent();
		Component selectDestinyComponent = generateSearchDestinyContent();
		Component selectRuleType = generateRuleType();

		barLayout.addComponent(selectOriginComponent);
		barLayout.addComponent(selectRuleType);
		barLayout.addComponent(selectDestinyComponent);

		barLayout.setComponentAlignment(selectOriginComponent, Alignment.BOTTOM_LEFT);
		barLayout.setComponentAlignment(selectRuleType, Alignment.BOTTOM_CENTER);
		barLayout.setComponentAlignment(selectDestinyComponent, Alignment.BOTTOM_RIGHT);
		barLayout.setExpandRatio(selectOriginComponent, 0.0f);
		barLayout.setExpandRatio(selectRuleType, 0.0f);
		barLayout.setExpandRatio(selectDestinyComponent, 0.0f);

		return barLayout;
	}

	private void updateControls() {
		searchOrigin.setTreeObject(rule.getOrigin());
		ruleTypeSelector.setValue(rule.getRuleType());
		searchDestiny.setTreeObject(rule.getDestiny());
		// TODO
		// richTextArea.setValue(rule.getConditionString());
	}

	private Component generateRuleType() {
		ruleTypeSelector = new ComboBox(LanguageCodes.CAPTION_RULE_TYPE.translation());
		ruleTypeSelector.setNullSelectionAllowed(false);
		for (RuleTypeUi ruleType : RuleTypeUi.values()) {
			ruleTypeSelector.addItem(ruleType.getType());
			ruleTypeSelector.setItemCaption(ruleType.getType(), ruleType.getLanguageCode().translation());
		}
		ruleTypeSelector.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 3216484668507720639L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				RuleType type = (RuleType) event.getProperty().getValue();
				if (type.isDestinyNull()) {
					searchDestiny.clear();
					searchDestiny.setEnabled(false);
				} else {
					searchDestiny.setEnabled(true);
				}
				if (type.isOthers()) {
					conditionEditor.clean();
					conditionEditor.setEnabled(false);
				} else {
					conditionEditor.setEnabled(true);
				}
			}
		});
		return ruleTypeSelector;
	}

	private Component generateSearchOriginContent() {
		searchOrigin = new SearchFormElementField();
		searchOrigin.setNullCaption(LanguageCodes.NULL_VALUE_SEARCH_ORIGIN);
		searchOrigin.setCaption(LanguageCodes.CAPTION_FROM.translation());

		return searchOrigin;
	}

	private Component generateSearchDestinyContent() {
		searchDestiny = new SearchFormElementField();
		searchDestiny.setNullCaption(LanguageCodes.NULL_VALUE_SEARCH_DESTINY);
		searchDestiny.setCaption(LanguageCodes.CAPTION_TO.translation());

		return searchDestiny;
	}

	@Override
	protected boolean acceptAction() {
		// Actualize the object.
		String conditions = null;
		if (conditionEditor.isEnabled()) {
			//TODO
			//conditions = richTextArea.getValue();
		}
		try {
			UserSessionHandler.getController().updateRuleContent(rule, searchOrigin.getTreeObject(),
					(RuleType) ruleTypeSelector.getValue(), searchDestiny.getTreeObject(), conditions);
		} catch (BadRuleContentException e) {
			MessageManager.showWarning(LanguageCodes.WARNING_CAPTION_RULE_NOT_CORRECT,
					LanguageCodes.WARNING_DESCRIPTION_DESTINY_IS_NULL);
			return false;
		} catch (RuleWithoutSource e) {
			MessageManager.showWarning(LanguageCodes.WARNING_CAPTION_RULE_NOT_CORRECT,
					LanguageCodes.WARNING_DESCRIPTION_ORIGIN_IS_NULL);
			return false;
		} catch (RuleSameOriginAndDestinyException e) {
			MessageManager.showWarning(LanguageCodes.WARNING_CAPTION_RULE_NOT_CORRECT,
					LanguageCodes.WARNING_DESCRIPTION_SAME_ORIGIN_AND_DESTINY);
			return false;
		} catch (RuleDestinyIsBeforeOrigin e) {
			MessageManager.showWarning(LanguageCodes.WARNING_CAPTION_RULE_NOT_CORRECT,
					LanguageCodes.WARNING_DESCRIPTION_DESTINY_IS_BEFORE_ORIGIN);
			return false;
		}
		return true;
	}

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
		updateControls();
	}
}