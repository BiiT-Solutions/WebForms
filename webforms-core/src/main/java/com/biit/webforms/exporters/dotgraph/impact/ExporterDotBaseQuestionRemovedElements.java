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

public class ExporterDotBaseQuestionRemovedElements extends ExporterDotBaseQuestion {

	private BaseQuestion newVersion;

	ExporterDotBaseQuestionRemovedElements(TreeObject newVersion) {
		if (newVersion != null && newVersion instanceof BaseQuestion) {
			this.newVersion = (BaseQuestion) newVersion;
		}
	}

	@Override
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
