package com.biit.webforms.gui.webpages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.form.BaseQuestion;
import com.biit.form.TreeObject;
import com.biit.liferay.security.IActivity;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.authentication.WebformsActivity;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.flow.FlowCleaner;
import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.SecuredWebPage;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowTextArea;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
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
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.exceptions.BadFlowContentException;
import com.biit.webforms.persistence.entity.exceptions.FlowDestinyIsBeforeOrigin;
import com.biit.webforms.persistence.entity.exceptions.FlowSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutDestiny;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutSource;
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
	private static final List<IActivity> activityPermissions = new ArrayList<IActivity>(
			Arrays.asList(WebformsActivity.READ));
	private static final double ZOOM_MIN_VALUE = 1.0f;
	private static final double ZOOM_MAX_VALUE = 50.0f;

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

		if (UserSessionHandler.getController().getFormInUse() != null
				&& !WebformsAuthorizationService.getInstance().isFormEditable(
						UserSessionHandler.getController().getFormInUse(), UserSessionHandler.getUser())) {
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
		Set<Flow> flows = UserSessionHandler.getController().getFormInUseFlows();
		tableFlows.addRows(flows);
		tableFlows.sortByUpdateDate(false);
	}

	private Component createLeftComponent() {
		VerticalLayout i = new VerticalLayout();
		i.setSizeFull();

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

		i.addComponent(tableFilterBar);
		i.addComponent(tableFlows);
		i.setExpandRatio(tableFlows, 1.0f);

		return i;
	}

	private Component createTableFilterBar() {
		HorizontalLayout i = new HorizontalLayout();
		i.setMargin(true);
		i.setSpacing(true);
		i.setWidth("100%");

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
				Filterable f = (Filterable) tableFlows.getContainerDataSource();
				if (destinyFilter != null) {
					f.removeContainerFilter(destinyFilter);
				}
				destinyFilter = new DestinyFilter((TreeObject) tableFilterDestiny.getValue(), tableFlows.getNewFlowId());
				f.addContainerFilter(destinyFilter);
			}
		});

		i.addComponent(tableFilterOrigin);
		i.addComponent(tableFilterDestiny);
		i.setExpandRatio(tableFilterOrigin, 0.5f);
		i.setExpandRatio(tableFilterDestiny, 0.5f);
		i.setComponentAlignment(tableFilterOrigin, Alignment.MIDDLE_LEFT);
		i.setComponentAlignment(tableFilterDestiny, Alignment.MIDDLE_RIGHT);

		return i;
	}

	private void updateUiState() {

		boolean somethingSelected = false;
		boolean selectedNew = false;
		@SuppressWarnings("unchecked")
		Set<Object> itemIds = (Set<Object>) tableFlows.getValue();
		if (itemIds == null || itemIds.isEmpty()) {
			somethingSelected = false;
		} else {
			somethingSelected = true;
		}
		boolean multipleSelection = itemIds.size() > 1;
		for (Object itemId : itemIds) {
			selectedNew = itemId != null && itemId.equals(tableFlows.getNewFlowId());
		}
		boolean canEdit = WebformsAuthorizationService.getInstance().isFormEditable(
				UserSessionHandler.getController().getFormInUse(), UserSessionHandler.getUser());

		// Top button state
		upperMenu.getSaveButton().setEnabled(canEdit);
		upperMenu.getNewFlowButton().setEnabled(canEdit);
		upperMenu.getEditFlowButton().setEnabled(canEdit && !selectedNew && !multipleSelection && somethingSelected);
		upperMenu.getCloneFlowButton().setEnabled(canEdit && !selectedNew && somethingSelected);
		upperMenu.getRemoveFlowButton().setEnabled(canEdit && !selectedNew && somethingSelected);
		upperMenu.getCleanFlowButton().setEnabled(canEdit);
	}

	private Component createRightComponent() {
		VerticalLayout i = new VerticalLayout();
		i.setSizeFull();

		formFlowViewer = new FormFlowViewer(ImgType.SVG, 1.0f);
		formFlowViewer.setSizeFull();
		formFlowViewer.setFormAndFilter(UserSessionHandler.getController().getFormInUse(), null);
		formFlowViewer.addZoomChangedListener(formFlowViewerZoomListener);

		Component flowViewerControlBar = createFlowViewerControlBar();

		i.addComponent(flowViewerControlBar);
		i.addComponent(formFlowViewer);
		i.setExpandRatio(formFlowViewer, 1.0f);

		return i;
	}

	private class FormFlowViewerZoomListener implements ZoomChangedListener {

		@Override
		public void zoomChanged(float zoom) {
			zoomSlider.removeValueChangeListener(zoomSliderValueChangeListener);
			zoomSlider.setValue((double) zoom);
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

		IconButton redrawButton = new IconButton(LanguageCodes.CAPTION_REDRAW, ThemeIcons.RULE_DIAGRAM_REDRAW,
				LanguageCodes.TOOLTIP_REDRAW);
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
		zoomSlider.setMin(ZOOM_MIN_VALUE);
		zoomSlider.setMax(ZOOM_MAX_VALUE);
		zoomSlider.setValue(ZOOM_MIN_VALUE);
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
		zoomSlider.setValue(ZOOM_MIN_VALUE);
		formFlowViewer.setFormAndFilter(UserSessionHandler.getController().getFormInUse(), filter);
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
					MessageManager.showInfo(LanguageCodes.INFO_MESSAGE_CAPTION_SAVE,
							LanguageCodes.INFO_MESSAGE_DESCRIPTION_SAVE);
				} catch (UnexpectedDatabaseException e) {
					MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
							LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
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
					}
					if (tableFlows.getValue() instanceof Set<?>) {
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
					Set<Flow> clones = UserSessionHandler.getController().cloneFlowsAndInsertIntoForm(selectedFlows);
					addOrUpdateFlowInTableAction(clones.toArray(new Flow[0]));
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
		FlowCleaner flowCleaner = new FlowCleaner(UserSessionHandler.getController().getFormInUse());
		flowCleaner.cleanFlow();
		tableFlows.setRows(UserSessionHandler.getController().getFormInUseFlows());
		if (!flowCleaner.getOtherFlowsRemoved().isEmpty() || !flowCleaner.getUselessFlowRemoved().isEmpty()) {
			WindowTextArea report = new WindowTextArea("");
			report.setCaption("");
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
		boolean canEdit = WebformsAuthorizationService.getInstance().isFormEditable(
				UserSessionHandler.getController().getFormInUse(), UserSessionHandler.getUser());
		if (canEdit) {
			createFlowWindow(new Flow());
		}
	}

	/**
	 * This method takes a existing flow and opens flow window with the parameters assigned in the flow to edit.
	 * 
	 * @param flow
	 */
	private void editFlowAction(Flow flow) {
		boolean canEdit = WebformsAuthorizationService.getInstance().isFormEditable(
				UserSessionHandler.getController().getFormInUse(), UserSessionHandler.getUser());
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
						MessageManager.showWarning(LanguageCodes.WARNING_CAPTION_RULE_NOT_CORRECT,
								LanguageCodes.WARNING_DESCRIPTION_CONDITION_BAD_FORMED);
					} else {
						UserSessionHandler.getController().updateFlowContent(flow, windowFlow.getOrigin(),
								windowFlow.getFlowType(), windowFlow.getDestiny(), windowFlow.isOthers(),
								windowFlow.getCondition());
						addOrUpdateFlowInTableAction(flow);
						window.close();
					}
				} catch (BadFlowContentException e) {
					MessageManager.showWarning(LanguageCodes.WARNING_CAPTION_RULE_NOT_CORRECT,
							LanguageCodes.WARNING_DESCRIPTION_RULE_BAD_FORMED);
					WebformsLogger.errorMessage(this.getClass().getName(), e);
				} catch (FlowWithoutSource e) {
					MessageManager.showWarning(LanguageCodes.WARNING_CAPTION_RULE_NOT_CORRECT,
							LanguageCodes.WARNING_DESCRIPTION_ORIGIN_IS_NULL);
				} catch (FlowSameOriginAndDestinyException e) {
					MessageManager.showWarning(LanguageCodes.WARNING_CAPTION_RULE_NOT_CORRECT,
							LanguageCodes.WARNING_DESCRIPTION_SAME_ORIGIN_AND_DESTINY);
				} catch (FlowDestinyIsBeforeOrigin e) {
					MessageManager.showWarning(LanguageCodes.WARNING_CAPTION_RULE_NOT_CORRECT,
							LanguageCodes.WARNING_DESCRIPTION_DESTINY_IS_BEFORE_ORIGIN);
				} catch (FlowWithoutDestiny e) {
					MessageManager.showWarning(LanguageCodes.WARNING_CAPTION_RULE_NOT_CORRECT,
							LanguageCodes.WARNING_DESCRIPTION_DESTINY_IS_NULL);
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
