package com.biit.webforms.gui.webpages;

import java.util.List;

import com.biit.form.BaseAnswer;
import com.biit.form.BaseQuestion;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.liferay.security.IActivity;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.common.components.SecuredWebPage;
import com.biit.webforms.gui.common.components.TreeObjectTable;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.webpages.designeditor.UpperMenuDesigner;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.Subcategory;
import com.biit.webforms.persistence.entity.Text;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;

public class DesignEditor extends SecuredWebPage {
	private static final long serialVersionUID = 9161313025929535348L;

	private UpperMenuDesigner upperMenu;
	private TreeObjectTable table;

	@Override
	protected void initContent() {
		setAsCentralPanel();
		upperMenu = createUpperMenu();
		setUpperMenu(upperMenu);

		table = new TreeObjectTable();
		table.addRow(UserSessionHandler.getController().getFormInUse(), null);
		table.setSizeFull();
		table.setSelectable(true);
		table.setValue(null);
		table.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -1169897738297107301L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateUpperMenu();
			}
		});
		table.setValue(UserSessionHandler.getController().getFormInUse());

		HorizontalLayout rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setSpacing(true);
		rootLayout.setMargin(true);

		rootLayout.addComponent(table);
		rootLayout.setExpandRatio(table, 0.75f);

		getWorkingArea().addComponent(rootLayout);
		// TODO terminar
	}

	@Override
	public List<IActivity> accessAuthorizationsRequired() {
		// TODO Auto-generated method stub
		return null;
	}

	private UpperMenuDesigner createUpperMenu() {
		UpperMenuDesigner upperMenu = new UpperMenuDesigner();
		upperMenu.addSaveButtonListener(new ClickListener() {
			private static final long serialVersionUID = 1679355377155929573L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

			}
		});
		upperMenu.addFlowButtonListener(new ClickListener() {
			private static final long serialVersionUID = -3161251470765214230L;

			@Override
			public void buttonClick(ClickEvent event) {
				ApplicationUi.navigateTo(WebMap.FLOW_EDITOR);
			}
		});
		upperMenu.addValidateButtonListener(new ClickListener() {
			private static final long serialVersionUID = -1627616225877959507L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

			}
		});
		upperMenu.addFinishButtonListener(new ClickListener() {
			private static final long serialVersionUID = 8869180038869702710L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

			}
		});
		upperMenu.addNewCategoryButtonListener(new ClickListener() {
			private static final long serialVersionUID = 742624238392918737L;

			@Override
			public void buttonClick(ClickEvent event) {
				Category newCategory = UserSessionHandler.getController().addNewCategory();
				table.addRow(newCategory, newCategory.getParent());
			}
		});
		upperMenu.addNewSubCategoryButtonListener(new ClickListener() {
			private static final long serialVersionUID = 9107418811326944058L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {

					TreeObject selectedRow = table.getSelectedRow();
					Subcategory newSubcategory;
					if (selectedRow instanceof Subcategory) {
						newSubcategory = UserSessionHandler.getController().addNewSubcategory(selectedRow.getParent());
					} else {
						newSubcategory = UserSessionHandler.getController().addNewSubcategory(selectedRow);
					}
					table.addRow(newSubcategory, newSubcategory.getParent());
				} catch (NotValidChildException e) {
					MessageManager.showError(LanguageCodes.ERROR_SUBCATEGORY_NOT_INSERTED);
				}
			}
		});
		upperMenu.addNewGroupButtonListener(new ClickListener() {
			private static final long serialVersionUID = 9107418811326944058L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					Group newGroup = UserSessionHandler.getController().addNewGroup(table.getSelectedRow());
					table.addRow(newGroup, newGroup.getParent());
				} catch (NotValidChildException e) {
					MessageManager.showError(LanguageCodes.ERROR_GROUP_NOT_INSERTED);
				}
			}
		});
		upperMenu.addNewQuestionButtonListener(new ClickListener() {
			private static final long serialVersionUID = 9107418811326944058L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					TreeObject selectedRow = table.getSelectedRow();
					Question newQuestion;
					if (selectedRow instanceof BaseQuestion) {
						newQuestion = UserSessionHandler.getController().addNewQuestion(selectedRow.getParent());
					} else {
						newQuestion = UserSessionHandler.getController().addNewQuestion(selectedRow);
					}
					table.addRow(newQuestion, newQuestion.getParent());
				} catch (NotValidChildException e) {
					MessageManager.showError(LanguageCodes.ERROR_QUESTION_NOT_INSERTED);
				}
			}
		});
		upperMenu.addNewTextButtonListener(new ClickListener() {
			private static final long serialVersionUID = 7251023427405784803L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					TreeObject selectedRow = table.getSelectedRow();
					Text newText;
					if (selectedRow instanceof BaseQuestion) {
						newText = UserSessionHandler.getController().addNewText(selectedRow.getParent());
					} else {
						newText = UserSessionHandler.getController().addNewText(selectedRow);
					}
					table.addRow(newText, newText.getParent());
				} catch (NotValidChildException e) {
					MessageManager.showError(LanguageCodes.ERROR_TEXT_NOT_INSERTED);
				}
			}
		});
		upperMenu.addNewAnswerButtonListener(new ClickListener() {
			private static final long serialVersionUID = 2104161068489369224L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					TreeObject selectedRow = table.getSelectedRow();
					Answer newAnswer;
					if (selectedRow instanceof BaseAnswer) {
						newAnswer = UserSessionHandler.getController().addNewAnswer(selectedRow.getParent());
					} else {
						newAnswer = UserSessionHandler.getController().addNewAnswer(selectedRow);
					}
					table.addRow(newAnswer, newAnswer.getParent());
				} catch (NotValidChildException e) {
					MessageManager.showError(LanguageCodes.ERROR_ANSWER_NOT_INSERTED);
				}
			}
		});

		upperMenu.addDeleteButtonListener(new ClickListener() {
			private static final long serialVersionUID = 9107418811326944058L;

			@Override
			public void buttonClick(ClickEvent event) {
				TreeObject row = table.getSelectedRow();
				TreeObject parent = row.getParent();
				table.selectPreviousRow();
				try {
					UserSessionHandler.getController().removeTreeObject(row);
				} catch (DependencyExistException e) {
					MessageManager.showError(LanguageCodes.CAPTION_ANSWER_FORMAT_DATE);
				}
				table.updateRow(parent);
			}
		});

		upperMenu.addUpButtonListener(new ClickListener() {
			private static final long serialVersionUID = -5060918501257565052L;

			@Override
			public void buttonClick(ClickEvent event) {
				TreeObject row = table.getSelectedRow();
				UserSessionHandler.getController().moveUp(row);
				table.redrawRow(table.getSelectedRow().getParent());
				table.setValue(row);
			}
		});

		upperMenu.addDownButtonListener(new ClickListener() {
			private static final long serialVersionUID = 1343167938156284153L;

			@Override
			public void buttonClick(ClickEvent event) {
				TreeObject row = table.getSelectedRow();
				UserSessionHandler.getController().moveDown(row);
				table.redrawRow(table.getSelectedRow().getParent());
				table.setValue(row);
			}
		});

		return upperMenu;
	}

	private void updateUpperMenu() {
		TreeObject selectedRow = table.getSelectedRow();
		System.out.println("UpdateUpperMenu "+selectedRow);
		if (selectedRow == null) {
			upperMenu.getNewCategoryButton().setEnabled(false);
			upperMenu.getNewSubcategoryButton().setEnabled(false);
			upperMenu.getNewGroupButton().setEnabled(false);
			upperMenu.getNewQuestionButton().setEnabled(false);
			upperMenu.getNewTextButton().setEnabled(false);
			upperMenu.getNewAnswerButton().setEnabled(false);
			upperMenu.getMoveButton().setEnabled(false);
			upperMenu.getDeleteButton().setEnabled(false);
			upperMenu.getUpButton().setEnabled(false);
			upperMenu.getDownButton().setEnabled(false);
		} else {
			if(selectedRow instanceof Form){
				upperMenu.getNewCategoryButton().setEnabled(selectedRow.isAllowedChildren(Category.class));
				upperMenu.getNewSubcategoryButton().setEnabled(selectedRow.isAllowedChildren(Subcategory.class));
				upperMenu.getNewGroupButton().setEnabled(selectedRow.isAllowedChildren(Group.class));
				upperMenu.getNewQuestionButton().setEnabled(selectedRow.isAllowedChildren(Question.class));
				upperMenu.getNewTextButton().setEnabled(selectedRow.isAllowedChildren(Text.class));
				upperMenu.getNewAnswerButton().setEnabled(selectedRow.isAllowedChildren(Answer.class));
				upperMenu.getDeleteButton().setEnabled(false);
				upperMenu.getUpButton().setEnabled(false);
				upperMenu.getDownButton().setEnabled(false);
			}else{
				upperMenu.getNewCategoryButton().setEnabled(selectedRow.isAllowedChildren(Category.class)||selectedRow.getParent().isAllowedChildren(Category.class));
				upperMenu.getNewSubcategoryButton().setEnabled(selectedRow.isAllowedChildren(Subcategory.class)||selectedRow.getParent().isAllowedChildren(Subcategory.class));
				upperMenu.getNewGroupButton().setEnabled(selectedRow.isAllowedChildren(Group.class));
				upperMenu.getNewQuestionButton().setEnabled(selectedRow.isAllowedChildren(Question.class)||selectedRow.getParent().isAllowedChildren(Question.class));
				upperMenu.getNewTextButton().setEnabled(selectedRow.isAllowedChildren(Text.class)||selectedRow.getParent().isAllowedChildren(Text.class));
				upperMenu.getNewAnswerButton().setEnabled(selectedRow.isAllowedChildren(Answer.class)||selectedRow.getParent().isAllowedChildren(Answer.class));
				upperMenu.getDeleteButton().setEnabled(true);
				upperMenu.getUpButton().setEnabled(true);
				upperMenu.getDownButton().setEnabled(true);
			}
			upperMenu.getMoveButton().setEnabled(true);
						
		}
	}
}
