package com.biit.webforms.gui.webpages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.security.IActivity;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.authentication.FormWithSameNameException;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.authentication.WebformsActivity;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.gui.common.components.SecuredWebPage;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.components.FormEditBottomMenu;
import com.biit.webforms.gui.components.FormEditBottomMenu.LockFormListener;
import com.biit.webforms.gui.components.WindowNameGroup;
import com.biit.webforms.gui.webpages.blockmanager.TableBlock;
import com.biit.webforms.gui.webpages.blockmanager.UpperMenuBlockManager;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Block;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class BlockManager extends SecuredWebPage {
	private static final long serialVersionUID = -2939326703361794764L;
	private static final List<IActivity> activityPermissions = new ArrayList<IActivity>(
			Arrays.asList(WebformsActivity.READ));

	private UpperMenuBlockManager upperMenu;
	private FormEditBottomMenu bottomMenu;
	private TableBlock blockTable;

	@Override
	protected void initContent() {
		UserSessionHandler.getController().clearFormInUse();

		setCentralPanelAsWorkingArea();
		upperMenu = createUpperMenu();
		bottomMenu = createBottomMenu();
		
		setUpperMenu(upperMenu);
		setBottomMenu(bottomMenu);

		blockTable = new TableBlock();
		blockTable.setSizeFull();
		blockTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1091351561402759962L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateMenus();
			}
		});
		// If it was already null
		updateMenus();

		getWorkingArea().addComponent(blockTable);
	}

	protected void updateMenus() {
		try {
			Block block = getSelectedBlock();

			boolean blockNotNull = block != null;
			boolean canCreateBlocks = WebformsAuthorizationService.getInstance().isUserAuthorizedInAnyOrganization(
					UserSessionHandler.getUser(), WebformsActivity.BUILDING_BLOCK_EDITING);

			upperMenu.getNewBlock().setEnabled(canCreateBlocks);

			// Bottom menu
			bottomMenu.getEditFormButton().setEnabled(blockNotNull);
			bottomMenu.getEditFlowButton().setEnabled(blockNotNull);

		} catch (IOException | AuthenticationRequired e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			MessageManager.showError(LanguageCodes.COMMON_ERROR_UNEXPECTED_ERROR);
			// failsafe, disable everything.
			upperMenu.getNewBlock().setEnabled(false);

			// Bottom menu
			bottomMenu.getEditFormButton().setEnabled(false);
			bottomMenu.getEditFlowButton().setEnabled(false);
		}
	}

	private FormEditBottomMenu createBottomMenu() {
		FormEditBottomMenu bottomMenu = new FormEditBottomMenu();
		bottomMenu.addLockFormListener(new LockFormListener() {

			@Override
			public void lockForm() {
				UserSessionHandler.getController().setFormInUse(getSelectedBlock());
			}
		});
		return bottomMenu;
	}

	@Override
	public List<IActivity> accessAuthorizationsRequired() {
		return activityPermissions;
	}

	private UpperMenuBlockManager createUpperMenu() {
		UpperMenuBlockManager upperMenu = new UpperMenuBlockManager();
		upperMenu.addNewBlockListener(new ClickListener() {
			private static final long serialVersionUID = -6470394743239067429L;

			@Override
			public void buttonClick(ClickEvent event) {
				openNewBlockWindow();
			}
		});
		return upperMenu;
	}

	protected Block getSelectedBlock() {
		return (Block) blockTable.getValue();
	}

	protected void openNewBlockWindow() {
		final WindowNameGroup newBlockWindow = new WindowNameGroup(LanguageCodes.COMMON_CAPTION_NAME.translation(),
				LanguageCodes.COMMON_CAPTION_GROUP.translation(),
				new IActivity[] { WebformsActivity.BUILDING_BLOCK_EDITING });
		newBlockWindow.setCaption(LanguageCodes.CAPTION_NEW_BLOCK.translation());
		newBlockWindow.setDefaultValue(LanguageCodes.NULL_VALUE_NEW_BLOCK.translation());
		newBlockWindow.showCentered();
		newBlockWindow.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				if (newBlockWindow.getValue() == null || newBlockWindow.getValue().isEmpty()) {
					MessageManager.showWarning(LanguageCodes.COMMON_WARNING_TITLE_BLOCK_NOT_CREATED,
							LanguageCodes.COMMON_WARNING_DESCRIPTION_BLOCK_NEEDS_NAME);
					return;
				}
				try {
					Block newBlock = UserSessionHandler.getController().createBlock(newBlockWindow.getValue(),
							newBlockWindow.getOrganization());
					addBlockToTable(newBlock);
					newBlockWindow.close();
				} catch (FieldTooLongException e) {
					MessageManager.showError(LanguageCodes.COMMON_ERROR_FIELD_TOO_LONG);
				} catch (FormWithSameNameException e) {
					MessageManager.showError(LanguageCodes.COMMON_ERROR_NAME_IS_IN_USE);
				}
			}
		});
	}

	protected void addBlockToTable(Block newBlock) {
		blockTable.addRow(newBlock);
		blockTable.setValue(newBlock);
	}
}
