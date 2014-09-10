package com.biit.webforms.gui.webpages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.biit.liferay.security.IActivity;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.authentication.WebformsActivity;
import com.biit.webforms.gui.common.components.SecuredWebPage;
import com.biit.webforms.gui.common.components.UpperMenu;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.components.FormEditBottomMenu;
import com.biit.webforms.gui.webpages.floweditor.EditItemAction;
import com.biit.webforms.gui.webpages.floweditor.TableRules;
import com.biit.webforms.gui.webpages.floweditor.TableRules.NewItemAction;
import com.biit.webforms.gui.webpages.floweditor.UpperMenuFlowEditor;
import com.biit.webforms.gui.webpages.floweditor.WindowNewRule;
import com.biit.webforms.persistence.entity.Rule;
import com.biit.webforms.theme.ThemeIcons;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Image;

public class FlowEditor extends SecuredWebPage {
	private static final long serialVersionUID = -6257723403353946354L;
	private static final List<IActivity> activityPermissions = new ArrayList<IActivity>(
			Arrays.asList(WebformsActivity.READ));

	private UpperMenu upperMenu;
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
		return tableRules;
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

	private UpperMenu createUpperMenu() {
		UpperMenuFlowEditor upperMenu = new UpperMenuFlowEditor();
		upperMenu.addSaveButtonListener(new ClickListener() {
			private static final long serialVersionUID = 1679355377155929573L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

			}
		});
		upperMenu.addNewRuleButtonListener(new ClickListener() {
			private static final long serialVersionUID = 9116553626932253896L;

			@Override
			public void buttonClick(ClickEvent event) {
				addNewRuleAction();
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
		WindowNewRule window = new WindowNewRule();
		window.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				Rule newRule = ((WindowNewRule) window).getRule();
				UserSessionHandler.getController().addRule(newRule);
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
		WindowNewRule window = new WindowNewRule();
		window.setRule(rule);
		window.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				Rule newRule = ((WindowNewRule) window).getRule();
				UserSessionHandler.getController().addRule(newRule);
				addOrUpdateRuleInTableAction(newRule);
				window.close();
			}
		});
		window.showCentered();
	}

	protected void addOrUpdateRuleInTableAction(Rule newRule) {
		if(tableRules.containsId(newRule)){
			tableRules.updateRow(newRule);
		}else{
			tableRules.addRow(newRule);
		}
	}

}
