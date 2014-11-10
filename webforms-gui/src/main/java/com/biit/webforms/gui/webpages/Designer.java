package com.biit.webforms.gui.webpages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.biit.form.BaseAnswer;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.security.IActivity;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.authentication.FormWithSameNameException;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.authentication.WebformsActivity;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.authentication.exception.CategoryWithSameNameAlreadyExistsInForm;
import com.biit.webforms.authentication.exception.DestinyIsContainedAtOrigin;
import com.biit.webforms.authentication.exception.SameOriginAndDestinationException;
import com.biit.webforms.gui.common.components.PropertieUpdateListener;
import com.biit.webforms.gui.common.components.SecuredWebPage;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.components.FormEditBottomMenu;
import com.biit.webforms.gui.components.TableTreeObjectLabel;
import com.biit.webforms.gui.components.WindowNameGroup;
import com.biit.webforms.gui.components.WindowTreeObject;
import com.biit.webforms.gui.webpages.designer.DesignerPropertiesComponent;
import com.biit.webforms.gui.webpages.designer.IconProviderTreeObjectWebforms;
import com.biit.webforms.gui.webpages.designer.UpperMenuDesigner;
import com.biit.webforms.gui.webpages.designer.WindowBlocks;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Block;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.persistence.entity.Text;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;

public class Designer extends SecuredWebPage {
	private static final long serialVersionUID = 9161313025929535348L;
	private static final List<IActivity> activityPermissions = new ArrayList<IActivity>(
			Arrays.asList(WebformsActivity.READ));
	private static final String FULL = "100%";
	private static final String PROPERTIES_MIN_WIDTH = "380px";

	private UpperMenuDesigner upperMenu;
	private TableTreeObjectLabel table;
	private DesignerPropertiesComponent properties;

	@Override
	protected void initContent() {
		if (!WebformsAuthorizationService.getInstance().isFormEditable(
				UserSessionHandler.getController().getFormInUse(), UserSessionHandler.getUser())) {
			MessageManager.showInfo(LanguageCodes.INFO_MESSAGE_FORM_IS_READ_ONLY);
		}

		setCentralPanelAsWorkingArea();
		upperMenu = createUpperMenu();
		setUpperMenu(upperMenu);
		setBottomMenu(new FormEditBottomMenu());

		table = new TableTreeObjectLabel();
		table.setIconProvider(new IconProviderTreeObjectWebforms());
		table.setSizeFull();
		table.setSelectable(true);
		table.loadTreeObject(UserSessionHandler.getController().getFormInUse(), null);
		table.collapseFrom(Category.class);
		table.setValue(null);
		table.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -1169897738297107301L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateUpperMenu();
				updateProperties();
			}
		});

		properties = new DesignerPropertiesComponent();
		properties.setHeight(FULL);
		properties.setWidth(PROPERTIES_MIN_WIDTH);
		properties.addPropertyUpdateListener(new PropertieUpdateListener() {

			@Override
			public void propertyUpdate(Object element) {
				table.updateRow((TreeObject) element);
				updateUpperMenu();
			}
		});

		HorizontalLayout rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setSpacing(true);
		rootLayout.setMargin(true);

		rootLayout.addComponent(table);
		rootLayout.setExpandRatio(table, 1.0f);
		rootLayout.addComponent(properties);

		getWorkingArea().addComponent(rootLayout);

		// Init
		table.setValue(UserSessionHandler.getController().getFormInUse());
	}

	@Override
	public List<IActivity> accessAuthorizationsRequired() {
		return activityPermissions;
	}

	private void updateProperties() {
		properties.updatePropertiesComponent(table.getSelectedRow());
	}

	private UpperMenuDesigner createUpperMenu() {
		UpperMenuDesigner upperMenu = new UpperMenuDesigner();
		upperMenu.addSaveButtonListener(new ClickListener() {
			private static final long serialVersionUID = 1679355377155929573L;

			@Override
			public void buttonClick(ClickEvent event) {
				UserSessionHandler.getController().saveForm();
				MessageManager.showInfo(LanguageCodes.INFO_MESSAGE_CAPTION_SAVE,
						LanguageCodes.INFO_MESSAGE_DESCRIPTION_SAVE);
			}
		});
		upperMenu.addSaveAsBlockButtonListener(new ClickListener() {
			private static final long serialVersionUID = -352984475178007L;

			@Override
			public void buttonClick(ClickEvent event) {
				openNewBlockWindow();
			}
		});
		upperMenu.addInsertBlockButtonListener(new ClickListener() {
			private static final long serialVersionUID = 3375617501902829858L;

			@Override
			public void buttonClick(ClickEvent event) {
				openInsertBlock();
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
		upperMenu.addNewGroupButtonListener(new ClickListener() {
			private static final long serialVersionUID = 9107418811326944058L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					TreeObject selectedRow = table.getSelectedRow();
					Group newGroup = UserSessionHandler.getController().addNewGroup(
							selectedRow.getAncestorThatAccepts(Group.class));
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
					newQuestion = UserSessionHandler.getController().addNewQuestion(
							selectedRow.getAncestorThatAccepts(Question.class));
					table.addRow(newQuestion, newQuestion.getParent());
				} catch (NotValidChildException e) {
					MessageManager.showError(LanguageCodes.ERROR_QUESTION_NOT_INSERTED);
				}
			}
		});
		upperMenu.addnewSystemFieldButtonListener(new ClickListener() {
			private static final long serialVersionUID = -6530079827949983018L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					TreeObject selectedRow = table.getSelectedRow();
					SystemField newField;
					newField = UserSessionHandler.getController().addNewSystemField(
							selectedRow.getAncestorThatAccepts(SystemField.class));
					table.addRow(newField, newField.getParent());
				} catch (NotValidChildException e) {
					MessageManager.showError(LanguageCodes.ERROR_SYSTEM_FIELD_NOT_INSERTED);
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
					newText = UserSessionHandler.getController().addNewText(
							selectedRow.getAncestorThatAccepts(Text.class));
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
						Question parentQuestion = (Question) selectedRow.getAncestor(Question.class);
						newAnswer = UserSessionHandler.getController().addNewAnswer(parentQuestion);
					} else {
						// If Parent is selected then we just add it as a new
						// child
						newAnswer = UserSessionHandler.getController().addNewAnswer(selectedRow);
					}
					table.addRow(newAnswer, newAnswer.getParent());
				} catch (NotValidChildException e) {
					MessageManager.showError(LanguageCodes.ERROR_ANSWER_NOT_INSERTED);
				}
			}
		});
		upperMenu.addNewSubanswerButtonListener(new ClickListener() {
			private static final long serialVersionUID = -4159824066373029540L;

			@Override
			public void buttonClick(ClickEvent event) {
				TreeObject selectedRow = table.getSelectedRow();
				if (selectedRow instanceof BaseAnswer) {
					try {
						Answer newAnswer;
						if (!((Answer) selectedRow).isSubanswer()) {
							newAnswer = UserSessionHandler.getController().addNewAnswer(selectedRow);
						}else{
							newAnswer = UserSessionHandler.getController().addNewAnswer(selectedRow.getParent());
						}
						table.addRow(newAnswer, newAnswer.getParent());
					} catch (NotValidChildException e) {
						MessageManager.showError(LanguageCodes.ERROR_ANSWER_NOT_INSERTED);
					}
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

		upperMenu.addMoveButtonListener(new ClickListener() {
			private static final long serialVersionUID = 808060310562321887L;

			@Override
			public void buttonClick(ClickEvent event) {
				openMoveWindow();
			}
		});

		return upperMenu;
	}

	/**
	 * Function to update active/Visible state of upper menu buttons.
	 */
	private void updateUpperMenu() {
		TreeObject selectedRow = table.getSelectedRow();

		try {
			boolean formIsBlock = getCurrentForm() instanceof Block;
			boolean formIsBlockAndNoCategories = formIsBlock && getCurrentForm().getChildren().isEmpty();
			boolean rowIsNull = selectedRow == null;
			boolean rowIsForm = selectedRow instanceof Form;
			boolean canEdit = WebformsAuthorizationService.getInstance().isFormEditable(
					UserSessionHandler.getController().getFormInUse(), UserSessionHandler.getUser());
			boolean canStoreBlock = WebformsAuthorizationService.getInstance().isUserAuthorizedInAnyOrganization(
					UserSessionHandler.getUser(), WebformsActivity.BUILDING_BLOCK_ADD_FROM_FORM);
			boolean selectedRowIsAnswer = (table.getSelectedRow() != null)
					&& (table.getSelectedRow() instanceof Answer);

			upperMenu.getSaveButton().setEnabled(canEdit);
			upperMenu.getSaveAsBlockButton().setEnabled(canStoreBlock && !rowIsForm);
			upperMenu.getInsertBlockButton().setEnabled(canEdit);
			upperMenu.getInsertBlockButton().setVisible(!formIsBlock);
			upperMenu.getNewCategoryButton().setEnabled(canEdit && (formIsBlockAndNoCategories || (!formIsBlock)));
			upperMenu.getNewGroupButton().setEnabled(canEdit && selectedRowHierarchyAllows(Group.class));
			upperMenu.getNewQuestionButton().setEnabled(canEdit && selectedRowHierarchyAllows(Question.class));
			upperMenu.getNewSystemFieldButton().setEnabled(canEdit && selectedRowHierarchyAllows(SystemField.class));
			upperMenu.getNewTextButton().setEnabled(canEdit && selectedRowHierarchyAllows(Text.class));
			upperMenu.getNewAnswerButton().setEnabled(canEdit && selectedRowHierarchyAllows(Answer.class));
			upperMenu.getNewSubanswerButton().setEnabled(
					canEdit && selectedRowIsAnswer && selectedRowHierarchyAllows(Answer.class));
			upperMenu.getMoveButton().setEnabled(canEdit && !rowIsNull && !rowIsForm);
			upperMenu.getDeleteButton().setEnabled(canEdit && !rowIsNull && !rowIsForm);
			upperMenu.getUpButton().setEnabled(canEdit && !rowIsForm && !rowIsForm);
			upperMenu.getDownButton().setEnabled(canEdit && !rowIsForm);
		} catch (IOException | AuthenticationRequired e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			// Disable everthing as a security measure.
			upperMenu.getSaveButton().setEnabled(false);
			upperMenu.getSaveAsBlockButton().setEnabled(false);
			upperMenu.getInsertBlockButton().setEnabled(false);
			upperMenu.getNewCategoryButton().setEnabled(false);
			upperMenu.getNewGroupButton().setEnabled(false);
			upperMenu.getNewQuestionButton().setEnabled(false);
			upperMenu.getNewSystemFieldButton().setEnabled(false);
			upperMenu.getNewTextButton().setEnabled(false);
			upperMenu.getNewAnswerButton().setEnabled(false);
			upperMenu.getMoveButton().setEnabled(false);
			upperMenu.getDeleteButton().setEnabled(false);
			upperMenu.getUpButton().setEnabled(false);
			upperMenu.getDownButton().setEnabled(false);
		}
	}

	protected boolean selectedRowHierarchyAllows(Class<? extends TreeObject> cls) {
		TreeObject selectedRow = table.getSelectedRow();
		return selectedRow != null && (selectedRow.getAncestorThatAccepts(cls) != null);
	}

	/**
	 * Opens Save as New block window (String field)
	 */
	protected void openNewBlockWindow() {
		final WindowNameGroup newBlockWindow = new WindowNameGroup(LanguageCodes.COMMON_CAPTION_NAME.translation(),
				LanguageCodes.COMMON_CAPTION_GROUP.translation(),
				new IActivity[] { WebformsActivity.BUILDING_BLOCK_EDITING });
		newBlockWindow.setCaption(LanguageCodes.CAPTION_NEW_BLOCK.translation());
		newBlockWindow.setDefaultValue(LanguageCodes.NULL_VALUE_NEW_BLOCK.translation());
		newBlockWindow.showCentered();
		newBlockWindow.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				if (newBlockWindow.getValue() == null || newBlockWindow.getValue().isEmpty()) {
					MessageManager.showWarning(LanguageCodes.COMMON_WARNING_TITLE_BLOCK_NOT_CREATED,
							LanguageCodes.COMMON_WARNING_DESCRIPTION_BLOCK_NEEDS_NAME);
					return;
				}
				try {
					UserSessionHandler.getController().saveAsBlock(table.getSelectedRow(), newBlockWindow.getValue(),
							newBlockWindow.getOrganization().getOrganizationId());
					newBlockWindow.close();

					MessageManager.showInfo(LanguageCodes.INFO_MESSAGE_CAPTION_SAVE,
							LanguageCodes.INFO_MESSAGE_DESCRIPTION_SAVE);

				} catch (FieldTooLongException e) {
					MessageManager.showError(LanguageCodes.COMMON_ERROR_FIELD_TOO_LONG);
				} catch (FormWithSameNameException e) {
					MessageManager.showError(LanguageCodes.COMMON_ERROR_NAME_IS_IN_USE);
				}
			}
		});
	}

	/**
	 * Opens Insert Block window
	 */
	protected void openInsertBlock() {
		final WindowBlocks windowBlocks = new WindowBlocks(LanguageCodes.CAPTION_INSERT_NEW_BLOCK);
		windowBlocks.showCentered();
		windowBlocks.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				// Insert block in form
				try {
					UserSessionHandler.getController().insertBlock(windowBlocks.getSelectedBlock());
					clearAndUpdateFormTable();
					window.close();
				} catch (CategoryWithSameNameAlreadyExistsInForm e) {
					MessageManager.showWarning(LanguageCodes.ERROR_CAPTION_NOT_ALLOWED,
							LanguageCodes.WARNING_DESCRIPTION_REPEATED_CATEGORY_NAME);
				}
			}
		});
	}

	/**
	 * Opens move element window.
	 */
	protected void openMoveWindow() {
		final WindowTreeObject moveWindow = new WindowTreeObject(LanguageCodes.CAPTION_WINDOW_MOVE, getCurrentForm(),
				Form.class, Category.class, Group.class);
		moveWindow.showCentered();
		moveWindow.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				try {
					TreeObject whatToMove = table.getSelectedRow();
					TreeObject whereToMove = moveWindow.getSelectedTreeObject();
					UserSessionHandler.getController().moveTo(whatToMove, whereToMove);
					window.close();
					clearAndUpdateFormTable();
					table.setValue(whatToMove);
				} catch (NotValidChildException e) {
					MessageManager.showWarning(LanguageCodes.WARNING_CAPTION_NOT_VALID,
							LanguageCodes.WARNING_DESCRIPTION_NOT_VALID);
				} catch (SameOriginAndDestinationException e) {
					MessageManager.showWarning(LanguageCodes.WARNING_CAPTION_SAME_ORIGIN,
							LanguageCodes.WARNING_DESCRIPTION_SAME_ORIGIN);
				} catch (DestinyIsContainedAtOrigin | ChildrenNotFoundException e) {
					MessageManager.showWarning(LanguageCodes.WARNING_CAPTION_SAME_ORIGIN,
							LanguageCodes.WARNING_DESCRIPTION_ORIGIN_INCLUDED_IN_DESTINY);
				}
			}
		});
	}

	protected void clearAndUpdateFormTable() {
		// Clear and update form
		TreeObject currentSelection = table.getSelectedRow();
		table.setValue(null);
		table.removeAllItems();
		table.loadTreeObject(getCurrentForm(), null);
		table.select(currentSelection);
	}

	protected Form getCurrentForm() {
		return UserSessionHandler.getController().getFormInUse();
	}
}
