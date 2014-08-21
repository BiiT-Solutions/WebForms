package com.biit.webforms.gui.webpages;

import java.util.List;

import com.biit.liferay.security.IActivity;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.common.components.SecuredWebPage;
import com.biit.webforms.gui.common.components.UpperMenu;
import com.biit.webforms.gui.webpages.floweditor.UpperMenuFlowEditor;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class FlowEditor extends SecuredWebPage {
	private static final long serialVersionUID = -6257723403353946354L;

	private UpperMenu upperMenu;

	@Override
	protected void initContent() {
		setAsCentralPanel();
		upperMenu = createUpperMenu();
		setUpperMenu(upperMenu);
		// TODO terminar
	}

	@Override
	public List<IActivity> accessAuthorizationsRequired() {
		// TODO Auto-generated method stub
		return null;
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
		upperMenu.addDesignButtonListener(new ClickListener() {
			private static final long serialVersionUID = -3161251470765214230L;

			@Override
			public void buttonClick(ClickEvent event) {
				ApplicationUi.navigateTo(WebMap.DESIGNER_EDITOR);
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
