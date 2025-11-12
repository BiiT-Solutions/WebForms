package com.biit.webforms.language;

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

import com.biit.webforms.enumerations.FormWorkStatus;

public enum FormWorkStatusUi {

	DESIGN(FormWorkStatus.DESIGN, LanguageCodes.CAPTION_FORM_WORK_STATUS_DESIGN),

	FINAL_DESIGN(FormWorkStatus.FINAL_DESIGN, LanguageCodes.CAPTION_FORM_WORK_STATUS_FINAL_DESIGN),

	DEVELOPMENT(FormWorkStatus.DEVELOPMENT, LanguageCodes.CAPTION_FORM_WORK_STATUS_DEVELOPMENT),

	TEST(FormWorkStatus.TEST, LanguageCodes.CAPTION_FORM_WORK_STATUS_TEST),

	PRODUCTION(FormWorkStatus.PRODUCTION, LanguageCodes.CAPTION_FORM_WORK_STATUS_PRODUCTION), ;

	private FormWorkStatus formStatus;
	private LanguageCodes languageCode;

	private FormWorkStatusUi(FormWorkStatus formStatus, LanguageCodes languageCode) {
		this.formStatus = formStatus;
		this.languageCode = languageCode;
	}

	public FormWorkStatus getFormWorkStatus() {
		return formStatus;
	}

	public LanguageCodes getLanguageCode() {
		return languageCode;
	}
}
