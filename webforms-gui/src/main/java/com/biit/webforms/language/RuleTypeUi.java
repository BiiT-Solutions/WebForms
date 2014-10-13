package com.biit.webforms.language;

import com.biit.webforms.enumerations.FlowType;
import com.biit.webforms.gui.common.language.ILanguageCode;

public enum RuleTypeUi {
	
	NORMAL(FlowType.NORMAL, LanguageCodes.CAPTION_RULE_TYPE_NORMAL),

	END_LOOP(FlowType.END_LOOP, LanguageCodes.CAPTION_RULE_TYPE_END_LOOP),

	END_FORM(FlowType.END_FORM, LanguageCodes.CAPTION_RULE_TYPE_END_FORM), ;

	private FlowType type;
	private ILanguageCode languageCode;
	
	private RuleTypeUi(FlowType type, ILanguageCode languageCode) {
		this.type = type;
		this.languageCode= languageCode;
	}

	public FlowType getType() {
		return type;
	}

	public ILanguageCode getLanguageCode() {
		return languageCode;
	}

	public static String getTranslation(FlowType ruleType) {
		for(RuleTypeUi type: values()){
			if(type.type.equals(ruleType)){
				return type.languageCode.translation();
			}
		}
		return new String("No ui translation for RuleType");
	}
}
