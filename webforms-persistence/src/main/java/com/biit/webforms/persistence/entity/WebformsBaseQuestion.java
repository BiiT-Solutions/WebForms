package com.biit.webforms.persistence.entity;

import com.biit.form.BaseQuestion;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

public abstract class WebformsBaseQuestion extends BaseQuestion {
	private static final long serialVersionUID = 5749191470931873398L;

	public WebformsBaseQuestion() {
		super();
	}

	public WebformsBaseQuestion(String name) throws FieldTooLongException, CharacterNotAllowedException {
		super(name);
	}

	@Override
	public void checkDependencies() throws DependencyExistException {
		Form form = (Form) this.getAncestor(Form.class);
		if (form == null) {
			return;
		}

		for (Flow flow : form.getFlows()) {
			if (flow.isDependent(this)) {
				throw new DependencyExistException("Flow '" + flow + "' depends of element '" + this + "'");
			}
		}
	}

}
