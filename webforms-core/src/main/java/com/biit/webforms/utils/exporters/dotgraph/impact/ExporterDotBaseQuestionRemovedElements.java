package com.biit.webforms.utils.exporters.dotgraph.impact;

import com.biit.form.BaseQuestion;
import com.biit.form.TreeObject;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.persistence.entity.Text;
import com.biit.webforms.utils.exporters.dotgraph.ExporterDotBaseQuestion;

public class ExporterDotBaseQuestionRemovedElements extends ExporterDotBaseQuestion {

	private BaseQuestion newVersion;

	ExporterDotBaseQuestionRemovedElements(TreeObject newVersion) {
		if (newVersion != null && newVersion instanceof BaseQuestion) {
			this.newVersion = (BaseQuestion) newVersion;
		}
	}

	public String generateDotNodeList(BaseQuestion baseQuestion) {
		if (newVersion != null
				&& ((newVersion instanceof Question && baseQuestion instanceof Question)
						|| (newVersion instanceof Text && baseQuestion instanceof Text)
						|| (newVersion instanceof SystemField && baseQuestion instanceof SystemField))) {
			setFillColor(DEFAULT_FILL_COLOR);
			setShapeColor(DEFAULT_SHAPE_COLOR);
			setFontColor(DEFAULT_FONT_COLOR);
		} else {
			setFillColor(DELETED_FILL_COLOR);
			setShapeColor(DELETED_SHAPE_COLOR);
			setFontColor(DELETED_FONT_COLOR);
		}
		return super.generateDotNodeList(baseQuestion);
	}

}
