package com.biit.webforms.gui.webpages.designer;

import java.util.Objects;

import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Question;
import com.vaadin.data.Validator;

public class ValidatorTablesSameColumnsInEachRow implements Validator {
	private static final long serialVersionUID = -5320539195645056658L;

	private TreeObject treeObject;

	public ValidatorTablesSameColumnsInEachRow(TreeObject treeObject) {
		this.treeObject = treeObject;
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		Boolean isTable = (Boolean) value;
		if (isTable != null && isTable) {
			for (TreeObject group1 : treeObject.getChildren()) {
				for (TreeObject group2 : treeObject.getChildren()) {
					// Different groups
					if (!Objects.equals(group1.getComparationId(), group2.getComparationId())) {
						if (group1.getChildren().size() != group2.getChildren().size()) {
							throw new InvalidValueException(LanguageCodes.CAPTION_VALIDATE_TABLE_DIFFERENT_NUMBER_OF_COLUMNS.translation());
						}
						for (int i = 0; i < group1.getChildren().size(); i++) {
							try {
								if (group1.getChild(i) instanceof Question && group2.getChild(i) instanceof Question) {
									if (!compare((Question) group1.getChild(i), (Question) group2.getChild(i))) {
										throw new InvalidValueException(LanguageCodes.CAPTION_VALIDATE_TABLE_DIFFERENT_COLUMNS.translation());
									}
								} else {
									// Already exists another validator for
									// this.
								}
							} catch (ChildrenNotFoundException e) {
								WebformsUiLogger.errorMessage(this.getClass().getName(), e);
							}
						}
					}
				}
			}
		}
	}

	private boolean compare(Question object1, Question object2) {
		return Objects.equals(object1.getName(), object2.getName()) && Objects.equals(object1.isMandatory(), object2.isMandatory())
				&& Objects.equals(object1.getAnswerType(), object2.getAnswerType()) && Objects.equals(object1.getAnswerFormat(), object2.getAnswerFormat())
				&& Objects.equals(object1.getAnswerSubformat(), object2.getAnswerSubformat())
				&& Objects.equals(object1.getDefaultValue(), object2.getDefaultValue());
	}
}