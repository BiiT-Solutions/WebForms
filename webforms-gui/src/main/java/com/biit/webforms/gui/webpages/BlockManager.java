package com.biit.webforms.gui.webpages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.biit.abcd.core.SpringContextHelper;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.usermanager.security.IActivity;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.UserSession;
import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.gui.common.components.SecuredWebPage;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.common.components.WindowProceedAction;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.components.FormEditBottomMenu;
import com.biit.webforms.gui.components.FormEditBottomMenu.LockFormListener;
import com.biit.webforms.gui.components.WindowNameGroup;
import com.biit.webforms.gui.exceptions.FormWithSameNameException;
import com.biit.webforms.gui.webpages.blockmanager.TableBlock;
import com.biit.webforms.gui.webpages.blockmanager.UpperMenuBlockManager;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.dao.IBlockDao;
import com.biit.webforms.persistence.entity.Block;
import com.biit.webforms.persistence.entity.IWebformsBlockView;
import com.biit.webforms.persistence.entity.SimpleBlockView;
import com.biit.webforms.security.WebformsActivity;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class BlockManager extends SecuredWebPage {
	private static final long serialVersionUID = -2939326703361794764L;
	private static final List<IActivity> activityPermissions = new ArrayList<IActivity>(Arrays.asList(WebformsActivity.READ));

	private UpperMenuBlockManager upperMenu;
	private FormEditBottomMenu bottomMenu;
	private TableBlock blockTable;

	private IBlockDao blockDao;

	public BlockManager() {
		super();
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		blockDao = (IBlockDao) helper.getBean("blockDao");
	}

	@Override
	protected void initContent() {
		ApplicationUi.getController().clearFormInUse();

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
		blockTable.selectLastUsedBlock();
	}

	private void updateMenus() {
		try {
			IWebformsBlockView block = getSelectedBlock();

			boolean blockNotNull = block != null;
			boolean canCreateBlocks = getWebformsSecurityService().isUserAuthorizedInAnyOrganization(UserSession.getUser(),
					WebformsActivity.BUILDING_BLOCK_EDITING);

			upperMenu.getNewBlock().setEnabled(canCreateBlocks);

			upperMenu.getRemoveBlock().setEnabled(
					blockNotNull
							&& getWebformsSecurityService().isAuthorizedActivity(UserSession.getUser(), block,
									WebformsActivity.BLOCK_REMOVE));

			// Bottom menu
			bottomMenu.getEditFormButton().setEnabled(blockNotNull);
			bottomMenu.getEditFlowButton().setEnabled(blockNotNull);
			bottomMenu.getEditWebserviceCall().setEnabled(blockNotNull);
			bottomMenu.getValidateForm().setEnabled(blockNotNull);

		} catch (IOException | AuthenticationRequired e) {
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
			MessageManager.showError(LanguageCodes.COMMON_ERROR_UNEXPECTED_ERROR);
			// failsafe, disable everything.
			upperMenu.getNewBlock().setEnabled(false);

			// Bottom menu
			bottomMenu.getEditFormButton().setEnabled(false);
			bottomMenu.getEditFlowButton().setEnabled(false);
			bottomMenu.getEditWebserviceCall().setEnabled(false);
			bottomMenu.getValidateForm().setEnabled(false);
		}
	}

	private FormEditBottomMenu createBottomMenu() {
		FormEditBottomMenu bottomMenu = new FormEditBottomMenu();
		bottomMenu.addLockFormListener(new LockFormListener() {

			@Override
			public void lockForm() {
				ApplicationUi.getController().setFormInUse(loadBlock(getSelectedBlock()));
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
		upperMenu.addRemoveBlock(new ClickListener() {
			private static final long serialVersionUID = -3264661636078442579L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (blockTable.getValue() != null) {
					new WindowProceedAction(LanguageCodes.WARNING_REMOVE_ELEMENT, new AcceptActionListener() {
						@Override
						public void acceptAction(WindowAcceptCancel window) {
							removeSelectedBlock();
							window.close();
						}
					});
				}
			}
		});
		return upperMenu;
	}

	public void removeSelectedBlock() {
		Block selectedBlock;
		try {
			selectedBlock = blockDao.get(((IWebformsBlockView) blockTable.getValue()).getId());
			if (selectedBlock != null) {
				// Remove the form.
				blockDao.makeTransient(selectedBlock);
				WebformsUiLogger.info(this.getClass().getName(), "User '" + UserSession.getUser().getEmailAddress()
						+ "' has removed form '" + selectedBlock.getLabel() + "' (version " + selectedBlock.getVersion() + ").");
				blockTable.refreshTableData();
			}
		} catch (ElementCannotBeRemovedException e) {
			MessageManager.showError(LanguageCodes.ERROR_ELEMENT_CANNOT_BE_REMOVED_TITLE,
					LanguageCodes.ERROR_ELEMENT_CANNOT_BE_REMOVED_LINKED_BLOCK_DESCRIPTION);
		}
	}

	protected IWebformsBlockView getSelectedBlock() {
		return ((IWebformsBlockView) blockTable.getValue());
	}

	private Block loadBlock(IWebformsBlockView blockView) {
		if (blockView != null) {
			Block block = ApplicationUi.getController().loadBlock(blockView);
			block.setLastVersion(blockView.isLastVersion());
			return block;
		}
		return null;
	}

	private void openNewBlockWindow() {
		final WindowNameGroup newBlockWindow = new WindowNameGroup(LanguageCodes.COMMON_CAPTION_NAME.translation(),
				LanguageCodes.COMMON_CAPTION_GROUP.translation(), new IActivity[] { WebformsActivity.BUILDING_BLOCK_EDITING });
		newBlockWindow.setCaption(LanguageCodes.CAPTION_NEW_BLOCK.translation());
		newBlockWindow.setDefaultValue(LanguageCodes.NULL_VALUE_NEW_BLOCK.translation());
		newBlockWindow.showCentered();
		newBlockWindow.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				if (newBlockWindow.getValue() == null || newBlockWindow.getValue().isEmpty()) {
					MessageManager.showError(LanguageCodes.COMMON_WARNING_TITLE_BLOCK_NOT_CREATED,
							LanguageCodes.COMMON_WARNING_DESCRIPTION_BLOCK_NEEDS_NAME);
					return;
				}
				try {
					if (newBlockWindow.getOrganization() != null) {
						Block newBlock = ApplicationUi.getController().createBlock(newBlockWindow.getValue(),
								newBlockWindow.getOrganization().getId());
						addBlockToTable(newBlock);
						newBlockWindow.close();
					}
				} catch (FieldTooLongException e) {
					MessageManager.showError(LanguageCodes.COMMON_ERROR_FIELD_TOO_LONG);
				} catch (FormWithSameNameException e) {
					MessageManager.showError(LanguageCodes.COMMON_ERROR_NAME_IS_IN_USE);
				} catch (CharacterNotAllowedException e) {
					// Impossible
					WebformsUiLogger.errorMessage(this.getClass().getName(), e);
				} catch (UnexpectedDatabaseException | ElementCannotBePersistedException e) {
					MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE, LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
					WebformsUiLogger.errorMessage(this.getClass().getName(), e);
				}
			}
		});
	}

	private void addBlockToTable(Block newBlock) {
		SimpleBlockView simpleBlock = SimpleBlockView.getSimpleBlockView(newBlock);
		blockTable.addRow(simpleBlock);
		blockTable.defaultSort();
		blockTable.setValue(simpleBlock);
	}
}
