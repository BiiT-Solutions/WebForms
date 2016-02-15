package com.biit.webforms.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.biit.form.entity.IBaseFormView;
import com.biit.usermanager.entity.IUser;
import com.biit.webforms.persistence.entity.Form;

public class UiAccesser {
	static ExecutorService executorService = Executors.newSingleThreadExecutor();

	public interface BroadcastListener {
		void receiveBroadcast(String message);
	}

	private static HashMap<String, IUser<Long>> formsInUse = new HashMap<String, IUser<Long>>();

	private static LinkedList<BroadcastListener> listeners = new LinkedList<BroadcastListener>();
	private static List<ApplicationController> controllers = new ArrayList<ApplicationController>();

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

	public static synchronized boolean isUserUserUsingForm(IUser<Long> user, IBaseFormView form) {
		if (form != null) {
			return formsInUse.get(form.getComparationId()) != null && formsInUse.get(form.getComparationId()).equals(user);
		}
		return false;
	}

	public static synchronized IUser<Long> getUserUsingForm(IBaseFormView form) {
		return formsInUse.get(form.getComparationId());
	}

	public static synchronized void lockForm(Form form, IUser<Long> user) {
		if (form == null || user == null) {
			return;
		}

		if (!formsInUse.containsKey(form.getComparationId())) {
			WebformsUiLogger.info(UiAccesser.class.getName(), "User '" + user.getEmailAddress() + "' has locked '" + form + "'");
			formsInUse.put(form.getComparationId(), user);
		}
	}

	public static synchronized void releaseForm(Form form, IUser<Long> user) {
		WebformsUiLogger.info(UiAccesser.class.getName(), "User '" + user.getEmailAddress() + "' has released '" + form + "'");
		// If form is still locked and the user is who lock the form.
		if (formsInUse.containsKey(form.getComparationId()) && formsInUse.get(form.getComparationId()).equals(user)) {
			formsInUse.remove(form.getComparationId());
		}
	}
}
