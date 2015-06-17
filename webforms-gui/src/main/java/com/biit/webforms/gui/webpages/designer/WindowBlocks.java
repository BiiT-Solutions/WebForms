package com.biit.webforms.gui.webpages.designer;

import java.util.Set;

import com.biit.form.entity.BaseForm;
import com.biit.form.entity.TreeObject;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.language.ILanguageCode;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;

public class WindowBlocks extends WindowAcceptCancel {
	private static final long serialVersionUID = -359502175714054679L;
	private static final String width = "640px";
	private static final String height = "480px";
	private BlockTreeTable blockTable;
	private Set<Class<? extends TreeObject>> selectableElements;

	public WindowBlocks(ILanguageCode code, Set<Class<? extends TreeObject>> selectableElements) {
		super();
		this.selectableElements = selectableElements;
		setCaption(code.translation());
		setContent(generateContent());
		setResizable(false);
		setDraggable(false);
		setClosable(false);
		setModal(true);
		setWidth(width);
		setHeight(height);
	}

	private Component generateContent() {
		blockTable = new BlockTreeTable();
		blockTable.setSizeFull();
		blockTable.setSelectable(true);
		blockTable.collapseFrom(BaseForm.class);

		blockTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 6392652020400436203L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				// Only a type of elements can be selected.
				if (selectableElements != null) {
					for (Class<? extends TreeObject> allowedClass : selectableElements) {
						if (getSelectedBlock() != null && allowedClass.isInstance(getSelectedBlock())) {
							getAcceptButton().setEnabled(true);
						} else {
							getAcceptButton().setEnabled(false);
						}
					}
				}
			}
		});

		return blockTable;
	}

	public TreeObject getSelectedBlock() {
		return blockTable.getSelectedRow();
	}

}
