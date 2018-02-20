package com.biit.webforms.gui.webpages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.abcd.core.SpringContextHelper;
import com.biit.form.entity.BaseAnswer;
import com.biit.form.entity.BaseCategory;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.usermanager.security.IActivity;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.UserSession;
import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.gui.common.components.PropertieUpdateListener;
import com.biit.webforms.gui.common.components.SecuredWebPage;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.common.components.WindowProceedAction;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.components.FormEditBottomMenu;
import com.biit.webforms.gui.components.TableTreeObjectLabel;
import com.biit.webforms.gui.components.WindowNameGroup;
import com.biit.webforms.gui.components.WindowTreeObject;
import com.biit.webforms.gui.exceptions.CategoryWithSameNameAlreadyExistsInForm;
import com.biit.webforms.gui.exceptions.DestinyIsContainedAtOrigin;
import com.biit.webforms.gui.exceptions.EmptyBlockCannotBeInserted;
import com.biit.webforms.gui.exceptions.FormWithSameNameException;
import com.biit.webforms.gui.exceptions.LinkCanOnlyBePerformedOnWholeBlock;
import com.biit.webforms.gui.exceptions.SameOriginAndDestinationException;
import com.biit.webforms.gui.webpages.designer.DesignerPropertiesComponent;
import com.biit.webforms.gui.webpages.designer.IconProviderTreeObjectExtraInformation;
import com.biit.webforms.gui.webpages.designer.IconProviderTreeObjectImage;
import com.biit.webforms.gui.webpages.designer.IconProviderTreeObjectWebforms;
import com.biit.webforms.gui.webpages.designer.UpperMenuDesigner;
import com.biit.webforms.gui.webpages.designer.WindowBlocks;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.dao.ISimpleFormViewDao;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Block;
import com.biit.webforms.persistence.entity.BlockReference;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.DynamicAnswer;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.SimpleFormView;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.persistence.entity.Text;
import com.biit.webforms.persistence.entity.exceptions.DependencyDynamicAnswerExistException;
import com.biit.webforms.persistence.entity.exceptions.WebserviceDependencyExistException;
import com.biit.webforms.security.WebformsActivity;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Tree.CollapseEvent;
import com.vaadin.ui.Tree.CollapseListener;
import com.vaadin.ui.Tree.ExpandEvent;
import com.vaadin.ui.Tree.ExpandListener;

public class Designer extends SecuredWebPage {
	private static final long serialVersionUID = 9161313025929535348L;
	private static final List<IActivity> activityPermissions = new ArrayList<IActivity>(Arrays.asList(WebformsActivity.READ));
	private static final String FULL = "100%";
	private static final String PROPERTIES_MIN_WIDTH = "380px";

	private UpperMenuDesigner upperMenu;
	private TableTreeObjectLabel table;
	private DesignerPropertiesComponent properties;
	private CollapseListener collapseListener;
	private ExpandListener expandListener;

	private ISimpleFormViewDao simpleFormViewDao;

	public Designer() {
		super();
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		simpleFormViewDao = (ISimpleFormViewDao) helper.getBean("simpleFormDaoWebforms");

		collapseListener = new CollapseListener() {
			private static final long serialVersionUID = -4969316575917593209L;

			@Override
			public void nodeCollapse(CollapseEvent event) {
				if (ApplicationUi.getController().getCollapsedStatus() == null) {
					ApplicationUi.getController().setCollapsedStatus(new HashSet<>());
				}
				ApplicationUi.getController().getCollapsedStatus().add(event.getItemId());
			}
		};
		expandListener = new ExpandListener() {
			private static final long serialVersionUID = -7235454850117978231L;

			@Override
			public void nodeExpand(ExpandEvent event) {
				if (ApplicationUi.getController().getCollapsedStatus() == null) {
					ApplicationUi.getController().setCollapsedStatus(new HashSet<>());
				}
				ApplicationUi.getController().getCollapsedStatus().remove(event.getItemId());
			}
		};
	}

	@Override
	protected void initContent() {
		if (ApplicationUi.getController().getFormInUse() != null
				&& !getWebformsSecurityService().isFormEditable(ApplicationUi.getController().getFormInUse(), UserSession.getUser())) {
			MessageManager.showWarning(LanguageCodes.INFO_MESSAGE_FORM_IS_READ_ONLY);
		}

		setCentralPanelAsWorkingArea();
		upperMenu = createUpperMenu();
		setUpperMenu(upperMenu);
		setBottomMenu(new FormEditBottomMenu());

		table = new TableTreeObjectLabel();
		table.setPageLength(0);
		table.setHiddenElementShown(true);
		table.setIconProvider(new IconProviderTreeObjectWebforms());
		table.setStatusIconProvider(new IconProviderTreeObjectExtraInformation());
		table.setImageIconProvider(new IconProviderTreeObjectImage());
		table.setSizeFull();
		table.setSelectable(true);
		table.loadTreeObject(getCurrentForm(), null);
		table.collapseFrom(Category.class);
		retrieveCollapsedTableState();
		saveCollapsedTableState();
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
		table.setValue(getCurrentForm());
	}

	@Override
	public List<IActivity> accessAuthorizationsRequired() {
		return activityPermissions;
	}

	private void updateProperties() {
		properties.updatePropertiesComponent(table.getSelectedRow());
	}

	private UpperMenuDesigner createUpperMenu() {
		final UpperMenuDesigner upperMenu = new UpperMenuDesigner();
		upperMenu.addSaveButtonListener(new ClickListener() {
			private static final long serialVersionUID = 1679355377155929573L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					ApplicationUi.getController().saveForm();
					clearAndUpdateFormTable();

					if (ApplicationUi.getController().getFormInUse() instanceof Block) {
						MessageManager.showInfo(LanguageCodes.INFO_MESSAGE_BLOCK_CAPTION_SAVE, LanguageCodes.INFO_MESSAGE_BLOCK_DESCRIPTION_SAVE);
					} else {
						MessageManager.showInfo(LanguageCodes.INFO_MESSAGE_FORM_CAPTION_SAVE, LanguageCodes.INFO_MESSAGE_FORM_DESCRIPTION_SAVE);
					}
				} catch (UnexpectedDatabaseException e) {
					MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE, LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
					WebformsUiLogger.errorMessage(this.getClass().getName(), e);
				} catch (ElementCannotBePersistedException e) {
					MessageManager.showError(LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED, LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED_DESCRIPTION);
					WebformsUiLogger.errorMessage(this.getClass().getName(), e);
				}
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
		upperMenu.addLinkBlockButtonListener(new ClickListener() {
			private static final long serialVersionUID = -4308973502774054307L;

			@Override
			public void buttonClick(ClickEvent event) {
				openLinkBlock();
			}
		});
		upperMenu.addNewCategoryButtonListener(new ClickListener() {
			private static final long serialVersionUID = 742624238392918737L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					Category newCategory = ApplicationUi.getController().addNewCategory();
					table.addRow(newCategory, newCategory.getParent());
				} catch (NotValidChildException e) {
					MessageManager.showError(LanguageCodes.ERROR_CATEGORY_NOT_INSERTED_IN_BLOCK);
				} catch (ElementIsReadOnly e) {
					MessageManager.showError(LanguageCodes.ERROR_READ_ONLY_ELEMENT);
				}
			}
		});
		upperMenu.addNewGroupButtonListener(new ClickListener() {
			private static final long serialVersionUID = 9107418811326944058L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					TreeObject selectedRow = table.getSelectedRow();
					Group newGroup = ApplicationUi.getController().addNewGroup(selectedRow.getAncestorThatAccepts(Group.class));
					table.addRow(newGroup, newGroup.getParent());
				} catch (NotValidChildException e) {
					MessageManager.showError(LanguageCodes.ERROR_GROUP_NOT_INSERTED);
					WebformsUiLogger.errorMessage(this.getClass().getName(), e);
				} catch (ElementIsReadOnly e) {
					MessageManager.showError(LanguageCodes.ERROR_READ_ONLY_ELEMENT);
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
					newQuestion = ApplicationUi.getController().addNewQuestion(selectedRow.getAncestorThatAccepts(Question.class));
					table.addRow(newQuestion, newQuestion.getParent());
				} catch (NotValidChildException e) {
					MessageManager.showError(LanguageCodes.ERROR_QUESTION_NOT_INSERTED);
				} catch (ElementIsReadOnly e) {
					MessageManager.showError(LanguageCodes.ERROR_READ_ONLY_ELEMENT);
				}
			}
		});
		upperMenu.addNewSystemFieldButtonListener(new ClickListener() {
			private static final long serialVersionUID = -6530079827949983018L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					TreeObject selectedRow = table.getSelectedRow();
					SystemField newField;
					newField = ApplicationUi.getController().addNewSystemField(selectedRow.getAncestorThatAccepts(SystemField.class));
					table.addRow(newField, newField.getParent());
				} catch (NotValidChildException e) {
					MessageManager.showError(LanguageCodes.ERROR_SYSTEM_FIELD_NOT_INSERTED);
				} catch (ElementIsReadOnly e) {
					MessageManager.showError(LanguageCodes.ERROR_READ_ONLY_ELEMENT);
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
					newText = ApplicationUi.getController().addNewText(selectedRow.getAncestorThatAccepts(Text.class));
					table.addRow(newText, newText.getParent());
				} catch (NotValidChildException e) {
					MessageManager.showError(LanguageCodes.ERROR_TEXT_NOT_INSERTED);
				} catch (ElementIsReadOnly e) {
					MessageManager.showError(LanguageCodes.ERROR_READ_ONLY_ELEMENT);
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
						newAnswer = ApplicationUi.getController().addNewAnswer(parentQuestion);
					} else {
						// If Parent is selected then we just add it as a new
						// child
						newAnswer = ApplicationUi.getController().addNewAnswer(selectedRow);
					}
					table.addRow(newAnswer, newAnswer.getParent());
				} catch (NotValidChildException e) {
					MessageManager.showError(LanguageCodes.ERROR_ANSWER_NOT_INSERTED);
				} catch (ElementIsReadOnly e) {
					MessageManager.showError(LanguageCodes.ERROR_READ_ONLY_ELEMENT);
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
							newAnswer = ApplicationUi.getController().addNewAnswer(selectedRow);
						} else {
							newAnswer = ApplicationUi.getController().addNewAnswer(selectedRow.getParent());
						}
						table.addRow(newAnswer, newAnswer.getParent());
					} catch (NotValidChildException e) {
						MessageManager.showError(LanguageCodes.ERROR_ANSWER_NOT_INSERTED);
					} catch (ElementIsReadOnly e) {
						MessageManager.showError(LanguageCodes.ERROR_READ_ONLY_ELEMENT);
					}
				}
			}
		});
		upperMenu.addDeleteButtonListener(new ClickListener() {
			private static final long serialVersionUID = 9107418811326944058L;

			@Override
			public void buttonClick(ClickEvent event) {
				// Do not remove any element of a block if a form that is
				// linking it is in use.
				if (!(ApplicationUi.getController().getFormInUse() instanceof Block)
						|| !isBlockLinkedByFormInUse((Block) ApplicationUi.getController().getFormInUse())) {
					TreeObject row = table.getSelectedRow();
					// Do not remove an element of a block if is in use in any
					// flow.
					try {
						if ((ApplicationUi.getController().getFormInUse() instanceof Block) && ApplicationUi.getController().existFormThatUseElementInFlow(row)) {
							MessageManager.showError(LanguageCodes.ERROR_ELEMENT_CANNOT_BE_REMOVED_TITLE,
									LanguageCodes.ERROR_ELEMENT_CANNOT_BE_REMOVED_BLOCK_ELEMENT_DESCRIPTION);
						} else {
							try {
								table.selectPreviousRow();
								ApplicationUi.getController().removeTreeObject(row);
								table.updateRow(table.getParentRowItem(row));
							} catch (DependencyExistException | ChildrenNotFoundException e) {
								table.setValue(row);
								if (e instanceof DependencyDynamicAnswerExistException) {
									MessageManager.showError(LanguageCodes.ERROR_DYNAMIC_ANSWER_DEPENDENCY);
								} else if (e instanceof DependencyExistException) {
									MessageManager.showError(LanguageCodes.ERROR_TREE_OBJECT_FLOW_DEPENDENCY);
								} else if (e instanceof WebserviceDependencyExistException) {
									MessageManager.showError(LanguageCodes.ERROR_TREE_OBJECT_WEBSERVICE_CALL_DEPENDENCY);
								} else {
									MessageManager.showError(LanguageCodes.COMMON_ERROR_UNEXPECTED_ERROR);
									WebformsUiLogger.errorMessage(this.getClass().getName(), e);
								}
							} catch (ElementIsReadOnly e) {
								table.setValue(row);
								MessageManager.showError(LanguageCodes.ERROR_READ_ONLY_ELEMENT);
							}
						}
					} catch (ReadOnlyException | UnexpectedDatabaseException e) {
						MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE, LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
					}
				} else {
					MessageManager.showError(LanguageCodes.ERROR_FORM_WITH_BLOCK_IS_IN_USE, LanguageCodes.ERROR_FORM_WITH_BLOCK_IS_IN_USE_DESCRIPTION);
				}
			}
		});

		upperMenu.addHideButtonListener(new ClickListener() {
			private static final long serialVersionUID = 7737086452985900469L;

			@Override
			public void buttonClick(ClickEvent event) {
				showOrHideElement(table.getSelectedRow());
			}
		});

		upperMenu.addUpButtonListener(new ClickListener() {
			private static final long serialVersionUID = -5060918501257565052L;

			@Override
			public void buttonClick(ClickEvent event) {
				TreeObject row = table.getSelectedRow();
				try {
					ApplicationUi.getController().moveUp(row);
					// Remove collapse state listeners, redraw row and recover
					// the original collapse state and
					// listeners.
					removeCollapseStateListeners();
					table.redrawRow(table.getParentRowItem(row));
					retrieveCollapsedTableState();
					table.setValue(row);
				} catch (ElementIsReadOnly e) {
					MessageManager.showError(LanguageCodes.ERROR_READ_ONLY_ELEMENT);
				}
			}
		});

		upperMenu.addDownButtonListener(new ClickListener() {
			private static final long serialVersionUID = 1343167938156284153L;

			@Override
			public void buttonClick(ClickEvent event) {
				TreeObject row = table.getSelectedRow();
				try {
					ApplicationUi.getController().moveDown(row);
					// Remove collapse state listeners, redraw row and recover
					// the original collapse state and
					// listeners.
					removeCollapseStateListeners();
					table.redrawRow(table.getParentRowItem(row));
					retrieveCollapsedTableState();
					table.setValue(row);
				} catch (ElementIsReadOnly e) {
					MessageManager.showError(LanguageCodes.ERROR_READ_ONLY_ELEMENT);
				}
			}
		});

		upperMenu.addMoveButtonListener(new ClickListener() {
			private static final long serialVersionUID = 808060310562321887L;

			@Override
			public void buttonClick(ClickEvent event) {
				openMoveWindow();
			}
		});
		upperMenu.addFinishListener(new ClickListener() {
			private static final long serialVersionUID = 8869180038869702710L;

			@Override
			public void buttonClick(ClickEvent event) {
				finishForm();
			}
		});

		upperMenu.addNewDynamicAnswerListener(new ClickListener() {
			private static final long serialVersionUID = 6905174400430714888L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					TreeObject selectedRow = table.getSelectedRow();
					DynamicAnswer newDynamicQuestion = ApplicationUi.getController().addNewDynamicQuestion(
							selectedRow.getAncestorThatAccepts(DynamicAnswer.class));
					table.addRow(newDynamicQuestion, newDynamicQuestion.getParent());
				} catch (NotValidChildException e) {
					MessageManager.showError(LanguageCodes.ERROR_ANSWER_NOT_INSERTED);
				} catch (ElementIsReadOnly e) {
					MessageManager.showError(LanguageCodes.ERROR_READ_ONLY_ELEMENT);
				}
			}
		});

		return upperMenu;
	}

	private void showOrHideElement(TreeObject element) {
		try {
			BlockReference blockReference = ApplicationUi.getController().getCompleteFormView().getBlockReference(element);
			// Do not hide an element of a form reference if it is in use in any
			// external flow.
			if (ApplicationUi.getController().existDefinedFlowToReferencedElementOrItsChildren(element)) {
				MessageManager.showError(LanguageCodes.ERROR_ELEMENT_CANNOT_BE_HIDDEN_TITLE, LanguageCodes.ERROR_ELEMENT_CANNOT_BE_HIDDEN_DESCRIPTION);
				// Do not remove an element of a block if it is in use in any
				// external flow of the block.
			} else if (blockReference != null && ApplicationUi.getController().existExternalFlowToReferencedElementOrItsChildren(element, blockReference)) {
				MessageManager.showError(LanguageCodes.ERROR_ELEMENT_CANNOT_BE_HIDDEN_TITLE, LanguageCodes.ERROR_ELEMENT_CANNOT_BE_HIDDEN_DESCRIPTION);
			} else {
				// It is not an element or it is a form reference element.
				if (element.isHiddenElement()) {
					if (ApplicationUi.getController().getCompleteFormView().showElement(element)) {
						element.setHiddenElement(false);
						if (blockReference != null) {
							WebformsUiLogger.info(this.getClass().getName(), "Has show element '" + element + "' of block '" + blockReference + "'.");
						} else {
							WebformsUiLogger.info(this.getClass().getName(), "Has show element '" + element + "'.");
						}
						ApplicationUi.getController().setUnsavedFormChanges(true);
					} else {
						MessageManager.showWarning(LanguageCodes.WARNING_CANNOT_SHOW_ELEMENT_DUE_TO_HIDDEN_PARENT);
					}
				} else {
					try {
						if (ApplicationUi.getController().getCompleteFormView().hideElement(element)) {
							element.setHiddenElement(true);
							if (blockReference != null) {
								WebformsUiLogger.info(this.getClass().getName(), "Has hide element '" + element + "' of block '" + blockReference + "'.");
							} else {
								WebformsUiLogger.info(this.getClass().getName(), "Has hide element '" + element + "'.");
							}
							ApplicationUi.getController().setUnsavedFormChanges(true);
						}
					} catch (ElementCannotBeRemovedException e) {
						WebformsUiLogger.errorMessage(this.getClass().getName(), e);
					}
				}
				upperMenu.updateHideButton(element.isHiddenElement());
				updateRowAndItsChildren(element);
			}
		} catch (ReadOnlyException e) {
			MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE, LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
		}
	}

	private void updateRowAndItsChildren(TreeObject row) {
		table.updateRow(row);
		for (TreeObject child : row.getChildren()) {
			updateRowAndItsChildren(child);
		}
	}

	private void finishForm() {
		new WindowProceedAction(LanguageCodes.TEXT_PROCEED_FORM_CLOSE, new AcceptActionListener() {
			@Override
			public void acceptAction(WindowAcceptCancel window) {
				try {
					ApplicationUi.getController().finishForm(ApplicationUi.getController().getFormInUse());
					ApplicationUi.navigateTo(WebMap.getMainPage());
				} catch (UnexpectedDatabaseException e) {
					MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE, LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
				} catch (ElementCannotBePersistedException e) {
					MessageManager.showError(LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED, LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED_DESCRIPTION);
					WebformsUiLogger.errorMessage(this.getClass().getName(), e);
				}
			}
		});
	}

	/**
	 * Function to update active/Visible state of upper menu buttons.
	 */
	private void updateUpperMenu() {
		try {
			TreeObject selectedElement = table.getSelectedRow();

			if (ApplicationUi.getController().getFormInUse() != null) {
				boolean formIsBlock = getCurrentForm() instanceof Block;
				boolean formIsBlockAndNoCategories = formIsBlock && getCurrentForm().getChildren().isEmpty();
				boolean formHasLinkedForm;
				formHasLinkedForm = ApplicationUi.getController().getFormInUse().getFormReference() != null;
				boolean rowIsNull = (selectedElement == null);
				boolean rowIsForm = (selectedElement != null && selectedElement instanceof Form);
				boolean rowIsElementReference = (selectedElement != null && selectedElement.isReadOnly());

				boolean rowIsBlockReferenceCategory = rowIsElementReference && (selectedElement instanceof BaseCategory)
						&& ApplicationUi.getController().getCompleteFormView().getBlockReference(selectedElement) != null;
				boolean canEdit = getWebformsSecurityService().isFormEditable(ApplicationUi.getController().getFormInUse(), UserSession.getUser());
				boolean canStoreBlock = getWebformsSecurityService().isUserAuthorizedInAnyOrganization(UserSession.getUser(),
						WebformsActivity.BUILDING_BLOCK_ADD_FROM_FORM);
				boolean selectedRowIsAnswer = (table.getSelectedRow() != null) && (table.getSelectedRow() instanceof Answer);
				boolean isHidden = selectedElement != null && selectedElement.isHiddenElement();

				upperMenu.getSaveButton().setEnabled(canEdit);
				upperMenu.getBlockMenu().setEnabled(canEdit && !formHasLinkedForm);
				upperMenu.getOtherElementsMenu().setEnabled(canEdit && !formHasLinkedForm);
				upperMenu.getSaveAsBlockButton().setEnabled(canStoreBlock && !rowIsForm);
				upperMenu.getInsertBlockButton().setEnabled(canEdit && !formHasLinkedForm);
				upperMenu.getInsertBlockButton().setVisible(!formIsBlock && !formHasLinkedForm);
				upperMenu.getNewCategoryButton().setEnabled(
						canEdit && (formIsBlockAndNoCategories || (!formIsBlock)) && !rowIsElementReference && !formHasLinkedForm);
				upperMenu.getNewGroupButton().setEnabled(canEdit && selectedRowHierarchyAllows(Group.class) && !rowIsElementReference && !formHasLinkedForm);
				upperMenu.getNewQuestionButton().setEnabled(
						canEdit && selectedRowHierarchyAllows(Question.class) && !rowIsElementReference && !formHasLinkedForm && !isTableRowDefinition());
				upperMenu.getNewSystemFieldButton().setEnabled(
						canEdit && selectedRowHierarchyAllows(SystemField.class) && !rowIsElementReference && !formHasLinkedForm && !isTableRowDefinition());
				upperMenu.getNewTextButton().setEnabled(
						canEdit && selectedRowHierarchyAllows(Text.class) && !rowIsElementReference && !formHasLinkedForm && !isTableRowDefinition());
				upperMenu.getNewAnswerButton().setEnabled(
						canEdit && selectedRowHierarchyAllows(Answer.class) && !rowIsElementReference && !formHasLinkedForm && !isTableRowDefinition());
				upperMenu.getNewDynamicAnswer().setEnabled(
						canEdit && selectedRowHierarchyAllows(DynamicAnswer.class) && !rowIsElementReference && !formHasLinkedForm && !isTableRowDefinition());
				upperMenu.getNewSubanswerButton().setEnabled(
						canEdit
								&& !formHasLinkedForm
								&& !rowIsElementReference
								&& selectedRowIsAnswer
								&& selectedRowHierarchyAllows(Answer.class)
								&& (isParentQuestionOfType(table.getSelectedRow(), AnswerType.SINGLE_SELECTION_RADIO) || isParentQuestionOfType(
										table.getSelectedRow(), AnswerType.MULTIPLE_SELECTION)));
				upperMenu.getMoveButton().setEnabled(canEdit && !rowIsNull && !rowIsForm && !rowIsElementReference && !formHasLinkedForm);
				upperMenu.getDeleteButton().setEnabled(
						canEdit && !rowIsNull && !rowIsForm && !formHasLinkedForm && (!rowIsElementReference || selectedElement instanceof Category));
				upperMenu.getUpButton().setEnabled(
						canEdit && !rowIsNull && !rowIsForm && (!rowIsElementReference || rowIsBlockReferenceCategory) && !formHasLinkedForm);
				upperMenu.getDownButton().setEnabled(
						canEdit && !rowIsNull && !rowIsForm && (!rowIsElementReference || rowIsBlockReferenceCategory) && !formHasLinkedForm);
				upperMenu.getFinish().setVisible(!formIsBlock);
				upperMenu.getFinish().setEnabled(!formIsBlock && canEdit);
				upperMenu.getDeleteButton().setVisible(!rowIsElementReference || rowIsBlockReferenceCategory);
				upperMenu.updateHideButton(isHidden);
				upperMenu.getHideButton().setVisible(!upperMenu.getDeleteButton().isVisible());
				upperMenu.getHideButton().setEnabled(rowIsElementReference && !rowIsBlockReferenceCategory && canEdit);
				upperMenu.getOtherElementsMenu().setEnabled(
						upperMenu.getNewSubanswerButton().isEnabled() || upperMenu.getNewTextButton().isEnabled()
								|| upperMenu.getNewSystemFieldButton().isEnabled());
			} else {
				disableMenu();
			}
		} catch (IOException | AuthenticationRequired e) {
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
			// Disable everything as a security measure.
			disableMenu();
		}
	}

	private void disableMenu() {
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
		upperMenu.getFinish().setEnabled(false);
	}

	private boolean isTableRowDefinition() {
		TreeObject selectedRow = table.getSelectedRow();
		if (selectedRow != null && selectedRow instanceof Group) {
			// parent is a group defining a table.
			if (((Group) selectedRow).isShownAsTable()) {
				return true;
			}
		}
		return false;
	}

	private boolean selectedRowHierarchyAllows(Class<? extends TreeObject> cls) {
		TreeObject selectedRow = table.getSelectedRow();
		return selectedRow != null && (selectedRow.getAncestorThatAccepts(cls) != null);
	}

	private boolean isParentQuestionOfType(TreeObject answer, AnswerType type) {
		if (answer != null && answer.getParent() != null && answer.getParent() instanceof Question) {
			return ((Question) answer.getParent()).getAnswerType().equals(type);
		} else if (answer != null && answer.getParent() != null && answer.getParent() instanceof Answer) {
			return isParentQuestionOfType(answer.getParent(), type);
		}
		return false;
	}

	/**
	 * Opens Save as New block window (String field)
	 */
	private void openNewBlockWindow() {
		final WindowNameGroup newBlockWindow = new WindowNameGroup(LanguageCodes.COMMON_CAPTION_NAME.translation(),
				LanguageCodes.COMMON_CAPTION_GROUP.translation(), new IActivity[] { WebformsActivity.BUILDING_BLOCK_EDITING });
		newBlockWindow.setCaption(LanguageCodes.CAPTION_NEW_BLOCK.translation());
		String name = table.getSelectedRow().getName();
		if (name != null && name.length() > 0) {
			newBlockWindow.setDefaultValue(name);
		} else {
			newBlockWindow.setDefaultValue(LanguageCodes.NULL_VALUE_NEW_BLOCK.translation());
		}
		newBlockWindow.showCentered();
		newBlockWindow.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				if (newBlockWindow.getValue() == null || newBlockWindow.getValue().isEmpty()) {
					MessageManager.showError(LanguageCodes.COMMON_WARNING_TITLE_BLOCK_NOT_CREATED, LanguageCodes.COMMON_WARNING_DESCRIPTION_BLOCK_NEEDS_NAME);
					return;
				}
				try {
					ApplicationUi.getController().saveAsBlock(table.getSelectedRow(), newBlockWindow.getValue(), newBlockWindow.getOrganization().getId());
					newBlockWindow.close();

					if (ApplicationUi.getController().getFormInUse() instanceof Block) {
						MessageManager.showInfo(LanguageCodes.INFO_MESSAGE_BLOCK_CAPTION_SAVE, LanguageCodes.INFO_MESSAGE_BLOCK_DESCRIPTION_SAVE);
					} else {
						MessageManager.showInfo(LanguageCodes.INFO_MESSAGE_FORM_CAPTION_SAVE, LanguageCodes.INFO_MESSAGE_FORM_DESCRIPTION_SAVE);
					}

				} catch (FieldTooLongException e) {
					MessageManager.showError(LanguageCodes.COMMON_ERROR_FIELD_TOO_LONG);
				} catch (FormWithSameNameException e) {
					MessageManager.showError(LanguageCodes.COMMON_ERROR_NAME_IS_IN_USE);
				} catch (UnexpectedDatabaseException e) {
					MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE, LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
				} catch (ElementCannotBeRemovedException e) {
					MessageManager.showError(LanguageCodes.ERROR_ELEMENT_CANNOT_BE_REMOVED_TITLE);
					WebformsUiLogger.errorMessage(this.getClass().getName(), e);
				} catch (ElementCannotBePersistedException e) {
					MessageManager.showError(LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED, LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED_DESCRIPTION);
					WebformsUiLogger.errorMessage(this.getClass().getName(), e);
				}
			}
		});
	}

	/**
	 * Opens Insert Block window
	 */
	private void openInsertBlock() {
		final WindowBlocks windowBlocks = new WindowBlocks(LanguageCodes.CAPTION_INSERT_NEW_BLOCK, null);
		windowBlocks.showCentered();
		windowBlocks.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				// Insert block in form
				try {
					if (windowBlocks.getSelectedBlock() != null) {
						TreeObject insertedElement = ApplicationUi.getController().insertBlock(windowBlocks.getSelectedBlock());
						clearAndUpdateFormTable();
						table.expand(insertedElement);
						table.setValue(insertedElement);
						window.close();
					} else {
						MessageManager.showError(LanguageCodes.ERROR_SELECT_BLOCK);
					}
				} catch (CategoryWithSameNameAlreadyExistsInForm e) {
					MessageManager.showError(LanguageCodes.ERROR_CAPTION_NOT_ALLOWED, LanguageCodes.WARNING_DESCRIPTION_REPEATED_CATEGORY_NAME);
				} catch (EmptyBlockCannotBeInserted e) {
					MessageManager.showError(LanguageCodes.ERROR_CAPTION_NOT_ALLOWED, LanguageCodes.WARNING_DESCRIPTION_EMPTY_BLOCK);
				} catch (ElementIsReadOnly e) {
					MessageManager.showError(LanguageCodes.ERROR_READ_ONLY_ELEMENT);
				}
			}
		});
	}

	private void openLinkBlock() {
		Set<Class<? extends TreeObject>> selectableElements = new HashSet<>();
		selectableElements.add(Block.class);
		final WindowBlocks windowBlocks = new WindowBlocks(LanguageCodes.CAPTION_LINK_BLOCK, selectableElements);
		windowBlocks.showCentered();
		windowBlocks.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				// Insert block in form
				try {
					TreeObject linkedElement = ApplicationUi.getController().linkBlock(windowBlocks.getSelectedBlock());
					clearAndUpdateFormTable();
					table.expand(linkedElement);
					table.setValue(linkedElement);
					window.close();
				} catch (CategoryWithSameNameAlreadyExistsInForm e) {
					MessageManager.showError(LanguageCodes.ERROR_CAPTION_NOT_ALLOWED, LanguageCodes.WARNING_DESCRIPTION_REPEATED_CATEGORY_NAME);
				} catch (EmptyBlockCannotBeInserted e) {
					MessageManager.showError(LanguageCodes.ERROR_CAPTION_NOT_ALLOWED, LanguageCodes.WARNING_DESCRIPTION_EMPTY_BLOCK);
				} catch (ElementIsReadOnly e) {
					MessageManager.showError(LanguageCodes.ERROR_READ_ONLY_ELEMENT);
				} catch (LinkCanOnlyBePerformedOnWholeBlock e) {
					MessageManager.showError(LanguageCodes.ERROR_LINK_BLOCK_NOT_COMPLETE);
				}
			}
		});
	}

	/**
	 * Opens move element window.
	 */
	private void openMoveWindow() {
		final WindowTreeObject moveWindow = new WindowTreeObject(LanguageCodes.CAPTION_WINDOW_MOVE, getCurrentForm(), Form.class, Category.class, Group.class);
		moveWindow.showCentered();
		moveWindow.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				try {
					TreeObject whatToMove = table.getSelectedRow();
					TreeObject whereToMove = moveWindow.getSelectedTreeObject();
					if (!whereToMove.isReadOnly()) {
						TreeObject movedObjectNewInstance = ApplicationUi.getController().moveTo(whatToMove, whereToMove);
						window.close();
						table.setValue(null);
						table.removeRow(whatToMove);

						// Remove collapse state update listeners
						removeCollapseStateListeners();
						table.loadTreeObject(movedObjectNewInstance, whereToMove, false);
						// Retrieve old collapse state (also listeners)
						retrieveCollapsedTableState();
						table.expand(movedObjectNewInstance);

						// Fix to force a jump to this point in table.
						table.setValue(null);
						table.setValue(movedObjectNewInstance);
					} else {
						MessageManager.showError(LanguageCodes.ERROR_READ_ONLY_ELEMENT);
					}
				} catch (NotValidChildException e) {
					MessageManager.showError(LanguageCodes.WARNING_CAPTION_NOT_VALID, LanguageCodes.WARNING_DESCRIPTION_NOT_VALID);
				} catch (SameOriginAndDestinationException e) {
					MessageManager.showError(LanguageCodes.WARNING_CAPTION_SAME_ORIGIN, LanguageCodes.WARNING_DESCRIPTION_SAME_ORIGIN);
				} catch (DestinyIsContainedAtOrigin | ChildrenNotFoundException e) {
					MessageManager.showError(LanguageCodes.WARNING_CAPTION_SAME_ORIGIN, LanguageCodes.WARNING_DESCRIPTION_ORIGIN_INCLUDED_IN_DESTINY);
				} catch (ElementIsReadOnly e) {
					MessageManager.showError(LanguageCodes.ERROR_READ_ONLY_ELEMENT);
				}
			}
		});
	}

	protected void saveCollapsedTableState() {
		ApplicationUi.getController().setCollapsedStatus(table.getCollapsedStatus(ApplicationUi.getController().getFormInUse()));
	}

	private void removeCollapseStateListeners() {
		table.removeCollapseListener(collapseListener);
		table.removeExpandListener(expandListener);
	}

	private void addCollapseStateListeners() {
		table.addCollapseListener(collapseListener);
		table.addExpandListener(expandListener);
	}

	private void retrieveCollapsedTableState() {
		removeCollapseStateListeners();
		if (ApplicationUi.getController().getCollapsedStatus() != null) {
			table.setCollapsedStatus(ApplicationUi.getController().getFormInUse(), ApplicationUi.getController().getCollapsedStatus());
		}
		addCollapseStateListeners();

	}

	private void clearAndUpdateFormTable() {
		// Clear and update form
		TreeObject currentSelection = table.getSelectedRow();
		table.setValue(null);
		table.removeAllItems();

		// Remove collapsed state listeners, load the new tree and recover the
		// old state and the update state listeners.
		removeCollapseStateListeners();
		table.loadTreeObject(getCurrentForm(), null);
		retrieveCollapsedTableState();

		if (currentSelection != null) {
			if (currentSelection instanceof Form || currentSelection instanceof Block) {
				table.setValue(getCurrentForm());
			} else {
				table.setValue(getCurrentForm().findByComparationId(currentSelection.getComparationId()));
			}
		}
	}

	private Form getCurrentForm() {
		return ApplicationUi.getController().getCompleteFormView();
	}

	public boolean isBlockLinkedByFormInUse(Block block) {
		List<SimpleFormView> forms = simpleFormViewDao.getFormsThatUse(block);
		for (SimpleFormView form : forms) {
			if (getWebformsSecurityService().isFormInUse(form)) {
				return true;
			}
		}
		return false;
	}

}
