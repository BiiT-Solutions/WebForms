package com.biit.webforms.gui.webpages;

import java.util.List;

import com.biit.liferay.security.IActivity;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.common.components.SecuredWebPage;
import com.biit.webforms.gui.common.components.TreeObjectTable;
import com.biit.webforms.gui.common.components.UpperMenu;
import com.biit.webforms.gui.webpages.designeditor.UpperMenuDesigner;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class DesignEditor extends SecuredWebPage {
	private static final long serialVersionUID = 9161313025929535348L;

	private UpperMenu upperMenu;
	private TreeObjectTable table;

	@Override
	protected void initContent() {
		setAsCentralPanel();
		upperMenu = createUpperMenu();
		setUpperMenu(upperMenu);
		
		table = new TreeObjectTable();
		table.addRow(UserSessionHandler.getController().getFormInUse(), null);
		table.setSizeFull();
		
		HorizontalLayout rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setSpacing(true);
		rootLayout.setMargin(true);

		rootLayout.addComponent(table);
		rootLayout.setExpandRatio(table, 0.75f);

		getWorkingArea().addComponent(rootLayout);
		// TODO terminar
	}

	@Override
	public List<IActivity> accessAuthorizationsRequired() {
		// TODO Auto-generated method stub
		return null;
	}

	private UpperMenu createUpperMenu() {
		UpperMenuDesigner upperMenu = new UpperMenuDesigner();
		upperMenu.addSaveButtonListener(new ClickListener() {
			private static final long serialVersionUID = 1679355377155929573L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

			}
		});
		upperMenu.addFlowButtonListener(new ClickListener() {
			private static final long serialVersionUID = -3161251470765214230L;

			@Override
			public void buttonClick(ClickEvent event) {
				ApplicationUi.navigateTo(WebMap.FLOW_EDITOR);
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

}
