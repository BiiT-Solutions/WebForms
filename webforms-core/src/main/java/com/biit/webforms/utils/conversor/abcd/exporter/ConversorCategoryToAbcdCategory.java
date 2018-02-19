package com.biit.webforms.utils.conversor.abcd.exporter;

import com.biit.abcd.persistence.entity.Category;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.utils.conversor.abcd.importer.ConversorTreeObject;

/**
 * Conversor from Abcd categories to Webforms Categories.
 * 
 * A category has base question and groups as childs
 *
 */
public class ConversorCategoryToAbcdCategory extends ConversorTreeObject<com.biit.webforms.persistence.entity.Category, Category> {

	private ConversorGroupToAbcdGroup conversorGroup = new ConversorGroupToAbcdGroup();
	private ConversorQuestionToAbcdQuestion conversorQuestion = new ConversorQuestionToAbcdQuestion();

	@Override
	public Category createDestinyInstance() {
		return new Category();
	}

	@Override
	public void copyData(com.biit.webforms.persistence.entity.Category origin, Category destiny) {
		// Copy base data
		super.copyData(origin, destiny);

		// Create copy of the childs and assign.
		for (TreeObject child : origin.getChildren()) {
			if (!(child instanceof com.biit.webforms.persistence.entity.Group || child instanceof com.biit.webforms.persistence.entity.Question)) {
				WebformsLogger.errorMessage(this.getClass().getName(), new Throwable("Child type not expected"));
				continue;
			}
			TreeObject convertedChild = null;
			if (child instanceof com.biit.webforms.persistence.entity.Group) {
				convertedChild = conversorGroup.convert((com.biit.webforms.persistence.entity.Group) child);
			}
			if (child instanceof com.biit.webforms.persistence.entity.Question) {
				convertedChild = conversorQuestion.convert((com.biit.webforms.persistence.entity.Question) child);
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
