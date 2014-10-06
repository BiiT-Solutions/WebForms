package com.biit.webforms.gui.webpages.floweditor;

import java.util.ArrayList;
import java.util.List;

import com.biit.form.TreeObject;
import com.biit.webforms.enumerations.RuleType;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.webpages.floweditor.SearchFormElementField.SearchFormElementChanged;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.language.RuleTypeUi;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.Rule;
import com.biit.webforms.persistence.entity.condition.Token;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
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
	private static final String width = "75%";
	private static final String height = "90%";
	private static final String BAR_HEIGHT = "50px";

	private SearchFormElementField searchOrigin;
	private SearchFormElementField searchDestiny;
	private ComboBox ruleTypeSelector;
	private CheckBox others;
	private ConditionEditor conditionEditor;

	public WindowRule() {
		super();
		setContent(generateContent());
		setResizable(false);
		setDraggable(false);
		setClosable(true);
		setModal(true);
		setWidth(width);
		setHeight(height);
	}

	public Component generateContent() {
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);
		rootLayout.setSpacing(true);

		Component barLayout = generateControlBar();
		barLayout.setHeight(BAR_HEIGHT);
		Component selectOthers = generateOthersComponent();

		generateConditionEditor();

		rootLayout.addComponent(barLayout);
		rootLayout.addComponent(selectOthers);
		rootLayout.addComponent(conditionEditor);

		rootLayout.setExpandRatio(conditionEditor, 1.0f);

		return rootLayout;
	}

	private Component generateConditionEditor() {
		conditionEditor = new ConditionEditor();
		conditionEditor.setSizeFull();

		return conditionEditor;
	}

	private Component generateControlBar() {
		HorizontalLayout barLayout = new HorizontalLayout();
		barLayout.setWidth("100%");
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

		return barLayout;
	}

	private void updateControls(Rule rule) {
		searchOrigin.setTreeObject(rule.getOrigin());
		ruleTypeSelector.setValue(rule.getRuleType());
		searchDestiny.setTreeObject(rule.getDestiny());
		others.setValue(rule.isOthers());
		if (!rule.isOthers()) {
			for (Token token : rule.getCondition()) {
				conditionEditor.addToken(token);
			}
		}
	}

	private Component generateRuleType() {
		ruleTypeSelector = new ComboBox(LanguageCodes.CAPTION_RULE_TYPE.translation());
		ruleTypeSelector.setNullSelectionAllowed(false);
		ruleTypeSelector.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 3216484668507720639L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				RuleType type = (RuleType) event.getProperty().getValue();
				if (type==null || type.isDestinyNull()) {
					searchDestiny.clear();
				}
				updateSearchDestinyEnabledState();
			}
		});
		return ruleTypeSelector;
	}

	protected void updateRuleTypeSelector() { 		
		if(searchOrigin.getValue()==null){
			ruleTypeSelector.setValue(null);
			ruleTypeSelector.removeAllItems();
			ruleTypeSelector.setEnabled(false);
		}else{
			RuleType currentRuleType = (RuleType) ruleTypeSelector.getValue();
			//If the selected ruleType requires repeatable group and current element is not in a repeatable group then, reset the type.
			if(currentRuleType==null || (currentRuleType.isOnlyInRepeatableGroups() &&  !((Question)searchOrigin.getValue()).isInRepeatableGroup())){
				//Reset currentRuleType
				currentRuleType = RuleType.getDefaultRuleType();
			}
			
			ruleTypeSelector.removeAllItems();
			for (RuleTypeUi ruleType : RuleTypeUi.values()) {
				if (ruleType.getType().isOnlyInRepeatableGroups() && (searchOrigin.getValue()==null|| !((Question)searchOrigin.getValue()).isInRepeatableGroup())) {
					continue;
				}
				ruleTypeSelector.addItem(ruleType.getType());
				ruleTypeSelector.setItemCaption(ruleType.getType(), ruleType.getLanguageCode().translation());
			}
			ruleTypeSelector.setValue(currentRuleType);
			ruleTypeSelector.setEnabled(true);
		}
	}

	protected void updateSearchDestinyEnabledState() {
		if (searchOrigin.getValue() == null || ruleTypeSelector.getValue() == null
				|| ((RuleType) ruleTypeSelector.getValue()).isDestinyNull()) {
			searchDestiny.setEnabled(false);
		} else {
			searchDestiny.setEnabled(true);
		}
	}

	private Component generateSearchOriginContent() {
		searchOrigin = new SearchFormElementField(Form.class, Category.class, Group.class, Question.class);
		searchOrigin.setSelectableFilter(Question.class);
		searchOrigin.setNullCaption(LanguageCodes.NULL_VALUE_SEARCH_ORIGIN);
		searchOrigin.setCaption(LanguageCodes.CAPTION_FROM.translation());
		searchOrigin.addValueChangeListener(new SearchFormElementChanged() {

			@Override
			public void currentElement(Object object) {
				if (object != null) {
					updateSearchDestiny();
					updateConditionTreeObjectSearch((TreeObject) object);
				} else {
					searchDestiny.clear();
				}
				updateRuleTypeSelector();
				updateSearchDestinyEnabledState();
			}
		});

		return searchOrigin;
	}

	protected void updateConditionTreeObjectSearch(TreeObject treeObject) {
		conditionEditor.selectReferenceElement(treeObject);
		// TODO filter
	}

	protected void updateSearchDestiny() {
		// TODO Auto-generated method stub

	}

	private Component generateSearchDestinyContent() {
		searchDestiny = new SearchFormElementField(Form.class, Category.class, Group.class, Question.class);
		searchDestiny.setSelectableFilter(Question.class);
		searchDestiny.setNullCaption(LanguageCodes.NULL_VALUE_SEARCH_DESTINY);
		searchDestiny.setCaption(LanguageCodes.CAPTION_TO.translation());
		searchDestiny.setEnabled(false);

		return searchDestiny;
	}

	private Component generateOthersComponent() {
		others = new CheckBox(LanguageCodes.CAPTION_OTHERS.translation());
		others.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1241982496892267519L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (others.getValue()) {
					conditionEditor.setEnabled(false);
					conditionEditor.clean();
				} else {
					conditionEditor.setEnabled(true);
				}
			}
		});
		return others;
	}

	public void setRule(Rule rule) {
		updateControls(rule);
	}

	public TreeObject getOrigin() {
		return searchOrigin.getTreeObject();
	}

	public TreeObject getDestiny() {
		return searchDestiny.getTreeObject();
	}

	public RuleType getRuleType() {
		return (RuleType) ruleTypeSelector.getValue();
	}

	public List<Token> getCondition() {
		if (conditionEditor.isEnabled()) {
			return conditionEditor.getTokens();
		}
		return new ArrayList<>();
	}

	public boolean isOthers() {
		return others.getValue();
	}

	public boolean isConditionValid() {
		return conditionEditor.isConditionValid();
	}
}