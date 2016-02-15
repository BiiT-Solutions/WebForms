package com.biit.webforms.gui.webpages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.usermanager.security.IActivity;
import com.biit.webforms.flow.FlowCleaner;
import com.biit.webforms.gui.UserSessionHandler;
import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.SecuredWebPage;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.common.components.WindowTextArea;
import com.biit.webforms.gui.common.language.ServerTranslate;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.components.FormEditBottomMenu;
import com.biit.webforms.gui.components.FormFlowViewer;
import com.biit.webforms.gui.components.ZoomChangedListener;
import com.biit.webforms.gui.webpages.floweditor.DestinyFilter;
import com.biit.webforms.gui.webpages.floweditor.OriginFilter;
import com.biit.webforms.gui.webpages.floweditor.SearchFormElementField;
import com.biit.webforms.gui.webpages.floweditor.SearchFormElementField.SearchFormElementChanged;
import com.biit.webforms.gui.webpages.floweditor.TableFlows;
import com.biit.webforms.gui.webpages.floweditor.TableFlows.NewItemAction;
import com.biit.webforms.gui.webpages.floweditor.UpperMenuFlowEditor;
import com.biit.webforms.gui.webpages.floweditor.WindowFlow;
import com.biit.webforms.gui.webpages.floweditor.listeners.EditItemAction;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Block;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.exceptions.BadFlowContentException;
import com.biit.webforms.persistence.entity.exceptions.FlowDestinyIsBeforeOriginException;
import com.biit.webforms.persistence.entity.exceptions.FlowNotAllowedException;
import com.biit.webforms.persistence.entity.exceptions.FlowSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutSourceException;
import com.biit.webforms.security.WebformsActivity;
import com.biit.webforms.theme.ThemeIcons;
import com.biit.webforms.utils.GraphvizApp.ImgType;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Slider;
import com.vaadin.ui.VerticalLayout;

public class FlowEditor extends SecuredWebPage {
	private static final long serialVersionUID = -6257723403353946354L;
	private static final List<IActivity> activityPermissions = new ArrayList<IActivity>(Arrays.asList(WebformsActivity.READ));

	private UpperMenuFlowEditor upperMenu;
	private TableFlows tableFlows;
	private FormFlowViewer formFlowViewer;
	private FormFlowViewerZoomListener formFlowViewerZoomListener;
	private SearchFormElementField tableFilterOrigin;
	private SearchFormElementField tableFilterDestiny;
	private SearchFormElementField flowViewerFilter;
	private Slider zoomSlider;
	private ZoomSliderValueChangeListener zoomSliderValueChangeListener;

	private OriginFilter originFilter;
	private OriginFilter destinyFilter;

	@Override
	protected void initContent() {
		formFlowViewerZoomListener = new FormFlowViewerZoomListener();
		zoomSliderValueChangeListener = new ZoomSliderValueChangeListener();

		if (UserSessionHandler.getController().getCompleteFormView() != null
				&& !getWebformsSecurityService().isFormEditable(UserSessionHandler.getController().getCompleteFormView(),
						UserSessionHandler.getUser())) {
			MessageManager.showWarning(LanguageCodes.INFO_MESSAGE_FORM_IS_READ_ONLY);
		}

		setCentralPanelAsWorkingArea();
		upperMenu = createUpperMenu();
		setUpperMenu(upperMenu);
		setBottomMenu(new FormEditBottomMenu());
		getWorkingArea().addComponent(generateContent());
		initializeContent();
		updateUiState();
	}

	private Component generateContent() {
		HorizontalSplitPanel horizontalSplitPanel = new HorizontalSplitPanel();
		horizontalSplitPanel.setSizeFull();
		horizontalSplitPanel.setFirstComponent(createLeftComponent());
		horizontalSplitPanel.setSecondComponent(createRightComponent());
		horizontalSplitPanel.setSplitPosition(50.0f);

		return horizontalSplitPanel;
	}

	private void initializeContent() {
		Set<Flow> flows = UserSessionHandler.getController().getCompleteFormView().getFlows();
		tableFlows.addRows(flows);
		tableFlows.sortByUpdateDate(false);
	}

	private Component createLeftComponent() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		Component tableFilterBar = createTableFilterBar();

		tableFlows = new TableFlows();
		tableFlows.setMultiSelect(true);
		tableFlows.setSizeFull();
		tableFlows.addNewItemActionListener(new NewItemAction() {

			@Override
			public void newItemAction() {
				addNewFlowAction();
			}
		});
		tableFlows.addEditItemActionListener(new EditItemAction() {

			@Override
			public void editItemAction(Flow flow) {
				editFlowAction(flow);
			}
		});
		tableFlows.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -4525037694598967266L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateUiState();
			}
		});
		// Shortcut to remove flows with delete key.
		tableFlows.addShortcutListener(new ShortcutListener("REMOVE_RULES", KeyCode.DELETE, null) {
			private static final long serialVersionUID = -6749245457349356221L;

			@Override
			public void handleAction(Object sender, Object target) {
				removeSelectedFlows();
			}
		});

		layout.addComponent(tableFilterBar);
		layout.addComponent(tableFlows);
		layout.setExpandRatio(tableFlows, 1.0f);

		return layout;
	}

	private Component createTableFilterBar() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setWidth("100%");

		tableFilterOrigin = new SearchFormElementField(Form.class, Category.class, Group.class, BaseQuestion.class);
		tableFilterOrigin.setCaption(LanguageCodes.CAPTION_FILTER_ORIGIN.translation());
		tableFilterOrigin.addValueChangeListener(new SearchFormElementChanged() {

			@Override
			public void currentElement(Object object) {
				Filterable f = (Filterable) tableFlows.getContainerDataSource();
				if (originFilter != null) {
					f.removeContainerFilter(originFilter);
				}
				originFilter = new OriginFilter((TreeObject) tableFilterOrigin.getValue(), tableFlows.getNewFlowId());
				f.addContainerFilter(originFilter);
			}
		});
		tableFilterDestiny = new SearchFormElementField(Form.class, Category.class, Group.class, BaseQuestion.class);
		tableFilterDestiny.setCaption(LanguageCodes.CAPTION_FILTER_DESTINY.translation());
		tableFilterDestiny.addValueChangeListener(new SearchFormElementChanged() {

			@Override
			public void currentElement(Object object) {
				Filterable filterable = (Filterable) tableFlows.getContainerDataSource();
				if (destinyFilter != null) {
					filterable.removeContainerFilter(destinyFilter);
				}
				destinyFilter = new DestinyFilter((TreeObject) tableFilterDestiny.getValue(), tableFlows.getNewFlowId());
				filterable.addContainerFilter(destinyFilter);
			}
		});

		layout.addComponent(tableFilterOrigin);
		layout.addComponent(tableFilterDestiny);
		layout.setExpandRatio(tableFilterOrigin, 0.5f);
		layout.setExpandRatio(tableFilterDestiny, 0.5f);
		layout.setComponentAlignment(tableFilterOrigin, Alignment.MIDDLE_LEFT);
		layout.setComponentAlignment(tableFilterDestiny, Alignment.MIDDLE_RIGHT);

		return layout;
	}

	private void updateUiState() {

		@SuppressWarnings("unchecked")
		Set<Object> itemIds = (Set<Object>) tableFlows.getValue();
		boolean somethingSelected = !itemIds.isEmpty();
		boolean multipleSelection = itemIds.size() > 1;
		boolean selectedNew = false;
		for (Object itemId : itemIds) {
			selectedNew = itemId != null && itemId.equals(tableFlows.getNewFlowId());
		}
		boolean canEdit = getWebformsSecurityService().isFormEditable(UserSessionHandler.getController().getFormInUse(),
				UserSessionHandler.getUser());

		Object selectedRow = null;
		if (somethingSelected) {
			selectedRow = itemIds.iterator().next();
		}
		boolean elementIsReadOnly = (selectedRow != null) && (selectedRow instanceof Flow) && ((Flow) selectedRow).isReadOnly();

		// Top button state
		upperMenu.getSaveButton().setEnabled(canEdit);
		upperMenu.getNewFlowButton().setEnabled(canEdit);
		upperMenu.getEditFlowButton().setEnabled(canEdit && !selectedNew && !multipleSelection && somethingSelected && !elementIsReadOnly);
		upperMenu.getCloneFlowButton().setEnabled(canEdit && !selectedNew && somethingSelected);
		upperMenu.getRemoveFlowButton().setEnabled(canEdit && !selectedNew && somethingSelected && !elementIsReadOnly);
		upperMenu.getCleanFlowButton().setEnabled(canEdit);
	}

	private Component createRightComponent() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		formFlowViewer = new FormFlowViewer(ImgType.SVG, 1.0f);
		formFlowViewer.setSizeFull();
		formFlowViewer.setFormAndFilter(UserSessionHandler.getController().getCompleteFormView(), null);
		formFlowViewer.addZoomChangedListener(formFlowViewerZoomListener);

		Component flowViewerControlBar = createFlowViewerControlBar();

		layout.addComponent(flowViewerControlBar);
		layout.addComponent(formFlowViewer);
		layout.setExpandRatio(formFlowViewer, 1.0f);

		return layout;
	}

	private class FormFlowViewerZoomListener implements ZoomChangedListener {

		@Override
		public void zoomChanged(double zoom) {
			zoomSlider.removeValueChangeListener(zoomSliderValueChangeListener);
			zoomSlider.setValue(zoom);
			zoomSlider.addValueChangeListener(zoomSliderValueChangeListener);
		}

	}

	private class ZoomSliderValueChangeListener implements ValueChangeListener {
		private static final long serialVersionUID = 5649221005286915625L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			double currentValue = (double) event.getProperty().getValue();
			formFlowViewer.removeZoomChangedListener(formFlowViewerZoomListener);
			formFlowViewer.setZoom((float) currentValue);
			formFlowViewer.addZoomChangedListener(formFlowViewerZoomListener);
		}
	}

	private Component createFlowViewerControlBar() {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);
		horizontalLayout.setWidth("100%");

		flowViewerFilter = new SearchFormElementField(Form.class, Category.class, Group.class);
		flowViewerFilter.setCaption(LanguageCodes.CAPTION_FILTER.translation());
		flowViewerFilter.setSelectableFilter(Category.class, Group.class);
		flowViewerFilter.addValueChangeListener(new SearchFormElementChanged() {

			@Override
			public void currentElement(Object object) {
				filterFlowDiagram((TreeObject) object);
			}
		});

		IconButton redrawButton = new IconButton(LanguageCodes.CAPTION_REDRAW, ThemeIcons.RULE_DIAGRAM_REDRAW, LanguageCodes.TOOLTIP_REDRAW);
		redrawButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7689117025171099202L;

			@Override
			public void buttonClick(ClickEvent event) {
				formFlowViewer.setZoom(1.0f);
				formFlowViewer.redraw();
				zoomSlider.removeValueChangeListener(zoomSliderValueChangeListener);
				zoomSlider.setValue(1.0);
				zoomSlider.addValueChangeListener(zoomSliderValueChangeListener);
			}
		});

		zoomSlider = new Slider();
		zoomSlider.setCaption(LanguageCodes.CAPTION_ZOOM_SLIDER.translation());
		zoomSlider.setMin(FormFlowViewer.MIN_AUGMENT);
		zoomSlider.setMax(FormFlowViewer.MAX_AUGMENT);
		zoomSlider.setValue(FormFlowViewer.MIN_AUGMENT);
		zoomSlider.setWidth("100%");
		zoomSlider.addValueChangeListener(zoomSliderValueChangeListener);

		horizontalLayout.addComponent(flowViewerFilter);
		horizontalLayout.addComponent(redrawButton);
		horizontalLayout.addComponent(zoomSlider);
		horizontalLayout.setExpandRatio(flowViewerFilter, 0.4f);
		horizontalLayout.setExpandRatio(zoomSlider, 0.4f);
		horizontalLayout.setComponentAlignment(flowViewerFilter, Alignment.MIDDLE_LEFT);
		horizontalLayout.setComponentAlignment(redrawButton, Alignment.BOTTOM_CENTER);
		horizontalLayout.setComponentAlignment(zoomSlider, Alignment.MIDDLE_RIGHT);

		return horizontalLayout;
	}

	protected void filterFlowDiagram(TreeObject filter) {
		zoomSlider.setValue(FormFlowViewer.MIN_AUGMENT);
		formFlowViewer.setFormAndFilter(UserSessionHandler.getController().getCompleteFormView(), filter);
	}

	@Override
	public List<IActivity> accessAuthorizationsRequired() {
		return activityPermissions;
	}

	private UpperMenuFlowEditor createUpperMenu() {
		UpperMenuFlowEditor upperMenu = new UpperMenuFlowEditor();
		upperMenu.addSaveButtonListener(new ClickListener() {
			private static final long serialVersionUID = 1679355377155929573L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					UserSessionHandler.getController().saveForm();
					// Refresh the table.
					// Now the form has changes so current selected elements are
					// not exactly the same as the ones
					// in the form, so we search for the new instances and
					// replace the selection.
					// Also we redraw the current form.
					@SuppressWarnings("unchecked")
					Set<Object> selectedObjects = new HashSet<Object>((Set<Object>) tableFlows.getValue());
					Set<Object> newSelectedObjects = new HashSet<Object>();
					tableFlows.setRows(UserSessionHandler.getController().getCompleteFormView().getFlows());
					for (Flow newFlow : UserSessionHandler.getController().getCompleteFormView().getFlows()) {
						if (selectedObjects.contains(newFlow)) {
							newSelectedObjects.add(newFlow);
							selectedObjects.remove(newFlow);
							if (selectedObjects.isEmpty()) {
								break;
							}
						}
					}
					tableFlows.setValue(null);
					tableFlows.setValue(newSelectedObjects);
					formFlowViewer.setFormAndFilter(UserSessionHandler.getController().getCompleteFormView(), null);

					if (UserSessionHandler.getController().getFormInUse() instanceof Block) {
						MessageManager.showInfo(LanguageCodes.INFO_MESSAGE_BLOCK_CAPTION_SAVE,
								LanguageCodes.INFO_MESSAGE_BLOCK_DESCRIPTION_SAVE);
					} else {
						MessageManager.showInfo(LanguageCodes.INFO_MESSAGE_FORM_CAPTION_SAVE,
								LanguageCodes.INFO_MESSAGE_FORM_DESCRIPTION_SAVE);
					}
				} catch (UnexpectedDatabaseException e) {
					MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE, LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
				} catch (ElementCannotBePersistedException e) {
					MessageManager.showError(LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED,
							LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED_DESCRIPTION);
					WebformsUiLogger.errorMessage(this.getClass().getName(), e);
				}

			}
		});
		upperMenu.addNewFlowButtonListener(new ClickListener() {
			private static final long serialVersionUID = 9116553626932253896L;

			@Override
			public void buttonClick(ClickEvent event) {
				addNewFlowAction();
			}
		});
		upperMenu.addEditFlowButtonListener(new ClickListener() {
			private static final long serialVersionUID = 9116553626932253896L;

			@SuppressWarnings("unchecked")
			@Override
			public void buttonClick(ClickEvent event) {
				if (tableFlows != null) {
					Flow selectedFlow = null;
					if (tableFlows.getValue() instanceof Flow) {
						selectedFlow = (Flow) tableFlows.getValue();
					} else if (tableFlows.getValue() instanceof Set<?>) {
						selectedFlow = (Flow) ((Set<Object>) tableFlows.getValue()).iterator().next();
					}
					editFlowAction(selectedFlow);
				}
			}
		});
		upperMenu.addCloneFlowButtonListener(new ClickListener() {
			private static final long serialVersionUID = -151662907240066702L;

			@Override
			public void buttonClick(ClickEvent event) {
				@SuppressWarnings("unchecked")
				Set<Object> selectedObjects = (Set<Object>) tableFlows.getValue();
				if (selectedObjects != null) {
					Set<Flow> selectedFlows = new HashSet<Flow>();
					for (Object selectedObject : selectedObjects) {
						if (selectedObject instanceof Flow) {
							selectedFlows.add((Flow) selectedObject);
						}
					}
					Set<Flow> clones;
					try {
						clones = UserSessionHandler.getController().cloneFlowsAndInsertIntoForm(selectedFlows);
						addOrUpdateFlowInTableAction(clones.toArray(new Flow[0]));
					} catch (FlowNotAllowedException e) {
						// Not possible.
						WebformsUiLogger.errorMessage(this.getClass().getName(), e);
					}
				}
			}
		});
		upperMenu.addRemoveFlowButtonListener(new ClickListener() {
			private static final long serialVersionUID = -5479947134511433646L;

			@Override
			public void buttonClick(ClickEvent event) {
				removeSelectedFlows();
			}
		});
		upperMenu.addCleanFlowButtonListener(new ClickListener() {
			private static final long serialVersionUID = 5705204419397542939L;

			@Override
			public void buttonClick(ClickEvent event) {
				cleanFlowOfForm();
			}
		});

		return upperMenu;
	}

	private void cleanFlowOfForm() {
		FlowCleaner flowCleaner = new FlowCleaner(UserSessionHandler.getController().getCompleteFormView());
		flowCleaner.cleanFlow();
		tableFlows.setRows(UserSessionHandler.getController().getCompleteFormView().getFlows());
		tableFlows.sortByUpdateDate(false);
		if (!flowCleaner.getOtherFlowsRemoved().isEmpty() || !flowCleaner.getUselessFlowRemoved().isEmpty()) {
			StringBuilder report = new StringBuilder();
			for (Flow flow : flowCleaner.getOtherFlowsRemoved()) {
				report.append(ServerTranslate.translate(LanguageCodes.CLEAN_FLOW_REPORT_OTHERS_RULE, new Object[] {
						flow.getOrigin().getPathName(), flow.getDestiny().getPathName() }));
				report.append(System.lineSeparator());
			}
			for (Flow flow : flowCleaner.getUselessFlowRemoved()) {
				report.append(ServerTranslate.translate(LanguageCodes.CLEAN_FLOW_REPORT_USELESS_FLOW, new Object[] {
						flow.getOrigin().getPathName(), flow.getDestiny().getPathName() }));
				report.append(System.lineSeparator());
			}
			WindowTextArea reportWindow = new WindowTextArea(LanguageCodes.CLEAN_FLOW_REPORT_CAPTION.translation());
			reportWindow.setCaption(LanguageCodes.CLEAN_FLOW_REPORT_TITLE.translation());
			reportWindow.setValue(report.toString());
			reportWindow.setTextReadOnly(true);
			reportWindow.showCentered();
			reportWindow.setWidth("80%");
			reportWindow.setHeight("80%");
		} else {
			MessageManager.showInfo(LanguageCodes.CLEAN_FLOW_REPORT_NO_RULES_CHANGED);
		}
	}

	private void removeSelectedFlows() {
		@SuppressWarnings("unchecked")
		Set<Object> selectedObjects = (Set<Object>) tableFlows.getValue();
		Set<Flow> flowsToDelete = new HashSet<Flow>();
		for (Object selectedObject : selectedObjects) {
			if (selectedObject instanceof Flow) {
				flowsToDelete.add((Flow) selectedObject);
			}
		}
		UserSessionHandler.getController().removeFlows(flowsToDelete);
		removeFlowsFromTable(flowsToDelete);
	}

	/**
	 * This method opens the new flow window
	 */
	private void addNewFlowAction() {
		boolean canEdit = getWebformsSecurityService().isFormEditable(UserSessionHandler.getController().getFormInUse(),
				UserSessionHandler.getUser());
		if (canEdit) {
			createFlowWindow(new Flow());
		}
	}

	/**
	 * This method takes a existing flow and opens flow window with the
	 * parameters assigned in the flow to edit.
	 * 
	 * @param flow
	 */
	private void editFlowAction(Flow flow) {
		boolean canEdit = getWebformsSecurityService().isFormEditable(UserSessionHandler.getController().getFormInUse(),
				UserSessionHandler.getUser());
		if (canEdit) {
			createFlowWindow(flow);
		}
	}

	/**
	 * Opens the window to create a new flow and controls the return conditions.
	 * 
	 * @param flow
	 */
	private void createFlowWindow(final Flow flow) {
		WindowFlow window = new WindowFlow();
		window.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				try {
					WindowFlow windowFlow = (WindowFlow) window;
					if (!windowFlow.isConditionValid()) {
						MessageManager.showError(LanguageCodes.ERROR_CAPTION_RULE_NOT_CORRECT,
								LanguageCodes.ERROR_DESCRIPTION_CONDITION_BAD_FORMED);
					} else {
						UserSessionHandler.getController().updateFlowContent(flow, (BaseQuestion) windowFlow.getOrigin(),
								windowFlow.getFlowType(), (BaseQuestion) windowFlow.getDestiny(), windowFlow.isOthers(),
								windowFlow.getCondition());
						addOrUpdateFlowInTableAction(flow);
						window.close();
					}
				} catch (BadFlowContentException e) {
					MessageManager.showError(LanguageCodes.ERROR_CAPTION_RULE_NOT_CORRECT, LanguageCodes.ERROR_DESCRIPTION_RULE_BAD_FORMED);
					WebformsUiLogger.errorMessage(this.getClass().getName(), e);
				} catch (FlowWithoutSourceException e) {
					MessageManager.showError(LanguageCodes.ERROR_CAPTION_RULE_NOT_CORRECT, LanguageCodes.ERROR_DESCRIPTION_ORIGIN_IS_NULL);
				} catch (FlowSameOriginAndDestinyException e) {
					MessageManager.showError(LanguageCodes.ERROR_CAPTION_RULE_NOT_CORRECT,
							LanguageCodes.ERROR_DESCRIPTION_SAME_ORIGIN_AND_DESTINY);
				} catch (FlowDestinyIsBeforeOriginException e) {
					MessageManager.showError(LanguageCodes.ERROR_CAPTION_RULE_NOT_CORRECT,
							LanguageCodes.ERROR_DESCRIPTION_DESTINY_IS_BEFORE_ORIGIN);
				} catch (FlowWithoutDestinyException e) {
					MessageManager.showError(LanguageCodes.ERROR_CAPTION_RULE_NOT_CORRECT, LanguageCodes.ERROR_DESCRIPTION_DESTINY_IS_NULL);
				} catch (FlowNotAllowedException e) {
					MessageManager.showError(LanguageCodes.ERROR_CAPTION_RULE_NOT_CORRECT, LanguageCodes.ERROR_READ_ONLY_ELEMENT);
				}
			}
		});
		window.setFlow(flow);
		window.showCentered();
	}

	private void addOrUpdateFlowInTableAction(Flow... newFlows) {
		tableFlows.addOrUpdateFlows(newFlows);
	}

	private void removeFlowsFromTable(Set<Flow> flows) {
		for (Flow flow : flows) {
			tableFlows.removeItem(flow);
		}
	}
}
