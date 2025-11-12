package com.biit.webforms.gui.webpages.compare.structure;

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

import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.IconSize;
import com.biit.webforms.gui.components.UpperMenuWebforms;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.theme.ThemeIcons;
import com.vaadin.ui.Button.ClickListener;

public class UpperMenu extends UpperMenuWebforms{
	private static final long serialVersionUID = 4805380256312666168L;
	
	private IconButton upload;
	private IconButton remove;
	private IconButton removeAll;
	private IconButton validate;
	private IconButton validateAll;
	
	public UpperMenu(){
		upload = new IconButton(LanguageCodes.CAPTION_UPLOAD, ThemeIcons.UPLOAD , LanguageCodes.TOOLTIP_UPLOAD ,IconSize.BIG);
		remove = new IconButton(LanguageCodes.CAPTION_REMOVE, ThemeIcons.REMOVE , LanguageCodes.TOOLTIP_REMOVE ,IconSize.BIG);
		removeAll = new IconButton(LanguageCodes.CAPTION_REMOVE_ALL, ThemeIcons.REMOVE_ALL , LanguageCodes.TOOLTIP_REMOVE_ALL ,IconSize.BIG);
		validate = new IconButton(LanguageCodes.CAPTION_VALIDATE, ThemeIcons.VALIDATE , LanguageCodes.TOOLTIP_VALIDATE ,IconSize.BIG);
		validateAll = new IconButton(LanguageCodes.CAPTION_VALIDATE_ALL, ThemeIcons.VALIDATE_ALL , LanguageCodes.TOOLTIP_VALIDATE_ALL ,IconSize.BIG);
		
		addIconButton(upload);
		addIconButton(remove);
		addIconButton(removeAll);
		addIconButton(validate);
		addIconButton(validateAll);
	}
	
	public void addUploadListener(ClickListener listener){
		upload.addClickListener(listener);
	}
	
	public void addRemoveListener(ClickListener listener){
		remove.addClickListener(listener);
	}
	
	public void addRemoveAllListener(ClickListener listener){
		removeAll.addClickListener(listener);
	}
	
	public void addValidateListener(ClickListener listener){
		validate.addClickListener(listener);
	}
	
	public void addValidateAllListener(ClickListener listener){
		validateAll.addClickListener(listener);
	}
	
	public void setUploadEnabled(Boolean value){
		upload.setEnabled(value);
	}
	
	public void setRemoveEnabled(Boolean value){
		remove.setEnabled(value);
	}
	
	public void setRemoveAllEnabled(Boolean value){
		removeAll.setEnabled(value);
	}
	
	public void setValidateEnabled(Boolean value){
		validate.setEnabled(value);
	}
	
	public void setValidateAllEnabled(Boolean value){
		validateAll.setEnabled(value);
	}
}
