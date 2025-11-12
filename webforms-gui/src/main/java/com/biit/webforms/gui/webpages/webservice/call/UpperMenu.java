package com.biit.webforms.gui.webpages.webservice.call;

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

public class UpperMenu extends UpperMenuWebforms {
	private static final long serialVersionUID = 6910390631017964877L;

	private final IconButton saveButton;
	private final IconButton addWebserviceCall;
	private final IconButton removeWebserviceCall;
	private final IconButton editWebserviceLink;
	private final IconButton removeWebserviceLink;
	
	public UpperMenu(){
		saveButton = new IconButton(LanguageCodes.COMMON_CAPTION_SAVE, ThemeIcons.FORM_SAVE,
				LanguageCodes.COMMON_TOOLTIP_SAVE, IconSize.BIG);
		addWebserviceCall = new IconButton(LanguageCodes.CAPTION_ADD_WEBSERVICE_CALL, ThemeIcons.ADD_WEBSERVICE_CALL,
				LanguageCodes.TOOLTIP_ADD_WEBSERVICE_CALL, IconSize.BIG);
		removeWebserviceCall = new IconButton(LanguageCodes.CAPTION_REMOVE_WEBSERVICE_CALL, ThemeIcons.REMOVE_WEBSERVICE_CALL,
				LanguageCodes.TOOLTIP_REMOVE_WEBSERVICE_CALL, IconSize.BIG);
		editWebserviceLink = new IconButton(LanguageCodes.CAPTION_EDIT_WEBSERVICE_LINK, ThemeIcons.EDIT_WEBSERVICE_PORT,
				LanguageCodes.TOOLTIP_EDIT_WEBSERVICE_LINK, IconSize.BIG);
		removeWebserviceLink = new IconButton(LanguageCodes.CAPTION_REMOVE_WEBSERVICE_LINK, ThemeIcons.REMOVE_WEBSERVICE_PORT,
				LanguageCodes.TOOLTIP_REMOVE_WEBSERVICE_LINK, IconSize.BIG);
		
		addIconButton(saveButton);
		addIconButton(addWebserviceCall);
		addIconButton(removeWebserviceCall);
		addIconButton(editWebserviceLink);
		addIconButton(removeWebserviceLink);
	}

	public IconButton getSaveButton() {
		return saveButton;
	}

	public IconButton getAddWebserviceCall() {
		return addWebserviceCall;
	}

	public IconButton getRemoveWebserviceCall() {
		return removeWebserviceCall;
	}

	public IconButton getEditWebserviceLink() {
		return editWebserviceLink;
	}

	public IconButton getRemoveWebserviceLink() {
		return removeWebserviceLink;
	}
	
}
