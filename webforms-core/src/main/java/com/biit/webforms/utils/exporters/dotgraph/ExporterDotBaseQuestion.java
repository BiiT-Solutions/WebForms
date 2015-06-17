package com.biit.webforms.utils.exporters.dotgraph;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.persistence.entity.Text;

public class ExporterDotBaseQuestion extends ExporterDot<BaseQuestion> {

	@Override
	public String export(BaseQuestion structure) {
		return null;
	}

	@Override
	public String generateDotNodeList(BaseQuestion baseQuestion) {
		String dotElement = new String();
		if (!baseQuestion.isHiddenElement()) {
			dotElement = "\t\t" + getDotName(baseQuestion) + ";\n";
		}
		return dotElement;
	}

	@Override
	public String generateDotNodeFlow(BaseQuestion structure) {
		return null;
	}

	// TODO translations!
	private String getDotName(BaseQuestion baseQuestion) {
		if (baseQuestion instanceof Question) {
			Question question = (Question) baseQuestion;

			if (question.getAnswerType() == AnswerType.MULTIPLE_SELECTION) {
				return getDotId(question) + " [label=\"" + filterDotLanguage(question.getName()) + " \\n ("
						+ filterDotLanguage(question.getAnswerType().toString()) + ")\", color="
						+ getFontColor(baseQuestion.isReadOnly()) + ", penwidth=" + getPenWidth() + ", fontcolor="
						+ getFontColor(baseQuestion.isReadOnly()) + "]";
			} else {
				return getDotId(question) + " [label=\"" + filterDotLanguage(question.getName()) + "\", color="
						+ getFontColor(baseQuestion.isReadOnly()) + ", penwidth=" + getPenWidth() + ", fontcolor="
						+ getFontColor(baseQuestion.isReadOnly()) + "]";
			}
		}
		if (baseQuestion instanceof SystemField) {
			return getDotId(baseQuestion) + " [label=\"" + filterDotLanguage(baseQuestion.getName()) + " \\n ("
					+ "system-field" + ")\", color=" + getFontColor(baseQuestion.isReadOnly()) + ", penwidth="
					+ getPenWidth() + ", fontcolor=" + getFontColor(baseQuestion.isReadOnly()) + "]";
		}
		if (baseQuestion instanceof Text) {
			return getDotId(baseQuestion) + " [label=\"" + filterDotLanguage(baseQuestion.getName()) + " \\n ("
					+ "info-text" + ")\", color=" + getFontColor(baseQuestion.isReadOnly()) + ", penwidth="
					+ getPenWidth() + ", fontcolor=" + getFontColor(baseQuestion.isReadOnly()) + "]";
		}
		WebformsLogger.severe(this.getClass().getName(), "received not known base question class "
				+ baseQuestion.getClass().getName() + " '" + baseQuestion + "'");
		return new String();
	}

	protected String getDotId(TreeObject treeObject) {
		return filterDotLanguageId("id_" + treeObject.getComparationId());
	}

	@Override
	public String generateDotNodeChilds(BaseQuestion structure) {
		return null;
	}
}
