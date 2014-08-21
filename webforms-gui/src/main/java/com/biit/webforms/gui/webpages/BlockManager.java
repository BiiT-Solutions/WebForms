package com.biit.webforms.gui.webpages;

import java.util.List;

import com.biit.liferay.security.IActivity;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.gui.common.components.SecuredWebPage;
import com.biit.webforms.gui.common.components.UpperMenu;
import com.biit.webforms.gui.webpages.blockmanager.UpperMenuBlockManager;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class BlockManager extends SecuredWebPage {
	private static final long serialVersionUID = -2939326703361794764L;

	private UpperMenu upperMenu;
	
	@Override
	protected void initContent() {
		UserSessionHandler.getController().clearFormInUse();

		setAsCentralPanel();
		upperMenu = createUpperMenu();
		setUpperMenu(upperMenu);

//		formTable = new TreeTableFormVersion();
//		formTable.setSizeFull();
//		formTable.addEditInfoListener(new EditInfoListener() {
//			@Override
//			public void editInfo(Form form) {
//				openEditInfoWindow(form);
//			}
//		});
//		formTable.selectLastUsedForm();
//
//		getWorkingArea().addComponent(formTable);
	}

	@Override
	public List<IActivity> accessAuthorizationsRequired() {
		// TODO Auto-generated method stub
		return null;
	}

	private UpperMenu createUpperMenu() {
		UpperMenuBlockManager upperMenu = new UpperMenuBlockManager();
		upperMenu.addNewBlockListener(new ClickListener() {
			private static final long serialVersionUID = -6470394743239067429L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		upperMenu.addEditDesignListener(new ClickListener() {
			private static final long serialVersionUID = -3282383387800296295L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		upperMenu.addEditFlowListener(new ClickListener() {
			private static final long serialVersionUID = -7568233796456454868L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		
		return upperMenu;
	}
}
