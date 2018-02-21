package com.biit.webforms.validators;

import java.util.Objects;

import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.validators.reports.NestedTablesNotAllowed;
import com.biit.webforms.validators.reports.TableColumnsOnlyCanBeQuestions;
import com.biit.webforms.validators.reports.TableRowsOnlyCanBeGroups;
import com.biit.webforms.validators.reports.TableSameColumnDefinitions;
import com.biit.webforms.validators.reports.TableSameColumnsNumber;

public class ValidateTables extends SimpleValidator<TreeObject> {

	public ValidateTables() {
		super(TreeObject.class);
	}

	@Override
	protected void validateImplementation(TreeObject element) {
		for (TreeObject child : element.getAllChildrenInHierarchy(Group.class)) {
			Group group = ((Group) child);
			if (group.isShownAsTable()) {
				for (TreeObject childOfGroup : group.getChildren()) {
					if (!assertTrue(childOfGroup instanceof Group, new TableRowsOnlyCanBeGroups(childOfGroup))) {
						return;
					}
					assertTrue(!(childOfGroup instanceof Group) || !((Group) childOfGroup).isShownAsTable(), new NestedTablesNotAllowed(childOfGroup));
					for (TreeObject question : childOfGroup.getChildren()) {
						if (!assertTrue(question instanceof Question, new TableColumnsOnlyCanBeQuestions(childOfGroup))) {
							return;
						}
					}
				}
				for (TreeObject group1 : group.getChildren()) {
					if (group1 instanceof Group) {
						for (TreeObject group2 : group.getChildren()) {
							// Different groups
							if (group2 instanceof Group) {
								if (!Objects.equals(group1.getComparationId(), group2.getComparationId())) {
									if (!assertTrue(group1.getChildren().size() == group2.getChildren().size(), new TableSameColumnsNumber(group2, group1))) {
										return;
									}
									if (group1.getChildren().size() == group2.getChildren().size()) {
										for (int i = 0; i < group1.getChildren().size(); i++) {
											try {
												if (group1.getChild(i) instanceof Question && group2.getChild(i) instanceof Question) {
													if (!assertTrue(compare((Question) group1.getChild(i), (Question) group2.getChild(i)),
															new TableSameColumnDefinitions(group1.getChild(i), group2.getChild(i)))) {
														return;
													}
												}
											} catch (ChildrenNotFoundException e) {
												WebformsLogger.errorMessage(this.getClass().getName(), e);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private boolean compare(Question object1, Question object2) {
		return Objects.equals(object1.getName(), object2.getName()) && Objects.equals(object1.isMandatory(), object2.isMandatory())
				&& Objects.equals(object1.getAnswerType(), object2.getAnswerType()) && Objects.equals(object1.getAnswerFormat(), object2.getAnswerFormat())
				&& Objects.equals(object1.getAnswerSubformat(), object2.getAnswerSubformat())
				&& Objects.equals(object1.getDefaultValue(), object2.getDefaultValue());
	}
}
