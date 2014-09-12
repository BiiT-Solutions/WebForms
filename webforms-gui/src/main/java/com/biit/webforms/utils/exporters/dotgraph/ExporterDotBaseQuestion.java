package com.biit.webforms.utils.exporters.dotgraph;

import com.biit.form.BaseQuestion;
import com.biit.form.TreeObject;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.persistence.entity.Text;
import com.biit.webforms.persistence.entity.enumerations.AnswerType;

public class ExporterDotBaseQuestion extends ExporterDot<BaseQuestion> {

	@Override
	public String export(BaseQuestion structure) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String generateDotNodeList(BaseQuestion baseQuestion) {
		String dotElement = new String();
		dotElement = "\t\t" + getDotName(baseQuestion) + ";\n";
		return dotElement;
	}

	@Override
	public String generateDotNodeFlow(BaseQuestion structure) {
		// TODO Auto-generated method stub
		return null;
	}

	// TODO translations!
	private String getDotName(BaseQuestion baseQuestion) {
		if (baseQuestion instanceof Question) {
			Question question = (Question) baseQuestion;

			if (question.getAnswerType() == AnswerType.MULTIPLE_SELECTION) {
				return getDotId(question) + " [label=\"" + filterDotLanguage(question.getName()) + " \\n ("
						+ filterDotLanguage(question.getAnswerType().toString()) + ")\", color=" + getFontColor()
						+ ", penwidth=" + getPenWidth() + ", fontcolor=" + getFontColor() + "]";
			} else {
				return getDotId(question) + " [label=\"" + filterDotLanguage(question.getName()) + "\", color="
						+ getFontColor() + ", penwidth=" + getPenWidth() + ", fontcolor=" + getFontColor() + "]";
			}
		}
		if (baseQuestion instanceof SystemField) {
			return getDotId(baseQuestion) + " [label=\"" + filterDotLanguage(baseQuestion.getName()) + " \\n ("
					+ "system-field" + ")\", color=" + getFontColor() + ", penwidth=" + getPenWidth() + ", fontcolor="
					+ getFontColor() + "]";
		}
		if (baseQuestion instanceof Text) {
			return getDotId(baseQuestion) + " [label=\"" + filterDotLanguage(baseQuestion.getName()) + " \\n ("
					+ "info-text" + ")\", color=" + getFontColor() + ", penwidth=" + getPenWidth() + ", fontcolor="
					+ getFontColor() + "]";
		}
		WebformsLogger.severe(this.getClass().getName(), "received not known base question class "
				+ baseQuestion.getClass().getName() + " '" + baseQuestion + "'");
		return new String();
	}

	protected String getDotId(TreeObject treeObject) {
		return filterDotLanguageId("id_" + treeObject.getComparationId());
	}
}
