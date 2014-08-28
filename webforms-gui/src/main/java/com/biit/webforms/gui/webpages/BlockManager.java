package com.biit.webforms.gui.webpages;

import java.util.List;

import com.biit.liferay.security.IActivity;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.authentication.FormWithSameNameException;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.common.components.SecuredWebPage;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.common.components.WindowStringInput;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.webpages.blockmanager.TableBlock;
import com.biit.webforms.gui.webpages.blockmanager.UpperMenuBlockManager;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Block;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class BlockManager extends SecuredWebPage {
	private static final long serialVersionUID = -2939326703361794764L;

	private UpperMenuBlockManager upperMenu;
	private TableBlock blockTable;

	@Override
	protected void initContent() {
		UserSessionHandler.getController().clearFormInUse();

		setAsCentralPanel();
		upperMenu = createUpperMenu();
		setUpperMenu(upperMenu);

		blockTable = new TableBlock();
		blockTable.setSizeFull();
		blockTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1091351561402759962L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateUpperMenu();
			}
		});
		// If it was already null
		updateUpperMenu();

		getWorkingArea().addComponent(blockTable);
	}

	protected void updateUpperMenu() {
		Block block = getSelectedBlock();
		if (block == null) {
			upperMenu.getEditDesign().setEnabled(false);
			upperMenu.getEditFlow().setEnabled(false);
		} else {
			upperMenu.getEditDesign().setEnabled(true);
			upperMenu.getEditFlow().setEnabled(true);
		}
	}

	@Override
	public List<IActivity> accessAuthorizationsRequired() {
		// TODO Auto-generated method stub
		return null;
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
		upperMenu.addEditDesignListener(new ClickListener() {
			private static final long serialVersionUID = -3282383387800296295L;

			@Override
			public void buttonClick(ClickEvent event) {
				UserSessionHandler.getController().setFormInUse(getSelectedBlock());
				ApplicationUi.navigateTo(WebMap.DESIGNER_EDITOR);
			}
		});
		upperMenu.addEditFlowListener(new ClickListener() {
			private static final long serialVersionUID = -7568233796456454868L;

			@Override
			public void buttonClick(ClickEvent event) {
				UserSessionHandler.getController().setFormInUse(getSelectedBlock());
				ApplicationUi.navigateTo(WebMap.FLOW_EDITOR);
			}
		});

		return upperMenu;
	}

	protected Block getSelectedBlock() {
		return (Block) blockTable.getValue();
	}

	protected void openNewBlockWindow() {
		final WindowStringInput stringWindow = new WindowStringInput(LanguageCodes.COMMON_CAPTION_NAME.translation());
		stringWindow.setCaption(LanguageCodes.CAPTION_NEW_BLOCK.translation());
		stringWindow.setDefaultValue(LanguageCodes.NULL_VALUE_NEW_BLOCK.translation());
		stringWindow.showCentered();
		stringWindow.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				if (stringWindow.getValue() == null || stringWindow.getValue().isEmpty()) {
					MessageManager.showWarning(LanguageCodes.COMMON_WARNING_TITLE_BLOCK_NOT_CREATED,
							LanguageCodes.COMMON_WARNING_DESCRIPTION_BLOCK_NEEDS_NAME);
					return;
				}
				try {
					Block newBlock = UserSessionHandler.getController().createBlock(stringWindow.getValue());
					addBlockToTable(newBlock);
					stringWindow.close();
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
