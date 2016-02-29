package com.biit.webforms.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.biit.form.entity.IBaseFormView;
import com.biit.usermanager.entity.IUser;
import com.biit.webforms.persistence.entity.Form;
import com.vaadin.ui.UI;

public class UiAccesser {
	static ExecutorService executorService = Executors.newSingleThreadExecutor();

	public interface BroadcastListener {
		void receiveBroadcast(String message);
	}

	private static HashMap<String, IUser<Long>> formsToUser = new HashMap<>();
	private static HashMap<String, UI> formsToUi = new HashMap<>();

	private static LinkedList<BroadcastListener> listeners = new LinkedList<>();
	private static List<ApplicationController> controllers = new ArrayList<>();

	public static synchronized void register(ApplicationController controller) {
		controllers.add(controller);
	}

	public static synchronized void unregister(ApplicationController controller) {
		controllers.remove(controller);
		controller.freeLockedResources();
	}

	public static synchronized void register(BroadcastListener listener) {
		listeners.add(listener);
	}

	public static synchronized void unregister(BroadcastListener listener) {
		listeners.remove(listener);
	}

	public static synchronized void broadcast(final String message) {
		for (final BroadcastListener listener : listeners)
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					listener.receiveBroadcast(message);
				}
			});
	}

	public static synchronized boolean isUserUiUsingForm(IUser<Long> user, UI ui, IBaseFormView form) {
		if (form != null) {
			return formsToUser.get(form.getComparationId()) != null && formsToUser.get(form.getComparationId()).equals(user)
					&& formsToUi.get(form.getComparationId()) != null && formsToUi.get(form.getComparationId()).equals(ui);
		}
		return false;
	}

	public static synchronized IUser<Long> getUserUsingForm(IBaseFormView form) {
		return formsToUser.get(form.getComparationId());
	}

	public static synchronized void lockForm(Form form, IUser<Long> user, UI ui) {
		if (form == null || user == null) {
			return;
		}

		if ((!formsToUser.containsKey(form.getComparationId())) || Objects.equals(formsToUser.get(form.getComparationId()), user)) {
			WebformsUiLogger.info(UiAccesser.class.getName(), "User already has locked '" + form + "'");
			if (formsToUi.get(form.getComparationId()) != null && !Objects.equals(formsToUi.get(form.getComparationId()), ui)) {
				WebformsUiLogger.info(UiAccesser.class.getName(), "Killing previous ui '" + formsToUi + "'");
				unregister(((ApplicationUi) formsToUi.get(form.getComparationId())).getControllerInstance());
				// formsToUi.get(form.getComparationId()).access(new Runnable()
				// {
				//
				// @Override
				// public void run() {
				//
				// }
				// });
				formsToUi.get(form.getComparationId()).close();
			}
			WebformsUiLogger.info(UiAccesser.class.getName(), "Has locked '" + form + "'");
			formsToUser.put(form.getComparationId(), user);
			formsToUi.put(form.getComparationId(), ui);
		}
	}

	public static synchronized void releaseForm(Form form, IUser<Long> user, UI ui) {
		// If form is still locked and the user is who lock the form.
		if (formsToUser.containsKey(form.getComparationId()) && formsToUser.get(form.getComparationId()).equals(user)
				&& formsToUi.containsKey(form.getComparationId()) && Objects.equals(formsToUi.get(form.getComparationId()), ui)) {
			WebformsUiLogger.info(UiAccesser.class.getName(), "Has released '" + form + "'");
			formsToUser.remove(form.getComparationId());
			formsToUi.remove(form.getComparationId());
		}
	}
}
