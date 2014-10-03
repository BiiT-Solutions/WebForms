package com.biit.webforms.language;

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
