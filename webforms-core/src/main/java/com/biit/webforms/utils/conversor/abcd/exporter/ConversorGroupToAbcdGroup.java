package com.biit.webforms.utils.conversor.abcd.exporter;

import com.biit.abcd.persistence.entity.Group;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.utils.conversor.abcd.importer.ConversorTreeObject;

/**
 * Conversor from abcd groups to webforms groups.
 * 
 * A group only has base question elements.
 *
 */
public class ConversorGroupToAbcdGroup extends ConversorTreeObject<com.biit.webforms.persistence.entity.Group, Group> {

	private ConversorQuestionToAbcdQuestion conversorQuestion = new ConversorQuestionToAbcdQuestion();

	@Override
	public Group createDestinyInstance() {
		return new Group();
	}

	@Override
	public void copyData(com.biit.webforms.persistence.entity.Group origin, Group destiny) {
		// Copy base data
		super.copyData(origin, destiny);

		// Copy repeatable info
		destiny.setRepeatable(origin.isRepeatable());

		for (TreeObject child : origin.getChildren()) {
			if (!(child instanceof com.biit.webforms.persistence.entity.Group || child instanceof com.biit.webforms.persistence.entity.Question)) {
				WebformsLogger.errorMessage(this.getClass().getName(), new Throwable("Child type not expected"));
				continue;
			}
			TreeObject convertedChild = null;
			if (child instanceof com.biit.webforms.persistence.entity.Group) {
				convertedChild = convert((com.biit.webforms.persistence.entity.Group) child);
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
