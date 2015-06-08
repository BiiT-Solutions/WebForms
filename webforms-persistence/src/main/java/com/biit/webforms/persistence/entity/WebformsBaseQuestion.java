package com.biit.webforms.persistence.entity;

import java.util.Objects;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.persistence.entity.exceptions.DependencyDynamicAnswerExistException;
import com.biit.webforms.persistence.entity.exceptions.FlowDependencyExistException;

public abstract class WebformsBaseQuestion extends BaseQuestion {
	private static final long serialVersionUID = 5749191470931873398L;

	public WebformsBaseQuestion() {
		super();
	}

	public WebformsBaseQuestion(String name) throws FieldTooLongException, CharacterNotAllowedException {
		super(name);
	}

	@Override
	public void checkDependencies() throws DependencyExistException, DependencyDynamicAnswerExistException, FlowDependencyExistException {
		Form form = (Form) this.getAncestor(Form.class);
		if (form == null) {
			return;
		}
		
		for(TreeObject child: form.getAllChildrenInHierarchy(DynamicAnswer.class)){
			DynamicAnswer dynamicAnswer = (DynamicAnswer) child;
			if(Objects.equals(dynamicAnswer.getReference(), this)){
				throw new DependencyDynamicAnswerExistException("Question '"+dynamicAnswer.getReference()+"' depends of element + '"+this+"'");
			}
		}

		for (Flow flow : form.getFlows()) {
			if (flow.isDependent(this)) {
				throw new FlowDependencyExistException("Flow '" + flow + "' depends of element '" + this + "'");
			}
		}
	}

}
