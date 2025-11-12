package com.biit.webforms.gui.webpages.designer;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (GUI)
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
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.UserSession;
import com.biit.webforms.gui.components.StorableObjectProperties;
import com.biit.webforms.gui.components.utils.FilterByTreeObjectOrderLess;
import com.biit.webforms.gui.webpages.floweditor.SearchFormElementField;
import com.biit.webforms.gui.webpages.floweditor.SearchFormElementField.SearchFormElementChanged;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.*;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.VerticalLayout;

public class PropertiesDynamicAnswer extends StorableObjectProperties<DynamicAnswer> {
	private static final long serialVersionUID = 986400400927849415L;
	private static final String WIDTH = "300px";

	private SearchFormElementField search;

	public PropertiesDynamicAnswer() {
		super(DynamicAnswer.class);
	}

	@Override
	protected void initElement() {
		search = new SearchFormElementField(Form.class, Category.class, Group.class, Question.class);
		search.setSelectableFilter(BaseQuestion.class);
		search.setNullCaption(LanguageCodes.NULL_VALUE_SEARCH_DYNAMIC_REFERENCE);
		search.setCaption(LanguageCodes.CAPTION_SEARCH_DYNAMIC_REFERENCE.translation());
		search.setWidth(WIDTH);
		search.addValueChangeListener(new SearchFormElementChanged() {

			@Override
			public void currentElement(Object object) {
				if (object instanceof Question) {
					getInstance().setReference((Question) object);
					updateElement();
				}
			}
		});

		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setWidth(null);
		rootLayout.setHeight(null);
		rootLayout.addComponent(search);
		rootLayout.setMargin(new MarginInfo(true, false, true, false));

		boolean canEdit = getWebformsSecurityService().isElementEditable(ApplicationUi.getController().getFormInUse(),
				UserSession.getUser());
		rootLayout.setEnabled(canEdit);

		addTab(rootLayout, LanguageCodes.CAPTION_PROPERTIES_DYNAMIC_QUESTION.translation(), true);

		super.initElement();
	}

	@Override
	protected void initValues() {
		super.initValues();

		search.setTreeObject(getInstance().getReference());
		search.setEnabled(!getInstance().isReadOnly());

		FilterByTreeObjectOrderLess filter = new FilterByTreeObjectOrderLess();
		filter.setFilterSeed(getInstance().getParent());
		search.addFilter(filter);
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		updateElement();
	}

}
