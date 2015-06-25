package com.biit.webforms.utils.exporters.dotgraph.impact;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.persistence.entity.Text;
import com.biit.webforms.utils.exporters.dotgraph.ExporterDotBaseQuestion;

public class ExporterDotBaseQuestionAddedElements extends ExporterDotBaseQuestion {

	private BaseQuestion oldVersion;

	ExporterDotBaseQuestionAddedElements(TreeObject oldVersion) {
		if (oldVersion != null && oldVersion instanceof BaseQuestion) {
			this.oldVersion = (BaseQuestion) oldVersion;
		}
	}

	@Override
	public String generateDotNodeList(BaseQuestion baseQuestion) {
		if (oldVersion == null || (oldVersion.isHiddenElement() && !baseQuestion.isHiddenElement())) {
			setFillColor(NEW_FILL_COLOR);
			setShapeColor(NEW_SHAPE_COLOR);
			setFontColor(NEW_FONT_COLOR);
		} else {
			if ((oldVersion instanceof Question && baseQuestion instanceof Question)
					|| (oldVersion instanceof Text && baseQuestion instanceof Text)
					|| (oldVersion instanceof SystemField && baseQuestion instanceof SystemField)) {
				if (oldVersion.isContentEqual(baseQuestion) 
						&& oldVersion.hasSameChildren(baseQuestion)) {
					setFillColor(DEFAULT_FILL_COLOR);
					setShapeColor(DEFAULT_SHAPE_COLOR);
					setFontColor(DEFAULT_FONT_COLOR);
				} else {
					setFillColor(MODIFIED_FILL_COLOR);
					setShapeColor(MODIFIED_SHAPE_COLOR);
					setFontColor(MODIFIED_FONT_COLOR);
				}
			} else {
				// The type of question has changed, then is new.
				setFillColor(NEW_FILL_COLOR);
				setShapeColor(NEW_SHAPE_COLOR);
				setFontColor(NEW_FONT_COLOR);
			}
		}
		return super.generateDotNodeList(baseQuestion);
	}
}
