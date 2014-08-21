package com.biit.webforms.gui;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.biit.webforms.authentication.ApplicationController;
import com.biit.webforms.persistence.entity.Form;
import com.liferay.portal.model.User;

public class UiAccesser {
	static ExecutorService executorService = Executors.newSingleThreadExecutor();

	public interface BroadcastListener {
		void receiveBroadcast(String message);
	}

	private static LinkedList<BroadcastListener> listeners = new LinkedList<BroadcastListener>();
	private static List<ApplicationController> controllers = new ArrayList<ApplicationController>();

	public static synchronized void register(ApplicationController controller) {
		controllers.add(controller);
	}

	public static synchronized void unregister(ApplicationController controller) {
		controllers.remove(controller);
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

	public static synchronized List<SimpleEntry<User, Form>> getFormsInUseData() {
		List<SimpleEntry<User, Form>> formsInUseData = new ArrayList<SimpleEntry<User, Form>>();
		for (ApplicationController controller : controllers) {
			User user = controller.getUser();
			Form form = controller.getFormInUse();
			if (user != null && form != null) {
				formsInUseData.add(new SimpleEntry<User, Form>(user, form));
			}
		}
		return formsInUseData;
	}
	
	public static synchronized User getUserIfFormIsInUse(Form form) {
		for (ApplicationController controller : controllers) {
			User user = controller.getUser();
			Form formInUse = controller.getFormInUse();
			if (user != null && formInUse!=null && formInUse.equals(form)) {
				return user;
			}
		}
		return null;
	}

}
