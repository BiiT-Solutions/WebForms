package com.biit.webforms.exporters.xforms;

import com.biit.form.entity.BaseQuestion;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.persistence.entity.ElementWithImage;
import com.biit.webforms.persistence.entity.TreeObjectImage;

/**
 * Class to hold the information of a validation link.
 *
 */
public class WebserviceValidationField extends BaseQuestion implements ElementWithImage {

	private static final long serialVersionUID = -1264035729915575376L;

	private final BaseQuestion question;

	public WebserviceValidationField(BaseQuestion question) {
		this.question = question;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		// This method is not implemented this class is not intended to be used
		// as a member of the form.
	}

	public BaseQuestion getQuestion() {
		return question;
	}

	@Override
	public String getName() {
		return question.getName() + "-validation";
	}

	@Override
	public void setImage(TreeObjectImage image) {
		// Do nothing
	}

	@Override
	public TreeObjectImage getImage() {
		return null;
	}
}
