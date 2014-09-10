package com.biit.webforms.gui.webpages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.biit.liferay.security.IActivity;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.authentication.WebformsActivity;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.gui.common.components.SecuredWebPage;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.components.FormEditBottomMenu;
import com.biit.webforms.gui.webpages.floweditor.EditItemAction;
import com.biit.webforms.gui.webpages.floweditor.TableRules;
import com.biit.webforms.gui.webpages.floweditor.TableRules.NewItemAction;
import com.biit.webforms.gui.webpages.floweditor.UpperMenuFlowEditor;
import com.biit.webforms.gui.webpages.floweditor.WindowRule;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Rule;
import com.biit.webforms.theme.ThemeIcons;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Image;

public class FlowEditor extends SecuredWebPage {
	private static final long serialVersionUID = -6257723403353946354L;
	private static final List<IActivity> activityPermissions = new ArrayList<IActivity>(
			Arrays.asList(WebformsActivity.READ));

	private UpperMenuFlowEditor upperMenu;
	private TableRules tableRules;

	@Override
	protected void initContent() {
		setCentralPanelAsWorkingArea();
		upperMenu = createUpperMenu();
		setUpperMenu(upperMenu);
		setBottomMenu(new FormEditBottomMenu());

		getWorkingArea().addComponent(generateContent());
	}

	private Component generateContent() {
		HorizontalSplitPanel horizontalSplitPanel = new HorizontalSplitPanel();
		horizontalSplitPanel.setSizeFull();
		horizontalSplitPanel.setFirstComponent(createLeftComponent());
		horizontalSplitPanel.setSecondComponent(createRightComponent());
		horizontalSplitPanel.setSplitPosition(50.0f);

		return horizontalSplitPanel;
	}

	private Component createLeftComponent() {
		tableRules = new TableRules();
		tableRules.setSizeFull();
		tableRules.addNewItemActionListener(new NewItemAction() {

			@Override
			public void newItemAction() {
				addNewRuleAction();
			}
		});
		tableRules.addEditItemActionListener(new EditItemAction() {

			@Override
			public void editItemAction(Rule rule) {
				editRuleAction(rule);
			}
		});
		tableRules.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -4525037694598967266L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateUiState();
			}
		});
		Set<Rule> rules = UserSessionHandler.getController().getFormInUse().getRules();
		tableRules.addRows(rules);
		return tableRules;
	}

	private void updateUiState() {

		boolean selectedRule = (tableRules.getValue() != null) && (tableRules.getValue() instanceof Rule);
		boolean canEdit = WebformsAuthorizationService.getInstance().isFormEditable(
				UserSessionHandler.getController().getFormInUse(), UserSessionHandler.getUser());

		// Top button state
		upperMenu.getSaveButton().setEnabled(canEdit);
		upperMenu.getNewRuleButton().setEnabled(canEdit);
		upperMenu.getEditRuleButton().setEnabled(canEdit && selectedRule);
		upperMenu.getValidateButton().setEnabled(false);
		upperMenu.getFinishButton().setEnabled(canEdit);
	}

	private Component createRightComponent() {
		Image image = new Image(null, ThemeIcons.ALERT.getThemeResource());
		image.setSizeFull();

		return image;
	}

	@Override
	public List<IActivity> accessAuthorizationsRequired() {
		return activityPermissions;
	}

	private UpperMenuFlowEditor createUpperMenu() {
		UpperMenuFlowEditor upperMenu = new UpperMenuFlowEditor();
		upperMenu.addSaveButtonListener(new ClickListener() {
			private static final long serialVersionUID = 1679355377155929573L;

			@Override
			public void buttonClick(ClickEvent event) {
				UserSessionHandler.getController().saveForm();
				MessageManager.showInfo(LanguageCodes.INFO_MESSAGE_CAPTION_SAVE,
						LanguageCodes.INFO_MESSAGE_DESCRIPTION_SAVE);
			}
		});
		upperMenu.addNewRuleButtonListener(new ClickListener() {
			private static final long serialVersionUID = 9116553626932253896L;

			@Override
			public void buttonClick(ClickEvent event) {
				addNewRuleAction();
			}
		});
		upperMenu.addEditRuleButtonListener(new ClickListener() {
			private static final long serialVersionUID = 9116553626932253896L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (tableRules != null) {
					editRuleAction((Rule) tableRules.getValue());
				}
			}
		});
		upperMenu.addValidateButtonListener(new ClickListener() {
			private static final long serialVersionUID = -1627616225877959507L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

			}
		});
		upperMenu.addFinishButtonListener(new ClickListener() {
			private static final long serialVersionUID = 8869180038869702710L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

			}
		});

		return upperMenu;
	}

	/**
	 * This method opens the new rule window
	 */
	protected void addNewRuleAction() {
		WindowRule window = new WindowRule();
		window.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				Rule newRule = ((WindowRule) window).getRule();
				addOrUpdateRuleInTableAction(newRule);
				window.close();
			}
		});
		window.showCentered();
	}

	/**
	 * This method takes a existing rule and opens rule window with the
	 * parameters assigned in the rule to edit.
	 * 
	 * @param rule
	 */
	protected void editRuleAction(Rule rule) {
		WindowRule window = new WindowRule();
		window.setRule(rule);
		window.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				Rule newRule = ((WindowRule) window).getRule();
				addOrUpdateRuleInTableAction(newRule);
				window.close();
			}
		});
		window.showCentered();
	}

	protected void addOrUpdateRuleInTableAction(Rule newRule) {
		if (tableRules.containsId(newRule)) {
			tableRules.updateRow(newRule);
		} else {
			tableRules.addRow(newRule);
		}
	}
}
