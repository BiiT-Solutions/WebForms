package com.biit.webforms.exporters.dotgraph;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Core)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.persistence.entity.Text;
import com.biit.webforms.persistence.entity.AttachedFiles;

/**
 * Dot graph exporter for base questions.
 *
 */
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

	/**
	 * Returns the code with the title of the question.
	 *
	 * @param baseQuestion
	 * @return
	 */
	private String getDotName(BaseQuestion baseQuestion) {
		if (baseQuestion instanceof Question) {
			Question question = (Question) baseQuestion;

			if (question.getAnswerType() == AnswerType.MULTIPLE_SELECTION) {
				return getDotId(question) + " [label=\"" + filterDotLanguage(question.getName()) + " \\n ("
						+ filterDotLanguage(question.getAnswerType().toString()) + ")\", color=" + getFontColor(baseQuestion.isReadOnly()) + ", penwidth="
						+ getPenWidth() + ", fontcolor=" + getFontColor(baseQuestion.isReadOnly()) + "]";
			} else {
				return getDotId(question) + " [label=\"" + filterDotLanguage(question.getName()) + "\", color=" + getFontColor(baseQuestion.isReadOnly())
						+ ", penwidth=" + getPenWidth() + ", fontcolor=" + getFontColor(baseQuestion.isReadOnly()) + "]";
			}
		}
		if (baseQuestion instanceof SystemField) {
			return getDotId(baseQuestion) + " [label=\"" + filterDotLanguage(baseQuestion.getName()) + " \\n (" + "system-field" + ")\", color="
					+ getFontColor(baseQuestion.isReadOnly()) + ", penwidth=" + getPenWidth() + ", fontcolor=" + getFontColor(baseQuestion.isReadOnly()) + "]";
		}
		if (baseQuestion instanceof Text) {
			return getDotId(baseQuestion) + " [label=\"" + filterDotLanguage(baseQuestion.getName()) + " \\n (" + "info-text" + ")\", color="
					+ getFontColor(baseQuestion.isReadOnly()) + ", penwidth=" + getPenWidth() + ", fontcolor=" + getFontColor(baseQuestion.isReadOnly()) + "]";
		}
		if (baseQuestion instanceof AttachedFiles) {
			return getDotId(baseQuestion) + " [label=\"" + filterDotLanguage(baseQuestion.getName()) + " \\n (" + "attached-files" + ")\", color="
					+ getFontColor(baseQuestion.isReadOnly()) + ", penwidth=" + getPenWidth() + ", fontcolor=" + getFontColor(baseQuestion.isReadOnly()) + "]";
		}
		WebformsLogger.severe(this.getClass().getName(), "received not known base question class " + baseQuestion.getClass().getName() + " '" + baseQuestion
				+ "'");
		return getDotId(baseQuestion) + " [label=\"" + filterDotLanguage(baseQuestion.getName()) + " \\n (" + "<<unknown>>" + ")\", color="
		+ getFontColor(baseQuestion.isReadOnly()) + ", penwidth=" + getPenWidth() + ", fontcolor=" + getFontColor(baseQuestion.isReadOnly()) + "]";
	}

	protected String getDotId(TreeObject treeObject) {
		return filterDotLanguageId("id_" + treeObject.getComparationId());
	}

	@Override
	public String generateDotNodeChilds(BaseQuestion structure) {
		return null;
	}
}
