package com.biit.webforms.utils.exporters.dotgraph.impact;

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
		LinkedHashSet<TreeObject> newCategories = newForm.getAllChildrenInHierarchy(Category.class);
		for (TreeObject newCategory : newCategories) {
			if (oldForm.getChild(newCategory.getPathName()) != null) {
				TreeObject oldCategory = oldForm.getChild(newCategory.getPathName());
				if (!oldCategory.isContentEqual(newCategory)) {
					updatedCategories.add((Category) newCategory);
				}
			}
		}
		LinkedHashSet<TreeObject> newGroups = newForm.getAllChildrenInHierarchy(Group.class);
		for (TreeObject newGroup : newGroups) {
			if (oldForm.getChild(newGroup.getPathName()) != null) {
				TreeObject oldGroup = oldForm.getChild(newGroup.getPathName());
				if (!oldGroup.isContentEqual(newGroup)) {
					updatedGroups.add((Group) newGroup);
					markParentsAsUpdated(newGroup);
				}
			}
		}
		LinkedHashSet<TreeObject> newBaseQuestions = newForm.getAllChildrenInHierarchy(BaseQuestion.class);
		for (TreeObject newBaseQuestion : newBaseQuestions) {
			if (oldForm.getChild(newBaseQuestion.getPathName()) != null) {
				TreeObject oldBaseQuestion = oldForm.getChild(newBaseQuestion.getPathName());
				if ((oldBaseQuestion instanceof Question && newBaseQuestion instanceof Question)
						|| (oldBaseQuestion instanceof Text && newBaseQuestion instanceof Text)
						|| (oldBaseQuestion instanceof SystemField && newBaseQuestion instanceof SystemField)) {
					if (!oldBaseQuestion.isContentEqual(newBaseQuestion)) {
						updatedBaseQuestions.add((BaseQuestion) newBaseQuestion);
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
		LinkedHashSet<TreeObject> newCategories = newForm.getAllChildrenInHierarchy(Category.class);
		for (TreeObject newCategory : newCategories) {
			if (oldForm.getChild(newCategory.getPathName()) == null) {
				addedCategories.add((Category) newCategory);
			}
		}
		LinkedHashSet<TreeObject> newGroups = newForm.getAllChildrenInHierarchy(Group.class);
		for (TreeObject newGroup : newGroups) {
			if (oldForm.getChild(newGroup.getPathName()) == null) {
				addedGroups.add((Group) newGroup);
			}
		}
		LinkedHashSet<TreeObject> newBaseQuestions = newForm.getAllChildrenInHierarchy(BaseQuestion.class);
		for (TreeObject newBaseQuestion : newBaseQuestions) {
			if (oldForm.getChild(newBaseQuestion.getPathName()) == null) {
				addedBaseQuestions.add((BaseQuestion) newBaseQuestion);
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
		LinkedHashSet<TreeObject> oldCategories = oldForm.getAllChildrenInHierarchy(Category.class);
		for (TreeObject oldCategory : oldCategories) {
			if (newForm.getChild(oldCategory.getPathName()) == null) {
				removedCategories.add((Category) oldCategory);
			}
		}
		LinkedHashSet<TreeObject> oldGroups = oldForm.getAllChildrenInHierarchy(Group.class);
		for (TreeObject oldGroup : oldGroups) {
			if (newForm.getChild(oldGroup.getPathName()) == null) {
				removedGroups.add((Group) oldGroup);
			}
		}
		LinkedHashSet<TreeObject> oldBaseQuestions = oldForm.getAllChildrenInHierarchy(BaseQuestion.class);
		for (TreeObject oldBaseQuestion : oldBaseQuestions) {
			if (newForm.getChild(oldBaseQuestion.getPathName()) == null) {
				removedBaseQuestions.add((BaseQuestion) oldBaseQuestion);
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
