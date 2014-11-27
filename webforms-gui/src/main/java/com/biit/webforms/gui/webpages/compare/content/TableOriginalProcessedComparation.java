package com.biit.webforms.gui.webpages.compare.content;

import com.biit.webforms.gui.common.theme.CommonThemeIcon;
import com.biit.webforms.gui.common.utils.UploadedFile;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.theme.ThemeIcons;
import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import com.vaadin.ui.Table;

public class TableOriginalProcessedComparation extends Table {
	private static final long serialVersionUID = -6481950780301383292L;
	private static final int RESULT_WIDTH = 140;

	enum Properties {
		ORIGINAL_FILENAME, ORIGINAL_FILE, PROCESSED_FILENAME, PROCESSED_FILE, STATUS_ICON, RESULT_MESSAGE,
	};

	public TableOriginalProcessedComparation() {
		super();
		initContainerProperties();
	}

	protected void initContainerProperties() {
		addContainerProperty(Properties.ORIGINAL_FILENAME, String.class, null,
				LanguageCodes.CAPTION_NAME.translation(), null, Align.LEFT);
		addContainerProperty(Properties.ORIGINAL_FILE, UploadedFile.class, null, "", null, Align.LEFT);

		addContainerProperty(Properties.PROCESSED_FILENAME, String.class, null,
				LanguageCodes.CAPTION_NAME.translation(), null, Align.LEFT);
		addContainerProperty(Properties.PROCESSED_FILE, UploadedFile.class, null, "", null, Align.LEFT);

		addContainerProperty(Properties.STATUS_ICON, Component.class, null,
				LanguageCodes.CAPTION_COMPARATION.translation(), null, Align.CENTER);
		addContainerProperty(Properties.RESULT_MESSAGE, String.class, null, LanguageCodes.CAPTION_LABEL.translation(),
				null, Align.CENTER);

		setVisibleColumns(new Object[] { Properties.ORIGINAL_FILENAME, Properties.PROCESSED_FILENAME,
				Properties.STATUS_ICON });

		setColumnWidth(Properties.STATUS_ICON, RESULT_WIDTH);
	}

	public Object getItemIdWithOriginalFilename(String filename) {
		for (Object itemId : getItemIds()) {
			Object value = getItem(itemId).getItemProperty(Properties.ORIGINAL_FILENAME).getValue();
			if (value != null && ((String) value).equals(filename)) {
				return itemId;
			}
		}
		return null;
	}

	public Object getItemIdWithProcessedFilename(String filename) {
		for (Object itemId : getItemIds()) {
			Object value = getItem(itemId).getItemProperty(Properties.PROCESSED_FILENAME).getValue();
			if (value != null && ((String) value).equals(filename)) {
				return itemId;
			}
		}
		return null;
	}

	public void addRow(UploadedFile originalFile, UploadedFile processedFile) {
		Object itemId = null;
		if (originalFile != null && processedFile ==null) {
			itemId = getItemIdWithOriginalFilename(originalFile.getFileName());
		}
		if (itemId==null && originalFile == null && processedFile !=null) {
			itemId = getItemIdWithOriginalFilename(processedFile.getFileName());
		}
		if (itemId == null && originalFile == null && processedFile !=null) {
			itemId = getItemIdWithProcessedFilename(processedFile.getFileName());
		}
		if (itemId == null && originalFile != null && processedFile ==null) {
			itemId = getItemIdWithProcessedFilename(originalFile.getFileName());
		}
		if (itemId == null) {
			itemId = addItem();
		}
		updateRow(itemId, originalFile, processedFile);
	}

	@SuppressWarnings("unchecked")
	public void updateRow(Object itemId, UploadedFile originalFile, UploadedFile processedFile) {
		if (originalFile != null) {
			getItem(itemId).getItemProperty(Properties.ORIGINAL_FILENAME).setValue(originalFile.getFileName());
			getItem(itemId).getItemProperty(Properties.ORIGINAL_FILE).setValue(originalFile);
		}
		if (processedFile != null) {
			getItem(itemId).getItemProperty(Properties.PROCESSED_FILENAME).setValue(processedFile.getFileName());
			getItem(itemId).getItemProperty(Properties.PROCESSED_FILE).setValue(processedFile);
		}
	}

	public boolean isOriginalAndProcessedFileUploaded(Object itemId) {
		if (getItem(itemId) == null) {
			return false;
		} else {
			Item item = getItem(itemId);
			return (item.getItemProperty(Properties.ORIGINAL_FILE).getValue() != null)
					&& (item.getItemProperty(Properties.PROCESSED_FILE).getValue() != null);
		}
	}

	public UploadedFile getOriginalFile(Object itemId) {
		if (getItem(itemId) != null) {
			return (UploadedFile) getItem(itemId).getItemProperty(Properties.ORIGINAL_FILE).getValue();
		}
		return null;
	}

	public UploadedFile getProcessedFile(Object itemId) {
		if (getItem(itemId) != null) {
			return (UploadedFile) getItem(itemId).getItemProperty(Properties.PROCESSED_FILE).getValue();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public void addOk(Object itemId, String message) {
		Item item = getItem(itemId);
		item.getItemProperty(Properties.STATUS_ICON).setValue(getOkIcon());
		item.getItemProperty(Properties.RESULT_MESSAGE).setValue(message);
	}

	@SuppressWarnings("unchecked")
	public void addError(Object itemId, String message) {
		Item item = getItem(itemId);
		item.getItemProperty(Properties.STATUS_ICON).setValue(getErrorIcon());
		item.getItemProperty(Properties.RESULT_MESSAGE).setValue(message);
	}

	private Object getOkIcon() {
		Image ok = new Image(null, CommonThemeIcon.ACCEPT.getThemeResource());
		ok.setHeight("14px");
		ok.setWidth("14px");
		return ok;
	}

	private Object getErrorIcon() {
		Image error = new Image(null, ThemeIcons.ALERT.getThemeResource());
		error.setHeight("14px");
		error.setWidth("14px");
		return error;
	}

	public void removeSelected() {
		if(getValue()!=null){
			removeItem(getValue());
		}
	}

	public String getResult(Object itemId) {
		String result = new String();
		if(getItem(itemId)!=null){
			if(getItem(itemId).getItemProperty(Properties.RESULT_MESSAGE).getValue()!=null){
				result = (String) getItem(itemId).getItemProperty(Properties.RESULT_MESSAGE).getValue();
			}
		}
		return result;
	}
}
