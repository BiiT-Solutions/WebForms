package com.biit.webforms.exporters.dotgraph.impact;

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
import com.biit.webforms.exporters.dotgraph.ExporterDotBaseQuestion;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.persistence.entity.Text;

/**
 * Exporter to dot graph code for baseQuestions in the "added elements" diagram
 * of the impact analysis
 *
 */
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
				if (oldVersion.isContentEqual(baseQuestion) && oldVersion.hasSameChildren(baseQuestion)) {
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
