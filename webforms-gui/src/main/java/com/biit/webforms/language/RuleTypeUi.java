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
