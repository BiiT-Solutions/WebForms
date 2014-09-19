package com.biit.webforms.language;

import com.biit.webforms.gui.common.language.ILanguageCode;
import com.biit.webforms.persistence.entity.enumerations.RuleType;

public enum RuleTypeUi {
	
	NORMAL(RuleType.NORMAL, LanguageCodes.CAPTION_RULE_TYPE_NORMAL),

	END_LOOP(RuleType.END_LOOP, LanguageCodes.CAPTION_RULE_TYPE_END_LOOP),

	END_FORM(RuleType.END_FORM, LanguageCodes.CAPTION_RULE_TYPE_END_FORM), ;

	private RuleType type;
	private ILanguageCode languageCode;
	
	private RuleTypeUi(RuleType type, ILanguageCode languageCode) {
		this.type = type;
		this.languageCode= languageCode;
	}

	public RuleType getType() {
		return type;
	}

	public ILanguageCode getLanguageCode() {
		return languageCode;
	}

	public static String getTranslation(RuleType ruleType) {
		for(RuleTypeUi type: values()){
			if(type.type.equals(ruleType)){
				return type.languageCode.translation();
			}
		}
		return new String("No ui translation for RuleType");
	}
}
