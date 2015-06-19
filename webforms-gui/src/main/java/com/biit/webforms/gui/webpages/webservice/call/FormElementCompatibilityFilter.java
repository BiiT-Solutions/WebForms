package com.biit.webforms.gui.webpages.webservice.call;

import java.util.Objects;

import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.SystemField;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;

public class FormElementCompatibilityFilter implements Filter {
	private static final long serialVersionUID = 6762055574561628797L;

	private AnswerType type;
	private AnswerFormat format;
	private AnswerSubformat subformat;

	public FormElementCompatibilityFilter(AnswerType type, AnswerFormat format, AnswerSubformat subformat) {
		this.type = type;
		this.format = format;
		this.subformat = subformat;
	}

	@Override
	public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
		//Passes filter if its a system field
		if (itemId instanceof SystemField) {
			return true;
		}
		if (itemId instanceof Question) {
			Question question = (Question) itemId;
			//Passes filter if its a question and the type, format and subformat are equal.
			return Objects.equals(question.getAnswerType(), type) && Objects.equals(question.getAnswerFormat(), format)
					&& Objects.equals(question.getAnswerSubformat(), subformat);
		}
		return false;
	}

	@Override
	public boolean appliesToProperty(Object propertyId) {
		return true;
	}

}
