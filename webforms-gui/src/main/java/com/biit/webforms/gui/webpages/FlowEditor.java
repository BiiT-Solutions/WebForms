package com.biit.webforms.gui.webpages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.biit.liferay.security.IActivity;
import com.biit.webforms.authentication.WebformsActivity;
import com.biit.webforms.gui.common.components.SecuredWebPage;
import com.biit.webforms.gui.common.components.UpperMenu;
import com.biit.webforms.gui.components.FormEditBottomMenu;
import com.biit.webforms.gui.webpages.floweditor.UpperMenuFlowEditor;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class FlowEditor extends SecuredWebPage {
	private static final long serialVersionUID = -6257723403353946354L;
	private static final List<IActivity> activityPermissions = new ArrayList<IActivity>(
			Arrays.asList(WebformsActivity.READ));

	private UpperMenu upperMenu;

	@Override
	protected void initContent() {
		setCentralPanelAsWorkingArea();
		upperMenu = createUpperMenu();
		setUpperMenu(upperMenu);
		setBottomMenu(new FormEditBottomMenu());
		// TODO terminar
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
