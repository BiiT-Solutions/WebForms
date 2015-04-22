package com.biit.webforms.utils.exporters.json;

import com.biit.form.entity.BaseAnswer;
import com.biit.form.entity.BaseCategory;
import com.biit.form.entity.BaseForm;
import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.BaseRepeatableGroup;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;

public class BaseFormToJsonExporter {

	public static String toJson(Form form) throws NotValidStorableObjectException, NotValidChildException,
			ElementIsReadOnly, FieldTooLongException, CharacterNotAllowedException {
		return webformToBaseForm(form).toJson();
	}

	private static BaseForm webformToBaseForm(Form form) throws NotValidStorableObjectException,
			NotValidChildException, ElementIsReadOnly {
		BaseForm baseForm = new com.biit.form.implementation.Form();
		baseForm.copyData(form);

		for (TreeObject child : form.getChildren()) {
			createBaseTreeObject(child, baseForm);
		}
		return baseForm;
	}

	private static void createBaseTreeObject(TreeObject treeObject, TreeObject baseParent)
			throws NotValidStorableObjectException, NotValidChildException, ElementIsReadOnly {

		TreeObject baseChild = null;
		if (treeObject instanceof Category) {
			baseChild = new com.biit.form.implementation.Category();
			((BaseCategory) baseChild).copyData(treeObject);
			
		} else if (treeObject instanceof Group) {
			baseChild = new com.biit.form.implementation.RepeatableGroup();
			((BaseRepeatableGroup) baseChild).copyData(treeObject);
			
		} else if (treeObject instanceof Question) {
			baseChild = new com.biit.form.implementation.Question();
			((BaseQuestion) baseChild).copyData(treeObject);
			
		} else if (treeObject instanceof Answer) {
			baseChild = new com.biit.form.implementation.Answer();
			((BaseAnswer) baseChild).copyData(treeObject);
		}
		if (baseChild != null) {
			baseParent.addChild(baseChild);
			if ((treeObject.getChildren() != null) && !(treeObject.getChildren().isEmpty())) {
				for (TreeObject child : treeObject.getChildren()) {
					createBaseTreeObject(child, baseChild);
				}
			}
		}
	}
}
