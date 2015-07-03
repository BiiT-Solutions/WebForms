package com.biit.webforms.utils.conversor;

import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.webforms.logger.WebformsLogger;

/**
 * Conversor from Abcd categories to Webforms Categories.
 * 
 * A category has base question and groups as childs
 *
 */
public class ConversorAbcdCategoryToCategory extends
		ConversorTreeObject<Category, com.biit.webforms.persistence.entity.Category> {

	private ConversorAbcdGroupToGroup conversorGroup = new ConversorAbcdGroupToGroup();
	private ConversorAbcdQuestionToQuestion conversorQuestion = new ConversorAbcdQuestionToQuestion();

	@Override
	public com.biit.webforms.persistence.entity.Category createDestinyInstance() {
		return new com.biit.webforms.persistence.entity.Category();
	}

	@Override
	public void copyData(Category origin, com.biit.webforms.persistence.entity.Category destiny) {
		// Copy base data
		super.copyData(origin, destiny);

		// Create copy of the childs and assign.
		for (TreeObject child : origin.getChildren()) {
			if (!(child instanceof Group || child instanceof Question)) {
				WebformsLogger.errorMessage(this.getClass().getName(), new Throwable("Child type not expected"));
				continue;
			}
			TreeObject convertedChild = null;
			if (child instanceof Group) {
				convertedChild = conversorGroup.convert((Group) child);
			}
			if (child instanceof Question) {
				convertedChild = conversorQuestion.convert((Question) child);
			}
			try {
				destiny.addChild(convertedChild);
			} catch (NotValidChildException | ElementIsReadOnly e) {
				// Impossible
				WebformsLogger.errorMessage(this.getClass().getName(), e);
			}
		}
	}

}
