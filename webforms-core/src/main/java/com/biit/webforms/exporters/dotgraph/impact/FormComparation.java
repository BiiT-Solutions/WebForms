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

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.persistence.entity.Text;

/**
 * Class to compare two different versions of the same form. It deduces which
 * elements have been added or removed from the oldest version.
 *
 */
public class FormComparation {

	private Set<Category> addedCategories;
	private Set<Category> updatedCategories;
	private Set<Category> removedCategories;

	private Set<Group> addedGroups;
	private Set<Group> updatedGroups;
	private Set<Group> removedGroups;

	private Set<BaseQuestion> addedBaseQuestions;
	private Set<BaseQuestion> updatedBaseQuestions;
	private Set<BaseQuestion> removedBaseQuestions;

	@SuppressWarnings("unused")
	private Set<Flow> addedFlow;
	@SuppressWarnings("unused")
	private Set<Flow> updatedFlow;
	@SuppressWarnings("unused")
	private Set<Flow> removedFlow;

	public FormComparation(Form oldForm, Form newForm) {
		addedCategories = new HashSet<>();
		updatedCategories = new HashSet<>();
		removedCategories = new HashSet<>();

		addedGroups = new HashSet<>();
		updatedGroups = new HashSet<>();
		removedGroups = new HashSet<>();

		addedBaseQuestions = new HashSet<>();
		updatedBaseQuestions = new HashSet<>();
		removedBaseQuestions = new HashSet<>();

		addedFlow = new HashSet<>();
		updatedFlow = new HashSet<>();
		removedFlow = new HashSet<>();

		searchRemovedElements(oldForm, newForm);
		searchNewElements(oldForm, newForm);
		searchUpdatedElements(oldForm, newForm);
	}

	/**
	 * Search for categories, group and base question that are maintained in the
	 * same hierarchy path between versions but their content has been changed
	 *
	 * @param oldForm
	 * @param newForm
	 */
	private void searchUpdatedElements(Form oldForm, Form newForm) {
		LinkedHashSet<Category> newCategories = newForm.getAllChildrenInHierarchy(Category.class);
		for (Category newCategory : newCategories) {
			if (oldForm.getChild(newCategory.getPathName()) != null) {
				TreeObject oldCategory = oldForm.getChild(newCategory.getPathName());
				if (!oldCategory.isContentEqual(newCategory)) {
					updatedCategories.add(newCategory);
				}
			}
		}
		LinkedHashSet<Group> newGroups = newForm.getAllChildrenInHierarchy(Group.class);
		for (Group newGroup : newGroups) {
			if (oldForm.getChild(newGroup.getPathName()) != null) {
				TreeObject oldGroup = oldForm.getChild(newGroup.getPathName());
				if (!oldGroup.isContentEqual(newGroup)) {
					updatedGroups.add(newGroup);
					markParentsAsUpdated(newGroup);
				}
			}
		}
		LinkedHashSet<BaseQuestion> newBaseQuestions = newForm.getAllChildrenInHierarchy(BaseQuestion.class);
		for (BaseQuestion newBaseQuestion : newBaseQuestions) {
			if (oldForm.getChild(newBaseQuestion.getPathName()) != null) {
				TreeObject oldBaseQuestion = oldForm.getChild(newBaseQuestion.getPathName());
				if ((oldBaseQuestion instanceof Question && newBaseQuestion instanceof Question)
						|| (oldBaseQuestion instanceof Text && newBaseQuestion instanceof Text)
						|| (oldBaseQuestion instanceof SystemField && newBaseQuestion instanceof SystemField)) {
					if (!oldBaseQuestion.isContentEqual(newBaseQuestion)) {
						updatedBaseQuestions.add(newBaseQuestion);
						markParentsAsUpdated(newBaseQuestion);
					}
				}
			}
		}
	}

	/**
	 * Search for categories, groups and base questions that are present on the
	 * current form version but they don't exist with the same hierarchy
	 * position in the old form version.
	 *
	 * @param oldForm
	 * @param newForm
	 */
	private void searchNewElements(Form oldForm, Form newForm) {
		LinkedHashSet<Category> newCategories = newForm.getAllChildrenInHierarchy(Category.class);
		for (Category newCategory : newCategories) {
			if (oldForm.getChild(newCategory.getPathName()) == null) {
				addedCategories.add(newCategory);
			}
		}
		LinkedHashSet<Group> newGroups = newForm.getAllChildrenInHierarchy(Group.class);
		for (Group newGroup : newGroups) {
			if (oldForm.getChild(newGroup.getPathName()) == null) {
				addedGroups.add(newGroup);
			}
		}
		LinkedHashSet<BaseQuestion> newBaseQuestions = newForm.getAllChildrenInHierarchy(BaseQuestion.class);
		for (BaseQuestion newBaseQuestion : newBaseQuestions) {
			if (oldForm.getChild(newBaseQuestion.getPathName()) == null) {
				addedBaseQuestions.add(newBaseQuestion);
			} else {
				TreeObject oldBaseQuestion = oldForm.getChild(newBaseQuestion.getPathName());
				if ((oldBaseQuestion instanceof Question && !(newBaseQuestion instanceof Question))
						|| (oldBaseQuestion instanceof Text && !(newBaseQuestion instanceof Text))
						|| (oldBaseQuestion instanceof SystemField && !(newBaseQuestion instanceof SystemField))) {
					addedBaseQuestions.add((BaseQuestion) newBaseQuestion);
				}
			}
		}
	}

	/**
	 * Search for categories, groups and baseQuestions that don't appear on the
	 * same hierarchy position as in the old version.
	 *
	 * @param oldForm
	 * @param newForm
	 */
	private void searchRemovedElements(Form oldForm, Form newForm) {
		LinkedHashSet<Category> oldCategories = oldForm.getAllChildrenInHierarchy(Category.class);
		for (Category oldCategory : oldCategories) {
			if (newForm.getChild(oldCategory.getPathName()) == null) {
				removedCategories.add(oldCategory);
			}
		}
		LinkedHashSet<Group> oldGroups = oldForm.getAllChildrenInHierarchy(Group.class);
		for (Group oldGroup : oldGroups) {
			if (newForm.getChild(oldGroup.getPathName()) == null) {
				removedGroups.add(oldGroup);
			}
		}
		LinkedHashSet<BaseQuestion> oldBaseQuestions = oldForm.getAllChildrenInHierarchy(BaseQuestion.class);
		for (BaseQuestion oldBaseQuestion : oldBaseQuestions) {
			if (newForm.getChild(oldBaseQuestion.getPathName()) == null) {
				removedBaseQuestions.add(oldBaseQuestion);
			} else {
				TreeObject newBaseQuestion = newForm.getChild(oldBaseQuestion.getPathName());
				if ((oldBaseQuestion instanceof Question && !(newBaseQuestion instanceof Question))
						|| (oldBaseQuestion instanceof Text && !(newBaseQuestion instanceof Text))
						|| (oldBaseQuestion instanceof SystemField && !(newBaseQuestion instanceof SystemField))) {
					removedBaseQuestions.add((BaseQuestion) newBaseQuestion);
				}
			}
		}
	}

	/**
	 * Propagates the updated status to paren elements of updated elements
	 * answer -> question -> group -> category
	 *
	 * @param element
	 */
	private void markParentsAsUpdated(TreeObject element) {
		if (!(element.getParent() instanceof Form)) {
			if (element.getParent() instanceof Category) {
				updatedCategories.add((Category) element.getParent());
			} else {
				if (element.getParent() instanceof Group) {
					updatedGroups.add((Group) element.getParent());
				}
			}
			markParentsAsUpdated(element.getParent());
		}
	}
}
