package com.biit.webforms.gui.webpages.designer;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (GUI)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.form.entity.BaseForm;
import com.biit.form.entity.TreeObject;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.language.ILanguageCode;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;

import java.util.Set;

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
				if(getSelectedBlock()==null){
					getAcceptButton().setEnabled(false);
				}else{				
					if (selectableElements != null) {
						for (Class<? extends TreeObject> allowedClass : selectableElements) {
							if (allowedClass.isInstance(getSelectedBlock())) {
								getAcceptButton().setEnabled(true);
							} else {
								getAcceptButton().setEnabled(false);
							}
						}
					}else{
						getAcceptButton().setEnabled(true);
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
