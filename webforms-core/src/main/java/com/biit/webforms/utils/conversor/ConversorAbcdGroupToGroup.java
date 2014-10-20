package com.biit.webforms.utils.conversor;

import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.webforms.logger.WebformsLogger;

public class ConversorAbcdGroupToGroup extends ConversorTreeObject<Group, com.biit.webforms.persistence.entity.Group> {

	private ConversorAbcdQuestionToQuestion conversorQuestion = new ConversorAbcdQuestionToQuestion();

	@Override
	public com.biit.webforms.persistence.entity.Group createDestinyInstance() {
		return new com.biit.webforms.persistence.entity.Group();
	}

	@Override
	public void copyData(Group origin, com.biit.webforms.persistence.entity.Group destiny) {
		// Copy base data
		super.copyData(origin, destiny);

		// Copy repeatable info
		destiny.setRepeatable(origin.isRepeatable());

		for (TreeObject child : origin.getChildren()) {
			if (!(child instanceof Group || child instanceof Question)) {
				WebformsLogger.errorMessage(this.getClass().getName(), new Throwable("Child type not expected"));
				continue;
			}
			TreeObject convertedChild = null;
			if (child instanceof Group) {
				convertedChild = convert((Group) child);
			}
			if (child instanceof Question) {
				convertedChild = conversorQuestion.convert((Question) child);
			}
			try {
				destiny.addChild(convertedChild);
			} catch (NotValidChildException e) {
				// Impossible
				WebformsLogger.errorMessage(this.getClass().getName(), e);
			}
		}
	}

}
